/**
 * Author: YuLuoQAQ (Github)
 * Github Page: https://github.com/YuLuoQAQ/
 * Respository: https://github.com/YuLuoQAQ/MBLevels
 * Version: 1.0
 */
package com.YuLuoQAQ.MBLevels;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.configuration.file.FileConfiguration;

public class MBLevels extends JavaPlugin {

    private static MBLevels instance;
    private LevelManager levelManager;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();

        levelManager = new LevelManager(this);

        if (getServer().getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new Placeholders(this).register();
        }

        getCommand("bwlevel").setExecutor(new Commands(this));

        getLogger().info("MBlevels has been enabled!");
    }

    @Override
    public void onDisable() {
        getLogger().info("MBlevels has been disabled!");
    }

    public static MBLevels getInstance() {
        return instance;
    }

    public LevelManager getLevelManager() {
        return levelManager;
    }
}
