package cc.ruok.tetris.tasks;

import cc.ruok.tetris.TetrisGame;
import cn.nukkit.scheduler.Task;

public class GameTask extends Task {
    @Override
    public void onRun(int i) {
        TetrisGame game = TetrisGame.getGame();
        game.move(0);
    }
}
