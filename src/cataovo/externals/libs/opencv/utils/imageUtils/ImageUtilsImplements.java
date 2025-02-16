/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cataovo.externals.libs.opencv.utils.imageUtils;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Implements resources from Opencv to transform an image.
 *
 * @author Bianca Leopoldo Ramos
 */
public class ImageUtilsImplements implements ImageUtils {

    private static final Logger LOG = Logger.getLogger(ImageUtilsImplements.class.getName());

    /**
     * Draw the dot clicked in the image.
     *
     * @param point a {@link org.opencv.core.Point Point} coordinate to paint
     * @param imagePointed the image to draw a circule point
     * @return the image pointed
     * @see org.opencv.imgproc.Imgproc#circle(org.opencv.core.Mat,
     * org.opencv.core.Point, int, org.opencv.core.Scalar)
     */
    @Override
    public Mat circle(Point point, Mat imagePointed) {
        //draw the circle
        LOG.log(Level.INFO, "Draw the circle...");
        Imgproc.circle(imagePointed,
                point,
                3,
                new Scalar(0, 0, 255),
                Core.FILLED);
        return imagePointed;
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
    @Override
    public Mat rectangle(Point beginPoint, Point endPoint, Mat imageGrid) {
        LOG.log(Level.INFO, "Draw the rectangle...");
        Imgproc.rectangle(imageGrid,
                beginPoint,
                endPoint,
                new Scalar(0, 255, 0),Core.BORDER_REFLECT);
        return imageGrid;
    }

    /**
     * Capture the Rect of the grid for identification. Allows to capture the
     * rect so it can be possible to indentify which grid has a certain egg
     * inside.
     *
     * @param beginGrid the point to begin
     * @param endGrid the point to end
     * @return the area {@link Rect} of the Grid
     */
    @Override
    public Rect captureGridMat(Point beginGrid, Point endGrid) {
        LOG.log(Level.INFO, "Capture the Region...");
        Rect grid = new Rect();
        grid.x = (int) beginGrid.x;
        grid.y = (int) beginGrid.y;
        grid.width = (int) (beginGrid.x - endGrid.x);
        grid.height = (int) (beginGrid.y - endGrid.y);
        return grid;

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
     * @param region the area {@link Rect} of the Grid
     * @param frame the image to capture submat
     * @return the capture submat based on the region coordinates.
     * @see org.opencv.core.Mat#submat(org.opencv.core.Rect)
     */
    @Override
    public Mat captureSubmat(Rect region, Mat frame) {
        LOG.log(Level.INFO, "Capturing submat");
        region.x = region.width > 0 ? (region.x - region.width) : region.x;
        region.y = region.height > 0 ? (region.y - region.height) : region.y;
        region.width = region.width > 0 ? region.width : Math.abs(region.width);
        region.height = region.height > 0 ? region.height : Math.abs(region.height);
        Mat submat = frame.submat(region);
        return submat;
    }

}
