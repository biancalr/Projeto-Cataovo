/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cataovo.externals.UI.swing.controllers;

import cataovo.entities.Frame;
import cataovo.entities.Point;
import cataovo.exceptions.DirectoryNotValidException;
import cataovo.exceptions.ImageNotValidException;
import java.awt.Component;
import java.io.File;
import javax.swing.Icon;
import javax.swing.JLabel;

/**
 * Controls how the frames are demonstrated on screen and how to move from one
 * to the other
 *
 * @author Bianca Leopoldo Ramos
 */
public interface PageController {

    /**
     * Set to the next frame
     *
     * @param parentName
     * @param parent
     * @throws ImageNotValidException
     * @throws DirectoryNotValidException
     */
    public void onNextFrameInManual(JLabel parentName, JLabel parent) throws ImageNotValidException, DirectoryNotValidException, AssertionError ;

    /**
     * Set to the next frame
     *
     * @param parentName
     * @param jTabbedPane
     * @param paletteSavingFolder
     * @param paletteName
     * @throws ImageNotValidException
     * @throws DirectoryNotValidException
     */
    public void onNextFrameInAutomatic(JLabel parentName, Component jTabbedPane, File paletteSavingFolder, String paletteName) throws ImageNotValidException, DirectoryNotValidException, ArrayIndexOutOfBoundsException;

    /**
     *
     * @param parentName
     * @param parent
     * @param paletteDirectory
     * @param reports
     * @throws DirectoryNotValidException
     * @throws ImageNotValidException
     */
    public void onNextFrameInEvaluation(JLabel parentName, JLabel parent, File paletteDirectory, String[] reports) throws DirectoryNotValidException, ImageNotValidException ;

    /**
     * When a Frame was finished its analysis, go to next Frame on Queue.
     *
     * @param parentName
     * @param parent
     * @param currentFrame
     * @throws cataovo.exceptions.ImageNotValidException
     * @throws cataovo.exceptions.DirectoryNotValidException
     */
    public void onFrameFinishedManual(JLabel parentName, JLabel parent, Frame currentFrame) throws ImageNotValidException, DirectoryNotValidException;

    /**
     *
     * @param tabComponent
     * @param parentNameLabel
     * @param parentLabel
     * @param frame
     * @throws cataovo.exceptions.ImageNotValidException
     * @throws cataovo.exceptions.DirectoryNotValidException
     */
    public void showFramesOnSelectedTabScreen(Component tabComponent, JLabel parentNameLabel, JLabel parentLabel, Object frame) throws ImageNotValidException, DirectoryNotValidException;

    /**
     *
     * @param parentNameLabel
     * @param parentLabel
     * @param frame
     * @return 
     * @throws ImageNotValidException
     * @throws DirectoryNotValidException
     */
    public boolean showFrameOnScreen(JLabel parentNameLabel, JLabel parentLabel, Object frame) throws ImageNotValidException, DirectoryNotValidException;

    /**
     *
     * @param subframeNameLabel
     * @param subframeLabel
     * @param frame
     * @param point
     * @throws cataovo.exceptions.DirectoryNotValidException
     */
    public void showSubFrameOnSelectedTabScreen(JLabel subframeNameLabel, JLabel subframeLabel, Icon frame, Point point) throws DirectoryNotValidException;

    /**
     *
     * @param parentName
     * @param jTabbedPane
     * @param savingFolder
     * @param paletteDirectoryName
     * @throws ImageNotValidException
     * @throws DirectoryNotValidException
     * @throws ArrayIndexOutOfBoundsException
     */
    public void onPreviousFrameInAutomatic(JLabel parentName, Component jTabbedPane, File savingFolder, String paletteDirectoryName) throws ImageNotValidException, DirectoryNotValidException, ArrayIndexOutOfBoundsException ;

    /**
     *
     * @param parentName
     * @param parent
     * @param paletteDirectory
     * @param reports
     * @return
     * @throws ImageNotValidException
     * @throws DirectoryNotValidException
     * @throws ArrayIndexOutOfBoundsException
     */
    public int onPreviousFrameInEvaluation(JLabel parentName, JLabel parent, File paletteDirectory, String[] reports) throws DirectoryNotValidException, ImageNotValidException ;

}
