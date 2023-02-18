package cc.ruok.tetris;

import cc.ruok.tetris.listeners.SettingListener;
import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.event.HandlerList;
import cn.nukkit.form.window.FormWindowModal;
import cn.nukkit.form.window.FormWindowSimple;
import cn.nukkit.level.Level;
import cn.nukkit.level.Position;

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
        TetrisConfig config = new TetrisConfig();
        config.level = level.getName();
        config.playX = (int) position.getX();
        config.playZ = (int) position.getZ();
        config.playY = (int) position.getY();
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
    }

}
