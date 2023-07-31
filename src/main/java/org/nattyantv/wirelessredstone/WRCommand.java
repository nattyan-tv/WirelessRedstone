package org.nattyantv.wirelessredstone;

import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.Command;

public class WRCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("wr")) {
            if (args.length == 0) {
                // コマンドエラーなので、使い方を表示
                sender.sendMessage("[WR]: /wr [list|delete]\n" +
                        "list: 受信機の一覧を表示します。\n" +
                        "delete: 受信機を削除します。");
                return true;
            } else {
                if (args[0].equalsIgnoreCase("list")) {
                    // 受信機の名前と座標を表示
                    sender.sendMessage("[WR]: 現在登録されている受信機の一覧 [名前: 座標]");
                    HashMaps.map.forEach((key, value) -> {
                        sender.sendMessage(key + ": " + value.getX() + ", " + value.getY() + ", " + value.getZ());
                    });
                } else if (args[0].equalsIgnoreCase("delete")) {
                    // args[1]がnullの場合
                    if (args.length == 1) {
                        sender.sendMessage("[WR]: /wr delete [name]\n" +
                                "name: 削除する受信機の名前を指定します。");
                        return true;
                    }
                    // args[1]がnullでない場合
                    if (HashMaps.map.containsKey(args[1])) {
                        HashMaps.map.remove(args[1]);
                        WirelessRedstone.getPlugin().saveData();
                        sender.sendMessage("[WR]: 受信機のポイント" + args[1] + "を削除しました！");
                    } else {
                        sender.sendMessage("[WR]: その名前の受信機は存在しません。");
                    }
                } else {
                    sender.sendMessage("[WR]: /wr [list|delete]\n" +
                            "list: 受信機の一覧を表示します。\n" +
                            "delete: 受信機を削除します。");
                }
                return true;
            }
        }
        return false;
    }
}
