package cc.ruok.tetris.tasks;

import cc.ruok.tetris.Ranking;
import cc.ruok.tetris.Tetris;
import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.scheduler.Task;

import java.util.Collection;

public class UpdateRankingTask extends Task {

    @Override
    public void onRun(int i) {
        if (Tetris.tetris.config.ranking != null) {
            Collection<Player> players = Server.getInstance().getOnlinePlayers().values();
            for (Player player : players) {
                if (Tetris.tetris.config.level.equals(player.getLevel().getName())) {
                    Ranking.removeFloatingText(player);
                    Ranking.updateFloatingText(player);
                }
            }
        }
    }
}
