package cc.ruok.tetris.listeners;

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
                player.sendMessage("§e第3步: §a请破坏最右下角的方块");
            } else {
                try {
                    if (event.getBlock().y != p1.y - 19 ||
                            (Math.abs(event.getBlock().x - p1.x) == 9
                                    && Math.abs(event.getBlock().z - p1.z) == 9) ||
                            (p1.x != event.getBlock().x && p1.z != event.getBlock().z)) {
                        player.sendMessage("§c操作有误，重置步骤!请检查画布尺寸是否正确(高20,宽10)");
                        TetrisSetting.getInstance().resetStep();
                    } else {
                        p2 = event.getBlock().getLocation();
                        player.sendMessage("§e第4步: §a请前往进行游戏的最佳位置，再次输入/tetris set");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
