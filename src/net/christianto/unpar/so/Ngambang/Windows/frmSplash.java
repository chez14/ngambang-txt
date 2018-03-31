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
import javafx.scene.control.ProgressIndicator;

/**
 * FXML Controller class
 *
 * @author Gunawan Christianto
 */
public class frmSplash implements Initializable {

    @FXML
    private ProgressIndicator prgStatus;
    @FXML
    private Label lblStatus;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    public void updateStatus(String status) {
        this.lblStatus.setText(status);
    }

    public void updateStatus(double progress) {
        this.prgStatus.setProgress(progress);
    }

    public void setIntermediate() {
        this.prgStatus.setProgress(ProgressIndicator.INDETERMINATE_PROGRESS);
    }

    public void updateStatus(String status, double progress) {
        this.updateStatus(status);
        this.updateStatus(progress);
    }
}
