package cc.ruok.tetris;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.data.EntityMetadata;
import cn.nukkit.level.Level;
import cn.nukkit.level.Position;
import cn.nukkit.math.NukkitRandom;
import cn.nukkit.network.protocol.AddEntityPacket;
import cn.nukkit.network.protocol.RemoveEntityPacket;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.*;

public class Ranking {

    private static final File FILE = new File(Tetris.tetris.getDataFolder(), "ranking.properties");
    private static HashMap<String, String> ranking;
    private static AddEntityPacket packet;


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
        TetrisConfig config = TetrisGame.getConfig();
        if (config.ranking == null) return;
        removeFloatingText();
        Map<String, String> map = sort();
        int i = 0;
        StringBuilder name = new StringBuilder(L.get("ranking.title") + "\n");
        for (Map.Entry<String, String> entry : map.entrySet()) {
            name.append("§aNo.").append(++i).append(" §l§e").append(entry.getKey()).append(" §f- §b").append(entry.getValue()).append("\n");
        }
        name.append(L.get("ranking.title"));

        packet = new AddEntityPacket();
        int id = new NukkitRandom().nextInt();
        packet.entityRuntimeId = id;
        packet.entityUniqueId = id;
        packet.type = 64;
        packet.yaw = 0;
        packet.headYaw = 0;
        packet.pitch = 0;
        packet.speedX = 0;
        packet.speedY = 0;
        packet.speedZ = 0;
        packet.x = (float) config.ranking.x;
        packet.y = (float) config.ranking.y;
        packet.z = (float) config.ranking.z;
        packet.metadata = new EntityMetadata()
                .putString(Entity.DATA_NAMETAG, name.toString())
                .putBoolean(Entity.DATA_ALWAYS_SHOW_NAMETAG, true);
        config.rankingId = id;
        config.save(Tetris.tetris.configFile);
        for (Player player : Server.getInstance().getOnlinePlayers().values()) {
            player.dataPacket(packet);
        }
    }

    public static void updateFloatingText(Player player) {
        if (packet != null) {
            player.dataPacket(packet);
        }
    }

    public static void removeFloatingText(Player player) {
        RemoveEntityPacket pk = new RemoveEntityPacket();
        pk.eid = Tetris.tetris.config.rankingId;
        player.dataPacket(pk);
    }

    public static void removeFloatingText() {
        for (Player player : Server.getInstance().getOnlinePlayers().values()) {
            removeFloatingText(player);
        }
    }

    public static void deleteFloatingText(Player player) {
        removeFloatingText();
        player.sendMessage(L.get("ranking.remove.tip"));
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
        player.sendMessage(L.get("ranking.set.tip"));
    }

}