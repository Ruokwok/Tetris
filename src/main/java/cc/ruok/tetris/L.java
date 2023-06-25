package cc.ruok.tetris;

import cn.nukkit.Server;
import cn.nukkit.utils.Utils;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class L {

    public static Map<String, String> languages;

    public static void load(String lang) {
        if (lang.equals("auto")) {
            lang = Server.getInstance().getLanguage().getLang();
        }
        InputStream stream = L.class.getResourceAsStream("/languages/" + lang + ".lang");
        if (stream == null) {
            stream = L.class.getResourceAsStream("/languages/chs.lang");
        }
        languages = loadLang(stream);
    }

    private static Map<String, String> loadLang(InputStream stream) {
        try {
            String content = Utils.readFile(stream);
            Map<String, String> d = new HashMap<>();
            for (String line : content.split("\n")) {
                line = line.trim();
                if (line.equals("") || line.charAt(0) == '#') {
                    continue;
                }
                String[] t = line.split("=");
                if (t.length < 2) {
                    continue;
                }
                String key = t[0];
                String value = "";
                for (int i = 1; i < t.length - 1; i++) {
                    value += t[i] + "=";
                }
                value += t[t.length - 1];
                if (value.equals("")) {
                    continue;
                }
                d.put(key, value);
            }
            return d;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String get(String key) {
        return languages.get(key);
    }

    public static boolean exists(String lang) {
        InputStream stream = L.class.getResourceAsStream("/languages/" + lang + ".lang");
        return stream != null;
    }

}
