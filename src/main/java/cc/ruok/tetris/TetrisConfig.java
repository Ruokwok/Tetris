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
            TetrisConfig config = new Gson().fromJson(Utils.readFile(Tetris.tetris.configFile), TetrisConfig.class);
            if (config.direction < 0 || config.direction > 3) {
                Tetris.tetris.getLogger().error("读取配置文件出错，请检查direction项!");
                return null;
            }
            return config;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
