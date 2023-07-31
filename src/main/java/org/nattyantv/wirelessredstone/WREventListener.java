package org.nattyantv.wirelessredstone;

import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.scheduler.BukkitRunnable;

import it.unimi.dsi.fastutil.Hash;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.TreeSpecies;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;


public class WREventListener implements Listener {
    public void createReciver(String name, Location location) {
        HashMaps.map.put(name, location);
        WirelessRedstone.getPlugin().saveData();
    }

    @EventHandler
    public void onSignChange(SignChangeEvent event) {
        String[] lines = event.getLines();
        if (lines[0].equals("[WRRECV]")) {
            if (lines[1].equals("")) {
                event.getPlayer().sendMessage("[WR]: 受信機の名前を指定してください。");
                event.getBlock().breakNaturally();
                return;
            }
            // 同じ名前の受信機が存在する場合
            if (HashMaps.map.containsKey(lines[1])) {
                Location location = HashMaps.map.get(lines[1]);
                if (location.equals(event.getBlock().getLocation())) {
                    return;
                }
                event.getPlayer().sendMessage("[WR]: その名前の受信機は既に存在します。");
                event.getBlock().breakNaturally();
                return;
            }
            createReciver(lines[1], event.getBlock().getLocation());
            event.getPlayer().sendMessage("[WR]: 受信機のポイント" + lines[1] + "を作成しました！");
        } else if (lines[0].equals("[WRSEND]")) {
            if (lines[1].equals("")) {
                event.getPlayer().sendMessage("[WR]: 受信機の名前を指定してください。");
                event.getBlock().breakNaturally();
                return;
            }
            event.getPlayer().sendMessage("[WR]: 送信機のポイント" + lines[1] + "を作成しました！");
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Block block = event.getBlock();
        if (block.getType().toString().contains("_SIGN")) {
            Sign sign = (Sign) block.getState();
            String[] lines = sign.getLines();
            if (lines[0].equals("[WRRECV]")) {
                String name = lines[1];
                if (HashMaps.map.containsKey(name)) {
                    HashMaps.map.remove(name);
                    WirelessRedstone.getPlugin().saveData();
                    event.getPlayer().sendMessage("[WR]: 受信機のポイント" + name + "を削除しました！");
                }
            }
        }
    }

    @EventHandler
    public void onBlockRedstone(BlockRedstoneEvent event) {
        Block block = event.getBlock();
        if (block.getType().equals(Material.REPEATER)) {
            Boolean isPowered = event.getNewCurrent() > 0;
            BlockFace face = ((org.bukkit.block.data.Directional) block.getBlockData()).getFacing();
            // faceの向きを反転する
            if (face.equals(BlockFace.NORTH)) {
                face = BlockFace.SOUTH;
            } else if (face.equals(BlockFace.SOUTH)) {
                face = BlockFace.NORTH;
            } else if (face.equals(BlockFace.EAST)) {
                face = BlockFace.WEST;
            } else if (face.equals(BlockFace.WEST)) {
                face = BlockFace.EAST;
            }
            Block connected = block.getRelative(face);
            if (connected.getType().toString().contains("_SIGN")) {
                if (!(connected.getState() instanceof Sign)) {
                    return;
                }
                Sign sign = (Sign) connected.getState();
                String[] lines = sign.getLines();
                if (lines[0].equals("[WRSEND]")) {
                    String name = lines[1];
                    if (HashMaps.map.containsKey(name)) {
                        Location location = HashMaps.map.get(name);
                        Block reciver = location.getBlock();
                        if (isPowered) {
                            reciver.setType(Material.REDSTONE_TORCH);
                        } else {
                            reciver.setType(Material.OAK_SIGN);
                            Sign recv_sign = (Sign) reciver.getState();
                            recv_sign.setLine(0, "[WRRECV]");
                            recv_sign.setLine(1, name);
                            recv_sign.update();
                        }
                    }
                }
            }
        }
    }
}
