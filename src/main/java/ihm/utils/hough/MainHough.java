package ihm.utils.hough;

import ihm.utils.Point;
import ihm.utils.filter.Prewitt;

import java.io.IOException;

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

        System.out.println(/********************************************************/);

        hough.accImg(hough.accTable("src\\resources\\Hough\\Prewitt.png", hough.getRTable("src\\resources\\Hough\\Prewitt.png")));
    }
}
