package ihm.utils;

import java.io.*;
import java.net.URLDecoder;


public class ExecutePythonCommand {

    public static void main(String[] args) throws Exception {
        ProcessBuilder builder = new ProcessBuilder("python",
            getRessource("/python/generalized_hough_demo.py"),
            getRessource("/tmp/Prewitt.png"),
            getRessource("/python/Modele/Cercle.png"));

        Process process = builder.start();
        //process.waitFor();
        InputStream inputStream = process.getInputStream();
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

        String line;

        while ((line = bufferedReader.readLine()) != null) {
            System.out.println(line + "\n");
        }
    }

    private static String getRessource(String internalPath) {
        try {
            String ressource = ExecutePythonCommand.class.getResource(internalPath).getPath();
            String validRessource = URLDecoder.decode(ressource, "UTF-8").substring(1);
            return validRessource;

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }
}
