package ihm.utils.optimiser;

import ihm.utils.filter.Canny;
import ihm.utils.filter.Laplacien;
import ihm.utils.filter.Prewitt;
import ihm.utils.filter.Sobel;

public class ThreadFilter {

    public void generateImgFiltered(String fileIn, String fileOut) throws InterruptedException {
        /*

        Thread canny = new Thread() {
            public void run() {
                Canny canny = new Canny();
                canny.filterImg(fileIn, fileOut);
            }
        };

        Thread perwitt = new Thread() {
            public void run() {
                Prewitt prewitt = new Prewitt();
                prewitt.filterImg(fileIn, fileOut);
            }
        };

        Thread sobel = new Thread() {
            public void run() {
                Sobel sobel = new Sobel();
                sobel.filterImg(fileIn, fileOut);
            }
        };

        Thread laplacien = new Thread() {
            public void run() {
                Laplacien laplacien = new Laplacien();
                laplacien.filterImg(fileIn, fileOut);
            }
        };

        canny.start();
        sobel.start();
        laplacien.start();
        perwitt.start();

        canny.join();
        sobel.join();
        laplacien.join();
        perwitt.join();*/
    }
}
