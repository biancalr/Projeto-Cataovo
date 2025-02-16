/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cataovo.utils.frameUtils;

import cataovo.entities.Frame;
import cataovo.entities.Point;
import cataovo.entities.Region;
import cataovo.externals.libs.opencvlib.converters.Converter;
import cataovo.externals.libs.opencvlib.wrappers.MatWrapper;
import cataovo.externals.libs.opencvlib.wrappers.PointWrapper;
import cataovo.externals.libs.opencvlib.wrappers.RectWrapper;
import java.util.Collection;
import javax.swing.Icon;
import javax.swing.ImageIcon;

/**
 * Define the actions to do in a {@link cataovo.entities.Frame Frame}.
 *
 * @author Bianca Leopoldo Ramos
 */
public class FrameActionsUtils extends FrameUtils {

    public FrameActionsUtils(Frame frame) {
        super(frame);
    }

    public FrameActionsUtils() {
        super();
    }

    public Frame getFrame() {
        return frame;
    }

    /**
     * Draws a circle based on a point click.
     *
     * @param pw the Opencv {@link org.opencv.core.Point} Wrapper
     * @return a image with a drawn point circle.
     */
    @Override
    public Icon drawCircle(PointWrapper pw) {
        return super.dot(pw);
    }

    /**
     * Draws a rectangle based on two point clicks.
     *
     * @param rw the Opencv {@link org.opencv.core.Rect} Wrapper
     * @return a image with a drawn rectangle.
     */
    @Override
    public Icon drawRectangle(RectWrapper rw) {
        return super.rectangle(rw);
    }

    /**
     * Updates drawn rectangles based on add/remove
     * {@link cataovo.entities.Region}s.
     *
     * @return an image with the quantity of grids updated.
     */
    public MatWrapper updateGridsOnFrame() {
        return super.updateGrids();
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
    protected Region captureGridSubmat(PointWrapper beginGrid, PointWrapper endGrid) {
        return super.captureGrid(beginGrid, endGrid);
    }

    /**
     * Updates a grid if there's already denmarked
     * {@link cataovo.entities.Region}s
     *
     * @return the updated image with the proper number of grids.
     */
    @Override
    protected MatWrapper prepareGrids() {
        return super.updateGrids();
    }

    /**
     * Captures a subgrid based on a {@link cataovo.entities.Region} between two
     * point clicks.
     *
     * @param initialPoint a point to start calculating the grid.
     * @param pointClick a point to delimitate the grid
     * @return a Image within these clicks.
     */
    public Icon captureSubframe(Point initialPoint, Point pointClick) {
        rectWrapper = new RectWrapper(super.captureGrid(new PointWrapper(initialPoint), new PointWrapper(pointClick)));
        matWrapper = new MatWrapper(imageUtils.captureSubmat(rectWrapper.getOpencvRect(), Converter.getInstance().convertImageFrameToMat(frame).getOpencvMat()), null);
        return new ImageIcon(Converter.getInstance().convertMatToImg(matWrapper).get());
    }

    /**
     * 
     * @param rects
     * @param circles
     * @return 
     */
    public Icon drawFormatsOnFrame(Collection<RectWrapper> rects, Collection<Collection<PointWrapper>> circles) {
        matWrapper = new MatWrapper(frame);
        matWrapper.setOpencvMat(super.drawMultipleRectangles(rects).getOpencvMat());
        matWrapper.setOpencvMat(super.drawMultiplePoints(circles).getOpencvMat());
        return new ImageIcon(Converter.getInstance().convertMatToImg(matWrapper).get());
    }

}
