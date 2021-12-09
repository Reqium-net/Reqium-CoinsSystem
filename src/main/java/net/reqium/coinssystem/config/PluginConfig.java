/*
  Dieser Quelltext ist geistiges Eigentum von Michael Steinmötzger (ShortPing).
  Alle Rechte unterliegen der Lizenz unter dieser, dieser Quelltext geführt wird.
  Jegliche Vervielfältigungsrechte unterliegen dieser Lizenz.
 
  This source code is the intellectual property of Michael Steinmötzger (ShortPing).
  All rights are subject to the license under which this source code is licensed.
  Any reproduction rights are subject to this license.
 
  Copyright © Michael Steinmötzger 2018-2021
 
  Alle Rechte vorbehalten
  All rights reserved
 */

package net.reqium.coinssystem.config;

import lombok.Data;
import net.reqium.coinssystem.CoinsSystem;
import net.reqium.coinssystem.config.properties.DatabaseConnection;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

@Data
public class PluginConfig {

    private transient File file = new File("plugins/Reqium/CoinsSystem/config.json");

    private String prefix = "§8× §2§lReq§a§lium.net §8┃ ";
    private int defaultCoins = 0;
    private String apiKey = "apikey";
    private String apiUrl = "https://api.reqium.net";

    public PluginConfig() {
        if(!file.exists()) {
            file.getParentFile().mkdirs();
        }
    }

    public boolean exists() {
        return file.exists();
    }

    public PluginConfig loadFromFile() {
        if(exists()) {
            try {
                Scanner scanner = new Scanner(file);

                String jsonString = "";

                while(scanner.hasNextLine()) {
                    jsonString += scanner.nextLine();
                }

                return CoinsSystem.getInstance().getGson().fromJson(jsonString, PluginConfig.class);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public void save() {
        if(exists()) file.delete();

        try {
            file.createNewFile();

            FileWriter writer = new FileWriter(file);

            writer.write(CoinsSystem.getInstance().getGson().toJson(this));
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
