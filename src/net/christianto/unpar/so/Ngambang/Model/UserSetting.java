/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.christianto.unpar.so.Ngambang.Model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 *
 * @author Gunawan Christianto
 */
public class UserSetting {
    
    /**
     * Preventing it being loaded
     */
    private UserSetting() {

    }

    private static final String settingLocation = "user.json";
    public static String authorName;

    public static String[] startUpArgs;

    /**
     * Saves the user setting.
     * @throws IOException 
     */
    public static void save() throws IOException {
        JSONObject setting = new JSONObject();
        setting.put("author", authorName);

        FileOutputStream fox = new FileOutputStream(new File(settingLocation));
        fox.write(setting.toJSONString().getBytes());
        fox.flush();
        fox.close();
    }

    /**
     * Load the user Settings
     * @throws FileNotFoundException
     * @throws ParseException
     * @throws IOException 
     */
    public static void load() throws FileNotFoundException, ParseException, IOException {
        BufferedReader setting = new BufferedReader(new FileReader(settingLocation));
        String set = "";
        String temp;
        while ((temp = setting.readLine()) != null) {
            set += temp;
        }

        JSONParser a = new JSONParser();
        JSONObject settingObj = (JSONObject) a.parse(set);
        authorName = (String) settingObj.get("author");
        setting.close();
    }
}
