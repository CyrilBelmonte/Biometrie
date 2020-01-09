package ihm.utils.hough;

import ihm.utils.Point;

import java.util.ArrayList;
import java.util.HashMap;
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

    public void remove(Integer integer) {
        this.rTable.remove(integer);
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

    @Override
    public String toString() {

        String tmp = "";

        for (Map.Entry<Integer, ArrayList<Point>> entry : this.rTable.entrySet()) {
            tmp = tmp = " Key " + entry.getKey() + " :  \n";
            for (Point point : entry.getValue()) {
                tmp = tmp = " Point " + point.toString() + "\n";
            }
            tmp = tmp + "\n";
        }
        return tmp;
    }

    public void toPrint(HashMap<Integer, ArrayList<Point>> rTable) {

        String tmp = "";

        for (Map.Entry<Integer, ArrayList<Point>> entry : rTable.entrySet()) {
            System.out.println("Key : " + entry.getKey() + " Size =" + entry.getValue().size());
        }
    }
}


