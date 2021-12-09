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

package net.reqium.coinssystem.event;

import net.reqium.coinssystem.CoinsSystem;
import net.reqium.coinssystem.api.CoinsPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerEvents implements Listener {

    @EventHandler
    public void on(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        CoinsPlayer coinsPlayer = new CoinsPlayer(player.getUniqueId().toString());
        coinsPlayer.create();
        coinsPlayer.loadFromDatabase();
        coinsPlayer.updateCache();
    }

    @EventHandler
    public void on(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        CoinsSystem.getInstance().getCacheManager().invalidateCache(player);
    }
}
