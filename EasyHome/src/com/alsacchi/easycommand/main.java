package com.alsacchi.easycommand;

import org.bukkit.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.logging.Logger;

public class main extends JavaPlugin {
    private Logger Log = Bukkit.getLogger();
    private FileConfiguration config = this.getConfig();

    @Override
    public void onEnable() {
        config.addDefault("home", true);
        config.addDefault("xpPay", false);
        config.addDefault("homeDistance", 15);
        config.addDefault("homes", "");
        config.options().copyDefaults(true);
        saveConfig();
        this.getCommand("home").setExecutor(new home(this));
        this.getCommand("sethome").setExecutor(new sethome(this));
        Log.warning("[EasyHome] Activated");
    }

    @Override
    public void onDisable() {
        Log.warning("[EasyHome] Deactivated");
    }

}


