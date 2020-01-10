package ihm.utils;

import java.io.IOException;

public class GetPythonCommand {

    public GetPythonCommand() {

    }

    public String getHist() {
        System.out.println(getClass().getClassLoader().getResource("/python/Modele/"));
        //String script = getClass().getResource("/python/generalized_hough_demo.py").getFile();
        //String argument = getClass().getResource("/tmp/Prewitt.png").getPath();
        //String argument2 = getClass().getResource("/python/Modele/Cercle.png").getPath();

        //String cmd = "pyhton -c " + script + " " + argument + " " + argument2;

        //System.out.println(cmd);
        /*try {
            //String cmd = "pyhton -c " + getClass().getResource("/python/generalized_hough_demo.py").getPath() + " " + getClass().getResource("/tmp/Prewitt.png").getPath() + " " + getClass().getResource("/python/Modele/Cercle.png").getPath();
            System.out.println(cmd);
            Process p = Runtime.getRuntime().exec(cmd);
            java.io.BufferedReader out = new java.io.BufferedReader(new java.io.InputStreamReader(p.getInputStream()));
            return out.lines().toString();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }*/
        return null;
    }

}

class Main {

    public static void main(String[] args) {

        GetPythonCommand getPythonCommand = new GetPythonCommand();
        getPythonCommand.getHist();
        //String script = getClass().getResource("/python/generalized_hough_demo.py").getFile();
        //String argument = getClass().getResource("/tmp/Prewitt.png").getPath();
        //String argument2 = getClass().getResource("/python/Modele/Cercle.png").getPath();

        //String cmd = "pyhton -c " + script + " " + argument + " " + argument2;
    }
}
