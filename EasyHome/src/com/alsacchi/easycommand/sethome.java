package com.alsacchi.easycommand;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.logging.Logger;

public class sethome implements CommandExecutor {
    main plugin;

    public sethome(main passedPlugin) {
        this.plugin = passedPlugin;
    }

    private Logger Log = Bukkit.getLogger();
    private HashMap<String, Location> playerHomes = new HashMap<>();

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
        Player player = (Player) commandSender;
        if (player.hasPermission("easyhome.sethome")) {
            Location tmploc = player.getLocation();
            playerHomes.put(player.getName(), tmploc);
            String tmpWorld = player.getWorld().getName();
            double tmpX = tmploc.getX();
            double tmpY = tmploc.getY();
            double tmpZ = tmploc.getZ();
            plugin.getConfig().set("homes." + String.valueOf(player.getUniqueId()), tmpWorld + "," + tmpX + "," + tmpY + "," + tmpZ);
            plugin.saveConfig();
            player.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "[EasyHome]" + ChatColor.WHITE + "" + " Home set!");
        } else {
            player.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "[EasyHome]" + ChatColor.WHITE + "" + " You don't have the right permission");
        }
        return false;
    }
}
