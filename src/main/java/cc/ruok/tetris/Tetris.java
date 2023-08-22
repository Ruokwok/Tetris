package cc.ruok.tetris;

import cc.ruok.tetris.commands.TetrisCommand;
import cc.ruok.tetris.listeners.BaseListener;
import cc.ruok.tetris.tasks.UpdateRankingTask;
import cn.nukkit.Server;
import cn.nukkit.plugin.PluginBase;

import java.io.File;

public class Tetris extends PluginBase {

    public Server server = Server.getInstance();
    public static Tetris tetris;
    public File configFile;
    public TetrisConfig config;
    public UpdateRankingTask task;

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
        L.load(config.language);
        Ranking.load();
    }

    @Override
    public void onEnable() {
        super.onEnable();
        TetrisGame.level = server.getLevelByName(Tetris.tetris.config.level);
        Ranking.updateFloatingText();
        server.getCommandMap().register("tetris", new TetrisCommand());
        server.getPluginManager().registerEvents(new BaseListener(), this);
        getLogger().info(L.get("tetris.enable") + " - v" + getDescription().getVersion());
        task = new UpdateRankingTask();
        server.getScheduler().scheduleRepeatingTask(task, 2400, true);
//        if (config == null) {
//            getLogger().warning("§c俄罗斯方块未进行设置，请在游戏中使用/tetris set命令开始配置.");
//        }
    }

    @Override
    public void onDisable() {
        super.onDisable();
        task.cancel();
    }
}
