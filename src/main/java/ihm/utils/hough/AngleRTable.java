package ihm.utils.hough;

import ihm.utils.Point;

public class AngleRTable {
    private int angle;
    private Point point;

    public AngleRTable(int angle, Point point) {
        this.angle = angle;
        this.point = point;
    }

    public int getAngle() {
        return angle;
    }

    public void setAngle(int angle) {
        this.angle = angle;
    }

    public Point getPoint() {
        return point;
    }

    public void setPoint(Point point) {
        this.point = point;
    }

    @Override
    public String toString() {
        return "AngleRTable{" + "angle=" + angle + ", point=" + point.toString() + '}';
    }
}
