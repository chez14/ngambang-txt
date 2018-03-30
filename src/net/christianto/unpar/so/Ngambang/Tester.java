/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.christianto.unpar.so.Ngambang;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import net.christianto.unpar.so.Ngambang.Model.LoaderModel;
import net.christianto.unpar.so.Ngambang.Model.UserSetting;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
/**
 *
 * @author hayashi
 */
public class Tester extends Application {
    static LoaderModel lm;
    static Stage masterStage;
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("Windows/frmMain.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(true);
        stage.show();
        masterStage = stage;
        lm = new LoaderModel();
        lm.setOwner(stage);
        lm.show();
        startCheckSettings();
        lm.close();
    }
    
    private static void startCheckSettings() { 
        
        Task<Void> settingsCheck = new Task(){
            @Override
            protected Void call() throws Exception {
                //checking user settings:
                lm.updateStatus("Trying to read the user setting.");
                try {
                    BufferedReader setting = new BufferedReader(new FileReader(UserSetting.settingLocation));
                    String set = "";
                    String temp;
                    while((temp = setting.readLine()) != null) {
                        set += temp;
                    }
                    
                    JSONParser a = new JSONParser();
                    JSONObject settingObj = (JSONObject)a.parse(set);
                    UserSetting.authorName = (String)settingObj.get("name");
                } catch(IOException e) {
                    // setting not found
                    lm.updateStatus("Fail to read user setting,\nprocceding with asking the user's name.");
                    Platform.runLater(()->{
                        Stage stgUserName = new Stage();
                        FXMLLoader form = new FXMLLoader();
                        form.setLocation(getClass()
                                .getResource("/net/christianto/unpar/so/Ngambang/Windows/frmName.fxml"));
                        try {
                            Scene scnUserName = new Scene(form.load());
                            stgUserName.setScene(scnUserName);
                        } catch (IOException ex) {
                            Logger.getLogger(Tester.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        
                        stgUserName.initOwner(masterStage);
                        stgUserName.initModality(Modality.WINDOW_MODAL);
                        stgUserName.initStyle(StageStyle.UNDECORATED);
                        stgUserName.showAndWait();
                    });
                }
                return null;
            }
        };
        
        Thread tasker = new Thread(settingsCheck);
        tasker.start();
        try {
            tasker.join();
        } catch (InterruptedException ex) {
            Logger.getLogger(Tester.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        UserSetting.startUpArgs = args;
        launch(args);
    }
    
}
