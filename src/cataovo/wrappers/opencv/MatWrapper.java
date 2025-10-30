/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cataovo.wrappers.opencv;

import cataovo.entities.Frame;
import cataovo.utils.Constants;
import cataovo.utils.enums.FileExtension;
import cataovo.wrappers.conversion.Conversions;
import java.awt.Color;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.imageio.ImageIO;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

/**
 * Wrapps a {@link org.opencv.core.Mat Mat}
 *
 * @author Bianca Leopoldo Ramos
 */
public final class MatWrapper {

    private static final int DEPTH = CvType.CV_8U;
    private int WIDTH;
    private int HEIGHT;
    private Mat mat;
    private String location;
    private final Conversions conversions;

    public MatWrapper() {
        this.location = null;
        this.WIDTH = 0;
        this.HEIGHT = 0;
        this.mat = new Mat(new Size(WIDTH, HEIGHT), DEPTH);
        this.conversions = new Conversions();
    }

    public MatWrapper(final Mat m, final String location) {
        this.mat = m;
        this.location = location;
        this.WIDTH = this.mat.width();
        this.HEIGHT = this.mat.height();
        this.conversions = new Conversions();
    }

    public MatWrapper(final Frame frame) {
        this.location = frame.getPaletteFrame().getAbsolutePath();
        this.mat = Imgcodecs.imread(frame.getPaletteFrame().getAbsolutePath()).clone();
        this.WIDTH = this.mat.width();
        this.HEIGHT = this.mat.height();
        this.conversions = new Conversions();
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Mat getOpencvMat() {
        return mat;
    }

    public void setOpencvMat(final Mat mat) {
        this.mat = mat;
        this.WIDTH = mat.width();
        this.HEIGHT = mat.height();
    }

    public int getWIDTH() {
        return WIDTH;
    }

    public int getHEIGHT() {
        return HEIGHT;
    }

    public int getArea() {
        return this.getWIDTH() * this.getHEIGHT();
    }

    /**
     * @return Mat converted to JPG format
     */
    public Image convertToImg() {
        return this.conversions.convertMatToImg(this).get();
    }

    /**
     * @return Mat converted to JPG format
     */
    public BufferedImage convertToPng() {
        return this.conversions.convertMatToPng(this).get();
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
     * @return the capture submat based on the region coordinates.
     * @see org.opencv.core.Mat#submat(org.opencv.core.Rect)
     */
    public MatWrapper submat(RectWrapper region) {
        this.setOpencvMat(this.mat.submat(region.getRect()));
        return this;
    }

    /**
     * Draw the dot clicked in the image.
     *
     * @param point a {@link org.opencv.core.Point Point} coordinate to paint
     * @return the image pointed
     * @see org.opencv.imgproc.Imgproc#circle(org.opencv.core.Mat,
     * org.opencv.core.Point, int, org.opencv.core.Scalar)
     */
    public MatWrapper circle(PointWrapper point) {
        final var img = this.mat.clone();
        Imgproc.circle(img,
                point.getOpencvPoint(),
                3,
                new Scalar(0, 0, 255),
                Core.FILLED);
        this.setOpencvMat(img);
        return this;
    }

    /**
     * Draw a grid made by two dots in the image
     *
     * @param beginPoint the point to begin
     * @param endPoint the point to end
     * @return the clicked Grid Image
     * @see org.opencv.imgproc.Imgproc#rectangle(org.opencv.core.Mat,
     * org.opencv.core.Rect, org.opencv.core.Scalar)
     */
    public MatWrapper rectangle(PointWrapper beginPoint, PointWrapper endPoint) {
        final var img = this.mat.clone();
        Imgproc.rectangle(img,
                beginPoint.getOpencvPoint(),
                endPoint.getOpencvPoint(),
                new Scalar(0, 255, 0), Core.BORDER_REFLECT);
        this.setOpencvMat(img);
        return this;
    }

    /**
     * Encodes an image into a memory buffer
     *
     * @param extension
     * @param ofBytesWrapper
     * @return
     */
    public boolean toMemBuffer(FileExtension extension, MatOfBytesWrapper ofBytesWrapper) {
        return Imgcodecs.imencode("." + extension.toString().toLowerCase(),
                this.getOpencvMat(), ofBytesWrapper);
    }

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

    /**
     *
     * @param savingPath
     * @param ksize_width
     * @param ksize_height
     * @return
     * @throws IOException
     */
    public MatWrapper applyBlur(final String savingPath, final int ksize_width, final int ksize_height) throws IOException {
        Mat toBlur = this.mat.clone();
        saveImage(toBlur, savingPath.replace(Constants.BLUR_PNG, Constants.ORIGINAL_PNG));
        Mat dstn = Mat.zeros(toBlur.size(), CvType.CV_8UC1);
        Imgproc.cvtColor(toBlur, dstn, Imgproc.COLOR_BGR2GRAY);
        Imgproc.blur(dstn, dstn, new Size(ksize_width, ksize_height));
        saveImage(dstn, savingPath);
        MatWrapper temp = this;
        temp.setOpencvMat(dstn);
        return temp;
    }

    /**
     *
     * @param savingPath
     * @return
     * @throws IOException
     */
    public MatWrapper applyBinary(final String savingPath) throws IOException {
        final var buffImgToBinary = convertToPng();
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
        MatWrapper temp = this;
        temp.setOpencvMat(dstn);
        return temp;
    }

    /**
     *
     * @param savingPath
     * @param structuringElementWidth
     * @param structuringElementHeight
     * @param morphologicalOperation
     * @return
     * @throws IOException
     */
    public MatWrapper applyMorph(String savingPath, int structuringElementWidth, int structuringElementHeight, int morphologicalOperation) throws IOException {
        final Mat result = this.mat.clone();
        final Mat dstn = Mat.zeros(result.size(), result.type());
        final Mat structuringElement = Imgproc.getStructuringElement(Imgproc.MORPH_ELLIPSE, new Size(structuringElementWidth, structuringElementHeight));
        Imgproc.morphologyEx(result.clone(), dstn, morphologicalOperation, structuringElement);
        saveImage(dstn, savingPath);
        MatWrapper temp = this;
        temp.setOpencvMat(dstn);
        return temp;
    }

    /**
     *
     * @param savingPath
     * @param imageToDraw
     * @param minSizeArea
     * @param maxSizeArea
     * @return
     * @throws IOException
     */
    public Map<Integer, List<List<cataovo.entities.Point>>> drawContours(String savingPath, MatWrapper imageToDraw, int minSizeArea, int maxSizeArea) throws IOException {
        int numOfContours = 0;
        Mat result = imageToDraw.getOpencvMat();
        final List<MatOfPoint> contours = findContours(this.mat.clone());
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

                Imgproc.drawContours(result, contours, i, new Scalar(0, 200, 0), 2, 8, new Mat(), 0, new Point(0, 0));
            }
        }

        final var list = foundContours.stream().map(c -> c.toList().stream().map(p -> {
            PointWrapper pointWrapper = new PointWrapper(p);
            return pointWrapper.getPoint();
        }).collect(Collectors.toList())).toList();

        saveImage(result, savingPath);
        return Map.of(numOfContours, list);
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
        List<MatOfPoint> contours = new ArrayList<>();
        Imgproc.findContours(src, contours, new Mat(), Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_NONE);
        return contours;
    }

    /**
     * Get Countour Area
     *
     * @return
     */
    public double getOpencvArea() {
        if (this.mat.empty()) {
            return 0;
        }
        return Imgproc.contourArea(this.mat);
    }
}
