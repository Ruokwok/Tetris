package cc.ruok.tetris;

import cn.nukkit.utils.Utils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.IOException;

public class TetrisConfig {

    public int originX = 0;
    public int originY = 0;
    public int originZ = 0;
    public String level = "world";
    public double playX = 0D;
    public double playY = 0D;
    public double playZ = 0D;
    public int direction = 0;
    public int block = 0;
    public int effect = 0;
    public int speed = 2;
    public int keen = 1;
    public boolean bgm = true;
    public Seat ranking;
    public long rankingId;
    public String language = "auto";
    public boolean layout = false;
    public int ranking_length = 10;


    public void save(File file) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try {
            file.createNewFile();
            Utils.writeFile(file, gson.toJson(this));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static TetrisConfig load() {
        try {
            String json = Utils.readFile(Tetris.tetris.configFile);
            TetrisConfig config = new Gson().fromJson(json, TetrisConfig.class);
            if (config.direction < 0 || config.direction > 3) {
                Tetris.tetris.getLogger().error("§c读取配置文件出错，请检查direction项!");
                return null;
            }
            if (!json.contains("\"layout\":") && config.originX != 0 && config.originY != 0 && config.originZ != 0) {
                config.layout = true;
            }
            return config;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public int getLangIndex() {
        if (language.equals("chs")) return 1;
        if (language.equals("eng")) return 2;
        return 0;
    }

    public static class Seat {

        public double x = 0D;
        public double y = 0D;
        public double z = 0D;
        public int chunkX = 0;
        public int chunkZ = 0;

    }
}
