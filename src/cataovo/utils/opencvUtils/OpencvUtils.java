/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cataovo.utils.opencvUtils;

import cataovo.utils.Constants;
import cataovo.utils.enums.FileExtension;
import cataovo.wrappers.conversion.Conversions;
import cataovo.wrappers.opencv.MatOfPointWrapper;
import cataovo.wrappers.opencv.MatWrapper;
import cataovo.wrappers.opencv.PointWrapper;
import cataovo.wrappers.opencv.RectWrapper;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.imageio.ImageIO;
import org.opencv.core.Core;
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
public class OpencvUtils {

    /**
     * Logging for OpencvUtils.
     */
    private static final Logger LOG = Logger.getLogger(OpencvUtils.class.getName());
    /**
     * Represents white the color.
     */
    private final double[] WHITE;
    /**
     * Represents the black color.
     */
    private final double[] BLACK;

    public OpencvUtils() {
        this.BLACK = new double[]{0};
        this.WHITE = new double[]{255};
    }

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

    public MatWrapper applyBinaryOnImage(String savingPath, MatWrapper imgToBinary) {
        final var buffImgToBinary = new Conversions().convertMatToPng(imgToBinary).get();
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

    public Map<Integer, List<List<cataovo.entities.Point>>> drawContoursOnImage(String savingPath, MatWrapper imageToDraw, MatWrapper imgToFindContours, int minSizeArea, int maxSizeArea) {
        LOG.log(Level.INFO, "drawing objects...");
        int numOfContours = 0;
        Mat result = imageToDraw.getOpencvMat();
        final List<MatOfPoint> contours = findContours(imgToFindContours.getOpencvMat().clone());
        final List<MatOfPoint> foundContours = new ArrayList<>();

        for (int i = 0; i < contours.size(); i++) {
            double contourArea = getArea(new MatOfPointWrapper(contours.get(i).toList().stream().map(OpencvUtils::toPoint).collect(Collectors.toList())));

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

        final var list = foundContours.stream().map(c -> c.toList().stream().map(OpencvUtils::toPoint).collect(Collectors.toList())).toList();

        if (saveImage(result, savingPath)) {
            return Map.of(numOfContours, list);
        }
        return null;
    }

    public static cataovo.entities.Point toPoint(Point point) {
        return new cataovo.entities.Point((int) point.x, (int) point.y);
    }

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
            BufferedImage image = new Conversions().convertMatToPng(new MatWrapper(dstn, savingPath)).get();
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
     * Draw the dot clicked in the image.
     *
     * @param point a {@link org.opencv.core.Point Point} coordinate to paint
     * @param imagePointed the image to draw a circule point
     * @return the image pointed
     * @see org.opencv.imgproc.Imgproc#circle(org.opencv.core.Mat,
     * org.opencv.core.Point, int, org.opencv.core.Scalar)
     */
    public MatWrapper circle(final PointWrapper point, final MatWrapper imagePointed) {
        //draw the circle
        LOG.log(Level.INFO, "Draw the circle...");
        MatWrapper tmp = imagePointed;
        final var img = tmp.getOpencvMat().clone();
        Imgproc.circle(img,
                point.getOpencvPoint(),
                3,
                new Scalar(0, 0, 255),
                Core.FILLED);
        tmp.setOpencvMat(img);
        return tmp;
    }

    /**
     * Draw a grid made by two dots in the image
     *
     * @param beginPoint the point to begin
     * @param endPoint the point to end
     * @param imageGrid the image to place the grid upon based on the points
     * @return the clicked Grid Image
     * @see org.opencv.imgproc.Imgproc#rectangle(org.opencv.core.Mat,
     * org.opencv.core.Rect, org.opencv.core.Scalar)
     */
    public MatWrapper rectangle(PointWrapper beginPoint, PointWrapper endPoint, MatWrapper imageGrid) {
        LOG.log(Level.INFO, "Draw the rectangle...");
        MatWrapper tmp = imageGrid;
        final var img = tmp.getOpencvMat().clone();
        Imgproc.rectangle(img,
                beginPoint.getOpencvPoint(),
                endPoint.getOpencvPoint(),
                new Scalar(0, 255, 0), Core.BORDER_REFLECT);
        tmp.setOpencvMat(img);
        return tmp;
    }

    /**
     * Captures the submat of an denmarked egg. It must have to obbey the
     * expression:
     * <p>
     * <strong><code>
     * 0 &le; roi.x &amp; 0 &le; roi.width &amp; roi.x + roi.width &le; m.cols &amp; 0 &le; roi.y
     * &amp; 0 &le; roi.height &amp; roi.y + roi.height &le; m.rows
     * </code></strong></p>
     *
     * As the <code>frame.submat(rect)</code> is only able to capture a
     * {@link Rect} denmarked from left to right and up to down. Since is the
     * signal (+ or -) what defines right or left, to up or to down, this method
     * must adapt the values to react without the signal.
     *
     * @param region the area {@link RectWrapper} of the Grid
     * @param frame the image to capture submat
     * @return the capture submat based on the region coordinates.
     * @see org.opencv.core.Mat#submat(org.opencv.core.Rect)
     */
    public MatWrapper submat(RectWrapper region, MatWrapper frame) {
        LOG.log(Level.INFO, "Capturing submat");
        MatWrapper tmp = frame;
        tmp.setOpencvMat(tmp.getOpencvMat().submat(region.getRect()));
        return tmp;
    }
}
