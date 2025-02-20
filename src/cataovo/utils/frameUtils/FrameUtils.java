/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cataovo.utils.frameUtils;

import cataovo.entities.Frame;
import cataovo.entities.Point;
import cataovo.entities.Region;
import cataovo.externals.libs.opencv.Conversion;
import cataovo.utils.libraryUtils.PolygonUtils;
import cataovo.wrappers.lib.MatWrapper;
import cataovo.wrappers.lib.PointWrapper;
import cataovo.wrappers.lib.RectWrapper;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Bianca Leopoldo Ramos
 */
public class FrameUtils {
    
    protected static final Logger LOG = Logger.getLogger(FrameUtils.class.getName());
    protected PolygonUtils imageUtils;

    public FrameUtils(PolygonUtils imageUtils) {
        this.imageUtils = imageUtils;
    }
    
    /**
     * Using the coordinates of a given {@link org.opencv.core.Point Point},
     * creates a dot to denmark the first click to start a region that locates
     * an object Egg.
     *
     * @param frame
     * @param pw the Opencv {@link org.opencv.core.Point Point} Wrapper
     * @return a image with a drawn point circle.
     * @see ImageUtils#circle(org.opencv.core.Point, org.opencv.core.Mat)
     */
    protected MatWrapper circle(final Frame frame, final PointWrapper pw) {
        LOG.log(Level.INFO, "Starting..");
        MatWrapper matWrapper;
        if (!frame.getRegionsContainingEggs().isEmpty()) {
            matWrapper = updateGrids(frame);
        } else {
            matWrapper = Conversion.getInstance().convertImageFrameToMat(frame);
        }
        matWrapper = imageUtils.circle(pw, matWrapper);
        return matWrapper;
    }
    
    /**
     * Using the first point plus width and height based on a second point,
     * creates a rectangle to denmark the region that locates an object Egg.
     *
     * @param frame
     * @param rw the Opencv {@link org.opencv.core.Rect Rect} Wrapper
     * @return a image with a drawn rectangle.
     * @see ImageUtils#rectangle(org.opencv.core.Point, org.opencv.core.Point,
     * org.opencv.core.Mat)
     */
    protected MatWrapper rectangle(final Frame frame, final RectWrapper rw) {
        LOG.log(Level.INFO, "Starting..");
        MatWrapper matWrapper = new MatWrapper(frame);
        if (!frame.getRegionsContainingEggs().isEmpty()) {
            matWrapper = updateGrids(frame);
        }
        final var pointWrapper = new PointWrapper(
                new Point(rw.getRegion().getInitialPoint().getX(),
                        rw.getRegion().getInitialPoint().getY()));
        final var pw2 = new PointWrapper(new Point(
                Math.abs(rw.getRegion().getInitialPoint().getX() - rw.getRegion().getWidth()),
                Math.abs(rw.getRegion().getInitialPoint().getY() - rw.getRegion().getHeight())));
        frame.getRegionsContainingEggs().add(captureGrid(pointWrapper, pw2));
        matWrapper = imageUtils.rectangle(
                pointWrapper, pw2, matWrapper);
        return matWrapper;

    }
    
    /**
     * Captures a submat.
     *
     * @param beginGrid a point to start calculating the
     * {@link org.opencv.core.Rect Rect}.
     * @param endGrid a point to delimitate {@link org.opencv.core.Rect Rect}.
     * @return a subGrid captured on a image {@link org.opencv.core.Mat Rect}
     * @see org.opencv.core.Mat#submat(org.opencv.core.Rect)
     */
    protected Region captureGrid(final PointWrapper beginGrid, final PointWrapper endGrid) {
        return this.imageUtils.captureGridMat(beginGrid, endGrid).getRegion();
    }
    
    /**
     * Draws dinamically each grid of the regions in the frame
     *
     * @param frame
     * @return the updated image with the proper number of grids.
     * @see ImageUtils#rectangle(org.opencv.core.Point, org.opencv.core.Point,
     * org.opencv.core.Mat)
     */
    protected MatWrapper updateGrids(final Frame frame) {
        var mw = new MatWrapper(frame);
        for (var r : frame.getRegionsContainingEggs()) {
            final var pw1 = new PointWrapper(
                    new Point(r.getInitialPoint().getX(),
                            r.getInitialPoint().getY()));
            final var pw2 = new PointWrapper(new Point(
                    Math.abs(r.getInitialPoint().getX() - r.getWidth()),
                    Math.abs(r.getInitialPoint().getY() - r.getHeight())));
            final var wrapper = new MatWrapper();
            wrapper.setOpencvMat(mw.getOpencvMat());
            mw = imageUtils.rectangle(
                    pw1, pw2, wrapper);
        }
        return mw;
    }
    
    /**
     *
     * @param matWrapper
     * @param rects
     * @return
     */
    protected MatWrapper multipleRects(final MatWrapper matWrapper, Collection<RectWrapper> rects) {
        MatWrapper mw = matWrapper;
        for (RectWrapper r : rects) {
            final var beginPoint = new PointWrapper(
                    new Point(r.getRegion().getInitialPoint().getX(),
                            r.getRegion().getInitialPoint().getY()));
            final var endPoint = new PointWrapper(new Point(
                    Math.abs(r.getRegion().getInitialPoint().getX() - r.getRegion().getWidth()),
                    Math.abs(r.getRegion().getInitialPoint().getY() - r.getRegion().getHeight())));
            final var wrapper = new MatWrapper();
            wrapper.setOpencvMat(mw.getOpencvMat());
            mw = imageUtils.rectangle(
                    beginPoint,
                    endPoint,
                    wrapper);
        }

        return mw;
    }
    
    /**
     *
     * @param matWrapper
     * @param circles
     * @return
     */
    protected MatWrapper multipleCircles(final MatWrapper matWrapper, final Collection<Collection<PointWrapper>> circles) {
        var mw = matWrapper;
        for (var col : circles) {
            for (var c : col) {
                mw = imageUtils.circle(c, mw);
            }
        }
        return mw;
    }
    
}
