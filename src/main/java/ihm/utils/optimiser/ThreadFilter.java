package ihm.utils.optimiser;

import ihm.utils.filter.Canny;
import ihm.utils.filter.Laplacien;
import ihm.utils.filter.Prewitt;
import ihm.utils.filter.Sobel;

public class ThreadFilter {

    public void generateImgFiltered(String filePath) throws InterruptedException {

        Thread canny = new Thread() {
            public void run() {
                Canny canny = new Canny();
                canny.filtre(filePath);
            }
        };

        Thread perwitt = new Thread() {
            public void run() {
                Prewitt prewitt = new Prewitt();
                prewitt.filtre(filePath,"src\\resources\\tmp");
            }
        };

        Thread sobel = new Thread() {
            public void run() {
                Sobel sobel = new Sobel();
                sobel.filtre(filePath);
            }
        };

        Thread laplacien = new Thread() {
            public void run() {
                Laplacien laplacien = new Laplacien();
                laplacien.filtre(filePath);
            }
        };

        canny.start();
        sobel.start();
        laplacien.start();
        perwitt.start();

        canny.join();
        sobel.join();
        laplacien.join();
        perwitt.join();
    }
}
