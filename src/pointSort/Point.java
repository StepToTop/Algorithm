package pointSort;

public class Point implements Comparable<Point> {
    public double x, y;
    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double distance (Point B) {
        return (this.x - B.x) * (this.x - B.x) + (this.y - B.y) * (this.y - B.y);
    }

    public int compareTo(Point o) {
        if(x == o.x && y == o.y)
            return 0;
        return 1;
    }
}
