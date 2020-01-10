package ihm.utils;

import java.io.*;
import java.net.URLDecoder;


public class Hough {
    public static String calculateHistogram() {
        try {
            ProcessBuilder builder = new ProcessBuilder("python",
                    getRessource("/python/generalized_hough_demo.py"),
                    getRessource("/tmp/Prewitt.png"),
                    getRessource("/python/Modele/Cercle.png"));

            Process process = builder.start();
            //process.waitFor();
            InputStream inputStream = process.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            return bufferedReader.readLine();
        } catch (IOException e) {

            return null;
        }
    }

    private static String getRessource(String internalPath) {
        try {
            String ressource = Hough.class.getResource(internalPath).getPath();
            String validRessource = URLDecoder.decode(ressource, "UTF-8").substring(1);
            return validRessource;

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }
}
