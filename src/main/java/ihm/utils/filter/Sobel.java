package ihm.utils.filter;

import java.awt.*;
import java.io.*;
import java.awt.image.BufferedImage;
import javax.imageio.*;


public class Sobel {


    public void filtre(String fil) {


        try {
            BufferedImage img = ImageIO.read(new File(fil));
            int[][] pixel = new int[img.getWidth()][img.getHeight()];
            int x, y, g;
//***************************************************
//Conversion enniveau du Gris
            for (int i = 0; i < img.getWidth(); i++) {
                for (int j = 0; j < img.getHeight(); j++) {

                    Color pixelcolor = new Color(img.getRGB(i, j));

                    int r = pixelcolor.getRed();
                    int gb = pixelcolor.getGreen();
                    int b = pixelcolor.getBlue();

                    int hy = (r + gb + b) / 3;

                    int rgb = new Color(hy, hy, hy).getRGB();

                    // changer la couleur de pixel avec la nouvelle couleur inversée
                    img.setRGB(i, j, rgb);
                }
            }
//***************************************************
            // parcourir les pixels de l'image
            for (int i = 0; i < img.getWidth(); i++) {
                for (int j = 0; j < img.getHeight(); j++) {

                    // recuperer couleur de chaque pixel
                    Color pixelcolor = new Color(img.getRGB(i, j));

                    // recuperer les valeur rgb (rouge ,vert ,bleu) de cette couleur
                    pixel[i][j] = img.getRGB(i, j);
                }
            }
//*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*

            for (int i = 1; i < img.getWidth() - 2; i++) {
                for (int j = 1; j < img.getHeight() - 2; j++) {

                    x = (pixel[i][j + 2] + 2 * pixel[i + 1][j + 2] + pixel[i + 2][j + 2]) - (pixel[i][j] + 2 * pixel[i + 1][j] + pixel[i + 2][j]);
                    y = (pixel[i + 2][j] + 2 * pixel[i + 2][j + 1] + pixel[i + 2][j + 2]) - (pixel[i][j] + 2 * pixel[i][j + 1] + pixel[i][j + 2]);

                    g = Math.abs(x) + Math.abs(y);

//System.out.println(g);
                    pixel[i][j] = g;
                }
            }
//*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*


//**********************************************************************************
            for (int i = 0; i < img.getWidth(); i++) {
                for (int j = 0; j < img.getHeight(); j++) {

                    Color pixelcolor = new Color(pixel[i][j]);

                    int r = pixelcolor.getRed();
                    int gb = pixelcolor.getGreen();
                    int b = pixelcolor.getBlue();

                    int rgb = new Color(r, gb, b).getRGB();

                    // changer la couleur de pixel avec la nouvelle couleur inversée
                    img.setRGB(i, j, rgb);
                }
            }

            // enregistrement d'image
            ImageIO.write(img, "png", new File("src\\resources\\tmp\\" + "Sobel.png"));//ImageIO.write()//;
        } catch (Exception e) {
            System.err.println("erreur -> " + e.getMessage());
        }
    }
}