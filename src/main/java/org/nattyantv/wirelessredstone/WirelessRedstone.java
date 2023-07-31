package org.nattyantv.wirelessredstone;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public final class WirelessRedstone extends JavaPlugin {
    private static WirelessRedstone plugin;

    private void createDefaultConfig() {
        FileConfiguration config = getConfig();
        config.createSection("wrdata");
        saveConfig();
    }

    @Override
    public void onEnable() {
        plugin = this;
        FileConfiguration config = getConfig();
        if (config.getConfigurationSection("wrdata") == null) {
            createDefaultConfig();
        } else {
            restoreData();
        }
        getServer().getPluginManager().registerEvents(new WREventListener(), this);
        getCommand("wr").setExecutor(new WRCommand());
        getLogger().info("[WirelessRedstone] Enable WirelessRedstone Plugin");
    }

    public void saveData() {
        getConfig().set("wrdata", null);
        HashMaps.map.forEach((key, value) -> {
            getConfig().set("wrdata." + key, value);
        });
        saveConfig();
    }

    private void restoreData() {
        getConfig().getConfigurationSection("wrdata").getKeys(false).forEach(key -> {
            HashMaps.map.put(key, (getConfig().getLocation("wrdata." + key)));
        });
    }

    public static WirelessRedstone getPlugin() {
        return plugin;
    }

    @Override
    public void onDisable() {
        saveData();
        getLogger().info("[WirelessRedstone] Disable WirelessRedstone Plugin");
    }
}
