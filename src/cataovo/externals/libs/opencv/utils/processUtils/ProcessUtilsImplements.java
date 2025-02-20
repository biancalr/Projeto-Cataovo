/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cataovo.externals.libs.opencv.utils.processUtils;

import cataovo.externals.libs.opencv.utils.conversionUtils.Conversion;
import cataovo.externals.libs.opencv.wrappers.MatWrapper;
import cataovo.utils.constants.Constants;
import cataovo.utils.enums.FileExtension;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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
public class ProcessUtilsImplements implements ProcessUtils {

    /**
     * Logging for ProcessUtilsImplements.
     */
    private static final Logger LOG = Logger.getLogger(ProcessUtilsImplements.class.getName());
    /**
     * Represents white the color.
     */
    private final double[] WHITE;
    /**
     * Represents the black color.
     */
    private final double[] BLACK;
    /**
     * Quantity of eggs in a frame.
     */
    private int quantityOfEggs = 0;
    /**
     * Objects found in each image. Potencial eggs, a single egg or collection
     * of two or mor eggs.
     */
    private List<MatOfPoint> eggsContours = new ArrayList<>();

    public ProcessUtilsImplements() {
        this.BLACK = new double[]{0};
        this.WHITE = new double[]{255};
    }

    @Override
    public Mat applyBlurOnImage(String savingPath, Mat imageMatToBlur, int ksize_width, int ksize_height) {
        saveImage(imageMatToBlur, savingPath.replace(Constants.BLUR_PNG, Constants.ORIGINAL_PNG));
        LOG.log(Level.INFO, "Applying blur...");
        Mat dstn = Mat.zeros(imageMatToBlur.size(), CvType.CV_8UC1);
        Imgproc.cvtColor(imageMatToBlur.clone(), dstn, Imgproc.COLOR_BGR2GRAY);
        Imgproc.blur(dstn, dstn, new Size(ksize_width, ksize_height));
        if (saveImage(dstn, savingPath)) {
            return dstn;
        }
        return null;
    }

    @Override
    public Mat applyBinaryOnImage(String savingPath, BufferedImage buffImgToBinary) {
        LOG.log(Level.INFO, "Applying binary...");
        Mat dstn = Mat.zeros(new Size(buffImgToBinary.getWidth(), buffImgToBinary.getHeight()), CvType.CV_8UC1);
        for (int i = 0; i < buffImgToBinary.getWidth(); i++) {
            for (int j = 0; j < buffImgToBinary.getHeight(); j++) {
                Color colores = new Color(buffImgToBinary.getRGB(i, j));
                double pixelColor = colores.getRed();
                if (pixelColor > 75){
                    dstn.put(j, i, WHITE);
                } else {
                    dstn.put(j, i, BLACK);
                }
            }
        }
        if (saveImage(dstn, savingPath)) {
            return dstn;
        }
        return null;
    }

    @Override
    public Mat applyMorphOnImage(String savingPath, int structuringElementWidth, int structuringElementHeight, int morphologicalOperation, Mat imageToMorph) {
        LOG.log(Level.INFO, "Applying morphology...");
        Mat dstn = Mat.zeros(imageToMorph.size(), imageToMorph.type());
        Mat structuringElement = Imgproc.getStructuringElement(Imgproc.MORPH_ELLIPSE, new Size(structuringElementWidth, structuringElementHeight));
        Imgproc.morphologyEx(imageToMorph.clone(), dstn, morphologicalOperation, structuringElement);
        if (saveImage(dstn, savingPath)) {
            return dstn;
        }
        return null;
    }

    @Override
    public Mat drawContoursOnImage(String savingPath, Mat imageToDraw, Mat imgToFindContours, int minSizeArea, int maxSizeArea) {
        LOG.log(Level.INFO, "drawing objects...");
        int numOfContours = 0;
        Mat result = imageToDraw.clone();
        final List<MatOfPoint> contours = findContours(imgToFindContours.clone());
        final List<MatOfPoint> foundContours = new ArrayList<>();

        for (int i = 0; i < contours.size(); i++) {
            double contourArea = getArea(contours.get(i));

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

        this.quantityOfEggs = numOfContours;
        this.eggsContours = foundContours;

        if (saveImage(result, savingPath)) {
            return result;
        }
        return null;
    }

    @Override
    public double getArea(MatOfPoint currentContour) {
        if (currentContour.empty()) {
            return 0;
        }
        return Imgproc.contourArea(currentContour);
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

    /**
     * <p>
     * Generates the content of the automatic counting.</p>
     *
     * @return A text containg the total of the eggs and a list of a certain
     * quantity of coodinates that forms each object.
     */
    @Override
    public StringBuffer generateAutoReport() {
        StringBuffer builder = new StringBuffer(Integer.toString(this.quantityOfEggs));
        List<Point> auxlist;
        List<Point> mainPoints = new ArrayList<>();
        for (var i = 0; i < this.eggsContours.size(); i++) {
            MatOfPoint get = this.eggsContours.get(i);

            auxlist = get.toList();
            for (int j = 0; j < auxlist.size(); j++) {

                // Saves a point each given steps
                if ((j % Constants.STEPS_TO_POINT_SAVING) == 0) {
                    mainPoints.add(auxlist.get(j));
                }
            }
           
           for (var j = 0; j < mainPoints.size(); j++) {
               if (j!=0) {
                   builder.append(Constants.SEPARATOR);
               } else {
                   builder.append(Constants.OBJECT_SEPARATOR);
               }
               builder.append(mainPoints.get(j).x);
               builder.append(",");
               builder.append(mainPoints.get(j).y);
           }
            
           mainPoints = new ArrayList<>();
            
        }

        return builder;
    }

}
