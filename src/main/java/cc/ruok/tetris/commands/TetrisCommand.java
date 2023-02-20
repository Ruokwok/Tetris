package cc.ruok.tetris.commands;

import cc.ruok.tetris.TetrisGame;
import cc.ruok.tetris.TetrisSetting;
import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;

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
                            commandSender.sendMessage("游戏正在进行");
                            return false;
                        }
                    case "help":
                        player.sendMessage("[俄罗斯方块] 帮助");
                        player.sendMessage("方向键 - 左右移动");
                        player.sendMessage("前进/跳跃 - 旋转");
                        player.sendMessage("后退 - 下落");
                        player.sendMessage("/tetris play - 开始游戏");
                        if (player.isOp()) {
                            player.sendMessage("/tetris set - 俄罗斯方块设置");
                        }
                }

            }
        }
        return false;
    }
}
