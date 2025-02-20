/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cataovo.externals.libs.opencv.utils;

import cataovo.wrappers.lib.MatWrapper;
import cataovo.wrappers.lib.PointWrapper;
import cataovo.wrappers.lib.RectWrapper;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.opencv.core.Core;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import cataovo.utils.libraryUtils.PolygonUtils;

/**
 * Implements resources from Opencv to transform an image.
 *
 * @author Bianca Leopoldo Ramos
 */
public class PolygonUtilsImplements implements PolygonUtils {

    private static final Logger LOG = Logger.getLogger(PolygonUtilsImplements.class.getName());

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
    @Override
    public MatWrapper rectangle(PointWrapper beginPoint, PointWrapper endPoint, MatWrapper imageGrid) {
        LOG.log(Level.INFO, "Draw the rectangle...");
        MatWrapper tmp = imageGrid;
        final var img = tmp.getOpencvMat().clone();
        Imgproc.rectangle(img,
                beginPoint.getOpencvPoint(),
                endPoint.getOpencvPoint(),
                new Scalar(0, 255, 0),Core.BORDER_REFLECT);
        tmp.setOpencvMat(img);
        return tmp;
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
    public RectWrapper captureGridMat(PointWrapper beginGrid, PointWrapper endGrid) {
        LOG.log(Level.INFO, "Capture the Region...");
        final Rect grid = new Rect();
        grid.x = (int) beginGrid.getOpencvPoint().x;
        grid.y = (int) beginGrid.getOpencvPoint().y;
        grid.width = (int) (beginGrid.getOpencvPoint().x - endGrid.getOpencvPoint().x);
        grid.height = (int) (beginGrid.getOpencvPoint().y - endGrid.getOpencvPoint().y);
        return new RectWrapper(grid);

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
    public MatWrapper captureSubmat(RectWrapper region, MatWrapper frame) {
        LOG.log(Level.INFO, "Capturing submat");
        MatWrapper tmp = frame;
        final Rect rect = new Rect();
        rect.x = region.getOpencvRect().width > 0 ? (region.getOpencvRect().x - region.getOpencvRect().width) : region.getOpencvRect().x;
        rect.y = region.getOpencvRect().height > 0 ? (region.getOpencvRect().y - region.getOpencvRect().height) : region.getOpencvRect().y;
        rect.width = region.getOpencvRect().width > 0 ? region.getOpencvRect().width : Math.abs(region.getOpencvRect().width);
        rect.height = region.getOpencvRect().height > 0 ? region.getOpencvRect().height : Math.abs(region.getOpencvRect().height);
        tmp.setOpencvMat(tmp.getOpencvMat().submat(rect));
        return tmp;
    }

}
