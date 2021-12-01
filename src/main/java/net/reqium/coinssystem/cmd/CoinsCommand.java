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

package net.reqium.coinssystem.cmd;

import net.reqium.coinssystem.CoinsSystem;
import net.reqium.coinssystem.api.CoinsPlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Locale;

public class CoinsCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(sender instanceof Player) {
            Player player = (Player) sender;
            //coins <add/set/remove> <Spieler> <Coins>

            if(args.length == 0) {
                CoinsPlayer coinsPlayer = CoinsSystem.getInstance().getCacheManager().resolve(player);
                player.sendMessage(CoinsSystem.getInstance().getPluginConfig().getPrefix() + "§6Du hast §7" + coinsPlayer.getCoins() + " §6Cookies.");
                return false;
            }

            if(args.length == 1) {
                String target = args[0];

                if(!player.hasPermission("reqium.coinssystem.coins.other")) {
                    player.sendMessage(CoinsSystem.getInstance().noPermission());
                    return true;
                }
                CoinsPlayer coinsTarget = CoinsPlayer.getCoinsPlayerByUsername(target);
                if(coinsTarget == null) {
                    player.sendMessage(CoinsSystem.getInstance().getPluginConfig().getPrefix() + "§cDieser Spieler existiert nicht in der Datenbank!");
                    return true;
                }
                player.sendMessage(CoinsSystem.getInstance().getPluginConfig().getPrefix() + "§6Cookies von §7" + coinsTarget.getUsername() + "§6: §7" + coinsTarget.getCoins());
            }

            if(args.length == 3) {

                if(!player.hasPermission("reqium.coinssystem.coins.admin")) {
                    player.sendMessage(CoinsSystem.getInstance().noPermission());
                    return true;
                }

                try {
                    String key = args[0].toLowerCase();
                    String target = args[1];
                    int coins = Integer.parseInt(args[2]);

                    CoinsPlayer coinsTarget = CoinsPlayer.getCoinsPlayerByUsername(target);

                    if(coinsTarget == null) {
                        player.sendMessage(CoinsSystem.getInstance().getPluginConfig().getPrefix() + "§cDieser Spieler existiert nicht in der Datenbank!");
                        return true;
                    }
                    int originalCoins = coinsTarget.getCoins();
                    switch (key) {
                        case "add":
                            coinsTarget.setCoins(coinsTarget.getCoins()+coins);
                            break;
                        case "set":
                            coinsTarget.setCoins(coins);
                            break;
                        case "remove":
                            coinsTarget.setCoins(coinsTarget.getCoins()-coins);
                            break;
                        default:
                            return true;
                    }
                    coinsTarget.update();
                    coinsTarget.updateCache();
                    player.sendMessage(CoinsSystem.getInstance().getPluginConfig().getPrefix() + "§6Die Coins von §7" + coinsTarget.getUsername() + " §6von §7" + originalCoins + " §6zu §7" + coinsTarget.getCoins() + " §6gesetzt.");


                } catch (NumberFormatException e) {
                    player.sendMessage(CoinsSystem.getInstance().getPluginConfig().getPrefix() + "§cBitte gib eine gültige Zahl ein!");
                }
            }


        } else {
            sender.sendMessage("You must be a player");
        }

        return false;
    }
}
