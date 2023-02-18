package cc.ruok.tetris.listeners;

import cc.ruok.tetris.TetrisGame;
import cn.nukkit.block.Block;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.block.BlockBreakEvent;
import cn.nukkit.event.player.PlayerInteractEvent;
import cn.nukkit.item.Item;
import cn.nukkit.level.Level;
import cn.nukkit.level.ParticleEffect;
import cn.nukkit.level.Position;
import cn.nukkit.level.particle.DestroyBlockParticle;

public class BaseListener implements Listener {

    @EventHandler
    public void onI(PlayerInteractEvent event) {
        if (event.getItem().getId() != 0) return;
        Block block = event.getBlock();
        if (event.getPlayer().getName().equals("Ruok2333")) {
            event.getPlayer().sendMessage("id:" + block.getId() + " x:" + block.getX() + " y:" + block.getY() + " z:" + block.getZ());
//            TetrisGame.useBreakOn(TetrisGame.getPosition(0, 0), event.getPlayer());
//            TetrisGame.getLevel().setBlock(block, Block.get(0));
        }
    }

    @EventHandler
    public void onBreak(BlockBreakEvent event) {

    }

}
