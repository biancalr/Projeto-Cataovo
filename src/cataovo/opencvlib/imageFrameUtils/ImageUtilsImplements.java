/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cataovo.opencvlib.imageFrameUtils;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author bibil
 */
public class ImageUtilsImplements implements ImageUtils {

    private static final Logger LOG = Logger.getLogger(ImageUtilsImplements.class.getName());

    /**
     * Draw the dot clicked in the image.
     *
     * @param point axis to paint
     * @param imagePointed the image to draw a circule point
     * @return the image pointed
     */
    @Override
    public Mat circle(Point point, Mat imagePointed) {
        //draw the circle
        LOG.log(Level.INFO, "Draw the circle...");
        Imgproc.circle(imagePointed,
                point,
                2,
                new Scalar(255, 0, 255),
                Core.FILLED);
        return imagePointed;
    }

    /**
     * Draw a grid made by two dots in the image
     *
     * @param beginPoint
     * @param endPoint
     * @param imageGrid
     * @return the clicked Grid Image
     */
    @Override
    public Mat rectangle(Point beginPoint, Point endPoint, Mat imageGrid) {
        LOG.log(Level.INFO, "Draw the rectangle...");
        Imgproc.rectangle(imageGrid,
                beginPoint,
                endPoint,
                new Scalar(0, 255, 0));
        return imageGrid;
    }

    /**
     * Capture the Rect of the grid for identification. Allows to capture the
     * rect so it can be possible to indentify which grid has a certain egg
     * inside.
     *
     * @param beginGrid
     * @param endGrid
     * @return the area Rect of the clicked Grid
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
     * &amp; 0 &le; roi.height &amp; roi.y + roi.height &le; m.rows </code></strong></p>
     *
     * As the <code>frame.submat(rect)</code> is only able to capture a
     * {@link Rect} denmarked from left to right and up to down. Since is 
     * the signal (+ or -) what defines right or left, to up or to down, this 
     * method must adapt the values to react without the signal.
     *
     * @param region
     * @param frame
     * @return
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
