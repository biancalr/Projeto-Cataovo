/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cataovo.controllers.implement;

import cataovo.controllers.FrameActionsController;
import cataovo.entities.Frame;
import cataovo.entities.Point;
import cataovo.entities.Region;
import cataovo.exceptions.DirectoryNotValidException;
import cataovo.exceptions.RegionNotValidException;
import cataovo.externals.UI.swing.utils.FrameActionsUtils;
import cataovo.resources.MainContext;
import cataovo.wrappers.lib.PointWrapper;
import cataovo.wrappers.lib.RectWrapper;
import java.util.Collection;
import javax.swing.Icon;

/**
 * Implements the frame actions controller.
 *
 * @author Bianca Leopoldo Ramos.
 */
public class FrameActionsControllerImplements implements FrameActionsController {

    /**
     * Controls the quantity of clicks to base the actions upon.
     */
    private int clickCount;
    /**
     * The initial point to start the actions.
     */
    private Point initialPoint = null;
    
    private final FrameActionsUtils frameUtils;
    
    public FrameActionsControllerImplements() {
        clickCount = 0;
        frameUtils = new FrameActionsUtils();
    }

    /**
     * Chooses the format to paint the frame whether is a dot or a rectangle
     *
     * @param point the point clicked in the frame
     * @return the image correspondent to the quantoty of clicks in the frame.
     * @throws DirectoryNotValidException
     * @throws RegionNotValidException
     * @throws CloneNotSupportedException
     */
    @Override
    public Icon paintFormats(Point point, Frame currentFrame) throws DirectoryNotValidException, RegionNotValidException, CloneNotSupportedException {
        switch (clickCount) {
            case 0 -> {
                clickCount++;
                this.initialPoint = new Point(point.getX(), point.getY());
                return paintDotOnFrame(this.initialPoint, currentFrame);
            }
            case 1 -> {
                clickCount = 0;
                return paintGridOnFrame(new Region(this.initialPoint, new Point(point.getX(), point.getY())), currentFrame);
            }
            default -> {
                return null;
            }
        }
    }

    /**
     * Paints a dot on a frame
     *
     * @param point
     * @return
     * @throws DirectoryNotValidException
     * @throws CloneNotSupportedException
     */
    private Icon paintDotOnFrame(Point point, Frame currentFrame) throws DirectoryNotValidException, CloneNotSupportedException {
        return this.frameUtils.drawCircle(currentFrame, new PointWrapper(point));
    }

    /**
     * Paints a grid on a frame.
     *
     * @param region
     * @return
     * @throws DirectoryNotValidException
     * @throws CloneNotSupportedException
     */
    private Icon paintGridOnFrame(Region region, Frame currentFrame) throws DirectoryNotValidException, CloneNotSupportedException {
        final Icon icon = this.frameUtils.drawRectangle(currentFrame.clone(), new RectWrapper(region));
        MainContext.getInstance().getCurrentFrame().getRegionsContainingEggs().addAll(currentFrame.getRegionsContainingEggs());
        return icon;
    }

    /**
     * Removes the last demarked region.
     *
     * @param currentFrame
     * @return the image updated
     * @throws DirectoryNotValidException
     */
    @Override
    public Icon removeLastRegion(Frame currentFrame) throws DirectoryNotValidException {
        this.initialPoint = null;
        if (currentFrame.getRegionsContainingEggs().isEmpty()) {
            return this.frameUtils.updateGrids(currentFrame);
        } else {
            MainContext.getInstance().getCurrentFrame().getRegionsContainingEggs().remove(
                    (Region) currentFrame.getRegionsContainingEggs().toArray()[currentFrame.getRegionsContainingEggs().size() - 1]
            );
            return this.frameUtils.updateGrids(currentFrame);
        }
    }

    /**
     * Captures a subframe in the current selected region to focus in the
     * application.
     *
     * @param pointClick the point clicked in the frame
     * @param currentFrame
     * @return the captured subframe
     * @throws DirectoryNotValidException
     */
    @Override
    public Icon captureSubframe(Point pointClick, Frame currentFrame) throws DirectoryNotValidException {
        Icon subframeImage = null;
        if (clickCount == 0 && initialPoint != null) {
            subframeImage = this.frameUtils.getSubframe(currentFrame, this.initialPoint, pointClick);
        }
        return subframeImage;
    }

    /**
     * 
     * @param currentFrame
     * @param rects
     * @param circles
     * @return 
     */
    @Override
    public Icon paintFormats(Frame currentFrame, Collection<RectWrapper> rects, Collection circles) {  
        return this.frameUtils.drawPolygons(currentFrame, rects, circles);
    }

}
