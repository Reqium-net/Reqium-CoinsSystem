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

package net.reqium.coinssystem.api;

import net.reqium.coinssystem.CoinsSystem;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class CoinsPlayer {

    private String username;
    private UUID uuid;
    private int coins;

    /**
     * Creates new CoinsPlayer instance
     *
     * @param uuid uuid of player
     */
    public CoinsPlayer(UUID uuid) {
        this.username = Bukkit.getOfflinePlayer(uuid).getName();
        this.uuid = uuid;
        this.coins = CoinsSystem.getInstance().getPluginConfig().getDefaultCoins();
    }

    private CoinsPlayer(UUID uuid, String username, int coins) {
        this.username = username;
        this.uuid = uuid;
        this.coins = coins;
    }

    public int getCoins() {
        return coins;
    }

    public void setCoins(int coins) {
        this.coins = coins;
    }

    public String getUsername() {
        return username;
    }

    public UUID getUuid() {
        return uuid;
    }

    /**
     * Inserts current CoinsPlayer instance as new item in database (cancels if already exists)
     */
    public void create() {
        if (!exists()) {
            CoinsSystem.getInstance().getMySQL().update("INSERT INTO users(uuid, username, coins) VALUES ('" + uuid + "', '" + username + "', '" + coins + "')");
        }
    }

    /**
     * Checks if user with {@link CoinsPlayer#uuid} exists in database
     *
     * @return if {@link CoinsPlayer#uuid} exists in database
     */
    public boolean exists() {
        ResultSet rs = CoinsSystem.getInstance().getMySQL().getResult("SELECT * FROM users WHERE uuid = '" + uuid.toString() + "'");
        
        int i = 0;

        while (true) {
            try {
                if (!rs.next()) break;
            } catch (SQLException e) {
                e.printStackTrace();
            }
            i++;
        }

        return i > 0;
    }

    /**
     * Loads data from database based on {@link CoinsPlayer#uuid} into this instance
     */
    public void loadFromDatabase() {
        ResultSet rs = CoinsSystem.getInstance().getMySQL().getResult("SELECT * FROM users WHERE uuid='" + uuid + "'");

        while (true) {
            try {
                if (!rs.next()) break;
                this.username = rs.getString("username");
                this.uuid = UUID.fromString(rs.getString("uuid"));
                this.coins = rs.getInt("coins");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Updates cache from player to current instance
     */
    public void updateCache() {
        Player player = Bukkit.getPlayer(uuid);

        if (player != null) {
            CoinsSystem.getInstance().getCacheManager().cachePlayer(player, this);
        }
    }

    /**
     * Loads data from cache in current instance
     */
    public void loadCache() {
        CoinsPlayer coinsPlayer = CoinsSystem.getInstance().getCacheManager().resolve(this.uuid);

        if(coinsPlayer != null) {
            this.coins = coinsPlayer.getCoins();
        }
    }

    /**
     * Writes updated player object into database
     */
    public void update() {
        CoinsSystem.getInstance().getMySQL().update("UPDATE users SET uuid = '" + uuid + "', username = '" + username + "', coins = '" + coins + "' WHERE uuid = '" + uuid + "'");
    }

    /**
     * Loads data from database based on {@code username}
     *
     * @param username
     * @return coinsPlayer instance (null when not found)
     */
    public static CoinsPlayer getCoinsPlayerByUsername(String username) {
        ResultSet rs = CoinsSystem.getInstance().getMySQL().getResult("SELECT * FROM users WHERE username = '" + username + "'");

        while (true) {
            try {
                if (!rs.next()) break;
                CoinsPlayer coinsPlayer = new CoinsPlayer(UUID.fromString(rs.getString("uuid")), rs.getString("username"), rs.getInt("coins"));
                return coinsPlayer;
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }

        return null;
    }


}
