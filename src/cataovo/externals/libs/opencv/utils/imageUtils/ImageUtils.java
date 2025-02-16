/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cataovo.externals.libs.opencv.utils.imageUtils;

import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;

/**
 * Use resources from Opencv to transform an image.
 *
 * @author Bianca Leopoldo Ramos.
 */
public interface ImageUtils {

    /**
     * Draw the dot clicked in the image.
     *
     * @param point a {@link org.opencv.core.Point Point} coordinate to paint
     * @param imagePointed the image to draw a circule point
     * @return the image pointed
     * @see org.opencv.imgproc.Imgproc#circle(org.opencv.core.Mat,
     * org.opencv.core.Point, int, org.opencv.core.Scalar)
     */
    public Mat circle(Point point, Mat imagePointed);

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
    public Mat rectangle(Point beginPoint, Point endPoint, Mat imageGrid);

    /**
     * Capture the Rect of the grid for identification. Allows to capture the
     * rect so it can be possible to indentify which grid has a certain egg
     * inside.
     *
     * @param beginGrid the point to begin
     * @param endGrid the point to end
     * @return the area {@link Rect} of the Grid
     */
    public Rect captureGridMat(Point beginGrid, Point endGrid);

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
    public Mat captureSubmat(Rect region, Mat frame);
}
