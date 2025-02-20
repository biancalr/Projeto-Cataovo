/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cataovo.controllers;

import cataovo.entities.Frame;
import cataovo.entities.Point;
import cataovo.exceptions.DirectoryNotValidException;
import cataovo.exceptions.RegionNotValidException;
import cataovo.wrappers.lib.PointWrapper;
import cataovo.wrappers.lib.RectWrapper;
import java.util.Collection;
import javax.swing.Icon;

/**
 * Controls the actions in a {@link cataovo.entities.Frame Frame}.
 *
 * @author Bianca Leopoldo Ramos.
 */
public interface FrameActionsController {

    /**
     * Chooses the format to paint the {@link cataovo.entities.Frame Frame} whether is a dot or a rectangle
     *
     * @param point the point clicked in the frame
     * @param currentFrame
     * @return the image correspondent to the quantity of clicks in the frame.
     * @throws DirectoryNotValidException
     * @throws cataovo.exceptions.RegionNotValidException
     * @throws CloneNotSupportedException
     * @see cataovo.utils.frameUtils.FrameActionsUtils#drawCircle(cataovo.opencvlib.wrappers.PointWrapper) 
     * @see cataovo.utils.frameUtils.FrameActionsUtils#drawRectangle(cataovo.opencvlib.wrappers.RectWrapper) 
     */
    public Icon paintFormats(Point point, Frame currentFrame) throws DirectoryNotValidException, RegionNotValidException, CloneNotSupportedException;

    /**
     * Paint a collection of {@link cataovo.entities.Region Region} and {@link cataovo.entities.Point Point} in a {@link cataovo.entities.Frame Frame}.
     * 
     * @param currentFrame
     * @param regions the regions found in the file for the frame.
     * @param points the points found in the file for the frame.
     * @return the image corresponding to the Regions and Points in the frame.
     * @see cataovo.controllers.FileReaderController#getPointsInFrameFile(java.lang.String, java.lang.String) 
     * @see cataovo.controllers.FileReaderController#getRegionsInFrameFile(java.lang.String, java.lang.String) 
     */
    public Icon paintFormats(Frame currentFrame, Collection<RectWrapper> regions, Collection points);
    
    /**
     * Removes the last demarked {@link cataovo.entities.Region}.
     *
     * @param currentFrame
     * @return the image updated
     * @throws DirectoryNotValidException
     * @see cataovo.utils.frameUtils.FrameActionsUtils#updateGridsOnFrame() 
     */
    public Icon removeLastRegion(Frame currentFrame) throws DirectoryNotValidException;

    /**
     * Captures a subframe in the current selected {@link cataovo.entities.Region} to focus in the
     * application.
     *
     * @param pointClick the point clicked in the frame
     * @param currentFrame
     * @return the captured subframe
     * @throws DirectoryNotValidException
     * @see cataovo.utils.frameUtils.FrameActionsUtils#captureSubframe(cataovo.entities.Point, cataovo.entities.Point) 
     */
    public Icon captureSubframe(Point pointClick, Frame currentFrame) throws DirectoryNotValidException;

}
