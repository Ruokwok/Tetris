package cc.ruok.tetris;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.*;

public class Ranking {

    private static final File FILE = new File(Tetris.tetris.getDataFolder(), "ranking.properties");

    private static HashMap<String, Integer> ranking;

    public static boolean put(String player, int score) {
        if (score <= get(player)) return false;
        ranking.put(player, score);
        save();
        return true;
    }

    public static int get(String player) {
        if (ranking.get(player) == null) return 0;
        return ranking.get(player);
    }

    public static void load() {
        if (FILE.exists()) {
            try {
                Properties properties = new Properties();
                properties.load(new FileInputStream(FILE));
                ranking = new HashMap<>((Map) properties);
                Map<String, Integer> sort = sort(ranking);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else {
            ranking = new HashMap<>();
            save();
        }
    }

    public static void save() {
        try {
            Properties properties = new Properties();
            for (Map.Entry<String, Integer> entry : ranking.entrySet()) {
                properties.put(entry.getKey(), String.valueOf(entry.getValue()));
            }
            properties.store(new FileOutputStream(FILE), "Ranking");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 取分数最高的前10名并排序
     * @param map 得分数据
     * @return 排行榜
     * @param <K> 玩家名称
     * @param <V> 分数
     */
    public static <K, V extends Comparable<? super V>> Map<String, Integer> sort(Map<String, Integer> map) {
        Comparator<Map.Entry<String, Integer>> valCmp = new Comparator<Map.Entry<String,Integer>>() {
            @Override
            public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
                return Integer.parseInt(String.valueOf(o2.getValue())) - Integer.parseInt(String.valueOf(o1.getValue()));
            }
        };
        List<Map.Entry<String, Integer>> list = new ArrayList<>(ranking.entrySet());
        Collections.sort(list,valCmp);
        //输出map
        LinkedHashMap<String, Integer> result = new LinkedHashMap<>();
        int sum = Math.min(list.size(), 10);
        for(int i = 0; i < sum; i++) {
            result.put(list.get(i).getKey(), list.get(i).getValue());
        }
        return result;
    }

}
