/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.christianto.unpar.so.Ngambang.Model;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;
import net.christianto.unpar.so.Ngambang.Windows.frmSplash;

/**
 * Used for showing loading modal
 *
 * @author Gunawan Christianto
 */
public class LoaderModel {

    Stage stage;
    Scene scene;
    frmSplash controller;

    public LoaderModel() {
        this.stage = new Stage();

        FXMLLoader form = new FXMLLoader();
        form.setLocation(getClass().getResource("/net/christianto/unpar/so/Ngambang/Windows/frmSplash.fxml"));
        try {
            this.scene = new Scene((Parent) form.load());
        } catch (IOException ex) {
            Logger.getLogger(LoaderModel.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.controller = form.getController();
        this.stage.setScene(scene);
        this.stage.initStyle(StageStyle.UNDECORATED);
    }

    public void show() {
        stage.show();
    }

    public void close() {
        stage.close();
    }

    public void setOwner(Window owner) {
        stage.initOwner(owner);
        if (owner != null) {
            stage.initModality(Modality.WINDOW_MODAL);
        }
    }

    public void updateStatus(String status) {
        Platform.runLater(() -> {
            controller.updateStatus(status);
        });
    }
}
