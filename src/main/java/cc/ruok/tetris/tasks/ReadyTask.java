package cc.ruok.tetris.tasks;

import cc.ruok.tetris.Tetris;
import cc.ruok.tetris.TetrisGame;
import cn.nukkit.Server;
import cn.nukkit.scheduler.Task;

public class ReadyTask extends Task {

    private int i = 3;

    @Override
    public void onRun(int _i) {
        TetrisGame game = TetrisGame.getGame();
        if (i == 0) {
            game.getPlayer().sendTitle("开始!");
            game.spawn();
            game.setStats(2);
            Server.getInstance().getScheduler().scheduleRepeatingTask(Tetris.tetris, game.getGameTask(), 10);
            cancel();
        } else {
            game.getPlayer().sendTitle(String.valueOf(i));
            i--;
        }
    }
}
