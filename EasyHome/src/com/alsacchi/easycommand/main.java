package com.alsacchi.easycommand;

import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.logging.Logger;

import org.bukkit.ChatColor;

public class main extends JavaPlugin {
    private Logger Log = Bukkit.getLogger();
    private HashMap<String, Location> playerHomes = new HashMap<>();
    private FileConfiguration config = this.getConfig();

    @Override
    public void onEnable() {
        config.addDefault("home", true);
        config.addDefault("xpPay", false);
        config.addDefault("homeDistance", 15);
        config.addDefault("homes", "");
        config.options().copyDefaults(true);
        saveConfig();
        Log.warning("[EasyHome] Activated");
    }

    @Override
    public void onDisable() {
        Log.warning("[EasyHome] Deactivated");
    }

    public boolean onCommand(CommandSender theSender, Command cmd, String commandLabel, String[] args) {
        Player player = (Player) theSender;
        if (theSender != null) {
            if (commandLabel.equalsIgnoreCase("sethome")) {
                if (player.hasPermission("easyhome.sethome")) {
                    Location tmploc = player.getLocation();
                    playerHomes.put(player.getName(), tmploc);
                    String tmpWorld = player.getWorld().getName();
                    double tmpX = tmploc.getX();
                    double tmpY = tmploc.getY();
                    double tmpZ = tmploc.getZ();
                    this.getConfig().set("homes." + String.valueOf(player.getUniqueId()), tmpWorld + "," + tmpX + "," + tmpY + "," + tmpZ);
                    this.saveConfig();
                    player.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "[EasyHome]" + ChatColor.WHITE + "" + " Home set!");
                } else {
                    player.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "[EasyHome]" + ChatColor.WHITE + "" + " You don't have the right permission");
                }
            } else if (commandLabel.equalsIgnoreCase("home")) {
                if (player.hasPermission("easyhome.home")) {
                    if (config.getBoolean("home")) {
                        if (this.getConfig().contains("homes." + String.valueOf(player.getUniqueId()))) {
                            String[] arg = this.getConfig().getString("homes." + String.valueOf(player.getUniqueId())).split(",");
                            double[] parsed = new double[3];
                            for (int a = 0; a < 3; a++) {
                                parsed[a] = Double.parseDouble(arg[a + 1]);
                            }
                            Location location = new Location(Bukkit.getServer().getWorld(arg[0]), parsed[0], parsed[1], parsed[2]);
                            if(config.getBoolean("xpPay")) {
                                double dst = player.getLocation().distance(location);
                                int xp = player.getLevel();
                                if ((int) dst <= config.getInt("homeDistance")) {
                                    player.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "[EasyHome]" + ChatColor.WHITE + "" + " You are already at home!");
                                    return true;
                                }
                                if(xp >= ((int) dst / config.getInt("homeDistance"))) {
                                    int xp1 = ((int) dst / config.getInt("homeDistance"));
                                    player.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "[EasyHome]" + ChatColor.WHITE + "" + " Payed " + String.valueOf(xp1) + " Levels!");
                                    player.giveExpLevels(xp1 * -1);
                                } else {
                                    int xp1 = ((int) dst / config.getInt("homeDistance"));
                                    player.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "[EasyHome]" + ChatColor.WHITE + "" + " You don't have the necessary Xp to travel");
                                    player.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "[EasyHome]" + ChatColor.WHITE + "" + " You need: " + xp1 + " levels to teleport to home");
                                    return true;
                                }
                            }
                            playerHomes.put(String.valueOf(player.getUniqueId()), location);
                            Location homeLocation = playerHomes.get(String.valueOf(player.getUniqueId()));
                            player.teleport(homeLocation);
                            player.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "[EasyHome]" + ChatColor.WHITE + "" + " Teleported to home!");
                        } else {
                            player.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "[EasyHome]" + ChatColor.WHITE + "" + " You have to set a house!\n Do /sethome");
                        }
                    }
                } else {
                    player.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "[EasyHome]" + ChatColor.WHITE + "" + " You don't have the right permission");
                }
            } else {
                player.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "[EasyHome]" + ChatColor.WHITE + "" + " Plugin deactivated in config file, contact an amministrator");
            }
        }
        return true;
    }
}


