package cc.ruok.tetris;

import cc.ruok.tetris.listeners.TetrisListener;
import cc.ruok.tetris.tasks.EndTask;
import cc.ruok.tetris.tasks.GameTask;
import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.block.Block;
import cn.nukkit.event.HandlerList;
import cn.nukkit.level.Level;
import cn.nukkit.level.Location;
import cn.nukkit.level.Position;
import cn.nukkit.level.particle.DestroyBlockParticle;
import cn.nukkit.math.Vector3;

import java.util.ArrayList;
import java.util.Collections;

public class TetrisGame {

    private static boolean stats = false;
    private static TetrisGame game;
//    private static Level level = null;
    private static Level level;
    private static Server server = Server.getInstance();

    private TetrisBlock nowBlock;
    private TetrisBlock nextBlock;
    private GameTask task;
    private Player player;
    private TetrisListener listener;

    private int score = 0;
    private int gamemode = 0;

    private int blockX;
    private int blockY;

    private int blockId = 35;

    private int[][] layout = new int[20][10];
    protected static Position origin;

    public static boolean getStats() {
        return stats;
    }

    public static TetrisGame getGame() {
        return game;
    }

    public static Level getLevel() {
        return level;
    }

    public static Position getOrigin() {
        return origin.clone(); //TODO 应当克隆一份
    }

    public static TetrisConfig getConfig() {
        return Tetris.tetris.config;
    }

    public static boolean getPlane() {
        return getConfig().direction >= 0 && getConfig().direction <= 1;
    }

    /**
     * 将坐标转换为地图方块位置
     * @param x 横坐标
     * @param y 纵坐标
     * @param beyond 在画布外的位置是否返回空
     * @return 地图中方块的位置
     */
    public static Position getPosition(int x, int y, boolean beyond) {
        if (beyond && (x > 10 || x < 0)) return null;
        if (beyond && (y > 19 || y < -1)) return null;
        Position position = new Position();
        position.setLevel(level);
        position.setY(origin.y + y);
        if (getConfig().direction == 0) {
            position.setX(origin.x + x);
            position.setZ(origin.z);
        } else if (getConfig().direction == 1) {
            position.setX(origin.x - x);
            position.setZ(origin.z);
        } else if (getConfig().direction == 2) {
            position.setX(origin.x);
            position.setZ(origin.z - x);
        } else if (getConfig().direction == 3) {
            position.setX(origin.x);
            position.setZ(origin.z + x);
        }
        return position;
    }

    public static Position getPosition(int x, int y) {
        return getPosition(x, y, true);
    }

    public static Position getPosition(Pos pos) {
        return getPosition(pos.x, pos.y);
    }

    public static TetrisGame start(Player player) {
        if (stats) return null;
        if (Tetris.tetris.config == null) {
            player.sendMessage("俄罗斯方块小游戏尚未进行配置，请联系管理员。");
            return null;
        }
        stats = true;
        game = new TetrisGame();
        game.setNextBlock(new TetrisBlock());
        game.next();
        game.task = new GameTask();
        game.player = player;
        origin = new Position(Tetris.tetris.config.originX, Tetris.tetris.config.originY, Tetris.tetris.config.originZ, level);
        level = server.getLevelByName(Tetris.tetris.config.level);
        game.blockX = 3;
        game.blockY = 20;
        game.clearScreen();
        game.spawn();
        game.gamemode = player.getGamemode();
        server.getScheduler().scheduleRepeatingTask(Tetris.tetris, game.task, 10);
        game.listener = new TetrisListener(player, game);
        server.getPluginManager().registerEvents(game.listener, Tetris.tetris);
        player.teleport(new Location(getConfig().playX, getConfig().playY, getConfig().playZ));
        return game;
    }

    public void setNextBlock(TetrisBlock block) {
        this.nextBlock = block;
    }

    public TetrisBlock getNowBlock() {
        return nowBlock;
    }

    public TetrisBlock getNextBlock() {
        return nextBlock;
    }

    public void next() {
        this.nowBlock = nextBlock;
        while (true) {
            TetrisBlock tb = new TetrisBlock();
            //确保不会连续出两个一样颜色的块
            if (tb.getColor() != nowBlock.getColor()) {
                setNextBlock(tb);
                break;
            }
        }
    }

    public void clear(Pos pos) {
        Position position = getPosition(pos);
        if (position != null) {
            getLevel().setBlock(position, Block.get(0));
        }
    }

    public void clear(Pos[] pos) {
        for (Pos p : pos) {
            clear(p);
        }
    }

    /**
     * 使方块移动
     * @param d 0=下落 1=左移 2=右移
     */
    public boolean move(int d) {
        if (d == 0) {
            for (Pos pos : nowBlock.pos) {
                Pos temp = new Pos(pos.x, pos.y - 1);
                Position position = getPosition(temp);
                if (position == null || temp.y == -1) {
                    spawn();
                    check();
                    return true;
                }
                if (level.getBlock(position).getId() != 0 && !nowBlock.isMine(temp)) {
                    for (Pos p : nowBlock.pos) {
                        if (p.y >= 20) {
                            stop();
                            return true;
                        }
                    }
                    spawn();
                    check();
                    return true;
                }
            }
            for (Pos pos : nowBlock.pos) {
                clear(pos);
                pos.y -= 1;
            }
            nowBlock.center.fall();
        } else if (d == 1) {
            // 这里判断方块的左移是否会发生碰撞。
            // 同样的写法在下落和右移中正常运行，但左移会出现错误便改用collision()方法。
            // 其他方向移动虽然有collision()方法可以实现，但是目前运行正常所以就不改了。
//            for (Pos pos : nowBlock.pos) {
//                Pos temp = new Pos(pos.x - 1, pos.y);
//                Position position = getPosition(temp);
//                if (position != null && level.getBlock(position).getId() != 0 && !nowBlock.isMine(temp)) {
//                    return false;
//                }
//            }

            Pos[] temp = new Pos[4];
            for (int i = 0; i < 4; i++) {
                temp[i] = new Pos(nowBlock.pos[i].x - 1, nowBlock.pos[i].y);
            }
            if (collision(temp)) return false;
            for (Pos pos : nowBlock.pos) {
                if (pos.x <= 0) return false;
                clear(pos);
                pos.x -= 1;
            }
            nowBlock.center.left();
        } else if (d == 2) {
            for (Pos pos : nowBlock.pos) {
                Pos temp = new Pos(pos.x + 1, pos.y);
                Position position = getPosition(temp);
                if (position != null && level.getBlock(position).getId() != 0 && !nowBlock.isMine(temp)) {
                    return false;
                }
            }
            for (Pos pos : nowBlock.pos) {
                if (pos.x >= 9) return false;
                clear(pos);
                pos.x += 1;
            }
            nowBlock.center.right();
        }
        show();
        return false;
    }

    public void fall() {
        while (true) {
            if (move(0)) return;
        }
    }

    /**
     * 控制方块旋转
     */
    public void rotate() {
        if (nowBlock.getType() == 1) return;
        Pos[] temp = new Pos[4];
        if (nowBlock.getType() == 0) {
            if (nowBlock.dire == 0) {
                for (int i = 0; i < 4; i++) {
                    switch (i) {
                        case 0: temp[i] = nowBlock.center.clone();break;
                        case 1: temp[i] = new Pos(nowBlock.center.x - 1, nowBlock.center.y); break;
                        case 2: temp[i] = new Pos(nowBlock.center.x + 1, nowBlock.center.y); break;
                        case 3: temp[i] = new Pos(nowBlock.center.x + 2, nowBlock.center.y); break;
                    }
                }
            } else {
                for (int i = 0; i < 4; i++) {
                    switch (i) {
                        case 0: temp[i] = nowBlock.center.clone();break;
                        case 1: temp[i] = new Pos(nowBlock.center.x, nowBlock.center.y + 1); break;
                        case 2: temp[i] = new Pos(nowBlock.center.x, nowBlock.center.y - 1); break;
                        case 3: temp[i] = new Pos(nowBlock.center.x, nowBlock.center.y - 2); break;
                    }
                }
            }
        } else if (nowBlock.getType() == 2) {
            if (nowBlock.dire == 0) {
                for (int i = 0; i < 4; i++) {
                    switch (i) {
                        case 0: temp[i] = nowBlock.center.clone();break;
                        case 1: temp[i] = new Pos(nowBlock.center.x - 1, nowBlock.center.y); break;
                        case 2: temp[i] = new Pos(nowBlock.center.x, nowBlock.center.y - 1); break;
                        case 3: temp[i] = new Pos(nowBlock.center.x - 1, nowBlock.center.y + 1); break;
                    }
                }
            } else if (nowBlock.dire == 1) {
                for (int i = 0; i < 4; i++) {
                    switch (i) {
                        case 0: temp[i] = nowBlock.center.clone();break;
                        case 1: temp[i] = new Pos(nowBlock.center.x - 1, nowBlock.center.y); break;
                        case 2: temp[i] = new Pos(nowBlock.center.x, nowBlock.center.y + 1); break;
                        case 3: temp[i] = new Pos(nowBlock.center.x + 1, nowBlock.center.y + 1); break;
                    }
                }
            }
        } else if (nowBlock.getType() == 3) {
            if (nowBlock.dire == 0) {
                for (int i = 0; i < 4; i++) {
                    switch (i) {
                        case 0: temp[i] = nowBlock.center.clone();break;
                        case 1: temp[i] = new Pos(nowBlock.center.x - 1, nowBlock.center.y); break;
                        case 2: temp[i] = new Pos(nowBlock.center.x + 1, nowBlock.center.y); break;
                        case 3: temp[i] = new Pos(nowBlock.center.x + 1, nowBlock.center.y + 1); break;
                    }
                }
            } else if (nowBlock.dire == 1) {
                for (int i = 0; i < 4; i++) {
                    switch (i) {
                        case 0: temp[i] = nowBlock.center.clone();break;
                        case 1: temp[i] = new Pos(nowBlock.center.x, nowBlock.center.y + 1); break;
                        case 2: temp[i] = new Pos(nowBlock.center.x, nowBlock.center.y - 1); break;
                        case 3: temp[i] = new Pos(nowBlock.center.x - 1, nowBlock.center.y + 1); break;
                    }
                }
            } else if (nowBlock.dire == 2) {
                for (int i = 0; i < 4; i++) {
                    switch (i) {
                        case 0: temp[i] = nowBlock.center.clone();break;
                        case 1: temp[i] = new Pos(nowBlock.center.x - 1, nowBlock.center.y); break;
                        case 2: temp[i] = new Pos(nowBlock.center.x + 1, nowBlock.center.y); break;
                        case 3: temp[i] = new Pos(nowBlock.center.x - 1, nowBlock.center.y - 1); break;
                    }
                }
            } else if (nowBlock.dire == 3) {
                for (int i = 0; i < 4; i++) {
                    switch (i) {
                        case 0: temp[i] = nowBlock.center.clone();break;
                        case 1: temp[i] = new Pos(nowBlock.center.x, nowBlock.center.y + 1); break;
                        case 2: temp[i] = new Pos(nowBlock.center.x, nowBlock.center.y - 1); break;
                        case 3: temp[i] = new Pos(nowBlock.center.x + 1, nowBlock.center.y - 1); break;
                    }
                }
            }
        } else if (nowBlock.getType() == 4) {
            if (nowBlock.dire == 0) {
                for (int i = 0; i < 4; i++) {
                    switch (i) {
                        case 0: temp[i] = nowBlock.center.clone();break;
                        case 1: temp[i] = new Pos(nowBlock.center.x, nowBlock.center.y + 1); break;
                        case 2: temp[i] = new Pos(nowBlock.center.x, nowBlock.center.y - 1); break;
                        case 3: temp[i] = new Pos(nowBlock.center.x + 1, nowBlock.center.y); break;
                    }
                }
            } else if (nowBlock.dire == 1) {
                for (int i = 0; i < 4; i++) {
                    switch (i) {
                        case 0: temp[i] = nowBlock.center.clone();break;
                        case 1: temp[i] = new Pos(nowBlock.center.x + 1, nowBlock.center.y); break;
                        case 2: temp[i] = new Pos(nowBlock.center.x - 1, nowBlock.center.y); break;
                        case 3: temp[i] = new Pos(nowBlock.center.x, nowBlock.center.y - 1); break;
                    }
                }
            } else if (nowBlock.dire == 2) {
                for (int i = 0; i < 4; i++) {
                    switch (i) {
                        case 0: temp[i] = nowBlock.center.clone();break;
                        case 1: temp[i] = new Pos(nowBlock.center.x, nowBlock.center.y + 1); break;
                        case 2: temp[i] = new Pos(nowBlock.center.x, nowBlock.center.y - 1); break;
                        case 3: temp[i] = new Pos(nowBlock.center.x - 1, nowBlock.center.y); break;
                    }
                }
            } else if (nowBlock.dire == 3) {
                for (int i = 0; i < 4; i++) {
                    switch (i) {
                        case 0: temp[i] = nowBlock.center.clone();break;
                        case 1: temp[i] = new Pos(nowBlock.center.x + 1, nowBlock.center.y); break;
                        case 2: temp[i] = new Pos(nowBlock.center.x - 1, nowBlock.center.y); break;
                        case 3: temp[i] = new Pos(nowBlock.center.x, nowBlock.center.y + 1); break;
                    }
                }
            }
        } else if (nowBlock.getType() == 5) {
            if (nowBlock.dire == 0) {
                for (int i = 0; i < 4; i++) {
                    switch (i) {
                        case 0: temp[i] = nowBlock.center.clone();break;
                        case 1: temp[i] = new Pos(nowBlock.center.x +1, nowBlock.center.y); break;
                        case 2: temp[i] = new Pos(nowBlock.center.x - 1, nowBlock.center.y); break;
                        case 3: temp[i] = new Pos(nowBlock.center.x + 1, nowBlock.center.y - 1); break;
                    }
                }
            } else if (nowBlock.dire == 1) {
                for (int i = 0; i < 4; i++) {
                    switch (i) {
                        case 0: temp[i] = nowBlock.center.clone();break;
                        case 1: temp[i] = new Pos(nowBlock.center.x, nowBlock.center.y + 1); break;
                        case 2: temp[i] = new Pos(nowBlock.center.x, nowBlock.center.y - 1); break;
                        case 3: temp[i] = new Pos(nowBlock.center.x - 1, nowBlock.center.y - 1); break;
                    }
                }
            } else if (nowBlock.dire == 2) {
                for (int i = 0; i < 4; i++) {
                    switch (i) {
                        case 0: temp[i] = nowBlock.center.clone();break;
                        case 1: temp[i] = new Pos(nowBlock.center.x + 1, nowBlock.center.y); break;
                        case 2: temp[i] = new Pos(nowBlock.center.x - 1, nowBlock.center.y); break;
                        case 3: temp[i] = new Pos(nowBlock.center.x - 1, nowBlock.center.y + 1); break;
                    }
                }
            } else if (nowBlock.dire == 3) {
                for (int i = 0; i < 4; i++) {
                    switch (i) {
                        case 0: temp[i] = nowBlock.center.clone();break;
                        case 1: temp[i] = new Pos(nowBlock.center.x, nowBlock.center.y + 1); break;
                        case 2: temp[i] = new Pos(nowBlock.center.x, nowBlock.center.y - 1); break;
                        case 3: temp[i] = new Pos(nowBlock.center.x + 1, nowBlock.center.y + 1); break;
                    }
                }
            }
        } else if (nowBlock.getType() == 6) {
            if (nowBlock.dire == 0) {
                for (int i = 0; i < 4; i++) {
                    switch (i) {
                        case 0: temp[i] = nowBlock.center.clone();break;
                        case 1: temp[i] = new Pos(nowBlock.center.x - 1, nowBlock.center.y); break;
                        case 2: temp[i] = new Pos(nowBlock.center.x, nowBlock.center.y + 1); break;
                        case 3: temp[i] = new Pos(nowBlock.center.x - 1, nowBlock.center.y - 1); break;
                    }
                }
            } else if (nowBlock.dire == 1) {
                for (int i = 0; i < 4; i++) {
                    switch (i) {
                        case 0: temp[i] = nowBlock.center.clone();break;
                        case 1: temp[i] = new Pos(nowBlock.center.x, nowBlock.center.y + 1); break;
                        case 2: temp[i] = new Pos(nowBlock.center.x + 1, nowBlock.center.y); break;
                        case 3: temp[i] = new Pos(nowBlock.center.x - 1, nowBlock.center.y + 1); break;
                    }
                }
            }
        }

        if (!collision(temp)) {
            clear(nowBlock.pos);
            nowBlock.pos = temp;
            show();
            if (nowBlock.getType() == 0 || nowBlock.getType() == 2 || nowBlock.getType() == 6) {
                nowBlock.dire = (nowBlock.dire == 0) ? 1 : 0;
            } else {
                if (nowBlock.dire == 3) {
                    nowBlock.dire = 0;
                } else {
                    nowBlock.dire++;
                }
            }
        }
    }

    /**
     * 检测是否会和已经固定的方块发生碰撞
     * @param pos 待检测的位置
     * @return true:会  false:不会
     */
    public boolean collision(Pos[] pos) {
        for (Pos p : pos) {
            Position position = getPosition(p);
            if (position != null) {
                if (getLevel().getBlock(position).getId() != 0 && !nowBlock.isMine(p)) {
                    return true;
                }
            } else {
                return true;
            }
        }
        return false;
    }

    /**
     * 检查是否有填满的行，如果有则清理。
     */
    public void check() {
        ArrayList<Integer> lines = new ArrayList<>();
        for (int y = 0; y < 20; y++) {
            boolean c = true;
            for (int x = 0; x < 10; x++) {
                Position pos = getPosition(x, y);
                if (pos != null) {
                    if (getLevel().getBlock(pos).getId() == 0) {
                        c = false;
                    }
                    if (nowBlock.isMine(x, y)) {
                        c = false;
                    }
                }
            }
            if (c) {
                lines.add(y);
            }
        }
        if (lines.size() > 0) {
            remove(lines);
        }
    }

    /**
     * 清理填满的行并使上方的方块向下移动
     * @param line 待清理的行号
     */
    public void remove(ArrayList<Integer> line) {
        //TODO 添加积分
        addScore(line.size() * line.size() * 10);
        for (int l : line) {
            for (int x = 0; x < 10; x++) {
                Position pos = getPosition(x, l);
                if (pos != null) {
                   useBreakOn(pos, player);
                }
            }
        }

        Collections.sort(line);
        Collections.reverse(line);
        for (int l : line) {
            for (int y = l + 1; y < 20; y++) {
                for (int x = 0; x < 10; x++) {
                    Position pos = getPosition(x, y);
                    if (pos != null && !nowBlock.isMine(x, y)) {
                        Block block = getLevel().getBlock(pos);
                        getLevel().setBlock(pos, Block.get(0));
                        getLevel().setBlock(new Position(pos.x, pos.y - 1, pos.z), block);
                    }
                }
            }
        }
    }

    public static void useBreakOn(Vector3 vector3, Player player) {
//        getLevel().useBreakOn(vector3, null, player, true);
        Block block = getLevel().getBlock(vector3);
        getLevel().addParticle(new DestroyBlockParticle(vector3.add(0.5), block));
        getLevel().setBlock(vector3, Block.get(0));
    }

    /**
     * 清空整个游戏窗口
     */
    public void clearScreen() {
        for (int y = 0; y < 20; y++) {
            for (int x = 0; x < 10; x++) {
                Position position = getPosition(x, y);
                if (position != null) {
                    getLevel().setBlock(position, Block.get(0));
                }
            }
        }
    }

    public Player getPlayer() {
        return player;
    }

    public void spawn() {
        next();
        int i = 0;
        int _x = 0;
        int _y = 0;
        for (int[] y : nowBlock.getBlock()) {
            for (int x : y) {
                if (x == 1) {
                    nowBlock.pos[i] = new Pos(blockX + _x, blockY + _y);
                    i++;
                    if (_x == 1 && _y == -1) {
                        nowBlock.center = new Pos(blockX + _x, blockY + _y);
                    }
                }
                _x++;
            }
            _x = 0;
            _y--;
        }
        show();
    }

    public void show() {
        for (Pos pos : nowBlock.pos) {
            Position position = getPosition(pos);
            if (position != null) {
                int id = getLevel().getBlock(position).getId();
                if (id == 0) {
                    getLevel().setBlock(position, Block.get(blockId, nowBlock.getColor()));
                }
            }
        }
    }

    public void stop() {
        task.cancel();
        player.sendMessage("游戏结束");
        player.sendMessage("得分:" + score);
        if (player.gamemode == 2) player.setGamemode(2);
        server.broadcastMessage(player.getName() + "完成了俄罗斯方块游戏，得分: " + getScore());
        HandlerList.unregisterAll(listener);
        server.getScheduler().scheduleRepeatingTask(Tetris.tetris, new EndTask(this), 2);
    }

    public void setStats(boolean stats) {
        TetrisGame.stats = stats;
    }

    public int getScore() {
        return score;
    }

    public void addScore(int i) {
        score += i;
    }

    static class Pos {

        public Pos(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public int x;
        public int y;

        public void left() {
            x--;
        }

        public void right() {
            x++;
        }

        public void fall() {
            y--;
        }

        @Override
        protected Pos clone() {
            return new Pos(x, y);
        }
    }
}
