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
//            config = new TetrisConfig();
//            config.save(configFile);
        } else {
            config = TetrisConfig.load();
        }
        Ranking.load();
    }

    @Override
    public void onEnable() {
        super.onEnable();
        server.getCommandMap().register("开始游戏", new TetrisCommand());
        server.getPluginManager().registerEvents(new BaseListener(), this);
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }
}
