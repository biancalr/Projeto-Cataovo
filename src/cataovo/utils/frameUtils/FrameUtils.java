/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cataovo.utils.frameUtils;

import cataovo.entities.Frame;
import cataovo.entities.Point;
import cataovo.wrappers.opencv.MatWrapper;
import cataovo.wrappers.opencv.PointWrapper;
import cataovo.wrappers.opencv.RectWrapper;
import java.util.Collection;
import java.util.List;
import javax.swing.Icon;
import javax.swing.ImageIcon;

/**
 * Define the actions to do in a {@link cataovo.entities.Frame Frame}.
 *
 * @author Bianca Leopoldo Ramos
 */
public class FrameUtils {

    private final FrameActions frameActions;
    
    public FrameUtils() {
        this.frameActions = new FrameActions();
    }

    /**
     * Draws a circle based on a point click.
     *
     * @param frame
     * @param point the Opencv {@link org.opencv.core.Point} Wrapper
     * @return a image with a drawn point circle.
     */
    public Icon drawCircle(final Frame frame, final PointWrapper point) {
        return new ImageIcon(this.frameActions.circle(frame, point).convertToImg());
    }

    /**
     * Draws a rectangle based on two point clicks.
     *
     * @param frame
     * @param rectangle the Opencv {@link org.opencv.core.Rect} Wrapper
     * @return a image with a drawn rectangle.
     */
    public Icon drawRectangle(final Frame frame, final RectWrapper rectangle) {
        return new ImageIcon(this.frameActions.rectangle(frame, rectangle).convertToImg());
    }

    /**
     * Updates the current frame.
     *
     * @param frame the frame to update
     * @return the updated image with the proper number of grids.
     * @see FrameUtils#rectangle(cataovo.entities.Frame, cataovo.wrappers.lib.RectWrapper)
     */
    public Icon updateGrids(final Frame frame) {
        return new ImageIcon(this.frameActions.update(frame).convertToImg());
    }

    /**
     * Captures a subgrid based on a {@link cataovo.entities.Region Region}
     * between two point clicks.
     *
     * @param frame
     * @param begin a point to start calculating the grid.
     * @param end a point to delimitate the grid
     * @return a Image within these clicks.
     */
    public Icon getSubframe(final Frame frame, final Point begin, final Point end) {
        final RectWrapper rectWrapper = new RectWrapper(
                this.frameActions.grid(new PointWrapper(begin), new PointWrapper(end)));
        final MatWrapper mat = new MatWrapper(frame).submat(rectWrapper);
        return new ImageIcon(mat.convertToImg());
    }

    /**
     *
     * @param frame
     * @param rects
     * @param circles
     * @return
     */
    public Icon drawPolygons(final Frame frame, final Collection<RectWrapper> rects,
            final List<List<PointWrapper>> circles) {
        MatWrapper mat = new MatWrapper(frame);
        mat = this.frameActions.multipleRects(mat, rects);
        mat = this.frameActions.multipleCircles(mat, circles);
        return new ImageIcon(mat.convertToImg());
    }

}
