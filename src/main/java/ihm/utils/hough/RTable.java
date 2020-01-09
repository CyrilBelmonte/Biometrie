package ihm.utils.hough;

import ihm.utils.Point;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class RTable {
    private HashMap<Integer, ArrayList<Point>> rTable;

    public RTable() {
        this.rTable = new HashMap<>();
    }

    public void addPoint(int theta, Point point) {
        if (!this.rTable.containsKey(theta)) {
            ArrayList<Point> points = new ArrayList<>();
            points.add(point);
            this.rTable.put(theta, points);
        } else {
            this.rTable.get(theta).add(point);
        }
    }

    public HashMap<Integer, ArrayList<Point>> getRTable() {
        return this.rTable;
    }

    public HashMap<Integer, ArrayList<Point>> getTableCleanUp() {
        HashMap<Integer, ArrayList<Point>> cleanUp = new HashMap<>();

        for (Map.Entry<Integer, ArrayList<Point>> entry : this.rTable.entrySet()) {
            if (entry.getValue() != null) {
                cleanUp.put(entry.getKey(), entry.getValue());
            } else {
                ArrayList<Point> pointArrayList = new ArrayList<>();
                for (Point point : entry.getValue()) {
                    if (point.getX() != 0 && point.getY() != 0) {
                        pointArrayList.add(point);
                    }
                }
            }

        }
        return cleanUp;
    }
}
