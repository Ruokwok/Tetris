package cc.ruok.tetris;

import cc.ruok.tetris.commands.TetrisCommand;
import cc.ruok.tetris.listeners.BaseListener;
import cn.nukkit.Server;
import cn.nukkit.plugin.PluginBase;

import java.io.File;

public class Tetris extends PluginBase {

    public Server server = Server.getInstance();
    public static Tetris tetris;
    public File configFile;
    public TetrisConfig config;

    @Override
    public void onLoad() {
        super.onLoad();
        tetris = this;
        configFile = new File(getDataFolder() + "/config.json");
        getDataFolder().mkdir();
        if (!configFile.exists()) {
            config = new TetrisConfig();
            config.save(configFile);
        } else {
            config = TetrisConfig.load();
        }
        Ranking.load();
    }

    @Override
    public void onEnable() {
        super.onEnable();
        TetrisGame.level = server.getLevelByName(Tetris.tetris.config.level);
        Ranking.updateFloatingText();
        server.getCommandMap().register("tetris", new TetrisCommand());
        server.getPluginManager().registerEvents(new BaseListener(), this);
        getLogger().info("俄罗斯方块小游戏已加载 - v" + getDescription().getVersion());
        if (config == null) {
            getLogger().warning("§c俄罗斯方块未进行设置，请在游戏中使用/tetris set命苦开始配置.");
        }
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }
}
