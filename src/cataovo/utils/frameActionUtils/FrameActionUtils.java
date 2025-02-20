/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cataovo.utils.frameActionUtils;

import cataovo.entities.Frame;
import cataovo.entities.Point;
import cataovo.entities.Region;
import cataovo.externals.libs.opencv.utils.conversionUtils.Conversion;
import cataovo.externals.libs.opencv.wrappers.MatWrapper;
import cataovo.externals.libs.opencv.wrappers.PointWrapper;
import cataovo.externals.libs.opencv.wrappers.RectWrapper;
import cataovo.utils.imageUtils.ImageUtils;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Define the actions to do in a {@link Frame}.
 *
 * @author Bianca Leopoldo Ramos
 */
public abstract class FrameActionUtils {

    protected static final Logger LOG = Logger.getLogger(FrameActionUtils.class.getName());
    protected PointWrapper pointWrapper;
    protected RectWrapper rectWrapper;
    protected MatWrapper matWrapper;
    protected Frame frame = null;
    protected ImageUtils imageUtils;

    public FrameActionUtils(Frame frame, ImageUtils imageUtils) {
        pointWrapper = new PointWrapper();
        rectWrapper = new RectWrapper();
        matWrapper = new MatWrapper(frame);
        this.frame = frame;
        this.imageUtils = imageUtils;
    }

    public FrameActionUtils(ImageUtils imageUtils) {
        pointWrapper = new PointWrapper();
        rectWrapper = new RectWrapper();
        matWrapper = new MatWrapper();
        this.imageUtils = imageUtils;
    }

    public PointWrapper getPointWrapper() {
        return pointWrapper;
    }

    public RectWrapper getRectWrapper() {
        return rectWrapper;
    }

    public MatWrapper getMatWrapper() {
        return matWrapper;
    }

    /**
     * Using the coordinates of a given {@link org.opencv.core.Point Point},
     * creates a dot to denmark the first click to start a region that locates
     * an object Egg.
     *
     * @param pw the Opencv {@link org.opencv.core.Point Point} Wrapper
     * @return a image with a drawn point circle.
     * @see ImageUtils#circle(org.opencv.core.Point, org.opencv.core.Mat)
     */
    protected MatWrapper circle(PointWrapper pw) {
        LOG.log(Level.INFO, "Starting..");
        if (!this.frame.getRegionsContainingEggs().isEmpty()) {
            this.matWrapper = updateGrids();
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
     * @param rw the Opencv {@link org.opencv.core.Rect Rect} Wrapper
     * @return a image with a drawn rectangle.
     * @see ImageUtils#rectangle(org.opencv.core.Point, org.opencv.core.Point,
     * org.opencv.core.Mat)
     */
    protected MatWrapper rectangle(RectWrapper rw) {
        LOG.log(Level.INFO, "Starting..");
        if (!this.frame.getRegionsContainingEggs().isEmpty()) {
            this.matWrapper = updateGrids();
        }
        pointWrapper = new PointWrapper(
                new Point(rw.getRegion().getInitialPoint().getX(),
                        rw.getRegion().getInitialPoint().getY()));
        final var pw2 = new PointWrapper(new Point(
                Math.abs(rw.getRegion().getInitialPoint().getX() - rw.getRegion().getWidth()),
                Math.abs(rw.getRegion().getInitialPoint().getY() - rw.getRegion().getHeight())));
        this.frame.getRegionsContainingEggs().add(captureGrid(pointWrapper, pw2));
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
     * @return the updated image with the proper number of grids.
     * @see ImageUtils#rectangle(org.opencv.core.Point, org.opencv.core.Point,
     * org.opencv.core.Mat)
     */
    protected MatWrapper updateGrids() {
        var mw = new MatWrapper(this.frame);
        for (Region r : this.frame.getRegionsContainingEggs()) {
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
     * @param rects
     * @return
     */
    protected MatWrapper multipleRects(Collection<RectWrapper> rects) {
        MatWrapper mw = new MatWrapper(this.frame);
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
     * @param circles
     * @return
     */
    protected MatWrapper multipleCircles(final Collection<Collection<PointWrapper>> circles) {
        var mw = this.matWrapper;
        for (var col : circles) {
            for (var c : col) {
                mw = imageUtils.circle(c, mw);
            }
        }
        return mw;
    }
}
