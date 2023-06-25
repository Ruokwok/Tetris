package cc.ruok.tetris.listeners;

import cc.ruok.tetris.L;
import cc.ruok.tetris.TetrisSetting;
import cn.nukkit.Player;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.block.BlockBreakEvent;
import cn.nukkit.level.Position;

public class SettingListener implements Listener {

    private Player player;
    public Position p1;
    public Position p2;

    public SettingListener(Player player) {
        this.player = player;
    }

    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        if (event.getPlayer() == player && TetrisSetting.getInstance().getStep() == 2) {
            Thread runnable = new Thread(() -> {
                try {
                    Thread.sleep(50);
                    event.getBlock().getLevel().setBlock(event.getBlock(), event.getBlock());
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            });
            runnable.start();
            if (p1 == null) {
                p1 = event.getBlock().getLocation().clone();
                player.sendMessage(L.get("tetris.setting.step.3"));
            } else {
                try {
                    if (event.getBlock().y != p1.y - 19 ||
                            (Math.abs(event.getBlock().x - p1.x) == 9
                                    && Math.abs(event.getBlock().z - p1.z) == 9) ||
                            (p1.x != event.getBlock().x && p1.z != event.getBlock().z)) {
                        player.sendMessage(L.get("tetris.setting.error"));
                        TetrisSetting.getInstance().resetStep();
                    } else {
                        p2 = event.getBlock().getLocation();
                        player.sendMessage(L.get("tetris.setting.step.4"));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
