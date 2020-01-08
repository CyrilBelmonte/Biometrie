package test.opencv;

import org.opencv.videoio.VideoCapture;

import java.awt.image.BufferedImage;

import static org.opencv.videoio.Videoio.CV_CAP_PROP_FRAME_HEIGHT;
import static org.opencv.videoio.Videoio.CV_CAP_PROP_FRAME_WIDTH;

public class VideoCap {

    static {
        nu.pattern.OpenCV.loadShared();
    }

    VideoCapture cap;
    Mat2Image mat2Img = new Mat2Image();

    VideoCap() {
        cap = new VideoCapture();
        cap.set(CV_CAP_PROP_FRAME_WIDTH, 1920);
        cap.set(CV_CAP_PROP_FRAME_HEIGHT, 1080);
        cap.open(0);
    }

    BufferedImage getOneFrame() {
        cap.read(mat2Img.mat);
        return mat2Img.getImage(mat2Img.mat);
    }
}