/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.christianto.unpar.so.Ngambang.Windows;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import net.christianto.unpar.so.Ngambang.Model.*;
/**
 * FXML Controller class
 *
 * @author hayashi
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
    private MenuItem mnuAbout_Click;
    @FXML
    private Label lblWords;
    @FXML
    private Label lblLines;
    @FXML
    private ListView<String> lstAuthors;

    private TextDocument td;
    
    private String location;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        if(UserSetting.startUpArgs.length == 0) {
            td = new TextDocument();
        } else {
            this.location = UserSetting.startUpArgs[0];
            td = new TextDocument(this.location);
            try {
                td.load();
            } catch (IOException ex) {
                Logger.getLogger(frmMain.class.getName()).log(Level.SEVERE, null, ex);
                (new Alert(Alert.AlertType.ERROR, "Error when proccessing the file.\nSystem will now be closed.")).showAndWait();
                System.exit(-2);
            }
        }
    }

    @FXML
    private void mnuSave_Click(ActionEvent event) {
        if(location == null || location.isEmpty()) {
            mnuSaveAs_Click(event);
        }
        td.document = txtContent.getText();
        try {
            td.save(location);
        } catch (IOException ex) {
            Logger.getLogger(frmMain.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void mnuSaveAs_Click(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Ngambang File");
        FileChooser.ExtensionFilter ex = new FileChooser.ExtensionFilter("Txtx File", "*.txtx");
        fileChooser.getExtensionFilters().add(ex);
        
        location = fileChooser.showSaveDialog(this.lblLines.getScene().getWindow()).getAbsolutePath();
        if(location == null || location.isEmpty()) {
            (new Alert(Alert.AlertType.ERROR, "Unable to save file. Location not set.")).show();
            return;
        }
        mnuSave_Click(event);
    }

    @FXML
    private void mnuClose_Click(ActionEvent event) {
        if(td.document != null && !td.document.equals(txtContent.getText())) {
            Alert a = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure to close this document without save?", ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);
            a.showAndWait().ifPresent(resp->{
                if(resp == ButtonType.NO) {
                    System.exit(0);
                } else if(resp == ButtonType.YES){
                    mnuSave_Click(event);
                }
            });
            return;
        }
        System.exit(0);
    }
    
}
