package com.alsacchi.easycommand;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import java.util.HashMap;


public class home implements CommandExecutor {
    main plugin;

    public home(main passedPlugin) {
        this.plugin = passedPlugin;
    }

    private HashMap<String, Location> playerHomes = new HashMap<>();

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
        FileConfiguration config = plugin.getConfig();
        Player player = (Player) commandSender;
        if (player.hasPermission("easyhome.home")) {
            if (config.getBoolean("home")) {
                if (config.contains("homes." + String.valueOf(player.getUniqueId()))) {
                    String[] arg = config.getString("homes." + String.valueOf(player.getUniqueId())).split(",");
                    double[] parsed = new double[3];
                    for (int a = 0; a < 3; a++) {
                        parsed[a] = Double.parseDouble(arg[a + 1]);
                    }
                    Location location = new Location(Bukkit.getServer().getWorld(arg[0]), parsed[0], parsed[1], parsed[2]);
                    if (config.getBoolean("xpPay")) {
                        double dst = player.getLocation().distance(location);
                        int xp = player.getLevel();
                        if ((int) dst <= config.getInt("homeDistance")) {
                            player.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "[EasyHome]" + ChatColor.WHITE + "" + " You are already at home!");
                            return true;
                        }
                        if (xp >= ((int) dst / config.getInt("homeDistance"))) {
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
            } else {
                player.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "[EasyHome]" + ChatColor.WHITE + "" + " Plugin deactivated in config file, contact an amministrator");
            }
        } else {
            player.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "[EasyHome]" + ChatColor.WHITE + "" + " You don't have the right permission");
        }
        return false;
    }
}
