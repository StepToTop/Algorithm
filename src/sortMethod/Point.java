package sortMethod;

import java.util.Arrays;
import java.util.Comparator;

import static java.lang.Math.random;
import static java.lang.Math.round;
import static java.lang.Math.sqrt;

public class Point {
    private double x;
    private double y;

    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double distance(Point a){
        return sqrt( (x - a.x)*(x - a.x) + (y - a.y)*(y - a.y) );
    }
    public double distance1(Point a){
        return (x - a.x)*(x - a.x) + (y - a.y)*(y - a.y);
    }

    public double getX(){
        return x;
    }
    public double getY(){
        return y;
    }
    public void print(){
        System.out.println("x:" + x + " y:" + y);
    }
    public static void xSort(Point[] a, int left, int right){
        if (left >= right)
            return ;
        Point b = a[left];
        int l = left, r = right;
        while (l < r) {
            while (l< r && a[r].getX() >= b.getX())
                r--;
            a[l] = a[r];
            while (l< r && a[l].getX() <= b.getX())
                l++;
            a[r] = a[l];
        }
        a[l] = b;
        xSort(a, left, l-1);
        xSort(a, l+1, right);
    }
    public static void ySort(Point[] a, int left, int right){
        if (left >= right)
            return ;
        Point b = a[left];
        int l = left, r = right;
        while (l < r) {
            while (l< r && a[r].getY() >= b.getY())
                r--;
            a[l] = a[r];
            while (l< r && a[l].getY() <= b.getY())
                l++;
            a[r] = a[l];
        }
        a[l] = b;
        ySort(a, left, l-1);
        ySort(a, l+1, right);
    }

    public  static double shortP0(Point[] a, int left, int right){
        if (right - left +1 <= 3){
			return violentP(a, left, right);
        }
        int mid = (left + right) /2;
		double m = shortP(a, left, mid);
		double n = shortP(a, mid +1, right);
		double d = m<n?m:n;
		double ds = d * d;
		for (int i = mid; i>=left; i--){
			if (a[mid].getX() - a[i].getX() < d) {
				for (int j = mid +1; j<=right; j++)
					if (a[j].getX() - a[i].getX() < d){
						double dis = a[i].distance1(a[j]);
						if (dis < ds){
							ds = dis;
						}
					}else break;
			}else break;
		}
		return sqrt(ds);
    }
    public  static double shortP(Point[] a, int left, int right){
        if (right - left +1 <= 3){
            return violentP(a, left, right);
        }
        int mid = (left + right) /2;
        double m = shortP(a, left, mid);
        double n = shortP(a, mid +1, right);
        double d = m<n?m:n;
        return getMid(a, left, right, mid, d);
    }
    public static double getMid(Point[] a, int left, int right, int mid, double d){
        double ds = d * d;
        int num1 = 0;
        int num2 = 0;
        for (int i = mid; i>=left; i--){
            if (a[mid].getX() - a[i].getX() < d) {
                num1++;
            }else break;
        }
        for (int i = mid +1; i<=right; i++) {
            if (a[i].getX() - a[mid].getX() < d) {
                num2++;
            } else break;
        }
        int length = num1 + num2;
        Point[] ypoint = new Point[length];
        for (int i = 0; i < num1; i++)
            ypoint[i] = a[mid-i];
        for (int i = 0; i < num2; i++)
            ypoint[i + num1] = a[mid+i+1];
        //ySort(ypoint, 0, length-1);
        Arrays.sort(ypoint, new Comparator<Point>() {
            @Override
            public int compare(Point o1, Point o2) {
                if(o1.getY() < o2.getY()) {
                    return -1;
                }else if (o1.getY() == o2.getY()) {
                    return 0;
                } else {
                    return 1;
                }
            }
        });
        for (int i = 0; i<length-1; i++){
            for (int j = i+1; j<i+7 && j<length; j++){
                double dis = ypoint[i].distance1(ypoint[j]);
                if (dis < ds){
                    ds = dis;
                }
            }
        }
        return sqrt(ds);
    }

    public static double violentP(Point[] a, int left, int right) {
        double min = 1000000000;
        for (int i = left; i< right; i++) {
            for (int j = i+1; j<= right; j++) {
                double dis = a[i].distance1(a[j]);
                if (dis < min) {
                    min = dis;
                }
            }
        }
        return sqrt(min);
    }

    public static void main(String[] args) {
        int n = 100000;
        Point[] a = new Point[n];
        for ( int i = 0; i<n; i++) {
            double p = round(random()*n*100)/100.0;
            double q = round(random()*n*100)/100.0;
            a[i] = new Point(p, q);
            a[i] = new Point(round(random()*n*100)/100.0, round(random()*n*100)/100.0);
        }
        //xSort(a, 0, n - 1);
        long s = System.nanoTime();
        double dis = violentP(a, 0, n-1);
        long e = System.nanoTime();
        System.out.println("蛮力法：测得距离：" + dis + "\ncost:" + (e-s)/1000000 + "ms\n");

        //xSort(a, 0, n - 1);
//        Arrays.sort(a, new Comparator<Point>() {
//            @Override
//            public int compare(Point o1, Point o2) {
//                if(o1.getX() < o2.getX()) {
//                    return -1;
//                }else if (o1.getX() == o2.getX()) {
//                    return 0;
//                } else {
//                    return 1;
//                }
//            }
//        });
//        long s1 = System.nanoTime();
//        double dis1 = shortP0(a, 0, n - 1);
//        long e1 = System.nanoTime();
//        System.out.println("分治法：测得距离：" + dis1 + "\ncost:" + (e1-s1)/1000000 + "ms\n");
    }
}
