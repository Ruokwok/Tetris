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
    public int playX = 0;
    public int playY = 0;
    public int playZ = 0;
    public int direction = 0;

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
            return new Gson().fromJson(Utils.readFile(Tetris.tetris.configFile), TetrisConfig.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
