package cc.ruok.tetris.tasks;

import cc.ruok.tetris.TetrisGame;
import cn.nukkit.block.Block;
import cn.nukkit.level.Position;
import cn.nukkit.scheduler.Task;

public class EndTask extends Task {

    private TetrisGame game;
    private int line;
    private boolean d;

    public EndTask(TetrisGame game) {
        this.game = game;
        line = 0;
        d = true;
    }

    @Override
    public void onRun(int _i) {
        for (int i = 0; i < 10; i++) {
            Position position = TetrisGame.getPosition(i, line);
            if (d) {
                if (position != null) {
                    TetrisGame.getLevel().setBlock(position, Block.get(35, 5));
                }
            } else  {
                if (position != null) {
                    TetrisGame.getLevel().setBlock(position, Block.get(0));
                }
            }
        }
        if (d) {
            line++;
        } else {
            line--;
        }
        if (line >= 20) {
            d = false;
        }
        if (line < 0) {
            game.setStats(false);
            cancel();
        }
    }
}
