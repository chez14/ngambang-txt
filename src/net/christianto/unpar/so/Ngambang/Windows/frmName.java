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
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import net.christianto.unpar.so.Ngambang.Model.UserSetting;

/**
 * FXML Controller class
 *
 * @author Gunawan Christianto
 */
public class frmName implements Initializable {

    @FXML
    private TextField txtName;
    @FXML
    private Button btnSave;
    @FXML
    private ProgressIndicator prgProccess;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }    

    @FXML
    private void btnSave_Click(ActionEvent event) {
        if(txtName.getText().isEmpty()) {
            (new Alert(Alert.AlertType.WARNING, "There's no name here, have you even type?!")).showAndWait();
            return;
        }
        
        (new Alert(Alert.AlertType.CONFIRMATION, "You sure to choose\n" + txtName.getText() + "\nas your author name?"))
            .showAndWait()
            .ifPresent((resp)->{
                if(resp == ButtonType.OK) {
                    UserSetting.authorName = txtName.getText();
                    ((Stage)txtName.getScene().getWindow()).close();
                }
            });
        
        
    }
    
}
