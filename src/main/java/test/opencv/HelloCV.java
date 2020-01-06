package test.opencv;

import org.opencv.core.CvType;
import org.opencv.core.Mat;

public class HelloCV {
    static {
        nu.pattern.OpenCV.loadShared();
    }

    public static void main(String[] args) {

        Mat mat = Mat.eye(3, 3, CvType.CV_8UC1);
        System.out.println("mat = " + mat.dump());
    }
}
