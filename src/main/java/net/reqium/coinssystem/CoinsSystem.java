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

package net.reqium.coinssystem;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.Getter;
import net.reqium.coinssystem.cmd.CoinsCommand;
import net.reqium.coinssystem.config.PluginConfig;
import net.reqium.coinssystem.event.PlayerEvents;
import net.reqium.coinssystem.managers.CacheManager;
import net.reqium.coinssystem.mysql.MySQL;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class CoinsSystem extends JavaPlugin {

    @Getter
    private Gson gson;
    @Getter
    private MySQL mySQL;
    @Getter
    private static CoinsSystem instance;
    @Getter
    private PluginConfig pluginConfig;
    @Getter
    private CacheManager cacheManager;

    @Override
    public void onEnable() {

        Bukkit.getConsoleSender().sendMessage("CoinsSystem started");
        if(instance == null) {
            instance = this;
        }
        init();
    }


    public void init() {
        this.gson = new GsonBuilder().setPrettyPrinting().create();
        this.cacheManager = new CacheManager();
        this.pluginConfig = new PluginConfig();
        if(this.pluginConfig.exists()) {
            this.pluginConfig = this.pluginConfig.loadFromFile();
        }
        this.pluginConfig.save();
        this.mySQL = new MySQL(
                this.pluginConfig.getDatabaseConnection().getHostname(),
                this.pluginConfig.getDatabaseConnection().getUsername(),
                this.pluginConfig.getDatabaseConnection().getPassword(),
                this.pluginConfig.getDatabaseConnection().getDatabase()
        );
        this.mySQL.connect();
        this.mySQL.update("CREATE TABLE IF NOT EXISTS users(uuid VARCHAR(100) PRIMARY KEY, username VARCHAR(100), coins INT)");

        //register events
        this.getServer().getPluginManager().registerEvents(new PlayerEvents(), this);

        //register commands
        this.getCommand("coins").setExecutor(new CoinsCommand());

    }

    public String noPermission() {
        return getPluginConfig().getPrefix() + "§cDu hast keine Rechte auf diesen Befehl!";
    }
}
