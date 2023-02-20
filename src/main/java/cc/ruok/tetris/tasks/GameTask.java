package cc.ruok.tetris.tasks;

import cc.ruok.tetris.TetrisGame;
import cn.nukkit.scheduler.Task;

public class GameTask extends Task {
    @Override
    public void onRun(int i) {
        TetrisGame game = TetrisGame.getGame();
        game.move(0);
        game.getPlayer().sendTip("速度: 1x  方块: %index%  消除: %line%  得分: %score%"
                        .replace("%index%", String.valueOf(game.getIndex()))
                        .replace("%line%", String.valueOf(game.getClearLine()))
                        .replace("%score%", String.valueOf(game.getScore())));
    }
}
