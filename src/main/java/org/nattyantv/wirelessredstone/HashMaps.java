package org.nattyantv.wirelessredstone;

import org.bukkit.Location;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;

public class HashMaps extends JavaPlugin implements Listener {
    public static HashMap<String, Location> map = new HashMap<String, Location>();
}
