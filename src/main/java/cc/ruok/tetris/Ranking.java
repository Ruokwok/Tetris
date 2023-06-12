package cc.ruok.tetris;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.EntityLiving;
import cn.nukkit.level.Level;
import cn.nukkit.level.Position;
import cn.nukkit.scheduler.AsyncTask;
import cn.nukkit.utils.ChunkException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.*;

public class Ranking {

    private static final File FILE = new File(Tetris.tetris.getDataFolder(), "ranking.properties");
    private static HashMap<String, String> ranking;


    public static boolean put(String player, int score) {
        if (score <= get(player)) return false;
        ranking.put(player, String.valueOf(score));
        save();
        updateFloatingText();
        return true;
    }

    public static int get(String player) {
        if (ranking.get(player) == null) return 0;
        return Integer.parseInt(ranking.get(player));
    }

    public static void load() {
        if (FILE.exists()) {
            try {
                Properties properties = new Properties();
                properties.load(new FileInputStream(FILE));
                ranking = new HashMap<>((Map) properties);
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
            for (Map.Entry<String, String> entry : ranking.entrySet()) {
                properties.put(entry.getKey(), entry.getValue());
            }
            properties.store(new FileOutputStream(FILE), "Ranking");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 取分数最高的前10名并排序
     * @return 排行榜
     * @param <K> 玩家名称
     * @param <V> 分数
     */
    public static <K, V extends Comparable<? super V>> Map<String, String> sort() {
        Comparator<Map.Entry<String, String>> valCmp = new Comparator<Map.Entry<String,String>>() {
            @Override
            public int compare(Map.Entry<String, String> o1, Map.Entry<String, String> o2) {
                return Integer.parseInt(o2.getValue()) - Integer.parseInt(o1.getValue());
            }
        };
        List<Map.Entry<String, String>> list = new ArrayList<>(ranking.entrySet());
        Collections.sort(list,valCmp);
        //输出map
        LinkedHashMap<String, String> result = new LinkedHashMap<>();
        int sum = Math.min(list.size(), 10);
        for(int i = 0; i < sum; i++) {
            result.put(list.get(i).getKey(), list.get(i).getValue());
        }
        return result;
    }

    public static void updateFloatingText() {
        Server.getInstance().getScheduler().scheduleAsyncTask(Tetris.tetris, new AsyncTask() {
            @Override
            public void onRun() {
                TetrisConfig config = TetrisGame.getConfig();
                if (config.ranking == null) return;
                Level level = TetrisGame.getLevel();
                Entity text = level.getEntity(TetrisGame.getConfig().rankingId);
                if (text != null) text.kill();
                Position position = new Position(config.ranking.x, config.ranking.y, config.ranking.z, level);
                EntityLiving entity;
                try {
                     entity = new EntityLiving(level.getChunk(config.ranking.chunkX, config.ranking.chunkZ), Entity.getDefaultNBT(position)) {
                        @Override
                        public int getNetworkId() {
                            return 81;
                        }
                    };
                } catch (ChunkException e) {
                    return;
                }
                Map<String, String> map = sort();
                config.rankingId = entity.getId();
                config.save(Tetris.tetris.configFile);
                entity.setScale(0F);
                int i = 0;
                StringBuilder name = new StringBuilder("§e[[ §6俄罗斯方块 §c-- §d排行榜 §e]]\n");
                for (Map.Entry<String, String> entry : map.entrySet()) {
                    name.append("§aNo.").append(++i).append(" §l§e").append(entry.getKey()).append(" §f- §b").append(entry.getValue()).append("\n");
                }
                name.append("§e[[ §6俄罗斯方块 §c-- §d排行榜 §e]]");
                entity.setNameTag(name.toString());
                entity.setNameTagAlwaysVisible(true);
                entity.spawnToAll();
            }
        });
    }

    public static void removeFloatingText(Player player) {
        Level level = TetrisGame.getLevel();
        Entity text = level.getEntity(TetrisGame.getConfig().rankingId);
        if (text != null) {
            text.kill();
            player.sendMessage("§l§a已移除排行榜.");
        }
    }

    public static void setFloatingText(Player player) {
        TetrisConfig.Seat seat = new TetrisConfig.Seat();
        seat.x = player.getX();
        seat.y = player.getY() + 2;
        seat.z = player.getZ();
        seat.chunkX = player.getChunkX();
        seat.chunkZ = player.getChunkZ();
        TetrisGame.getConfig().ranking = seat;
        updateFloatingText();
        player.sendMessage("§l§a已设置排行榜.");
    }

}
