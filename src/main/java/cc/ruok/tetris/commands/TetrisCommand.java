package cc.ruok.tetris.commands;

import cc.ruok.tetris.L;
import cc.ruok.tetris.Ranking;
import cc.ruok.tetris.TetrisGame;
import cc.ruok.tetris.TetrisSetting;
import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;

import java.util.Map;

public class TetrisCommand extends Command {

    public TetrisCommand() {
        super("tetris", L.get("tetris.title"));
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
                            commandSender.sendMessage(L.get("command.in_game"));
                            return false;
                        }
                    case "ranking":
                        if (args.length == 2 && args[1].equals("set") && player.isOp()) {
                            Ranking.setFloatingText(player); return true;
                        } else if (args.length == 2 && args[1].equals("remove") && player.isOp()) {
                            Ranking.removeFloatingText(player); return true;
                        }
                        player.sendMessage(L.get("command.ranking"));
                        int i = 0;
                        Map<String, String> map = Ranking.sort();
                        for (Map.Entry<String, String> entry : map.entrySet()) {
                            player.sendMessage("§c[§d" + ++i + "§c] §e" + entry.getKey() + " §f- §l§b" + entry.getValue());
                        }
                        break;
                    case "help":
                        player.sendMessage(L.get("command.help.title"));
                        player.sendMessage(L.get("command.help.move"));
                        player.sendMessage(L.get("command.help.rotate"));
                        player.sendMessage(L.get("command.help.fall"));
                        player.sendMessage(L.get("command.help.start"));
                        player.sendMessage("§a/tetris ranking §f- " + L.get("command.help.ranking"));
                        if (player.isOp()) {
                            player.sendMessage("§a/tetris ranking set §f- " + L.get("command.help.ranking.set"));
                            player.sendMessage("§a/tetris ranking remove §f- " + L.get("command.help.ranking.remove"));
                            player.sendMessage("§a/tetris set §f- " + L.get("command.help.set"));
                            player.sendMessage("§a/tetris config §f- " + L.get("command.help.config"));
                        }

                }

            } else {
                player.sendMessage(L.get("command.help"));
                player.sendMessage("§a/tetris §bhelp");
            }
        } else {
            commandSender.sendMessage(L.get("command.console.warning"));
        }
        return false;
    }
}
