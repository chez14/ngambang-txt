/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.christianto.unpar.so.Ngambang.Windows;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
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
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }

    @FXML
    private void mnuSave_Click(ActionEvent event) {
        
    }

    @FXML
    private void mnuSaveAs_Click(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        fileChooser.showOpenDialog(this.lblLines.getScene().getWindow());
    }

    @FXML
    private void mnuClose_Click(ActionEvent event) {
        
    }
    
}
