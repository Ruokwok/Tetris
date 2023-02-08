package cc.ruok.tetris;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class Utils {

    public static int[] colors = {1, 2, 3, 4, 5, 6, 9, 10, 11, 13, 14};

    public static ArrayList<Integer> bag7 = new ArrayList<>();
    public static int index = -1;

    static {
        bag7.add(0);
        bag7.add(1);
        bag7.add(2);
        bag7.add(3);
        bag7.add(4);
        bag7.add(5);
        bag7.add(6);
        Collections.shuffle(bag7);
    }

    /**
     * 获取一个方块，同时避免连续出同一形状或一直不出某个形状
     * @return 形状类型
     */
    public static int getBlock() {
        if (index >= 6) {
            Collections.shuffle(bag7);
            index = -1;
        }
        index++;
        return bag7.get(index);
    }

    public static int getColor() {
        return colors[getRandom(0, 11)];
    }

    public static int getRandom(int min, int max){
        Random random = new Random();
        return random.nextInt(max) % (max - min + 1) + min;
    }

}
