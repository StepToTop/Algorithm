package bridge;

import java.util.ArrayList;

public class Point {
    private int[] next;

    public Point(int[] nxt) {
        next = new int[nxt.length];

        for (int i = 0; i < nxt.length; i++) {
            next[i] = nxt[i];
        }
    }
}
