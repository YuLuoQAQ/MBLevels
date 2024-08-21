/**
 * Author: YuLuoQAQ (Github)
 * Github Page: https://github.com/YuLuoQAQ/
 * Respository: https://github.com/YuLuoQAQ/MBLevels
 * Version: 1.0
 */
package com.YuLuoQAQ.MBLevels;

import org.bukkit.configuration.file.FileConfiguration;

public class LevelManager {

    private final MBLevels plugin;

    public LevelManager(MBLevels plugin) {
        this.plugin = plugin;
    }

    public double getExpRate(int totalExp) {
        int level = getLevel(totalExp);
        int expToNextLevel = getExpToNextLevel(level);
        int remainingExp = totalExp - getTotalExpForLevel(level);
        return (double) remainingExp / expToNextLevel;
    }

    public int calculateExp(int wins, int kills, int finalKills, int bedsDestroyed) {
        FileConfiguration config = plugin.getConfig();
        int exp = wins * config.getInt("exp-formula.wins")
                + kills * config.getInt("exp-formula.kills")
                + finalKills * config.getInt("exp-formula.final_kills")
                + bedsDestroyed * config.getInt("exp-formula.beds_destroyed");
        return exp;
    }

    public int getLevel(int totalExp) {
        FileConfiguration config = plugin.getConfig();
        int level = 0;
        int currentExp = totalExp;

        for (String key : config.getConfigurationSection("levels").getKeys(false)) {
            int rankupCost = config.getInt("levels." + key + ".rankup-cost");
            int[] range = parseRange(key);

            for (int i = range[0]; i <= range[1]; i++) {
                if (currentExp >= rankupCost) {
                    currentExp -= rankupCost;
                    level++;
                } else {
                    return level;
                }
            }
        }
        return level;
    }

    public int getExpToNextLevel(int level) {
        FileConfiguration config = plugin.getConfig();
        String levelKey = getLevelKeyForLevel(level + 1);
        return getRankupCostForLevel(levelKey);
    }

    public String getLevelSign(int level) {
        FileConfiguration config = plugin.getConfig();
        String levelKey = getLevelKeyForLevel(level);
        String signTemplate = config.getString("levels." + levelKey + ".sign", "&7{level}✩");
        return signTemplate.replace("{level}", String.valueOf(level));
    }

    private String getLevelKeyForLevel(int level) {
        FileConfiguration config = plugin.getConfig();

        for (String key : config.getConfigurationSection("levels").getKeys(false)) {
            int[] range = parseRange(key);

            if (level >= range[0] && level <= range[1]) {
                return key;
            }
        }

        return null;
    }

    private int getRankupCostForLevel(String rangeKey) {
        if (rangeKey == null) {
            return 0;  // 如果没有找到合适的范围，返回0
        }
        FileConfiguration config = plugin.getConfig();
        String path = "levels." + rangeKey + ".rankup-cost";
        return config.getInt(path, 0);  // 如果路径不存在，返回0
    }

    private int[] parseRange(String key) {
        if (key.contains("-")) {
            String[] rangeParts = key.split("-");
            int start = Integer.parseInt(rangeParts[0]);
            int end = Integer.parseInt(rangeParts[1]);
            return new int[]{start, end};
        } else {
            int exactLevel = Integer.parseInt(key);
            return new int[]{exactLevel, exactLevel};
        }
    }

    int getTotalExpForLevel(int level) {
        int totalExp = 0;
        for (int i = 1; i < level; i++) {
            String rangeKey = getRangeKeyForLevel(i);
            totalExp += getRankupCostForLevel(rangeKey);
        }
        return totalExp;
    }
    private String getRangeKeyForLevel(int level) {
        FileConfiguration config = plugin.getConfig();
        for (String rangeKey : config.getConfigurationSection("levels").getKeys(false)) {
            String[] rangeParts = rangeKey.split("-");
            int start = Integer.parseInt(rangeParts[0]);
            int end = Integer.parseInt(rangeParts[1]);
            if (level >= start && level <= end) {
                return rangeKey;
            }
        }
        return null; // 如果没有匹配到合适的范围，则返回 null
    }
}
