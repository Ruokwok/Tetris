package cc.ruok.tetris.tasks;

import cc.ruok.tetris.TetrisGame;
import cc.ruok.tetris.music.SongPlayer;
import cn.nukkit.scheduler.Task;

public class BgmTask extends Task {

    private int index = 0;
    private SongPlayer sp;

    public BgmTask(SongPlayer sp) {
        this.sp = sp;
    }

    @Override
    public void onRun(int i) {
        if (index > sp.getSong().getLength()) index = 0;
        sp.playTick(index++, TetrisGame.getGame().getPlayer());
    }
}
