package bridge;

import java.io.*;
import java.util.ArrayList;


public class Deal {

    private int[][] points;
    private int[] pointsTemp, unionLabel;
    private int pointsNumber, edgeNum, Component;
    private ArrayList<Edge> edge;
    private ArrayList<Union> unions;

    private class Edge {
        int v1, v2;
        Edge(int v1, int v2) {
            this.v1 = v1;
            this.v2 = v2;
        }
    }

    private class Union {
        ArrayList<Integer> points;
        int Number;
        Union(int num) {
            this.points = new ArrayList<>();
            this.Number = num;
        }

        @Override
        public boolean equals(Object obj) {
            return ((Union)obj).Number == this.Number;
        }
    }

    public Deal() {
        this.edge = new ArrayList<>();
        this.unions = new ArrayList<>();
        this.edgeNum = 0;
        File data = new File("D:\\Ultimate\\Algorithm\\src\\bridge\\mediumDG.txt");
        try {
            BufferedReader br = new BufferedReader(new FileReader(data));
            String str;
            String[] strArr;
            while((str = br.readLine()) != null) {
                strArr = str.split(" +");

                if (strArr.length < 2) {

                    this.pointsNumber = Integer.parseInt(strArr[0]);
                    this.points = new int[this.pointsNumber][this.pointsNumber];
                } else {

                    this.edgeNum += 1;
                    int i = Integer.parseInt(strArr[0]), j = Integer.parseInt(strArr[1]);
                    this.edge.add(i<j?new Edge(i, j):new Edge(j, i));
                    if (i == j) {
                        this.points[i][i] += 1;
                        continue;
                    }
                    this.points[i][j] += 1;
                    this.points[j][i] += 1;
                }
            }
        }catch (Exception e) {
            System.out.println(e.toString());
        }
        init();
    }

    private void init() {
        Component = this.UnionCount();
        System.out.println("一共有"+this.edge.size()+"条边");
        System.out.println("一共存在"+this.unions.size()+"个连通片");
    }

    public void UnionDeal() {
        int count = 0;
        double startNs, endNs;
        startNs = System.nanoTime();
        for (int i = 0; i < edgeNum; i++) {
            Edge temp = edge.get(i);
            this.points[temp.v1][temp.v2] -= 1;
            this.points[temp.v2][temp.v1] -= 1;
            if (!this.UnionFind(temp.v1, temp.v2)) {
                count++;
            }
            this.points[temp.v1][temp.v2] += 1;
            this.points[temp.v2][temp.v1] += 1;
        }
        endNs = System.nanoTime();
        System.out.println("存在桥的数目为："+count+"\n用时"+(endNs - startNs)+"纳秒");
    }

    private boolean UnionFind(int v1, int v2) {
        initTempRec();
        return DFSFind(v1, v2);
    }

    private int UnionCount() {
        this.unionLabel = new int[this.pointsNumber];
        int tempEdgeSize = this.edge.size(), i;
        for (i = 0; i < unionLabel.length; i++) {
            unionLabel[i] = i;
        }

        for (i = 0; i < tempEdgeSize; i++) {
            Edge temp = this.edge.get(i);
            replaceAll(unionLabel[temp.v2], unionLabel[temp.v1]);
        }

        for (i = 0; i < unionLabel.length; i++) {
            int index = unions.indexOf(new Union(unionLabel[i]));
            if (index >= 0) {
                unions.get(index).points.add(i);
            }else {
                Union temp = new Union(unionLabel[i]);
                temp.points.add(i);
                unions.add(temp);
            }
        }

        return unions.size();
    }

    private void replaceAll(int s, int d) {
        for (int i = 0; i < this.unionLabel.length; i++) {
            if (this.unionLabel[i] == s) {
                this.unionLabel[i] = d;
            }
        }
    }

    private void initTempRec() {
        this.pointsTemp = new int[this.pointsNumber];
    }

    public void DFSDeal() {
        int count = 0;
        double startNs, endNs;
        startNs = System.nanoTime();
        for (int i = 0; i < this.pointsNumber; i++) {
            for (int j = i; j < this.pointsNumber; j++) {
                if (this.points[i][j] == 0 || i == j) {
                    continue;
                }
                initTempRec();
                this.points[i][j] -= 1;
                this.points[j][i] -= 1;
                if (!this.DFSFind(i, j)) {
                    System.out.println(i + "和" + j + "之间存在桥");
                    count++;
                }
                this.points[i][j] += 1;
                this.points[j][i] += 1;
            }
        }
        endNs = System.nanoTime();
        System.out.println("存在桥的数目为："+count+"\n用时"+(endNs - startNs)+"纳秒");
    }

    private boolean DFSFind(int now, int aim) {
        this.pointsTemp[now] = 1;
        if (this.points[now][aim] != 0) {
            return true;
        } else {
            for (int i = 0; i < this.pointsNumber; i++) {
                if (this.points[now][i] == 0) {
                    continue;
                }
                if (this.pointsTemp[i] == 0) {
                    if(this.DFSFind(i, aim)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public void debug() {
        for (int i = 0; i < this.pointsNumber; i++) {
            for (int j = 0; j < this.pointsNumber; j++) {
                System.out.print(this.points[i][j] + " ");
            }
            System.out.println();
        }
    }

}
