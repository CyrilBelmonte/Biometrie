package ihm.utils.hough;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;


import ihm.utils.Point;
import org.opencv.core.Mat;


public class Hough {

    public Hough() {

    }

    private int gradient(Point point) {
        if (point.getX() != 0) {
            int ratio = Integer.valueOf(point.getY() / point.getY()).intValue();
            Double arcTRation = Math.atan(ratio);
            Double degree = Math.toDegrees(arcTRation);
            return (int) Math.round(degree);
        } else {
            return 0;
        }
    }

    private AngleRTable getAngleDistance(Point center, Point point) {
        Point r = new Point(center.getX() - point.getX(), center.getY() - point.getY());
        int x1 = point.getX();
        int x2 = center.getX();
        int y1 = point.getY();
        int y2 = center.getY();
        if (x2 - x1 != 0) {

            int ratio = Integer.valueOf((y2 - y1) / (x2 - x1)).intValue();
            Double arcTRation = Math.atan(ratio);
            Double degree = Math.toDegrees(arcTRation);
            int degreeInt = (int) Math.round(degree);
            AngleRTable angleRTable = new AngleRTable(degreeInt, r);
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

        int border_limit = 3;

        for (int indexI = 0; indexI < (img.getWidth() - border_limit); indexI++) {
            for (int indexJ = 0; indexJ < (img.getHeight() - border_limit); indexJ++) {
                if (pixel[indexI][indexJ] != 0) {
                    AngleRTable angleRTable = getAngleDistance(center, new Point(indexI, indexJ));
                    if (angleRTable.getPoint().getY() != 0 || angleRTable.getPoint().getX() != 0) {
                        rTable.addPoint(Math.abs(angleRTable.getAngle()), angleRTable.getPoint());
                    }
                }
            }
        }
        System.out.println(rTable.getTableCleanUp().get(0).size() + ";" + img.getWidth());
        return rTable.getTableCleanUp();
    }

    public int[][] accTable(String fileIn, HashMap<Integer, ArrayList<Point>> rTable) throws IOException {

        BufferedImage img = ImageIO.read(new File(fileIn));
        int[][] pixel = new int[img.getWidth()][img.getHeight()];

        for (int i = 0; i < img.getWidth(); i++) {
            for (int j = 0; j < img.getHeight(); j++) {
                Color pixelColor = new Color(img.getRGB(i, j));
                // recuperer les valeur rgb (rouge ,vert ,bleu) de cette couleur
                pixel[i][j] = img.getRGB(i, j);
            }
        }
        int[][] acc = initTableau(img.getWidth() + 20, img.getHeight() + 20);

        for (int indexI = 1; indexI < (img.getHeight()); indexI++) {
            for (int indexJ = 1; indexJ < (img.getWidth()); indexJ++) {
                if (pixel[indexI][indexJ] != 0) {
                    int theta = gradient(new Point(indexI, indexJ));
                    ArrayList<Point> points = rTable.get(Math.abs(theta));
                    for (Point point : points) {
                        //point.printPoint();
                        //System.out.println(indexJ);
                        acc[point.getX() + indexI][point.getY() + indexJ] = acc[point.getX() + indexI][point.getY() + indexJ] + 1;
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

    private Point findMax(int[][] acc) {
        Point point = new Point(0, 0);
        int maxValue = acc[0][0];
        for (int i = 0; i < acc.length; i++) {
            for (int j = 0; j < acc[i].length; i++) {
                if (acc[j][i] > maxValue) {
                    maxValue = acc[j][i];
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

            for (int i = 0; i < acc.length; i++) {
                for (int j = 0; j < acc[i].length; i++) {
                    img.setRGB(i, j, acc[i][j]);
                }
            }
            ImageIO.write(img, "png", new File(fileOut + "acc.png"));//ImageIO.write()//;
        } catch (Exception e) {
            System.err.println("erreur -> " + e.getMessage());
        }
    }
}
