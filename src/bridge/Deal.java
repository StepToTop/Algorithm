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
        File data = new File("D:\\Ultimate\\Algorithm\\src\\bridge\\testD.txt");
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
        /*System.out.println("一共有"+this.edge.size()+"条边");
        System.out.println("一共存在"+this.unions.size()+"个连通片");*/
    }

    public long UnionDeal() {
        int count = 0;
        long startNs, endNs;
        startNs = System.nanoTime();
        for (int i = 0; i < edgeNum; i++) {
            Edge temp = edge.get(i);
            edge.remove(i);
            if (!this.UnionFind(temp.v1, temp.v2)) {
                count++;
            }
            edge.add(i, temp);
        }
        endNs = System.nanoTime();
        System.out.println("使用并查集得存在桥的数目为："+count+"\n用时"+(endNs - startNs)+"纳秒");
        return (endNs - startNs);
    }

    private boolean UnionFind(int v1, int v2) {
        int[] temp = new int[this.pointsNumber];
        int[] rank = new int[this.pointsNumber];
        int tempEdgeSize = this.edge.size(), i;
        for (i = 0; i < this.pointsNumber; i++) {
            temp[i] = -1;
//            temp[i] = i;
        }

        for (i = 0; i < tempEdgeSize; i++) {
            Edge tempE = this.edge.get(i);
//            replaceRoot(temp, tempE.v1, tempE.v2);
//            replaceAll(temp, temp[tempE.v2], temp[tempE.v1]);
            replaceRootRank(temp, rank, tempE.v1, tempE.v2);
        }

        int r1 = temp[v1], r2 = temp[v2];
        boolean result;
        if (r1 == -1 || r2 == -1) {
            result = false;
        } else {
            result = getRoot(temp, v1) == getRoot(temp, v2);
//            result = r1 == r2;
        }

        return result;
    }

    private int UnionCount() {
        this.unionLabel = new int[this.pointsNumber];
        int tempEdgeSize = this.edge.size(), i;
        for (i = 0; i < unionLabel.length; i++) {
            unionLabel[i] = i;
        }

        for (i = 0; i < tempEdgeSize; i++) {
            Edge temp = this.edge.get(i);
            replaceAll(unionLabel, unionLabel[temp.v2], unionLabel[temp.v1]);
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

    private void replaceAll(int[] arr, int s, int d) {
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] == s) {
                arr[i] = d;
            }
        }
    }

    private void replaceRoot(int[] arr, int s, int d) {
        if (arr[s] == -1 && arr[d] == -1) {
            arr[s] = s;
            arr[d] = s;
        } else if (arr[s] == -1 || arr[d] == -1) {
            int temp = arr[s] == -1?getRoot(arr, d):getRoot(arr, s);
            //int temp = arr[s] == -1?arr[d]:arr[s];
            arr[s] = temp;
            arr[d] = temp;
        } else {
            int index1 = getRoot(arr, s);
            int index2 = getRoot(arr, d);
            if (index1 != index2) {
                arr[index2] = arr[index1];
            }
        }
    }

    private void replaceRootRank(int[] arr, int[] rank, int s, int d) {
        if (arr[s] == -1 && arr[d] == -1) {
            arr[s] = s;
            arr[d] = s;
            rank[d] = 1;
        } else if (arr[s] == -1 || arr[d] == -1) {
            int temp = arr[s] == -1?getRoot(arr, d):getRoot(arr, s);
            //int temp = arr[s] == -1?arr[d]:arr[s];
            arr[s] = temp;
            arr[d] = temp;
            rank[temp] ++;
        } else {
            int index1 = getRoot(arr, s);
            int index2 = getRoot(arr, d);
            if (index1 == index2) {
                return;
            } else if (rank[index1] > rank[index2]) {
                arr[index2] = index1;
            } else if (rank[index2] > rank[index1]) {
                arr[index1] = index2;
            } else {
                arr[index2] = index1;
                rank[index1] ++;
            }
        }
    }

    private int getRoot(int[] temp, int index) {
        while(index != temp[index]) {
            index = temp[index];
        }
        return index;
    }

    private void initTempRec() {
        this.pointsTemp = new int[this.pointsNumber];
    }

    public long newDFSDeal() {
        int count = 0;
        long startNs, endNs;
        startNs = System.nanoTime();
        for (int i = 0; i < this.edgeNum; i++) {
            Edge temp = edge.get(i);
            int v1 = temp.v1, v2 = temp.v2;
            if (v1 == v2) {
                continue;
            }
            initTempRec();
            this.points[v1][v2] -= 1;
            this.points[v2][v1] -= 1;
            if (!this.DFSFind(v1, v2)) {
                count++;
            }
            this.points[v1][v2] += 1;
            this.points[v2][v1] += 1;
        }
        endNs = System.nanoTime();
        System.out.println("使用dfs检测得存在桥的数目为："+count+"\n用时"+(endNs - startNs)+"纳秒");
        return endNs - startNs;
    }

    public long DFSDeal() {
        int count = 0;
        long startNs, endNs;
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
                    count++;
                }
                this.points[i][j] += 1;
                this.points[j][i] += 1;
            }
        }
        endNs = System.nanoTime();
        System.out.println("使用dfs检测得存在桥的数目为："+count+"\n用时"+(endNs - startNs)+"纳秒");
        return (endNs - startNs);
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

    private void debugCom(int[] arr) {
        for (int i = 0; i < this.pointsNumber; i++) {
            System.out.print(arr[i] + " ");
        }
        System.out.println();
    }

}
