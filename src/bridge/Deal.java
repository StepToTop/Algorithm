package bridge;

import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import com.google.gson.*;

public class Deal {

    private int[][] points;
    private int[] pointsTemp;
    private JsonObject json;
    private int pointsNumber, edgeNum, Component;
    private ArrayList<Edge> edge;

    private class Edge {
        int v1, v2;
        Edge(int v1, int v2) {
            this.v1 = v1;
            this.v2 = v2;
        }
    }

    public Deal() {
        JsonParser parse =new JsonParser();
        File data = new File("D:\\Ultimate\\Algorithm\\src\\bridge\\mediumDG.json");
        try {

            this.json = (JsonObject)parse.parse(new FileReader(data));
        }catch (Exception e) {
            System.out.println(e.toString());
        }
        this.pointsNumber = this.json.get("Number").getAsInt();
        init();
    }

    private void init() {
        edge = new ArrayList<>();
        edgeNum = 0;
        this.points = new int[this.pointsNumber][this.pointsNumber];
        for (int i = 0; i < this.pointsNumber; i++) {
            JsonArray ja = this.json.getAsJsonObject("Edge").getAsJsonArray(String.valueOf(i));
            if (ja==null) {
                continue;
            }
            edgeNum += ja.size();
            for (int j = 0; j < ja.size(); j++) {
                int temp = ja.get(j).getAsInt();
                this.edge.add(i<temp?new Edge(i,temp):new Edge(temp, i));
                if (i == temp) {
                    this.points[i][i] += 1;
                    continue;
                }
                this.points[i][temp] += 1;
                this.points[temp][i] += 1;
            }
        }
        Component = this.UnionFind();
        System.out.println("一共有"+this.edge.size()+"条边");
        System.out.println(Component);
    }

    public void UnionDeal() {
        int count = 0;
        double startNs, endNs;
        startNs = System.nanoTime();
        for (int i = 0; i < edgeNum; i++) {
            Edge temp = edge.get(i);
            edge.remove(i);
            if (UnionFind() != this.Component) {
                count++;
            }
            edge.add(i, temp);
        }
        endNs = System.nanoTime();
        System.out.println("存在桥的数目为："+count+"\n用时"+(endNs - startNs)+"纳秒");
    }

    private int UnionFind() {
        int[] tempPoint = new int[this.pointsNumber];
        Set<Integer> tempSet = new HashSet<>();
        int tempEdgeSize = this.edge.size(), i;
        for (i = 0; i < tempPoint.length; i++) {
            tempPoint[i] = i;
        }

        for (i = 0; i < tempEdgeSize; i++) {
            Edge temp = this.edge.get(i);
            replaceAll(tempPoint, tempPoint[temp.v2], tempPoint[temp.v1]);
        }

        for (i = 0; i < tempPoint.length; i++) {
            tempSet.add(tempPoint[i]);
        }

        return tempSet.size();
    }

    private void replaceAll(int[]temp, int s, int d) {
        for (int i = 0; i < temp.length; i++) {
            if (temp[i] == s) {
                temp[i] = d;
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

    private boolean DFSFindB(int now, int aim) {
        if (this.points[now][aim] != 0) {
            return true;
        } else {
            for (int i = now-1; i > 0; i--) {
                if (this.points[now][i] == 0) {
                    continue;
                }
                if (this.DFSFindB(i, aim)) {
                    return true;
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
