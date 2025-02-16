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
import cataovo.externals.libs.opencvlib.converters.Converter;
import cataovo.externals.libs.opencvlib.wrappers.PointWrapper;
import cataovo.externals.libs.opencvlib.wrappers.RectWrapper;
import cataovo.resources.MainResources;
import cataovo.utils.frameUtils.FrameActionsUtils;
import java.util.Collection;
import javax.swing.Icon;
import javax.swing.ImageIcon;

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
    /**
     * Opencv utilitaries used to make actions in the frame.
     */
    private FrameActionsUtils frameUtils;

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
        this.frameUtils = new FrameActionsUtils(currentFrame.clone());
        PointWrapper pw = new PointWrapper(point);
        return frameUtils.drawCircle(pw);
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
        this.frameUtils = new FrameActionsUtils(currentFrame.clone());
        RectWrapper rw = new RectWrapper(region);
        Icon icon = frameUtils.drawRectangle(rw);
        MainResources.getInstance().getCurrentFrame().getRegionsContainingEggs().addAll(frameUtils.getFrame().getRegionsContainingEggs());
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
            return new ImageIcon(Converter.getInstance().convertMatToImg(frameUtils.updateGridsOnFrame()).get());
        } else {
            MainResources.getInstance().getCurrentFrame().getRegionsContainingEggs().remove(
                    (Region) currentFrame.getRegionsContainingEggs().toArray()[currentFrame.getRegionsContainingEggs().size() - 1]
            );
            return new ImageIcon(Converter.getInstance().convertMatToImg(frameUtils.updateGridsOnFrame()).get());
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
            this.frameUtils = new FrameActionsUtils(currentFrame);
            subframeImage = this.frameUtils.captureSubframe(this.initialPoint, pointClick);
        }
        return subframeImage;
    }

    /**
     * 
     * @param currentFrame
     * @param regions
     * @param points
     * @return 
     */
    @Override
    public Icon paintFormats(Frame currentFrame, Collection<RectWrapper> regions, Collection points) {
        this.frameUtils = new FrameActionsUtils(currentFrame);        
        return this.frameUtils.drawFormatsOnFrame(regions, points);
    }

}
