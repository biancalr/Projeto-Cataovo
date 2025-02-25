/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cataovo.externals.UI.swing.utils;

import cataovo.entities.Frame;
import cataovo.entities.Point;
import cataovo.externals.libs.opencv.utils.PolygonImplements;
import cataovo.utils.frameUtils.FrameUtils;
import cataovo.wrappers.conversion.Conversions;
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

    private final Conversions conversion;
    
    public FrameActionsUtils() {
        super(new PolygonImplements());
        conversion = new Conversions();
    }

    /**
     * Draws a circle based on a point click.
     *
     * @param frame
     * @param point the Opencv {@link org.opencv.core.Point} Wrapper
     * @return a image with a drawn point circle.
     */
    public Icon drawCircle(final Frame frame, final PointWrapper point) {
        return new ImageIcon(this.conversion.convertMatToImg(super.circle(frame, point)).get());
    }

    /**
     * Draws a rectangle based on two point clicks.
     *
     * @param frame
     * @param rectangle the Opencv {@link org.opencv.core.Rect} Wrapper
     * @return a image with a drawn rectangle.
     */
    public Icon drawRectangle(final Frame frame, final RectWrapper rectangle) {
        return new ImageIcon(this.conversion.convertMatToImg(super.rectangle(frame, rectangle)).get());
    }
    
    /**
     * Updates the current frame.
     *
     * @param frame the frame to update
     * @return the updated image with the proper number of grids.
     * @see FrameUtils#rectangle(cataovo.entities.Frame, cataovo.wrappers.lib.RectWrapper) 
     */
    public Icon updateGrids(final Frame frame) {
        return new ImageIcon(this.conversion.convertMatToImg(super.update(frame)).get());
    }

    /**
     * Captures a subgrid based on a {@link cataovo.entities.Region Region} between two
     * point clicks.
     *
     * @param frame
     * @param begin a point to start calculating the grid.
     * @param end a point to delimitate the grid
     * @return a Image within these clicks.
     */
    public Icon getSubframe(final Frame frame, final Point begin, final Point end) {
        final RectWrapper rectWrapper = new RectWrapper(super.grid(new PointWrapper(begin), new PointWrapper(end)));
        final MatWrapper matWrapper = imageUtils.submat(rectWrapper, new MatWrapper(frame));
        return new ImageIcon(this.conversion.convertMatToImg(matWrapper).get());
    }

    /**
     * 
     * @param frame
     * @param rects
     * @param circles
     * @return 
     */
    public Icon drawPolygons(final Frame frame, final Collection<RectWrapper> rects, final Collection<Collection<PointWrapper>> circles) {
        MatWrapper matWrapper = new MatWrapper(frame);
        matWrapper = super.multipleRects(matWrapper, rects);
        matWrapper = super.multipleCircles(matWrapper, circles);
        return new ImageIcon(this.conversion.convertMatToImg(matWrapper).get());
    }

}
