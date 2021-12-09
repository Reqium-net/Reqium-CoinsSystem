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

import net.reqium.backendbridge.request.RequestType;
import net.reqium.backendbridge.response.BackendResponse;
import net.reqium.coinssystem.CoinsSystem;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.json.JSONObject;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.UUID;

public class CoinsPlayer {

    private String username;
    private String uuid;
    private int coins;

    /**
     * Creates new CoinsPlayer instance
     *
     * @param uuid uuid of player
     */
    public CoinsPlayer(String uuid) {
        this.username = Bukkit.getOfflinePlayer(UUID.fromString(uuid)).getName();
        this.uuid = uuid;
        this.coins = CoinsSystem.getInstance().getPluginConfig().getDefaultCoins();
    }

    private CoinsPlayer(String uuid, String username, int coins) {
        this.username = username;
        this.uuid = uuid;
        this.coins = coins;
    }

    public int getCoins() {
        return coins;
    }

    public String getFormattedCoins() {
        NumberFormat formatter = NumberFormat.getInstance(Locale.GERMAN);

        return formatter.format(this.coins);
    }

    public void setCoins(int coins) {
        this.coins = coins;
    }

    public String getUsername() {
        return username;
    }

    public String getUuid() {
        return uuid;
    }

    /**
     * Inserts current CoinsPlayer instance as new item in database (cancels if already exists)
     */
    public void create() {
        CoinsSystem.getInstance().getBackendBridge().makeRequest(RequestType.POST, "coins", new JSONObject()
                .put("username", this.username)
                .put("uuid", this.uuid)
                .put("coins", CoinsSystem.getInstance().getPluginConfig().getDefaultCoins()));
    }

    /**
     * Checks if user with {@link CoinsPlayer#uuid} exists in database
     *
     * @return if {@link CoinsPlayer#uuid} exists in database
     */
    public boolean exists() {
        return CoinsSystem.getInstance().getBackendBridge().makeRequest(RequestType.GET, "coins/uuid/" + uuid, null).getResponseCode() != 404;
    }

    /**
     * Loads data from database based on {@link CoinsPlayer#uuid} into this instance
     */
    public void loadFromDatabase() {
        System.out.println(uuid);
        BackendResponse response = CoinsSystem.getInstance().getBackendBridge().makeRequest(RequestType.GET, "coins/uuid/" + uuid, null);
        System.out.println(response.getResponseCode());
        if (response.getResponseCode() != 404) {
            CoinsPlayer coinsPlayer = response.getData(CoinsPlayer.class);
            System.out.println(coinsPlayer.getCoins() + " ");
            System.out.println(coinsPlayer.getUsername() + " ");
            this.username = coinsPlayer.getUsername();
            this.coins = coinsPlayer.getCoins();
        }
    }

    /**
     * Updates cache from player to current instance
     */
    public void updateCache() {
        Player player = Bukkit.getPlayer(UUID.fromString(uuid));

        if (player != null) {
            CoinsSystem.getInstance().getCacheManager().cachePlayer(player, this);
        }
    }

    /**
     * Loads data from cache in current instance
     */
    public void loadCache() {
        CoinsPlayer coinsPlayer = CoinsSystem.getInstance().getCacheManager().resolve(UUID.fromString(this.uuid));

        if (coinsPlayer != null) {
            this.coins = coinsPlayer.getCoins();
        }
    }

    /**
     * Writes updated player object into database
     */
    public void update() {
        CoinsSystem.getInstance().getBackendBridge().makeRequest(RequestType.PUT, "coins", new JSONObject()
                .put("uuid", uuid)
                .put("values", new JSONObject()
                        .put("username", this.username)
                        .put("coins", this.coins)));
    }

    /**
     * Loads data from database based on {@code username}
     *
     * @param username
     * @return coinsPlayer instance (null when not found)
     */
    public static CoinsPlayer getCoinsPlayerByUsername(String username) {
        BackendResponse response = CoinsSystem.getInstance().getBackendBridge().makeRequest(RequestType.GET, "coins/username/" + username, null);


        return response.wasSuccessful() ? response.getData(CoinsPlayer.class) : null;
    }


}
