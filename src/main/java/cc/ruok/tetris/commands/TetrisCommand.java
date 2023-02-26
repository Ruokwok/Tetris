package cc.ruok.tetris.commands;

import cc.ruok.tetris.Ranking;
import cc.ruok.tetris.TetrisGame;
import cc.ruok.tetris.TetrisSetting;
import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;

import java.util.Map;

public class TetrisCommand extends Command {

    public TetrisCommand() {
        super("tetris");
    }

    @Override
    public boolean execute(CommandSender commandSender, String s, String[] args) {
        if (commandSender.isPlayer()) {
            Player player = (Player) commandSender;
            if (args.length > 0) {
                switch (args[0]) {
                    case "set":
                        if (player.isOp()) {
                            TetrisSetting.getInstance().setting(player);
                        } else {
                            return false;
                        }
                        break;
                    case "config":
                        if (player.isOp()) {
                            TetrisSetting.config(player);
                        } else {
                            return false;
                        }
                        break;
                    case "play":
                        if (TetrisGame.getStats() == 0) {
                            new Thread(() -> TetrisGame.start(player)).start();
                            return true;
                        } else {
                            commandSender.sendMessage("§c游戏正在进行");
                            return false;
                        }
                    case "ranking":
                        if (args.length == 2 && args[1].equals("set") && player.isOp()) {
                            Ranking.setFloatingText(player); return true;
                        } else if (args.length == 2 && args[1].equals("remove") && player.isOp()) {
                            Ranking.removeFloatingText(player); return true;
                        }
                        player.sendMessage("§l§e==== §6俄罗斯方块 §f- §c排行榜 §e====");
                        int i = 0;
                        Map<String, String> map = Ranking.sort();
                        for (Map.Entry<String, String> entry : map.entrySet()) {
                            player.sendMessage("§e[§a" + ++i + "§e] §e" + entry.getKey() + " §f- §l§b" + entry.getValue());
                        }
                        break;
                    case "help":
                        player.sendMessage("§e[§6俄罗斯方块§e] §b帮助");
                        player.sendMessage("§c方向键 - §b§e§l左右移动");
                        player.sendMessage("§c前进/跳跃 - §b§e§l旋转");
                        player.sendMessage("§c后退 - §b§e§l下落");
                        player.sendMessage("§a/tetris play §f- §b§l开始游戏");
                        player.sendMessage("§a/tetris ranking §f- §b§l查看排行榜");
                        if (player.isOp()) {
                            player.sendMessage("§a/tetris ranking set §f- §b§l设置排行榜位置");
                            player.sendMessage("§a/tetris ranking remove §f- §b§l移除排行榜");
                            player.sendMessage("§a/tetris set §f- §b§l俄罗斯方块画布设置");
                            player.sendMessage("§a/tetris config §f- §b§l俄罗斯方块详细设置");
                        }

                }

            } else {
                player.sendMessage("§e[§6俄罗斯方块§e] §h查看帮助§f:");
                player.sendMessage("§a/tetris §bhelp");
            }
        } else {
            commandSender.sendMessage("§d该指令不能以控制台执行!");
        }
        return false;
    }
}
