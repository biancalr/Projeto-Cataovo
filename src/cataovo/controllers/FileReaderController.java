/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package cataovo.controllers;

import cataovo.exceptions.DirectoryNotValidException;
import cataovo.externals.libs.opencv.wrappers.PointWrapper;
import cataovo.externals.libs.opencv.wrappers.RectWrapper;
import java.io.FileNotFoundException;
import java.util.List;

/**
 *
 * @author Bianca Leopoldo Ramos
 */
public interface FileReaderController {

    /**
     *
     * @param frameName
     * @param report
     * @return
     * @throws java.io.FileNotFoundException
     * @throws NumberFormatException
     */
    public List<RectWrapper> getRegionsInFrameFile(String frameName, String report) throws FileNotFoundException, NumberFormatException;

    /**
     *
     * @param frameName
     * @param report
     * @return
     * @throws java.io.FileNotFoundException
     * @throws NumberFormatException
     */
    public List<List<PointWrapper>> getPointsInFrameFile(String frameName, String report) throws FileNotFoundException, NumberFormatException;

    /**
     *
     * @param report
     * @return full report content
     * @throws java.io.FileNotFoundException
     */
    public StringBuilder readFullFileContent(String report) throws FileNotFoundException;

    /**
     * 
     * @param report
     * @return the palette which the report belong to.
     * @throws cataovo.exceptions.DirectoryNotValidException 
     */
    public String readPaletteDirectoryFromReport(String report) throws DirectoryNotValidException;

}
