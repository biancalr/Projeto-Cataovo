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
import cataovo.externals.libs.opencvlib.imageFrameUtils.ImageUtils;
import cataovo.externals.libs.opencvlib.imageFrameUtils.ImageUtilsImplements;
import cataovo.externals.libs.opencvlib.wrappers.MatWrapper;
import cataovo.externals.libs.opencvlib.wrappers.PointWrapper;
import cataovo.externals.libs.opencvlib.wrappers.RectWrapper;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Icon;
import javax.swing.ImageIcon;

/**
 * Define the actions to do in a {@link Frame}.
 *
 * @author Bianca Leopoldo Ramos
 */
public abstract class FrameUtils {

    protected static final Logger LOG = Logger.getLogger(FrameUtils.class.getName());
    protected PointWrapper pointWrapper;
    protected RectWrapper rectWrapper;
    protected MatWrapper matWrapper;
    protected Frame frame = null;
    protected ImageUtils imageUtils;

    public FrameUtils(Frame frame) {
        pointWrapper = new PointWrapper();
        rectWrapper = new RectWrapper();
        matWrapper = new MatWrapper(frame);
        this.frame = frame;
        this.imageUtils = new ImageUtilsImplements();
    }

    public FrameUtils() {
        pointWrapper = new PointWrapper();
        rectWrapper = new RectWrapper();
        matWrapper = new MatWrapper();
        this.imageUtils = new ImageUtilsImplements();
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
     * Draws a circle based on a point click.
     *
     * @param pw the Opencv {@link org.opencv.core.Point Point} Wrapper
     * @return a image with a drawn point circle.
     * @see ImageUtils#circle(org.opencv.core.Point, org.opencv.core.Mat)
     */
    protected abstract Icon drawCircle(PointWrapper pw);

    /**
     * Draws a rectangle based on two point clicks.
     *
     * @param rw the Opencv {@link org.opencv.core.Rect Rect} Wrapper
     * @return a image with a drawn rectangle.
     * @see ImageUtils#rectangle(org.opencv.core.Point, org.opencv.core.Point,
     * org.opencv.core.Mat)
     */
    protected abstract Icon drawRectangle(RectWrapper rw);

    /**
     * Captures a subgrid based on a {@link cataovo.entities.Region Region}
     * within two point clicks.
     *
     * @param beginGrid a point to start calculating the
     * {@link org.opencv.core.Rect Rect}.
     * @param endGrid a point to delimitate {@link org.opencv.core.Rect Rect}.
     * @return a subGrid captured on a image {@link org.opencv.core.Mat Rect}
     * @see org.opencv.core.Mat#submat(org.opencv.core.Rect)
     */
    protected abstract Region captureGridSubmat(PointWrapper beginGrid, PointWrapper endGrid);

    /**
     * Updates a grid if there's already denmarked
     * {@link cataovo.entities.Region Regions}
     *
     * @return the updated image with the proper number of grids.
     * @see ImageUtils#rectangle(org.opencv.core.Point, org.opencv.core.Point,
     * org.opencv.core.Mat)
     */
    protected abstract MatWrapper prepareGrids();

    /**
     * Create a dot.
     *
     * @param pw the Opencv {@link org.opencv.core.Point Point} Wrapper
     * @return a image with a drawn point circle.
     */
    protected Icon dot(PointWrapper pw) {
        return drawDot(pw);
    }

    /**
     * Create a rectangle.
     *
     * @param rw the Opencv {@link org.opencv.core.Rect Rect} Wrapper
     * @return a image with a drawn rectangle.
     */
    protected Icon rectangle(RectWrapper rw) {
        return drawSquare(rw);
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
    protected Region captureGrid(PointWrapper beginGrid, PointWrapper endGrid) {
        return captureSubmat(beginGrid, endGrid);
    }

    /**
     * Updates a grid.
     *
     * @return the updated image with the proper number of grids.
     * @see ImageUtils#rectangle(org.opencv.core.Point, org.opencv.core.Point,
     * org.opencv.core.Mat)
     */
    protected MatWrapper updateGrids() {
        return preprareRegions();
    }

    /**
     *
     * @param rects
     * @return
     */
    protected MatWrapper drawMultipleRectangles(Collection<RectWrapper> rects) {
        return drawMultipleRects(rects);
    }

    /**
     *
     * @param circles
     * @return
     */
    protected MatWrapper drawMultiplePoints(Collection<Collection<PointWrapper>> circles) {
        return drawMultipleCircles(circles);
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
    private Icon drawDot(PointWrapper pw) {
        LOG.log(Level.INFO, "Starting..");
        if (!this.frame.getRegionsContainingEggs().isEmpty()) {
            this.matWrapper = preprareRegions();
        } else {
            matWrapper = Converter.getInstance().convertImageFrameToMat(frame);
        }
        matWrapper.setOpencvMat(imageUtils.circle(pw.getOpencvPoint(), matWrapper.getOpencvMat()).clone());
        return new ImageIcon(Converter.getInstance().convertMatToImg(matWrapper).get());
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
    private Icon drawSquare(RectWrapper rw) {
        LOG.log(Level.INFO, "Starting..");
        if (!this.frame.getRegionsContainingEggs().isEmpty()) {
            this.matWrapper = preprareRegions();
        }
        pointWrapper = new PointWrapper(
                new Point(rw.getRegion().getInitialPoint().getX(),
                        rw.getRegion().getInitialPoint().getY()));
        PointWrapper pw2 = new PointWrapper(new Point(
                Math.abs(rw.getRegion().getInitialPoint().getX() - rw.getRegion().getWidth()),
                Math.abs(rw.getRegion().getInitialPoint().getY() - rw.getRegion().getHeight())));
        this.frame.getRegionsContainingEggs().add(captureSubmat(pointWrapper, pw2));
        matWrapper.setOpencvMat(imageUtils.rectangle(
                pointWrapper.getOpencvPoint(), pw2.getOpencvPoint(), matWrapper.getOpencvMat()));
        return new ImageIcon(Converter.getInstance().convertMatToImg(matWrapper).get());
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
    private Region captureSubmat(PointWrapper beginGrid, PointWrapper endGrid) {
        return new RectWrapper(imageUtils.captureGridMat(beginGrid.getOpencvPoint(), endGrid.getOpencvPoint())).getRegion();
    }

    /**
     * Draws dinamically each grid of the regions in the frame
     *
     * @return the updated image with the proper number of grids.
     * @see ImageUtils#rectangle(org.opencv.core.Point, org.opencv.core.Point,
     * org.opencv.core.Mat)
     */
    private MatWrapper preprareRegions() {
        MatWrapper mw = new MatWrapper(this.frame);
        this.frame.getRegionsContainingEggs().stream().forEach((r) -> {
            PointWrapper pw1 = new PointWrapper(
                    new Point(r.getInitialPoint().getX(),
                            r.getInitialPoint().getY()));
            PointWrapper pw2 = new PointWrapper(new Point(
                    Math.abs(r.getInitialPoint().getX() - r.getWidth()),
                    Math.abs(r.getInitialPoint().getY() - r.getHeight())));
            MatWrapper wrapper = new MatWrapper();
            wrapper.setOpencvMat(mw.getOpencvMat());
            mw.setOpencvMat(imageUtils.rectangle(
                    pw1.getOpencvPoint(), pw2.getOpencvPoint(), wrapper.getOpencvMat()).clone());
        });
        return mw;
    }

    /**
     *
     * @param rects
     * @return
     */
    private MatWrapper drawMultipleRects(Collection<RectWrapper> rects) {
        MatWrapper mw = new MatWrapper(this.frame);
        rects.stream().forEach((r) -> {
            PointWrapper beginPoint = new PointWrapper(
                    new Point(r.getRegion().getInitialPoint().getX(),
                            r.getRegion().getInitialPoint().getY()));
            PointWrapper endPoint = new PointWrapper(new Point(
                    Math.abs(r.getRegion().getInitialPoint().getX() - r.getRegion().getWidth()),
                    Math.abs(r.getRegion().getInitialPoint().getY() - r.getRegion().getHeight())));
            MatWrapper wrapper = new MatWrapper();
            wrapper.setOpencvMat(mw.getOpencvMat());
            mw.setOpencvMat(imageUtils.rectangle(
                    beginPoint.getOpencvPoint(),
                    endPoint.getOpencvPoint(),
                    wrapper.getOpencvMat()).clone());
        });
        return mw;
    }

    /**
     *
     * @param circles
     * @return
     */
    private MatWrapper drawMultipleCircles(Collection<Collection<PointWrapper>> circles) {
        MatWrapper mw = this.matWrapper;
        circles.stream().forEach((col) -> {col.stream().forEach((c) -> {
                mw.setOpencvMat(imageUtils.circle(c.getOpencvPoint(), mw.getOpencvMat()).clone());
        });});
        return mw;
    }
}
