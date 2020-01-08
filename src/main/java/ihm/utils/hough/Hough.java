package ihm.utils.hough;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class Hough {

    public Hough() {

    }

    public String toString() {

        return "[]";
    }

    public Point getBarycentre(String fileWithCont) throws IOException {

        BufferedImage imgWithCont = ImageIO.read(new File(fileWithCont));
        int[][] pixel = new int[imgWithCont.getWidth()][imgWithCont.getHeight()];
        int x, y, g;

        int by = 0;
        int bx = 0;
        int sum = 0;
        for (int i = 0; i < imgWithCont.getWidth(); i++) {
            for (int j = 0; j < imgWithCont.getHeight(); j++) {
                Color pixelColor = new Color(imgWithCont.getRGB(i, j));
                if (pixelColor.getBlue() == 255 & pixelColor.getGreen() == 255 & pixelColor.getRed() == 255) {
                    by = by + j;
                    bx = bx + i;
                    sum++;
                }
                // recuperer les valeur rgb (rouge ,vert ,bleu) de cette couleur
                pixel[i][j] = imgWithCont.getRGB(i, j);
            }
        }
        int xbx = Integer.valueOf((bx / sum)).intValue();
        int yby = Integer.valueOf((by / sum)).intValue();

        return new Point(xbx - 30, yby);
    }


    public static void main(String[] args) {
        Hough hough = new Hough();
        String fileSrc = "src\\resources\\Hough\\forme.png";
        String fileCont = "src\\resources\\Hough\\formeCont.png";

        HashMap<Integer, ArrayList<Float>> initTbl = new HashMap<>();


        try {
            BufferedImage img = ImageIO.read(new File(fileCont));
            int[][] pixel = new int[img.getWidth()][img.getHeight()];
            int x, y, g;

            for (int i = 0; i < img.getWidth(); i++) {
                for (int j = 0; j < img.getHeight(); j++) {

                    // recuperer couleur de chaque pixel
                    Color pixelcolor = new Color(img.getRGB(i, j));

                    // recuperer les valeur rgb (rouge ,vert ,bleu) de cette couleur
                    pixel[i][j] = img.getRGB(i, j);
                }
            }
            Point point = hough.getBarycentre();
            //ImageIO.write(img, "png", new File("src\\resources\\Hough\\" + "barycentre.png"));

        } catch (IOException e) {
            System.err.println("Fail to op image");
            e.printStackTrace();
        }


    }

    private Point getBarycentre() {
        return null;
    }
}