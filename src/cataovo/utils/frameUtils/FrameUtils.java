/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cataovo.utils.frameUtils;

import cataovo.entities.Frame;
import cataovo.entities.Point;
import cataovo.entities.Region;
import cataovo.wrappers.opencv.MatWrapper;
import cataovo.wrappers.opencv.PointWrapper;
import cataovo.wrappers.opencv.RectWrapper;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Bianca Leopoldo Ramos
 */
public class FrameUtils {

    private static final Logger LOG = Logger.getLogger(FrameUtils.class.getName());

    public FrameUtils() {

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
    public final MatWrapper circle(final Frame frame, final PointWrapper pw) {
        LOG.log(Level.INFO, "Starting..");
        MatWrapper matWrapper;
        if (!frame.getRegions().isEmpty()) {
            matWrapper = update(frame);
        } else {
            matWrapper = new MatWrapper(frame);
        }
        return matWrapper.circle(pw);
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
    public final MatWrapper rectangle(final Frame frame, final RectWrapper rw) {
        LOG.log(Level.INFO, "Starting..");
        MatWrapper matWrapper;
        final PointWrapper pointWrapper;
        final PointWrapper pw2;
        matWrapper = new MatWrapper(frame);
        if (!frame.getRegions().isEmpty()) {
            matWrapper = update(frame);
        }
        pointWrapper = new PointWrapper(
                new Point(rw.getRegion().getInitialPoint().getX(),
                        rw.getRegion().getInitialPoint().getY()));
        pw2 = new PointWrapper(new Point(
                Math.abs(rw.getRegion().getInitialPoint().getX() - rw.getRegion().getWidth()),
                Math.abs(rw.getRegion().getInitialPoint().getY() - rw.getRegion().getHeight())));
        frame.getRegions().add(grid(pointWrapper, pw2));
        return matWrapper.rectangle(pointWrapper, pw2);

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
    public final Region grid(final PointWrapper beginGrid, final PointWrapper endGrid) {
        LOG.log(Level.INFO, "Capture the Region...");
        return new Region(beginGrid.getPoint(),
                (int) (beginGrid.getPoint().getX() - endGrid.getPoint().getX()),
                (int) (beginGrid.getPoint().getY() - endGrid.getPoint().getY()));
    }

    /**
     * Draws dinamically each grid of the regions in the frame
     *
     * @param frame
     * @return the updated image with the proper number of grids.
     * @see ImageUtils#rectangle(org.opencv.core.Point, org.opencv.core.Point,
     * org.opencv.core.Mat)
     */
    public final MatWrapper update(final Frame frame) {
        PointWrapper pw1, pw2;
        MatWrapper mw = new MatWrapper(frame);
        for (Region r : frame.getRegions()) {
            pw1 = new PointWrapper(
                    new Point(r.getInitialPoint().getX(),
                            r.getInitialPoint().getY()));
            pw2 = new PointWrapper(new Point(
                    Math.abs(r.getInitialPoint().getX() - r.getWidth()),
                    Math.abs(r.getInitialPoint().getY() - r.getHeight())));
            mw = mw.rectangle(pw1, pw2);
        }
        return mw;
    }

    /**
     *
     * @param matWrapper
     * @param rects
     * @return
     */
    public final MatWrapper multipleRects(final MatWrapper matWrapper, Collection<RectWrapper> rects) {
        PointWrapper beginPoint, endPoint;
        MatWrapper mw = matWrapper;
        for (RectWrapper r : rects) {
            beginPoint = new PointWrapper(
                    new Point(r.getRegion().getInitialPoint().getX(),
                            r.getRegion().getInitialPoint().getY()));
            endPoint = new PointWrapper(new Point(
                    Math.abs(r.getRegion().getInitialPoint().getX() - r.getRegion().getWidth()),
                    Math.abs(r.getRegion().getInitialPoint().getY() - r.getRegion().getHeight())));
            mw = mw.rectangle(beginPoint, endPoint);
        }
        return mw;
    }

    /**
     *
     * @param matWrapper
     * @param circles
     * @return
     */
    public final MatWrapper multipleCircles(final MatWrapper matWrapper, final List<List<PointWrapper>> circles) {
        var mw = matWrapper;
        for (List<PointWrapper> col : circles) {
            for (PointWrapper c : col) {
                mw = mw.circle(c);
            }
        }
        return mw;
    }
}
