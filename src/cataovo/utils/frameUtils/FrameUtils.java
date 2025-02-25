/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cataovo.utils.frameUtils;

import cataovo.entities.Frame;
import cataovo.entities.Point;
import cataovo.entities.Region;
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
            matWrapper = update(frame);
        } else {
            matWrapper = new MatWrapper(frame);
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
            matWrapper = update(frame);
        }
        final var pointWrapper = new PointWrapper(
                new Point(rw.getRegion().getInitialPoint().getX(),
                        rw.getRegion().getInitialPoint().getY()));
        final var pw2 = new PointWrapper(new Point(
                Math.abs(rw.getRegion().getInitialPoint().getX() - rw.getRegion().getWidth()),
                Math.abs(rw.getRegion().getInitialPoint().getY() - rw.getRegion().getHeight())));
        frame.getRegionsContainingEggs().add(grid(pointWrapper, pw2));
        matWrapper = imageUtils.rectangle(
                pointWrapper, pw2, matWrapper);
        return matWrapper;

    }

    /**
     * Capture the Rect of the grid for identification. Allows to capture the
     * rect so it can be possible to indentify which grid has a certain egg
     * inside.
     *
     * @param beginGrid the point to begin
     * @param endGrid the point to end
     * @return the area {@link Region} of the Grid
     */
    protected Region grid(final PointWrapper beginGrid, final PointWrapper endGrid) {
        LOG.log(Level.INFO, "Capture the Region...");
        final Region grid = new Region();
        grid.setInitialPoint(beginGrid.getPoint());
        grid.setWidth((int) (beginGrid.getPoint().getX() - endGrid.getPoint().getX()));
        grid.setHeight((int) (beginGrid.getPoint().getY() - endGrid.getPoint().getY()));
        return grid;
    }

    /**
     * Draws dinamically each grid of the regions in the frame
     *
     * @param frame
     * @return the updated image with the proper number of grids.
     * @see ImageUtils#rectangle(org.opencv.core.Point, org.opencv.core.Point,
     * org.opencv.core.Mat)
     */
    protected MatWrapper update(final Frame frame) {
        var mw = new MatWrapper(frame);
        for (var r : frame.getRegionsContainingEggs()) {
            final var pw1 = new PointWrapper(
                    new Point(r.getInitialPoint().getX(),
                            r.getInitialPoint().getY()));
            final var pw2 = new PointWrapper(new Point(
                    Math.abs(r.getInitialPoint().getX() - r.getWidth()),
                    Math.abs(r.getInitialPoint().getY() - r.getHeight())));
            mw = imageUtils.rectangle(
                    pw1, pw2, mw);
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
            mw = imageUtils.rectangle(
                    beginPoint,
                    endPoint,
                    mw);
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
