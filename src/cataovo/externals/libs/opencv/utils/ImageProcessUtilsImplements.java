/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cataovo.externals.libs.opencv.utils;

import cataovo.externals.libs.opencv.Conversion;
import cataovo.utils.Constants;
import cataovo.utils.enums.FileExtension;
import cataovo.utils.libraryUtils.ImageProcessUtils;
import cataovo.wrappers.MatOfPointWrapper;
import cataovo.wrappers.MatWrapper;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

/**
 * Contains the implementation of the stablished steps to detect Aedes eggs in a
 * image.
 *
 * @author Bianca Leopoldo Ramos
 */
public class ImageProcessUtilsImplements implements ImageProcessUtils {

    /**
     * Logging for ImageProcessUtilsImplements.
     */
    private static final Logger LOG = Logger.getLogger(ImageProcessUtilsImplements.class.getName());
    /**
     * Represents white the color.
     */
    private final double[] WHITE;
    /**
     * Represents the black color.
     */
    private final double[] BLACK;

    public ImageProcessUtilsImplements() {
        this.BLACK = new double[]{0};
        this.WHITE = new double[]{255};
    }

    @Override
    public MatWrapper applyBlurOnImage(String savingPath, MatWrapper imageMatToBlur, int ksize_width, int ksize_height) {
        Mat toBlur = imageMatToBlur.getOpencvMat().clone();
        saveImage(toBlur, savingPath.replace(Constants.BLUR_PNG, Constants.ORIGINAL_PNG));
        LOG.log(Level.INFO, "Applying blur...");
        Mat dstn = Mat.zeros(toBlur.size(), CvType.CV_8UC1);
        Imgproc.cvtColor(toBlur, dstn, Imgproc.COLOR_BGR2GRAY);
        Imgproc.blur(dstn, dstn, new Size(ksize_width, ksize_height));
        if (saveImage(dstn, savingPath)) {
            final MatWrapper temp = imageMatToBlur;
            temp.setOpencvMat(dstn);
            return temp;
        }
        return null;
    }

    @Override
    public MatWrapper applyBinaryOnImage(String savingPath, MatWrapper imgToBinary) {
        final var buffImgToBinary = Conversion.getInstance().convertMatToPng(imgToBinary).get();
        LOG.log(Level.INFO, "Applying binary...");
        Mat dstn = Mat.zeros(new Size(buffImgToBinary.getWidth(), buffImgToBinary.getHeight()), CvType.CV_8UC1);
        for (int i = 0; i < buffImgToBinary.getWidth(); i++) {
            for (int j = 0; j < buffImgToBinary.getHeight(); j++) {
                Color colores = new Color(buffImgToBinary.getRGB(i, j));
                double pixelColor = colores.getRed();
                if (pixelColor > 75) {
                    dstn.put(j, i, WHITE);
                } else {
                    dstn.put(j, i, BLACK);
                }
            }
        }
        if (saveImage(dstn, savingPath)) {
            final MatWrapper temp = imgToBinary;
            temp.setOpencvMat(dstn);
            return temp;
        }
        return null;
    }

    @Override
    public MatWrapper applyMorphOnImage(String savingPath, int structuringElementWidth, int structuringElementHeight, int morphologicalOperation, MatWrapper imageToMorph) {
        LOG.log(Level.INFO, "Applying morphology...");
        final Mat result = imageToMorph.getOpencvMat();
        final Mat dstn = Mat.zeros(result.size(), result.type());
        final Mat structuringElement = Imgproc.getStructuringElement(Imgproc.MORPH_ELLIPSE, new Size(structuringElementWidth, structuringElementHeight));
        Imgproc.morphologyEx(result.clone(), dstn, morphologicalOperation, structuringElement);
        if (saveImage(dstn, savingPath)) {
            MatWrapper temp = imageToMorph;
            temp.setOpencvMat(dstn);
            return temp;
        }
        return null;
    }

    @Override
    public Map<Integer, List<List<cataovo.entities.Point>>> drawContoursOnImage(String savingPath, MatWrapper imageToDraw, MatWrapper imgToFindContours, int minSizeArea, int maxSizeArea) {
        LOG.log(Level.INFO, "drawing objects...");
        int numOfContours = 0;
        Mat result = imageToDraw.getOpencvMat();
        final List<MatOfPoint> contours = findContours(imgToFindContours.getOpencvMat().clone());
        final List<MatOfPoint> foundContours = new ArrayList<>();

        for (int i = 0; i < contours.size(); i++) {
            double contourArea = getArea(new MatOfPointWrapper(Conversion.getInstance().convertMatOfPointToList(contours.get(i))));

            if ((contourArea > minSizeArea) && (contourArea < maxSizeArea)) {
                numOfContours++;
                if (contourArea > 3000) {
                    numOfContours++;
                }
                foundContours.add(contours.get(i));

                Imgproc.drawContours(result, contours, i, new Scalar(0, 200, 0), 2, 8, new Mat(), 0, new Point(0, 0));
            }
        }
        LOG.log(Level.INFO, "Quantity of contours: {0}", numOfContours);

        final var list = foundContours.stream().map(c -> Conversion.getInstance().convertMatOfPointToList(c)).toList();

        if (saveImage(result, savingPath)) {
            return Map.of(numOfContours, list);
        }
        return null;
    }

    @Override
    public double getArea(MatOfPointWrapper currentContour) {
        if (currentContour.getMatOfPoint().empty()) {
            return 0;
        }
        return Imgproc.contourArea(currentContour.getMatOfPoint());
    }

    /**
     * Saves the resulted image from any step.
     *
     * @param dstn the image transformed.
     * @param savingPath directory where the transformed image will be saved.
     * @return <code>True</code> if the saving was ok, <code>False</code>
     * otherwise.
     */
    private boolean saveImage(Mat dstn, String savingPath) {
        try {
            BufferedImage image = Conversion.getInstance().convertMatToPng(new MatWrapper(dstn, savingPath)).get();
            ImageIO.write(image, FileExtension.PNG.toString().toLowerCase(), new File(savingPath));
            return true;
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, null, ex);
            return false;
        }
    }

    /**
     * Look for object detecting each boundary from the image
     *
     * @param src the image to find the contours.
     * @return the list of found contours
     * @see #drawContoursOnImage(java.lang.String, org.opencv.core.Mat,
     * org.opencv.core.Mat, int, int)
     */
    public List<MatOfPoint> findContours(Mat src) {
        LOG.log(Level.INFO, "Finding objects...");
        List<MatOfPoint> contours = new ArrayList<>();
        Imgproc.findContours(src, contours, new Mat(), Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_NONE);
        return contours;
    }

}
