package cc.ruok.tetris;

import cc.ruok.tetris.commands.TetrisCommand;
import cc.ruok.tetris.listeners.BaseListener;
import cn.nukkit.Server;
import cn.nukkit.level.GameRule;
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
    }

    @Override
    public void onEnable() {
        super.onEnable();
        server.getDefaultLevel().gameRules.setGameRule(GameRule.SHOW_COORDINATES, true);
        server.getDefaultLevel().gameRules.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, true);
        server.getCommandMap().register("开始游戏", new TetrisCommand());
        server.getPluginManager().registerEvents(new BaseListener(), this);
    }

    /**
     * 1橙色
     * 2紫色
     * 3青色
     * 4黄色
     * 5绿色
     * 6粉色
     * 7深灰色
     * 8灰色
     * 9深青色
     * 10紫色
     * 11蓝色
     * 12棕色
     * 13深绿
     * 14红色
     * 15黑色
     */

    @Override
    public void onDisable() {
        super.onDisable();
    }
}
