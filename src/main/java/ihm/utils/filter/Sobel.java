package ihm.utils.filter;

import org.opencv.core.Mat;

import java.awt.*;
import java.io.*;
import java.awt.image.BufferedImage;
import javax.imageio.*;

public class Sobel {

    private int limit;

    public Sobel(int limit) {
        this.limit = limit;
    }

    public void filterImg(String fileIn, String fileOut) {

        try {
            BufferedImage img = ImageIO.read(new File(fileIn));
            int[][] pixel = new int[img.getWidth()][img.getHeight()];
            int x, y, g;

            for (int i = 0; i < img.getWidth(); i++) {
                for (int j = 0; j < img.getHeight(); j++) {

                    Color pixelColor = new Color(img.getRGB(i, j));

                    int r = pixelColor.getRed();
                    int gb = pixelColor.getGreen();
                    int b = pixelColor.getBlue();

                    int hy = (r + gb + b) / 3;

                    int rgb = new Color(hy, hy, hy).getRGB();

                    img.setRGB(i, j, rgb);
                    pixel[i][j] = img.getRGB(i, j);
                }
            }
            //Apply Sobel filter
            for (int i = 1; i < img.getWidth() - 2; i++) {
                for (int j = 1; j < img.getHeight() - 2; j++) {
                    x = (pixel[i][j + 2] + 2 * pixel[i + 1][j + 2] + pixel[i + 2][j + 2]) - (pixel[i][j] + 2 * pixel[i + 1][j] + pixel[i + 2][j]);
                    y = (pixel[i + 2][j] + 2 * pixel[i + 2][j + 1] + pixel[i + 2][j + 2]) - (pixel[i][j] + 2 * pixel[i][j + 1] + pixel[i][j + 2]);
                    g = Math.abs(x) + Math.abs(y);
                    pixel[i][j] = g;
                }
            }
            //Record in the image
            for (int i = 0; i < img.getWidth(); i++) {
                for (int j = 0; j < img.getHeight(); j++) {

                    Color pixelColor = new Color(pixel[i][j]);

                    int r = pixelColor.getRed();
                    int gb = pixelColor.getGreen();
                    int b = pixelColor.getBlue();

                    int hy = (r + gb + b) / 3;

                    if (hy < limit) {
                        hy = 0;
                    } else {
                        hy = 255;
                    }

                    int rgb = new Color(hy, hy, hy).getRGB();

                    img.setRGB(i, j, rgb);
                }
            }
            ImageIO.write(img, "png", new File(fileOut + "Sobel.png"));//ImageIO.write()//;
        } catch (Exception e) {
            System.err.println("erreur -> " + e.getMessage());
        }
    }

    public Mat filterMat(Mat mat) {
        return null;
    }
}