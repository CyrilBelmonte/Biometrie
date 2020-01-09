package ihm.utils.hough;

public class PointRTable {

    private Float y;
    private int x;

    public PointRTable(int x, Float y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public Float getY() {
        return y;
    }

    public void printPoint() {
        System.out.println("[Point] : Point(" + this.x + "," + this.y + ")");
    }
}
