/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cataovo.wrappers.opencv;

import cataovo.entities.Point;
import cataovo.utils.Constants;
import cataovo.utils.enums.FileExtension;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.imageio.ImageIO;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

/**
 *
 * @author Bianca Leopoldo Ramos
 */
class MatHelper {
    
    /**
     * Saves the resulted image from any step.
     *
     * @param dstn the image transformed.
     * @param savingPath directory where the transformed image will be saved.
     * @return <code>True</code> if the saving was ok, <code>False</code>
     * otherwise.
     */
    private void saveImage(Mat dstn, String savingPath) throws IOException {
        final BufferedImage image = new MatWrapper(dstn, savingPath).convertToPng();
        ImageIO.write(image, FileExtension.PNG.toString().toLowerCase(), new File(savingPath));
    }

    Mat applyBlur(Mat mat, String savingPath, int ksize_width, int ksize_height) throws IOException {
        saveImage(mat, savingPath.replace(Constants.BLUR_PNG, Constants.ORIGINAL_PNG));
        Mat dstn = Mat.zeros(mat.size(), CvType.CV_8UC1);
        Imgproc.cvtColor(mat, dstn, Imgproc.COLOR_BGR2GRAY);
        Imgproc.blur(dstn, dstn, new Size(ksize_width, ksize_height));
        saveImage(dstn, savingPath);
        return dstn;
    }

    Mat applyBinary(BufferedImage image, String savingPath) throws IOException {
       final var buffImgToBinary = image;
        Mat dstn = Mat.zeros(new Size(buffImgToBinary.getWidth(), buffImgToBinary.getHeight()), CvType.CV_8UC1);
        for (int i = 0; i < buffImgToBinary.getWidth(); i++) {
            for (int j = 0; j < buffImgToBinary.getHeight(); j++) {
                Color colores = new Color(buffImgToBinary.getRGB(i, j));
                double pixelColor = colores.getRed();
                if (pixelColor > 75) {
                    dstn.put(j, i, Constants.WHITE);
                } else {
                    dstn.put(j, i, Constants.BLACK);
                }
            }
        }
        saveImage(dstn, savingPath);
        return dstn;
    }

    Mat applyMorph(Mat mat, String savingPath, int structuringElementWidth, int structuringElementHeight, int morphologicalOperation) throws IOException {
        final Mat result = mat;
        final Mat dstn = Mat.zeros(result.size(), result.type());
        final Mat structuringElement = Imgproc.getStructuringElement(Imgproc.MORPH_ELLIPSE, new Size(structuringElementWidth, structuringElementHeight));
        Imgproc.morphologyEx(result.clone(), dstn, morphologicalOperation, structuringElement);
        saveImage(dstn, savingPath);
        return dstn;
    }
    
    /**
     * Look for object detecting each boundary from the image
     *
     * @param src the image to find the contours.
     * @return the list of found contours
     * @see #drawContoursOnImage(java.lang.String, org.opencv.core.Mat,
     * org.opencv.core.Mat, int, int)
     */
    private List<MatOfPoint> findContours(Mat src) {
        List<MatOfPoint> contours = new ArrayList<>();
        Imgproc.findContours(src, contours, new Mat(), Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_NONE);
        return contours;
    }

    Map<Integer, List<List<Point>>> drawContours(Mat currentState, String savingPath, MatWrapper imageToDraw, int minSizeArea, int maxSizeArea) throws IOException {
        int numOfContours = 0;
        Mat result = imageToDraw.getOpencvMat();
        final List<MatOfPoint> contours = findContours(currentState.clone());
        final List<MatOfPoint> foundContours = new ArrayList<>();
        for (int i = 0; i < contours.size(); i++) {
            double contourArea = new MatOfPointWrapper(contours.get(i).toList().stream().map(p -> {
                PointWrapper pointWrapper = new PointWrapper(p);
                return pointWrapper.getPoint();
            }).collect(Collectors.toList())).getArea();

            if ((contourArea > minSizeArea) && (contourArea < maxSizeArea)) {
                numOfContours++;
                if (contourArea > 3000) {
                    numOfContours++;
                }
                foundContours.add(contours.get(i));

                Imgproc.drawContours(result, contours, i, new Scalar(0, 200, 0), 2, 8, new Mat(), 0, new org.opencv.core.Point(0, 0));
            }
        }

        final var list = foundContours.stream().map(c -> c.toList().stream().map(p -> {
            PointWrapper pointWrapper = new PointWrapper(p);
            return pointWrapper.getPoint();
        }).collect(Collectors.toList())).toList();

        saveImage(result, savingPath);
        return Map.of(numOfContours, list);
    }
}
