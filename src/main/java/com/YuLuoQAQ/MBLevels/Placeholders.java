/**
 * Author: YuLuoQAQ (Github)
 * Github Page: https://github.com/YuLuoQAQ/
 * Respository: https://github.com/YuLuoQAQ/MBLevels
 * Version: 1.0
 */
package com.YuLuoQAQ.MBLevels;

import me.clip.placeholderapi.PlaceholderAPI;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

public class Placeholders extends PlaceholderExpansion {

    private final MBLevels plugin;
    private final Map<String, CacheEntry> cachedResults = new ConcurrentHashMap<>();
    private static final long CACHE_DURATION = 10000; // 10秒缓存过期时间

    public Placeholders(MBLevels plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getIdentifier() {
        return "bwlevel";
    }

    public void clearCache() {
        cachedResults.clear();
    }

    @Override
    public String getAuthor() {
        return plugin.getDescription().getAuthors().toString();
    }

    @Override
    public String getVersion() {
        return plugin.getDescription().getVersion();
    }

    @Override
    public boolean persist() {
        return true;
    }
    private Map<String, String> lastResults = new HashMap<>();
    @Override
    public String onPlaceholderRequest(Player player, String identifier) {
        if (player == null) {
            return "";
        }

        String cacheKey = player.getUniqueId().toString() + "_" + identifier;
        CacheEntry cacheEntry = cachedResults.get(cacheKey);
        String lastValue = lastResults.get(cacheKey);

        if (cacheEntry != null && !cacheEntry.isExpired()) {
            return cacheEntry.getValue();
        }

        CompletableFuture.runAsync(() -> {
            while (true) {
                // 使用 PlaceholderAPI 获取统计数据
                String winsStr = PlaceholderAPI.setPlaceholders(player, "%mbedwars_stats-wins%");
                String killsStr = PlaceholderAPI.setPlaceholders(player, "%mbedwars_stats-kills%");
                String finalKillsStr = PlaceholderAPI.setPlaceholders(player, "%mbedwars_stats-final_kills%");
                String bedsDestroyedStr = PlaceholderAPI.setPlaceholders(player, "%mbedwars_stats-beds_destroyed%");

                // 检查是否获取到的是 "正在加载..."，如果是则等待
                if ("正在加载...".equals(winsStr) || "正在加载...".equals(killsStr) ||
                        "正在加载...".equals(finalKillsStr) || "正在加载...".equals(bedsDestroyedStr)) {
                    try {
                        Thread.sleep(1000); // 等待1秒
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        return;
                    }
                    continue;
                }

                // 解析并计算经验和等级
                int wins = Integer.parseInt(winsStr);
                int kills = Integer.parseInt(killsStr);
                int finalKills = Integer.parseInt(finalKillsStr);
                int bedsDestroyed = Integer.parseInt(bedsDestroyedStr);

                int totalExp = plugin.getLevelManager().calculateExp(wins, kills, finalKills, bedsDestroyed);
                int level = plugin.getLevelManager().getLevel(totalExp);
                int nextLevelExp = plugin.getLevelManager().getExpToNextLevel(level);
                int totalExpForCurrentLevel = plugin.getLevelManager().getTotalExpForLevel(level) + 100;
                if (plugin.getLevelManager().getTotalExpForLevel(level)<100){
                    totalExpForCurrentLevel = plugin.getLevelManager().getTotalExpForLevel(level);
                }
                int remainingExp = totalExp - totalExpForCurrentLevel;
                String levelSign = plugin.getLevelManager().getLevelSign(level);

                String result;
                switch (identifier) {
                    case "level":
                        result = String.valueOf(level);
                        break;
                    case "exp":
                        result = String.valueOf(totalExp);
                        break;
                    case "nextexp":
                        result = String.valueOf(nextLevelExp);
                        break;
                    case "rate":
                        double progress = (double) remainingExp / nextLevelExp;
                        result = String.format("%d", (int) (progress * 100));
                        break;
                    case "next":
                        result = String.valueOf(remainingExp);
                        break;
                    case "sign":
                        result = levelSign;
                        break;
                    default:
                        result = "";
                }

                // 更新缓存结果
                cachedResults.put(cacheKey, new CacheEntry(result, System.currentTimeMillis()));
                lastResults.put(cacheKey, result);
                break;
            }
        });

        return lastValue; // 如果没有缓存，就返回默认消息
    }


    // 缓存条目类，存储值和缓存时间戳
    private static class CacheEntry {
        private final String value;
        private final long timestamp;

        CacheEntry(String value, long timestamp) {
            this.value = value;
            this.timestamp = timestamp;
        }

        String getValue() {
            return value;
        }

        boolean isExpired() {
            return (System.currentTimeMillis() - timestamp) > CACHE_DURATION;
        }
    }
}
