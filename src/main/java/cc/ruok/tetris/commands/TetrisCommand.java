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
                if (args[0].equals("set")) {
                    if (player.isOp()) {
                        TetrisSetting.getInstance().setting(player);
                    } else {
                        return false;
                    }
                } else if (args[0].equals("play")) {
                    player.sendMessage("即将开始");
                    player.sendMessage("左右行走控制移动  跳跃控制旋转");
                    if (!TetrisGame.getStats()) {
                        new Thread(() -> TetrisGame.start(player)).start();
                        return true;
                    } else {
                        commandSender.sendMessage("游戏正在进行");
                        return false;
                    }
                }

            }
        }
        return false;
    }
}
