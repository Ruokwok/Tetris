package cc.ruok.tetris;

public class TetrisBlock {

    private int type;
    private int[][] block;
    private int color;

    public TetrisGame.Pos[] pos;
    public TetrisGame.Pos center;

    public int dire = 0;


    private int[][] t0 = {
            {0, 1, 0, 0},
            {0, 1, 0, 0},
            {0, 1, 0, 0},
            {0, 1, 0, 0}
    };

    private int[][] t1 = {
            {0, 1, 1, 0},
            {0, 1, 1, 0},
            {0, 0, 0, 0},
            {0, 0, 0, 0}
    };

    private int[][] t2 = {
            {0, 1, 1, 0},
            {1, 1, 0, 0},
            {0, 0, 0, 0},
            {0, 0, 0, 0}
    };

    private int[][] t3 = {
            {1, 1, 0, 0},
            {0, 1, 0, 0},
            {0, 1, 0, 0},
            {0, 0, 0, 0}
    };

    private int[][] t4 = {
            {0, 1, 0, 0},
            {1, 1, 1, 0},
            {0, 0, 0, 0},
            {0, 0, 0, 0}
    };

    private int[][] t5 = {
            {0, 1, 1, 0},
            {0, 1, 0, 0},
            {0, 1, 0, 0},
            {0, 0, 0, 0}
    };

    private int[][] t6 = {
            {1, 1, 0, 0},
            {0, 1, 1, 0},
            {0, 0, 0, 0},
            {0, 0, 0, 0}
    };

    public TetrisBlock() {
        this(Utils.getBlock());
    }

    public TetrisBlock(int type) {
        this.type = type;
        this.color = Utils.getColor();
        pos = new TetrisGame.Pos[4];
        switch (type){
            case 0:
                block = t0.clone();
                break;
            case 1:
                block = t1.clone();
                break;
            case 2:
                block = t2.clone();
                break;
            case 3:
                block = t3.clone();
                break;
            case 4:
                block = t4.clone();
                break;
            case 5:
                block = t5.clone();
                break;
            case 6:
                block = t6.clone();
                break;
        }
    }

    public int[][] getBlock() {
        return block;
    }

    public boolean isMine(TetrisGame.Pos pos) {
        for (TetrisGame.Pos p : this.pos) {
            if (pos.x == p.x && pos.y == p.y) {
                return true;
            }
        }
        return false;
    }

    public boolean isMine(int x, int y) {
        return isMine(new TetrisGame.Pos(x, y));
    }

    public int getType() {
        return type;
    }

    public int getColor() {
        return color;
    }
}
