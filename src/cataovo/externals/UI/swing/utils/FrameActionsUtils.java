/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cataovo.externals.UI.swing.utils;

import cataovo.entities.Frame;
import cataovo.entities.Point;
import cataovo.entities.Region;
import cataovo.externals.libs.opencv.Conversion;
import cataovo.externals.libs.opencv.utils.PolygonUtilsImplements;
import cataovo.utils.frameUtils.FrameUtils;
import cataovo.wrappers.lib.MatWrapper;
import cataovo.wrappers.lib.PointWrapper;
import cataovo.wrappers.lib.RectWrapper;
import java.util.Collection;
import javax.swing.Icon;
import javax.swing.ImageIcon;

/**
 * Define the actions to do in a {@link cataovo.entities.Frame Frame}.
 *
 * @author Bianca Leopoldo Ramos
 */
public class FrameActionsUtils extends FrameUtils {

    public FrameActionsUtils() {
        super(new PolygonUtilsImplements());
    }

    /**
     * Draws a circle based on a point click.
     *
     * @param frame
     * @param pw the Opencv {@link org.opencv.core.Point} Wrapper
     * @return a image with a drawn point circle.
     */
    public Icon drawCircle(final Frame frame, final PointWrapper pw) {
        return new ImageIcon(Conversion.getInstance().convertMatToImg(super.circle(frame, pw)).get());
    }

    /**
     * Draws a rectangle based on two point clicks.
     *
     * @param frame
     * @param rw the Opencv {@link org.opencv.core.Rect} Wrapper
     * @return a image with a drawn rectangle.
     */
    public Icon drawRectangle(final Frame frame, RectWrapper rw) {
        return new ImageIcon(Conversion.getInstance().convertMatToImg(super.rectangle(frame, rw)).get());
    }

    /**
     * Captures a subgrid based on a {@link cataovo.entities.Region} between two
     * point clicks.
     *
     * @param beginGrid a point to start calculating the
     * {@link org.opencv.core.Rect}.
     * @param endGrid a point to delimitate {@link org.opencv.core.Rect}.
     * @return a subGrid captured on a image {@link org.opencv.core.Mat}
     */
    @Override
    public Region captureGrid(PointWrapper beginGrid, PointWrapper endGrid) {
        return super.captureGrid(beginGrid, endGrid);
    }
    
    /**
     * Updates the current frame.
     *
     * @return the updated image with the proper number of grids.
     * @see FrameUtils#rectangle(org.opencv.core.Point, org.opencv.core.Point, org.opencv.core.Mat)
     */
    @Override
    public MatWrapper updateGrids(final Frame frame) {
        return super.updateGrids(frame);
    }

    /**
     * Captures a subgrid based on a {@link cataovo.entities.Region} between two
     * point clicks.
     *
     * @param frame
     * @param initialPoint a point to start calculating the grid.
     * @param pointClick a point to delimitate the grid
     * @return a Image within these clicks.
     */
    public Icon captureSubframe(final Frame frame, Point initialPoint, Point pointClick) {
        final RectWrapper rectWrapper = new RectWrapper(super.captureGrid(new PointWrapper(initialPoint), new PointWrapper(pointClick)));
        final MatWrapper matWrapper = imageUtils.captureSubmat(rectWrapper, Conversion.getInstance().convertImageFrameToMat(frame));
        return new ImageIcon(Conversion.getInstance().convertMatToImg(matWrapper).get());
    }

    /**
     * 
     * @param frame
     * @param rects
     * @param circles
     * @return 
     */
    public Icon drawFormats(final Frame frame, final Collection<RectWrapper> rects, final Collection<Collection<PointWrapper>> circles) {
        MatWrapper matWrapper = new MatWrapper(frame);
        matWrapper = super.multipleRects(matWrapper, rects);
        matWrapper = super.multipleCircles(matWrapper, circles);
        return new ImageIcon(Conversion.getInstance().convertMatToImg(matWrapper).get());
    }

}
