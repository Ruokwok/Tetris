package cc.ruok.tetris;

import cc.ruok.tetris.listeners.SettingListener;
import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.block.Block;
import cn.nukkit.event.HandlerList;
import cn.nukkit.form.element.*;
import cn.nukkit.form.window.FormWindowCustom;
import cn.nukkit.form.window.FormWindowModal;
import cn.nukkit.level.Level;
import cn.nukkit.level.Position;

import java.util.ArrayList;

public class TetrisSetting {

    private Player player;
    private int step = 0;
    private Position position;
    private Level level;

    private SettingListener listener;

    private static TetrisSetting setting = new TetrisSetting();

    public static TetrisSetting getInstance() {
        return setting;
    }

    private TetrisSetting() {
    }

    public void resetStep() {
        step = 0;
        if (listener != null) HandlerList.unregisterAll(listener);
    }

    public void setting(Player player) {
        this.player = player;
        if (TetrisGame.getStats() != 0) {
            player.sendMessage(L.get("tetris.setting.gaming"));
            return;
        }
        switch (step) {
            case 0: set1(); break;
            case 1: set2(); break;
            case 2: set3(); break;
        }
    }

    public void set1() {
        step = 1;
        listener = new SettingListener(player);
        player.sendMessage(L.get("tetris.setting.step.1"));
        Server.getInstance().getPluginManager().registerEvents(listener, Tetris.tetris);
        FormWindowModal window = new FormWindowModal(L.get("tetris.title"), L.get("tetris.setting.step.1.1"), L.get("gui.ok"), L.get("gui.cancel"));
        player.showFormWindow(window);
    }

    public void set2() {
        step = 2;
        level = player.getLevel();
        player.sendMessage(L.get("tetris.setting.step.2"));
    }

    public void set3() {
        step = 0;
        position = player.getPosition();
        player.sendMessage(L.get("tetris.setting.finish"));
        player.sendMessage(L.get("tetris.setting.finish.tip"));
        TetrisConfig config = Tetris.tetris.config == null ? new TetrisConfig() : Tetris.tetris.config;
        config.level = level.getName();
        config.playX = position.getX();
        config.playZ = position.getZ();
        config.playY = position.getY();
        config.originX = (int) listener.p1.x;
        config.originZ = (int) listener.p1.z;
        config.originY = (int) listener.p1.y - 19;
        if (listener.p1.z == listener.p2.z) {
            if (listener.p1.x > listener.p2.x) {
                config.direction = 1;
            } else {
                config.direction = 0;
            }
        }
        if (listener.p1.x == listener.p2.x) {
            if (listener.p1.z > listener.p2.z) {
                config.direction = 2;
            } else {
                config.direction = 3;
            }
        }
        HandlerList.unregisterAll(listener);
        config.save(Tetris.tetris.configFile);
        Tetris.tetris.config = config;
        generateBorder(config, player);
    }

    public int getStep() {
        return step;
    }

    /**
     * 生成默认边框
     * @param config 配置文件
     * @param player 管理员
     */
    public static void generateBorder(TetrisConfig config, Player player) {
        TetrisGame.origin = new Position(config.originX, config.originY, config.originZ);
        Level level = Server.getInstance().getLevelByName(config.level);
        int x = -1;
        int y = -1;
        boolean color = true;
        for (int i = 0; i < 64; i++) {
            Position pos = TetrisGame.getPosition(x, y, false);
            if (i <= 20) {
                y++;
            } else if (i <= 31) {
                x++;
            } else if (i <= 52) {
                y--;
            } else {
                x--;
            }
            color = !color;
            if (level.getBlock(pos).getId() == 0) {
                level.setBlock(pos, Block.get(236, color ? 4 : 15));
            }
        }
        player.sendMessage(L.get("tetris.setting.rim"));
    }

    public static void config(Player player) {
        TetrisConfig config = Tetris.tetris.config;
        if (config == null) {
            player.sendMessage(L.get("tetris.setting.tip"));
            return;
        }
        ArrayList<String> keen = new ArrayList<>();
        keen.add(L.get("gui.button.low"));
        keen.add(L.get("gui.button.middle"));
        keen.add(L.get("gui.button.high"));
        ArrayList<String> blocks = new ArrayList<>();
        blocks.add(L.get("gui.button.wool"));
        blocks.add(L.get("gui.button.concrete"));
        blocks.add(L.get("gui.button.glass"));
        blocks.add(L.get("gui.button.terracotta"));
        ArrayList<String> speed = new ArrayList<>();
        speed.add("0.2x");
        speed.add("0.5x");
        speed.add("1.0x");
        speed.add("1.5x");
        ArrayList<String> effects = new ArrayList<>();
        effects.add(L.get("gui.button.break"));
        effects.add(L.get("gui.button.explode"));
        effects.add(L.get("gui.button.big_explode"));
        effects.add(L.get("gui.button.smoke"));
        effects.add(L.get("gui.button.nothing"));
        FormWindowCustom window = new FormWindowCustom(L.get("tetris.title"));
        window.addElement(new ElementLabel(L.get("tetris.setting")));
        window.addElement(new ElementStepSlider(L.get("gui.speed") + " ", speed, config.speed));
        window.addElement(new ElementStepSlider(L.get("gui.sensitivity") + " ", keen, config.keen));
        window.addElement(new ElementDropdown(L.get("gui.type"), blocks, config.block));
        window.addElement(new ElementDropdown(L.get("gui.particle"), effects, config.effect < 0 ? 4 : config.effect));
        window.addElement(new ElementToggle("BGM", config.bgm));
        player.showFormWindow(window, 1145142233);
    }

}
