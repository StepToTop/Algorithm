package flow2;

import java.io.*;
import java.util.*;

public class Main {
    private int pNum, eNum, result, endPoint, startPoint;
    private static StreamTokenizer in = new StreamTokenizer(new BufferedReader(new InputStreamReader(System.in)));
    private PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
    private Node[] nodes;
    private HashMap<Integer, Edge> edges; //source &
    private class Node {
        HashMap<Integer, Integer> table = new HashMap<>(); // target & index
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

    private class Edge {
        int[] weight , rest;
        HashMap<Integer, Integer> index = new HashMap<>();
        Edge(int s1, int s2) {
            index.put(s1, 0);
            index.put(s2, 1);
            weight = new int[2];
            rest = new int[2];
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

    public Main (int start, int end, int pnum, int edgenum) {
        this.pNum = pnum + 1;
        this.eNum = edgenum;
        /////如果是通过读文件的话，不一定要用到输入
        this.endPoint = end;
        this.startPoint = start;
        this.edges = new HashMap<>();
        ////小心这个，下面这个，懂了吧兄弟
        this.pNum = 2000000;
        this.nodes = new Node[this.pNum];
        for (int i = 0; i < this.pNum; i++) {
            this.nodes[i] = new Node();
        }
        //inputEdge();
        inputEdgeByFile();
        long startNs, endNs;
        startNs = System.nanoTime();
        while(BFS());
        endNs = System.nanoTime();
        System.out.println("以" + this.startPoint+"点为源，"+this.endPoint+"点为终得到的最大流为" + this.result + "用时" + (endNs - startNs)/1000000 + "毫秒");
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

    private void inputEdgeByFile() {
        int s, t, flag = 0;
        Edge temp;
        File data = new File("D:\\Algorithm\\src\\flow\\test.txt");
        try {
            BufferedReader br = new BufferedReader(new FileReader(data));
            String str;
            String[] strArr;
            while((str = br.readLine()) != null) {
                strArr = str.split(" +|\t+");
                s = Integer.parseInt(strArr[0]);
                t = Integer.parseInt(strArr[1]);
                if (!this.nodes[s].table.containsKey(t)) { // 没有边
                    this.edges.put(flag, new Edge(s, t));
                    temp = this.edges.get(flag);
                    temp.weight[1] += 1;
                    temp.rest[1] += 1;
                    this.nodes[s].table.put(t, flag);
                    this.nodes[t].table.put(s, flag);
                    flag ++;
                } else {
                    int index = this.nodes[s].table.get(t); //获取边的下标
                    temp = this.edges.get(index);
                    index = temp.index.get(t);
                    temp.weight[index] += 1;
                    temp.rest[index] += 1;
                }
            }
        } catch (IOException e) {
            System.err.println(e.toString());
        }
    }

    private void inputEdge() {
        int s, t, flag = 0, num;
        Edge temp;
        for (int i = 0; i < this.eNum; i++) {
            s = getInt();
            t = getInt();
            num = getInt();
            if (!this.nodes[s].table.containsKey(t)) { // 没有边
                this.edges.put(flag, new Edge(s, t));
                temp = this.edges.get(flag);
                temp.weight[1] += num;
                temp.rest[1] += num;
                this.nodes[s].table.put(t, flag);
                this.nodes[t].table.put(s, flag);
                flag ++;
            } else {
                int index = this.nodes[s].table.get(t); //获取边的下标
                temp = this.edges.get(index);
                index = temp.index.get(t);
                temp.weight[index] += num;
                temp.rest[index] += num;
            }
        }
    }

    private boolean BFS() {
        Queue<Path> exc = new LinkedList<>(); //初始化队列
        int[] visited = new int[this.pNum]; //初始化访问记录数组
        visited[startPoint] = 1;
        //初始化道路参数
        boolean flag = false;
        int minWeight = Integer.MAX_VALUE, t, index;
        String minPath = startPoint + " "; //使用字符串保存路径
        exc.offer(new Path(startPoint, minWeight,minPath));
        //开始BFS
        while(!exc.isEmpty()) {
            Path temp = exc.poll(); //弹出队列中的最后一条路径
            if (temp.now == endPoint) {
                if (temp.weight < minWeight) {
                    minWeight = temp.weight;
                    minPath = temp.steps;
                }
                flag = true; //存在增广路径
                continue;
            }
            for (Map.Entry<Integer, Integer> entry: this.nodes[temp.now].table.entrySet() ) { //查看下一个节点是否存在
                index = entry.getKey();
                Edge tempE = this.edges.get(entry.getValue());
                t = tempE.index.get(index);
                if ( tempE.rest[t] > 0 && visited[index] == 0) {
                    int weight = temp.weight < tempE.rest[t]?temp.weight:tempE.rest[t]; //更新路径的大小
                    visited[index] = 1;
                    exc.offer(new Path(index, weight, temp.steps + index + " "));
                }
            }
        }
        if (flag) {
            dealRest(minPath, minWeight);  //对路径进行清算，减少
            this.result += minWeight;
        }
        return flag;
    }

    private void dealRest(String path, int weight) {
        String[] paths = path.split(" +");
        Edge temp;
        int p1, p2;
        for (int i = 1; i < paths.length; i++) {
            int s = Integer.parseInt(paths[i-1]);
            int t = Integer.parseInt(paths[i]);
            temp = this.edges.get(nodes[s].table.get(t));
            p1 = temp.index.get(s);
            p2 = temp.index.get(t);
            temp.rest[p2] -= weight;
            temp.rest[p1] += weight;
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
        int i = 1, s, t, n = getInt(), e = getInt();
        while(i <= times) {
            s = (int)(Math.random()*n);
            t = (int)(Math.random()*n);
            new Main(s, t, n, e);
//            new Main(0,5, n, e);
            i++;
        }
    }

    private void debug() {
        for (Map.Entry<Integer, Edge> entry: this.edges.entrySet()) {
            Edge temp = entry.getValue();
            System.out.println(temp.rest[1]);
        }
    }
}
