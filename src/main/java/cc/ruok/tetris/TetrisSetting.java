package cc.ruok.tetris;

import cc.ruok.tetris.listeners.SettingListener;
import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.block.Block;
import cn.nukkit.event.HandlerList;
import cn.nukkit.form.element.*;
import cn.nukkit.form.window.FormWindowCustom;
import cn.nukkit.form.window.FormWindowModal;
import cn.nukkit.form.window.FormWindowSimple;
import cn.nukkit.level.Level;
import cn.nukkit.level.Location;
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
//        System.out.println(step);
        switch (step) {
            case 0: set1(); break;
            case 1: set2(); break;
            case 2: set3(); break;
        }
    }

    public void set1() {
        step = 1;
        listener = new SettingListener(player);
        player.sendMessage("第1步: 请建造一个 高20 宽10 的结构作为游戏画布");
        Server.getInstance().getPluginManager().registerEvents(listener, Tetris.tetris);
        FormWindowModal window = new FormWindowModal("俄罗斯方块",
                "第1步:\n" +
                        "请建造一个 高20 宽10 的结构作为游戏画布\n" +
                        "*可使用任意方块\n" +
                        "*可位于x轴或z轴平面\n" +
                        "建造完成后请再次使用/tetris set命令进行下一步", "知道了", "关闭");
        player.showFormWindow(window);
    }

    public void set2() {
        step = 2;
        level = player.getLevel();
        player.sendMessage("第2步: 请破坏最左上角的方块");
    }

    public void set3() {
        step = 0;
        position = player.getPosition();
        player.sendMessage("设置完成! 详细配置请使用/tetris config修改");
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
        player.sendMessage("已为您生成默认边框，边框可自行修改建造。");
    }

    public static void config(Player player) {
        TetrisConfig config = Tetris.tetris.config;
        if (config == null) {
            player.sendMessage("请先使用/tetris set设置游戏画布.");
            return;
        }
        ArrayList<String> keen = new ArrayList<>();
        keen.add("低");
        keen.add("中");
        keen.add("高");
        ArrayList<String> blocks = new ArrayList<>();
        blocks.add("羊毛");
        blocks.add("混凝土");
        blocks.add("染色玻璃");
        blocks.add("陶瓦");
        ArrayList<String> speed = new ArrayList<>();
        speed.add("0.2x");
        speed.add("0.5x");
        speed.add("1.0x");
        speed.add("1.5x");
        ArrayList<String> effects = new ArrayList<>();
        effects.add("方块破坏");
        effects.add("爆炸");
        effects.add("大爆炸");
        effects.add("白色烟雾");
        effects.add("无");
        FormWindowCustom window = new FormWindowCustom("俄罗斯方块");
        window.addElement(new ElementLabel("俄罗斯方块游戏设置"));
        window.addElement(new ElementStepSlider("方块下落速度 ", speed, config.speed));
        window.addElement(new ElementStepSlider("方向键灵敏度 ", keen, config.keen));
        window.addElement(new ElementDropdown("方块类型", blocks, config.block));
        window.addElement(new ElementDropdown("粒子效果", effects, config.effect < 0 ? 4 : config.effect));
        window.addElement(new ElementToggle("BGM", config.bgm));
        player.showFormWindow(window, 1145142233);
    }

}
