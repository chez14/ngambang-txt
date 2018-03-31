/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.christianto.unpar.so.Ngambang.Windows;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Accordion;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextArea;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import net.christianto.unpar.so.Ngambang.Exception.InvalidDocFormatException;
import net.christianto.unpar.so.Ngambang.Model.*;
import org.json.simple.parser.ParseException;

/**
 * FXML Controller class
 *
 * @author Gunawan Christianto
 */
public class frmMain implements Initializable {

    @FXML
    private HBox pnlAutosave;
    @FXML
    private Label lblStatus;
    @FXML
    private ProgressBar prgBar;
    @FXML
    private TextArea txtContent;
    @FXML
    private Label lblWords;
    @FXML
    private Label lblLines;
    @FXML
    private ListView<String> lstAuthors;

    private TextDocument td;

    private String location;
    private boolean changed;
    @FXML
    private Label lblChars;
    @FXML
    private Accordion acrRightPane;
    @FXML
    private TitledPane tlpStats;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        if (UserSetting.startUpArgs.length == 0) {
            td = new TextDocument();
            changed = true;
        } else {
            this.location = UserSetting.startUpArgs[0];
            loadFile(location);
        }
        watch(); //start autosave on startup.
        Platform.runLater(() -> {
            ((Stage) txtContent.getScene().getWindow()).setTitle(buildTitle());
            ((Stage) txtContent.getScene().getWindow()).setOnCloseRequest((e) -> {
                if (closeRequest()) {
                    e.consume();
                    return;
                }
                Platform.exit();
            });
            acrRightPane.setExpandedPane(tlpStats);
        });
    }

    /**
     * File loader, will help me to load file in a bunch of places. Will prompt
     * automatically if file are not able be loaded or there's trouble with it.
     *
     * @param location of the file.
     */
    private void loadFile(String location) {
        TextDocument old_td = td;
        td = new TextDocument(location);
        Thread saver = new Thread(() -> {
            try {
                td.load();
            } catch (IOException ex) {
                Logger.getLogger(frmMain.class.getName())
                        .log(Level.SEVERE, null, ex);
                (new Alert(Alert.AlertType.ERROR,
                        "Error when proccessing the file.\n"
                        + "System will now be closed.")).showAndWait();
                Platform.exit();
            } catch (ParseException ex) {
                Logger.getLogger(frmMain.class.getName())
                        .log(Level.SEVERE, null, ex);
                (new Alert(Alert.AlertType.WARNING,
                        "Error when proccessing the file.\n"
                        + "We couldn't read the author list,"
                        + "proceeding to open it anyway.")).showAndWait();
            } catch (InvalidDocFormatException ex) {
                Logger.getLogger(frmMain.class.getName())
                        .log(Level.SEVERE, null, ex);
                (new Alert(Alert.AlertType.ERROR,
                        "Error when proccessing the file.\n"
                        + "The file you selected are not compatible"
                        + "with this app."))
                        .showAndWait();
                td = old_td;
            }
        });
        Platform.runLater(() -> prgBar.setVisible(true));
        saver.start();
        try {
            saver.join();
        } catch (InterruptedException ex) {
            Logger.getLogger(frmMain.class.getName())
                    .log(Level.SEVERE, null, ex);
        }

        refreshAuthor();
        txtContent.setText(td.document);
        Platform.runLater(() -> {
            ((Stage) txtContent.getScene().getWindow()).setTitle(buildTitle());
        });
        Platform.runLater(() -> prgBar.setVisible(false));

    }

    /**
     * Requests the close procedure. Will prompt for save if it's unsaved.
     *
     * @return false on it's not ok to close, true if it's ok.
     */
    private boolean closeRequest() {
        System.out.println("Close Request!");
        if (td.document != null && !td.document.equals(txtContent.getText())) {
            Alert a = new Alert(Alert.AlertType.CONFIRMATION,
                    "Are you sure to close this document\nwithout saving?",
                    ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);

            a.showAndWait().ifPresent(resp -> {
                if (resp == ButtonType.YES) {
                    executor.shutdownNow();
                    try {
                        executor.awaitTermination(10, TimeUnit.MILLISECONDS);
                    } catch (InterruptedException ex) {
                        System.exit(0);
                    }
                    Platform.exit();
                } else if (resp == ButtonType.NO) {
                    mnuSave_Click(null);
                }
            });
            return true;
        }
        executor.shutdownNow();
        try {
            executor.awaitTermination(10, TimeUnit.MILLISECONDS);
        } catch (InterruptedException ex) {
            System.exit(0);
        }
        return false;
    }

    /**
     * Will help build us the title bar
     *
     * @return
     */
    private String buildTitle() {
        String filename = "Untitled.txtx";
        if (location != null && !location.isEmpty()) {
            filename = location;
        }
        if (changed) {
            filename += "*";
        }

        return "Ngambang v1.0 - " + filename;
    }

    /**
     * Refresh the author list. This will always make sure that the author list
     * are the newest and fresh from the file.
     */
    private void refreshAuthor() {
        Platform.runLater(() -> {
            lstAuthors.getItems().clear();
            lstAuthors.getItems().addAll(td.authors);
        });
    }

    @FXML
    private void mnuSave_Click(ActionEvent event) {
        if (location == null || location.isEmpty()) {
            mnuSaveAs_Click(event);
        }
        td.document = txtContent.getText();
        Thread saver = new Thread(() -> {
            try {
                td.save(location);
            } catch (IOException ex) {
                Logger.getLogger(frmMain.class.getName())
                        .log(Level.SEVERE, null, ex);
            }
        });
        Platform.runLater(() -> prgBar.setVisible(true));
        saver.start();
        try {
            saver.join();
        } catch (InterruptedException ex) {
            Logger.getLogger(frmMain.class.getName())
                    .log(Level.SEVERE, null, ex);
        }
        Platform.runLater(() -> prgBar.setVisible(false));

        refreshAuthor();
        changed = false;
        ((Stage) txtContent.getScene().getWindow()).setTitle(buildTitle());

    }

    @FXML
    private void mnuSaveAs_Click(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Ngambang File");
        FileChooser.ExtensionFilter ex = new FileChooser.ExtensionFilter("Txtx File", "*.txtx");
        fileChooser.getExtensionFilters().add(ex);

        location = fileChooser.showSaveDialog(this.lblLines.getScene()
                .getWindow()).getAbsolutePath();
        if (location == null || location.isEmpty()) {
            (new Alert(Alert.AlertType.ERROR,
                    "Unable to save file. Location not set.")).show();
            return;
        }
        if (!location.endsWith(".txtx")) {
            location += ".txtx";
        }
        mnuSave_Click(event);
    }

    @FXML
    private void mnuClose_Click(ActionEvent event) {
        closeRequest();
    }

    @FXML
    private void mnuOpen_Click(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Ngambang File");
        FileChooser.ExtensionFilter ex = new FileChooser.ExtensionFilter("Txtx File", "*.txtx");
        fileChooser.getExtensionFilters().add(ex);

        location = fileChooser.showOpenDialog(this.lblLines.getScene()
                .getWindow()).getAbsolutePath();
        if (location == null || location.isEmpty()) {
            (new Alert(Alert.AlertType.ERROR,
                    "Unable to open file. Location not set.")).show();
            return;
        }
        loadFile(location);
    }

    private ScheduledExecutorService executor = Executors
            .newSingleThreadScheduledExecutor();

    /**
     * This method starts the watch event. The watch task are includes auto
     * save, and auto counter for word and lines.
     *
     * This will run another thread process to make sure that for a really long
     * line didn't blocks the main thread.
     */
    private void watch() {
        Runnable autoSave = () -> {
            if (location == null || location.isEmpty()) {
                System.out.println("[DAEMON] Autosave is running,"
                        + "but file location is unset. Ignoring...");
                return;
            }
            Platform.runLater(() -> {
                pnlAutosave.setVisible(true);
            });
            try {
                td.save();
                System.out.println("[DAEMON] Autosave runned");
            } catch (IOException ex) {
                System.out.println("[DAEMON] Autosave run failed");
                Logger.getLogger(frmMain.class.getName())
                        .log(Level.WARNING, null, ex);
            }
            Platform.runLater(() -> {
                pnlAutosave.setVisible(false);
            });
        };

        txtContent.setOnKeyTyped((event) -> {
            Thread counter = new Thread(() -> {
                synchronized (txtContent) {
                    String text = txtContent.getText().trim();
                    int words = text.isEmpty()?0:text.split("\\s").length;
                    int lines = text.isEmpty()?0:text.split("\\n").length;
                    Platform.runLater(() -> {
                        if (!this.changed) {
                            changed = true;
                            ((Stage) txtContent.getScene().getWindow())
                                    .setTitle(buildTitle());
                        }
                        lblLines.setText(Integer.toString(lines) + " lines");
                        lblWords.setText(Integer.toString(words) + " words");
                        lblChars.setText(Integer.toString(txtContent.getText()
                                .length()) + " chars");
                    });
                }
            });
            counter.start();
            try {
                counter.join();
            } catch (InterruptedException ex) {
                Logger.getLogger(frmMain.class.getName())
                        .log(Level.SEVERE, null, ex);
            }
        });
        // run autosave every 5 minutes
        executor.scheduleAtFixedRate(autoSave, 0, 5, TimeUnit.MINUTES);
    }

    @FXML
    private void mnuAbout_Click(ActionEvent event) throws IOException {
        Stage stgAbout = new Stage();
        Scene scnAbout = new Scene(FXMLLoader.load(getClass()
                .getResource("frmAbout.fxml")));
        stgAbout.setScene(scnAbout);
        stgAbout.initModality(Modality.WINDOW_MODAL);
        stgAbout.initOwner(txtContent.getScene().getWindow());
        stgAbout.showAndWait();
    }
}
