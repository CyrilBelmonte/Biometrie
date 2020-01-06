package test.opencv;

import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

public class Tools {

    private Mat mat;

    public Tools(Mat mat) {
        this.mat = mat;
    }

    public Mat getGrayMat() {
        Mat grayMat = new Mat();
        Imgproc.cvtColor(grayMat, this.mat, Imgproc.COLOR_BGR2GRAY);
        return grayMat;
    }
}
