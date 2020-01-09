package ihm.utils.hough;

import ihm.utils.Point;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class MainHough {


    public static void main(String[] args) throws IOException {

        Hough hough = new Hough();

        /*
        String fileSrc = "src\\resources\\Hough\\forme.png";

        Prewitt prewitt = new Prewitt(1);
        prewitt.filterImg("src\\resources\\Hough\\forme.png", "src\\resources\\Hough\\");

        String fileCont = "src\\resources\\Hough\\formeCont.png";

        Point point = hough.getBarycentre("src\\resources\\Hough\\Prewitt.png");

        hough.addBarycentrePoint("src\\resources\\Hough\\Prewitt.png", fileCont);

        point.printPoint();
        */

        System.out.println("/********************************************************/");

        String model = "src\\resources\\Hough\\Cercle.png";
        String img = "src\\resources\\Hough\\Cercle.png";
        String tmp = "src\\resources\\tmp\\Prewitt.png";
        Point center = hough.getBarycentre(model);

        System.out.println(center.toString());

        HashMap<Integer, ArrayList<Point>> rtable = hough.getRTable(model);

        int[][] acc = hough.accTable(model, rtable);

        hough.accImg(acc);

        System.out.println(hough.findMax(acc).toString());

        //Point center = new Point(0, 0);
        //Point point = new Point(1, 1);

        //System.out.println(hough.getAngleDistance(center, point).toString());

    }
}
