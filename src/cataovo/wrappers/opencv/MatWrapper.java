/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cataovo.wrappers.opencv;

import cataovo.entities.Frame;
import cataovo.utils.conversion.Conversions;
import cataovo.utils.enums.FileExtension;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
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
    private final MatHelper helper;

    public MatWrapper() {
        this.location = null;
        this.WIDTH = 0;
        this.HEIGHT = 0;
        this.mat = new Mat(new Size(WIDTH, HEIGHT), DEPTH);
        this.conversions = new Conversions();
        this.helper = new MatHelper();
    }

    public MatWrapper(final Mat m, final String location) {
        this.mat = m;
        this.location = location;
        this.WIDTH = this.mat.width();
        this.HEIGHT = this.mat.height();
        this.conversions = new Conversions();
        this.helper = new MatHelper();
    }

    public MatWrapper(final Frame frame) {
        this.location = frame.getPaletteFrame().getAbsolutePath();
        this.mat = Imgcodecs.imread(frame.getPaletteFrame().getAbsolutePath()).clone();
        this.WIDTH = this.mat.width();
        this.HEIGHT = this.mat.height();
        this.conversions = new Conversions();
        this.helper = new MatHelper();
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
     *
     * @param savingPath
     * @param ksize_width
     * @param ksize_height
     * @return
     * @throws IOException
     */
    public MatWrapper applyBlur(final String savingPath, final int ksize_width, final int ksize_height) throws IOException {
        MatWrapper aux = this;
        this.setOpencvMat(
                this.helper.applyBlur(this.mat.clone(), savingPath, ksize_width, ksize_height));
        return aux;
    }

    /**
     *
     * @param savingPath
     * @return
     * @throws IOException
     */
    public MatWrapper applyBinary(final String savingPath) throws IOException {
        MatWrapper temp = this;
        temp.setOpencvMat(this.helper.applyBinary(convertToPng(), savingPath));
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
        MatWrapper temp = this;
        temp.setOpencvMat(this.helper.applyMorph(this.mat.clone(), savingPath, structuringElementWidth, structuringElementHeight, morphologicalOperation));
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
        return this.helper.drawContours(this.mat.clone(), savingPath, imageToDraw, minSizeArea, maxSizeArea);
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
