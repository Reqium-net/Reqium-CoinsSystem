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

package net.reqium.coinssystem.managers;

import net.reqium.coinssystem.api.CoinsPlayer;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class CacheManager {

    private HashMap<UUID, CoinsPlayer> playerMap;

    public CacheManager() {
        this.playerMap = new HashMap<>();
    }

    public void cachePlayer(Player player, CoinsPlayer coinsPlayer) {
        playerMap.put(player.getUniqueId(), coinsPlayer);
    }

    public void cachePlayer(UUID uuid, CoinsPlayer coinsPlayer) {
        playerMap.put(uuid, coinsPlayer);
    }

    public void invalidateCache(UUID uuid) {
        playerMap.remove(uuid);
    }

    public void invalidateCache(Player player) {
        playerMap.remove(player.getUniqueId());
    }

    public CoinsPlayer resolve(Player player) {
        return playerMap.get(player.getUniqueId());
    }

    public CoinsPlayer resolve(UUID uuid) {
        return playerMap.get(uuid);
    }

    public int size() {
        return this.playerMap.size();
    }

}
