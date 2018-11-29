package pointSort;

import java.util.*;

public class Deal {
    private int num;
    private Point[] points;

    public Deal(int num) {
        this.num = num;
        points = new Point[num];
        generatePoint();
    }

    private void generatePoint() { //数组生成
        for (int i = 0; i < this.num; i++) {
            points[i] = new Point(Math.random()*this.num, Math.random()*this.num);
        }
    }

    public void forcePick() {
        long startNS, endNS;
        startNS = System.nanoTime();
        Point[] result = forcPickDeal();
        endNS = System.nanoTime();
        System.out.printf("暴力结果：最短距离是点(%f, %f) 和 点(%f, %f)，中间距离%f，排序用时%d纳秒\n", result[0].x, result[0].y, result[1].x, result[1].y, Math.sqrt(result[0].distance(result[1])), endNS - startNS);

    }

    private Point[] forcPickDeal() {
        double min = Double.MAX_VALUE;
        int indexA = 0, indexB = 0;
        for (int i = 0; i < this.num - 1 ; i++) {
            for (int j = i + 1; j < this.num ; j++) {
                double tempDistance =  points[i].distance(points[j]);
                if(min >= tempDistance) {
                    min = tempDistance;
                    indexA = i;
                    indexB = j;
                }
            }
        }
        return new Point[]{this.points[indexA], this.points[indexB]};
    }

    public void mergePick() {
        long startNS, endNS;
        startNS = System.nanoTime();
        Point[] result = mergePickDeal(this.points);
        endNS = System.nanoTime();
        System.out.printf("归并结果：最短距离是点(%f, %f) 和 点(%f, %f)，中间距离%f，排序用时%d纳秒\n", result[0].x, result[0].y, result[1].x, result[1].y, Math.sqrt(result[0].distance(result[1])), endNS - startNS);
    }

    private Point[] mergePickDeal(Point[] temp) {//必须保证有序
        Point[] result = new Point[2]; // 返回最近的两个点

        if(temp.length < 4) { //如果是小于四个点就直接蛮力求解
            int length = temp.length;
            double minDistance = Double.POSITIVE_INFINITY;  //给个无穷
            for(int i = 0; i < length; i++) {
                for(int j = i+1; j < length; j++) {
                    double distance = temp[i].distance(temp[j]);
                    if(distance < minDistance) {
                        minDistance = distance;
                        result[0] = temp[i];
                        result[1] = temp[j];
                    }
                }
            }
            return result;
        }


        int midIndex = (temp.length-1)/2;



        Point[] left = Arrays.copyOfRange(temp, 0, midIndex + 1);
        Point[] right = Arrays.copyOfRange(temp, midIndex + 1, temp.length);
        Point[] resultLeft = mergePickDeal(left);
        Point[] resultRight = mergePickDeal(right);

        double minLeft = resultLeft[0].distance(resultLeft[1]);
        double minRight = resultRight[0].distance(resultRight[1]);
        double minDistance = Math.min(minLeft, minRight);

        if(minLeft == minDistance) {
            result = resultLeft;
        }else {
            result = resultRight;
        }


        //获取中间left right两边的点
        Set<Point> Table1 = new TreeSet<>();
        Set<Point> Table2 = new TreeSet<>();


        Arrays.sort(temp, new Comparator<Point>() {
            @Override
            public int compare(Point o1, Point o2) {
                if (o1.y > o2.y)
                    return 1;
                else if (o1.y < o2.y)
                    return -1;
                else
                    return 0;
            }
        });

        for(int i=0;i<temp.length;i++) {
            if(temp[i].x > temp[midIndex].x - minDistance  && temp[i].x <= temp[midIndex].x) {
                Table1.add(temp[i]);
            }
            if(temp[i].x < temp[midIndex].x + minDistance  && temp[i].x > temp[midIndex].x) {
                Table2.add(temp[i]);
            }
        }
        Point[] midL = new Point[Table1.size()];
        Point[] midR = new Point[Table2.size()];

        Table1.toArray(midL);
        Table2.toArray(midR);
        //进行y轴排序

        for(int i = 0; i < midL.length; i++) {
            for(int j = 0; j < midR.length; j++) {
                if(Math.abs(midL[i].y - midR[j].y) < minDistance){
                    double distance = midL[i].distance(midR[j]);
                    if(distance < minDistance) {
                        result[0] = midL[i];
                        result[1] = midR[j];
                        minDistance = distance;
                    }
                } else {
                    break;
                }
            }
        }

        return result;
    }

    public void debug() {
        for (int i = 0; i < num; i ++) {
            System.out.println(this.points[i].x);
        }
    }
}
