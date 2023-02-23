package cc.ruok.tetris.tasks;

import cc.ruok.tetris.TetrisGame;
import cc.ruok.tetris.music.SongPlayer;
import cn.nukkit.level.Position;
import cn.nukkit.scheduler.Task;

public class BgmTask extends Task {

    private int index = 0;
    private SongPlayer sp;
    private boolean track = true;
    private TetrisGame game;
    private Position position;

    public BgmTask(SongPlayer sp) {
        this.sp = sp;
        game = TetrisGame.getGame();
        position = new Position(TetrisGame.getConfig().playX, TetrisGame.getConfig().playY, TetrisGame.getConfig().playZ);
    }

    @Override
    public void onRun(int i) {
        if (index > sp.getSong().getLength()) index = 0;
        Position pos;
        if (TetrisGame.getConfig().direction <= 1) {
            if (track) {
                pos = position.clone().add(1);
            } else {
                pos = position.clone().add(-1);
            }
        } else {
            if (track) {
                pos = position.clone().add(0, 0, 1);
            } else {
                pos = position.clone().add(0, 0, -1);
            }
        }
        track = !track;
        sp.playTick(index++, pos);
    }
}
