import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.highgui.HighGui;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;
import org.opencv.videoio.Videoio;

import java.util.Arrays;

public class Main {

    public static void print(String input) {
        System.out.println(input);
    }
    public static void wait(int ms) {
        try {
            Thread.sleep(ms);
        } catch(InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
    }
    private static void MyLine(Mat img, Point start, Point end) {
        int thickness = 2;
        int lineType = 8;
        int shift = 0;
        Imgproc.line( img,
                start,
                end,
                new Scalar( 0, 0, 0 ),
                thickness,
                lineType,
                shift );
    }
    public static void main (String[] args){
        System.out.println("Hello, World!");
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        // Init Camera
        VideoCapture capture = new VideoCapture(1);
        if(!capture.isOpened()) {
            System.out.println("No cam on index 1, trying again on index 0.");
            capture = new VideoCapture(0);
        }
        if(!capture.isOpened()) {
            System.out.println("No Camera Detected.");
            System.exit(1);
        }
        // Read Camera Resolution
//        Mat testMat = new Mat();
//        capture.read(testMat);
//        int camWidth = testMat.width();
//        int camHeight = testMat.height();
        int camWidth = 1280;
        int camHeight = 720;
        int gridX = 16;
        int gridY = 9;
        int camdivedX = camWidth / gridX;
        int camdivedY = camHeight /gridY;
        capture.set(Videoio.CAP_PROP_FRAME_WIDTH, camWidth);
        capture.set(Videoio.CAP_PROP_FRAME_HEIGHT, camHeight);
        System.out.println("Set Width to " + camWidth + "\nSet Height to "+ camHeight);

        // Create Variables.
        Mat src = new Mat();
        Mat srcHSV = new Mat();
        Mat srcBlue = new Mat();
        Mat srcRed = new Mat();
        Mat srcGreen = new Mat();
        Mat srcYellow = new Mat();
        Mat srcPurple = new Mat();
        Point point1 = new Point();
        Point point2 = new Point();
        double bluePixels;
        double greenPixels;
        double purplePixels;
        double yellowPixels;
        double[] pixelNumArray;
        int i = 0;
        while(i<100) {
            // Read Image From Webcam

            capture.read(src);

            // Create HSV Image

            Imgproc.cvtColor(src, srcHSV,Imgproc.COLOR_RGB2HSV);

//            for(int j = 0; j < gridY; j++) {
//                //Horizontal Stuff
//                point1.x = 0;
//                point1.y = camdivedY * j;
//                point2.x = camWidth;
//                point2.y = camdivedY * j;
//                MyLine(src,point1,point2);
//
//            }
//            for(int j = 0; j <= gridX; j++) {
//                point1.x = camdivedX * j;
//                point1.y = 0;
//                point2.x = camdivedX * j;
//                point2.y = camHeight;
//                MyLine(src,point1,point2);
//            }
            // Create Masked Images
            Core.inRange(srcHSV, new Scalar(0, 100, 100), new Scalar(10, 255, 255), srcRed);
            Core.inRange(srcHSV, new Scalar(10, 100, 100), new Scalar(40, 255, 255), srcBlue);
            Core.inRange(srcHSV, new Scalar(30, 80, 80), new Scalar(80, 255, 255), srcGreen);
            Core.inRange(srcHSV, new Scalar(90, 100, 60), new Scalar(100, 255, 255), srcYellow);
            Core.inRange(srcHSV, new Scalar(150, 60, 80), new Scalar(190, 255, 255), srcPurple);
            bluePixels = Core.sumElems(srcBlue).val[0];
            purplePixels = Core.sumElems(srcPurple).val[0];
            greenPixels = Core.sumElems(srcGreen).val[0];
            yellowPixels = Core.sumElems(srcYellow).val[0];
            pixelNumArray = new double[]{purplePixels, greenPixels, yellowPixels};
            Arrays.sort(pixelNumArray);
            if (pixelNumArray[2] == purplePixels) {
                print("purple");
            }   if(pixelNumArray[2] == greenPixels) {
                print("green");
            }   if (pixelNumArray[2] == yellowPixels) {
                print("yellow");
            }


            // Show Images.

            HighGui.imshow("src", src);
            HighGui.imshow("blue", srcBlue);
            HighGui.imshow("red", srcRed);
            HighGui.imshow("purple", srcPurple);
            HighGui.imshow("green", srcGreen);
            HighGui.imshow("yellow", srcYellow);
            HighGui.waitKey(1);
//            i++;
        }
        wait(1000);
        System.exit(0);
    }
}