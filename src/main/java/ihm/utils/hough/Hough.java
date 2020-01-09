package ihm.utils.hough;

import ihm.utils.Point;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;


public class Hough {

    private int black = new Color(0, 0, 0).getRGB();
    private int white = new Color(255, 255, 255).getRGB();

    public Hough() {

    }

    private int gradient(Point point) {
        if (point.getX() != 0) {
            int ratio = Integer.valueOf(point.getY() / point.getX()).intValue();
            Double arcTRation = Math.atan(ratio);
            Double degree = Math.toDegrees(arcTRation);

            return (int) Math.round((degree));
        } else {
            return 0;
        }
    }

    public AngleRTable getAngleDistance(Point center, Point point) {
        Point r = new Point(center.getX() - point.getX(), center.getY() - point.getY());

        int bx = center.getX();
        int by = center.getY();

        int x = point.getX();
        int y = point.getY();

        if (x - bx != 0) {

            int ratio = Integer.valueOf((y - by) / (x - bx)).intValue();
            double val = 180.0 / Math.PI;
            Double arcTRation = Math.atan(ratio);
            Double degree = Math.toDegrees(arcTRation);
            int degreeInt = (int) Math.round(degree);


            double theta1 = Math.atan2(y - by, x - bx) * val;
            int theta = (int) theta1;
            if (theta < 0) {
                theta = theta + 360;
            }
            AngleRTable angleRTable = new AngleRTable(theta, r);
            return angleRTable;
        } else {
            return new AngleRTable(0, new Point(0, 0));
        }
    }

    public int[][] getPixelTab(String fileIn) throws IOException {

        BufferedImage img = ImageIO.read(new File(fileIn));
        int[][] pixel = new int[img.getWidth()][img.getHeight()];
        int x, y, g;

        int by = 0;
        int bx = 0;
        int sum = 0;
        for (int i = 0; i < img.getWidth(); i++) {
            for (int j = 0; j < img.getHeight(); j++) {
                Color pixelColor = new Color(img.getRGB(i, j));
                if (pixelColor.getBlue() == 255 & pixelColor.getGreen() == 255 & pixelColor.getRed() == 255) {
                    by = by + j;
                    bx = bx + i;
                    sum++;
                }
                // recuperer les valeur rgb (rouge ,vert ,bleu) de cette couleur
                pixel[i][j] = img.getRGB(i, j);
            }
        }
        return pixel;
    }

    public Point getBarycentre(String fileIn) throws IOException {

        BufferedImage img = ImageIO.read(new File(fileIn));
        int[][] pixel = new int[img.getWidth()][img.getHeight()];
        int x, y, g;

        int by = 0;
        int bx = 0;
        int sum = 0;
        for (int i = 0; i < img.getWidth(); i++) {
            for (int j = 0; j < img.getHeight(); j++) {
                Color pixelColor = new Color(img.getRGB(i, j));
                if (pixelColor.getBlue() == 255 & pixelColor.getGreen() == 255 & pixelColor.getRed() == 255) {
                    by = by + j;
                    bx = bx + i;
                    sum++;
                }
                // recuperer les valeur rgb (rouge ,vert ,bleu) de cette couleur
                pixel[i][j] = img.getRGB(i, j);
            }
        }
        int xbx = Integer.valueOf((bx / sum)).intValue();
        int yby = Integer.valueOf((by / sum)).intValue();

        return new Point(xbx, yby);
    }

    public void addBarycentrePoint(String fileIn, String fileOut) throws IOException {

        BufferedImage imgIn = ImageIO.read(new File(fileIn));
        BufferedImage imgOut = imgIn;

        Point point = getBarycentre(fileIn);

        imgOut.setRGB(point.getX(), point.getY(), new Color(255, 255, 255).getRGB());
        ImageIO.write(imgOut, "png", new File(fileOut));
    }

    public HashMap<Integer, ArrayList<Point>> getRTable(String fileIn) throws IOException {

        //get the center of the model
        Point center = getBarycentre(fileIn);

        //initialize the R-Table
        RTable rTable = new RTable();

        //Get the table of pixel
        BufferedImage img = ImageIO.read(new File(fileIn));

        int[][] pixel = new int[img.getWidth()][img.getHeight()];

        for (int i = 0; i < img.getWidth(); i++) {
            for (int j = 0; j < img.getHeight(); j++) {
                Color pixelColor = new Color(img.getRGB(i, j));
                pixel[i][j] = img.getRGB(i, j);
            }
        }


        for (int indexI = 0; indexI < img.getWidth(); indexI++) {
            for (int indexJ = 0; indexJ < img.getHeight(); indexJ++) {

                if (pixel[indexI][indexJ] == white) {

                    AngleRTable angleRTable = getAngleDistance(center, new Point(indexI, indexJ));

                    if (angleRTable.getPoint().getY() != 0 && angleRTable.getPoint().getX() != 0) {
                        rTable.addPoint(angleRTable.getAngle(), angleRTable.getPoint());
                    }
                }
            }
        }

        return rTable.getTableCleanUp();
    }

    public int[][] accTable(String fileIn, HashMap<Integer, ArrayList<Point>> rTable) throws IOException {

        double val = 180.0 / Math.PI;
        Point center = getBarycentre("src\\resources\\Hough\\Cercle.png");

        BufferedImage img = ImageIO.read(new File(fileIn));
        int[][] pixel = new int[img.getWidth()][img.getHeight()];
        System.out.println(img.getHeight() + ";" + img.getWidth());

        for (int i = 0; i < img.getWidth(); i++) {
            for (int j = 0; j < img.getHeight(); j++) {
                pixel[i][j] = img.getRGB(i, j);
            }
        }

        int[][] acc = initTableau(img.getWidth(), img.getHeight());

        for (int indexI = 1; indexI < img.getWidth(); indexI++) {
            for (int indexJ = 1; indexJ < img.getHeight(); indexJ++) {

                if (pixel[indexI][indexJ] == white) {

                    double theta1 = Math.atan2(indexJ, indexI) * val;
                    int theta = (int) theta1;
                    if (theta < 0) {
                        theta = theta + 360;
                    }
                    ArrayList<Point> points = rTable.get(theta);


                    for (int indexP = 0; indexP < points.size(); indexP = indexP + 50) {

                        if (((center.getX() - points.get(indexP).getX())) != 0) {

                            int dir = ((center.getY() - points.get(indexP).getY()) / ((center.getX() - points.get(indexP).getX())));

                            int b = (indexJ - (dir * indexI));

                            for (int indexI2 = 1; indexI2 < img.getWidth(); indexI2++) {
                                for (int indexJ2 = 1; indexJ2 < img.getHeight(); indexJ2++) {
                                    int eqt = ((dir * indexI2) + b);
                                    if (indexJ2 == eqt) {
                                        acc[indexI2][indexJ2] += 1;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return acc;
    }

    private int[][] initTableau(int i, int j) {
        int[][] init = new int[i][j];
        for (int indexI = 0; indexI < i; indexI++) {
            for (int indexJ = 0; indexJ < j; indexJ++) {
                init[indexI][indexJ] = 0;
            }
        }
        return init;
    }

    public Point findMax(int[][] acc) {
        Point point = new Point(0, 0);
        int maxValue = acc[0][0];
        for (int i = 1; i < acc.length; i++) {
            for (int j = 1; j < acc[0].length; j++) {
                if (acc[i][j] > maxValue) {
                    maxValue = acc[i][j];
                    point.setX(i);
                    point.setY(j);
                }
            }
        }
        return point;
    }

    public void accImg(int[][] acc) {
        String fileOut = "src\\resources\\tmp\\";
        try {
            BufferedImage img = new BufferedImage(acc.length, acc[0].length, BufferedImage.TYPE_INT_RGB);

            for (int i = 0; i < acc.length - 1; i++) {
                for (int j = 0; j < acc[i].length - 1; j++) {
                    img.setRGB(i, j, acc[i][j]);
                }
            }
            ImageIO.write(img, "png", new File(fileOut + "acc.png"));//ImageIO.write()//;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
