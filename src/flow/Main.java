package flow;

import java.util.*;
import java.io.*;

public class Main {
    private int pNum, eNum, result;
    private static StreamTokenizer in = new StreamTokenizer(new BufferedReader(new InputStreamReader(System.in)));
    private PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
    private Node[] nodes;
    private int[][] c, f, r;
    private class Node {
        ArrayList<Integer> table = new ArrayList<>();
    }
    private class Path {
        int weight, now;
        String steps;
        Path(int now, int weight, String steps) {
            this.now = now;
            this.weight = weight;
            this.steps = steps;
        }
    }
    private static int getInt() {
        try {
            in.nextToken();
        } catch (Exception e) {
            System.out.print(e.toString());
        }
        return (int)in.nval;
    }

    public Main () {
        int s, t;
        this.pNum = getInt() + 1;
        this.eNum = getInt();
        this.c = new int[this.pNum][this.pNum];
        this.f = new int[this.pNum][this.pNum];
        this.r = new int[this.pNum][this.pNum];
        this.nodes = new Node[this.pNum];
        for (int i = 0; i < this.pNum; i++) {
            this.nodes[i] = new Node();
        }
        for (int i = 0; i < this.eNum; i++) {
            s = getInt();
            t = getInt();
            if (!this.nodes[s].table.contains(t)) {
                this.nodes[s].table.add(t);
            }
            if (!this.nodes[t].table.contains(s)) {
                this.nodes[t].table.add(s);
            }
            f[t][s] = r[s][t] = (c[s][t] += getInt());
        }
        while(BFS());
        /*for (int i = 0; i < this.pNum; i++) {
            for (int j = 0; j < this.pNum; j++) {
                if (c[i][j] > 0 || f[i][j] == 0)
                    continue;
                this.r[i][j] += this.f[i][j] - this.r[j][i];
            }
        }
        while(BFS());*/
        //debug();
    }

    private boolean BFS() {
        /* todo:要做反向边，BFS以及Dinic */
        Queue<Path> exc = new LinkedList<>();
        int[] visited = new int[this.pNum];
        visited[1] = 1;
        boolean flag = false;
        int minWeight = Integer.MAX_VALUE;
        String minPath = "1 ";
        exc.offer(new Path(1, minWeight,minPath));
        while(!exc.isEmpty()) {
            Path temp = exc.poll();
            if (temp.now == this.pNum - 1) {
                if (temp.weight < minWeight) {
                    minWeight = temp.weight;
                    minPath = temp.steps;
                }
                flag = true; //证明存在过这种东西
                continue;
            }

            for (int point : this.nodes[temp.now].table) {
                if (this.r[temp.now][point] > 0 && visited[point] == 0) {
                    int weight = temp.weight<this.r[temp.now][point]?temp.weight:this.r[temp.now][point];
                    visited[point] = 1;
                    exc.offer(new Path(point, weight, temp.steps + point + " "));
                }
            }
        }

        if (flag) {
            //System.out.println(minPath + "使用权重：" + minWeight);
            dealRest(minPath, minWeight);
            this.result += minWeight;
        }
        return flag;
    }

    private void dealRest(String path, int weight) {
        String[] paths = path.split(" +");
        for (int i = 1; i < paths.length; i++) {
            int s = Integer.parseInt(paths[i-1]);
            int t = Integer.parseInt(paths[i]);
            this.r[s][t] -= weight;
            this.r[t][s] += weight;
        }
    }

    private void releasePath(String path, int[] visited) {
        String[] paths = path.split(" +");
        for (int i = 0; i < paths.length; i++) {
            int s = Integer.parseInt(paths[i]);
            visited[s] = 0;
        }
    }

    public int getResult() {
        return this.result;
    }



    public static void main(String[] args) {
        int times = getInt();
        int i = 1;
        while(i <= times) {
            Main test = new Main();
            System.out.println("Case "+i+": " + test.getResult());
            i++;
        }
    }

    private void debug() {
        for (int i = 1; i < this.pNum; i++) {
            for (int j = 1; j < this.pNum; j++) {
                System.out.print(this.r[i][j] + " ");
            }
            System.out.println();
        }
    }
}
