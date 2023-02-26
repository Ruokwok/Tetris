package cc.ruok.tetris.tasks;

import cc.ruok.tetris.Tetris;
import cc.ruok.tetris.TetrisGame;
import cn.nukkit.Server;
import cn.nukkit.level.Sound;
import cn.nukkit.scheduler.Task;

public class ReadyTask extends Task {

    private int i = 3;

    @Override
    public void onRun(int _i) {
        TetrisGame game = TetrisGame.getGame();
        if (i == 0) {
            game.getPlayer().sendTitle("§l§a开始!");
            if (TetrisGame.getConfig().bgm) TetrisGame.getLevel().addSound(game.getPlayer(), Sound.NOTE_HARP, 1, 1.498307F);
            game.spawn();
            game.setStats(2);
            if (TetrisGame.getConfig().bgm) game.playBGM();
            Server.getInstance().getScheduler().scheduleRepeatingTask(Tetris.tetris, game.getGameTask(), game.getSpeed());
            cancel();
        } else {
            game.getPlayer().sendTitle("§l§e" + i);
            if (TetrisGame.getConfig().bgm) TetrisGame.getLevel().addSound(game.getPlayer(), Sound.NOTE_HARP, 1, 0.749154F);
            i--;
        }
    }
}
