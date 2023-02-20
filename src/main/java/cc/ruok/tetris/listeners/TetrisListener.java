package cc.ruok.tetris.listeners;

import cc.ruok.tetris.Tetris;
import cc.ruok.tetris.TetrisGame;
import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.block.BlockBreakEvent;
import cn.nukkit.event.player.*;
import cn.nukkit.level.Position;

public class TetrisListener implements Listener {

    int ori = 1;
    boolean press = false;

    private Player player;
    private TetrisGame game;
    private Position position;

    public TetrisListener(Player player, TetrisGame game) {
        this.player = player;
        this.game = game;
        position = new Position(
                    Tetris.tetris.config.playX,
                    Tetris.tetris.config.playY,
                    Tetris.tetris.config.playZ,
                    TetrisGame.getLevel()
                );
    }

    @EventHandler
    public void onQUit(PlayerQuitEvent event) {
        if (event.getPlayer() == player) {
            game.stop();
        }
    }

    @EventHandler
    public void onDead(PlayerDeathEvent event) {
        if (event.getEntity() == player) {
            game.stop();
        }
    }

    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        if (event.getPlayer() == null) {
            event.setDrops(null);
        }
    }

    @EventHandler
    public void onJump(PlayerJumpEvent event) {
//        event.setCancelled();
        if (event.getPlayer() == player) {
            game.rotate();
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (event.getPlayer() == game.getPlayer()) {
            game.fall();
        }
    }

    @EventHandler
    public void onSprint(PlayerToggleSprintEvent event) {
        if (event.getPlayer() == player) {
            player.setSprinting(false);
        }
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        if (event.getPlayer() != player) return;
        if (press) return;
        press = true;
        Server.getInstance().getScheduler().scheduleDelayedTask(this::onPress, 8);

        double x = event.getTo().x - event.getFrom().x;
        double z = event.getTo().z - event.getFrom().z;
        double _x = (x >= 0)? x : -x;
        double _z = (z >= 0)? z : -z;
        if (x == 0 && z == 0) return;
        event.getPlayer().teleport(position);
        if (_x > _z) {
            if (x >= 0) {
                if (Tetris.tetris.config.direction == 0) game.move(2);
                if (Tetris.tetris.config.direction == 1) game.move(1);
                if (Tetris.tetris.config.direction == 2) game.fall();
                if (Tetris.tetris.config.direction == 3) game.rotate();
            } else {
                if (Tetris.tetris.config.direction == 0) game.move(1);
                if (Tetris.tetris.config.direction == 1) game.move(2);
                if (Tetris.tetris.config.direction == 2) game.rotate();
                if (Tetris.tetris.config.direction == 3) game.fall();
            }
        } else {
            if (z >= 0) {
                if (Tetris.tetris.config.direction == 0) game.fall();
                if (Tetris.tetris.config.direction == 1) game.rotate();
                if (Tetris.tetris.config.direction == 2) game.move(1);
                if (Tetris.tetris.config.direction == 3) game.move(2);
            } else {
                if (Tetris.tetris.config.direction == 0) game.rotate();
                if (Tetris.tetris.config.direction == 1) game.fall();
                if (Tetris.tetris.config.direction == 2) game.move(2);
                if (Tetris.tetris.config.direction == 3) game.move(1);
            }
        }
    }

    public void onPress() {
        press = false;
    }

    public Position getPosition() {
        return position;
    }

}
