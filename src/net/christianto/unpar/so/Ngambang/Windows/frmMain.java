/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.christianto.unpar.so.Ngambang.Windows;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;

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

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
