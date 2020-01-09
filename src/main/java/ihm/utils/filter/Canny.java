package ihm.utils.filter;

import org.opencv.core.Mat;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class Canny {

    private int limit;

    public Canny(int limit) {
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
            //Apply the Canny filter
            for (int i = 1; i < img.getWidth() - 2; i++) {
                for (int j = 1; j < img.getHeight() - 2; j++) {

                    x = -pixel[i][j] + pixel[i][j + 2];
                    y = pixel[i][j] - pixel[i + 2][j];
                    g = Math.abs(x) + Math.abs(y);

                    pixel[i][j] = g;
                }
            }
            //Record in the img
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
            ImageIO.write(img, "png", new File(fileOut + "Canny.png"));//ImageIO.write()//;
        } catch (Exception e) {
            System.err.println("erreur -> " + e.getMessage());
        }
    }

    public Mat filterMat(Mat mat) {
        return null;
    }
}