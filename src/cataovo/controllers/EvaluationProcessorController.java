/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package cataovo.controllers;

import cataovo.entities.Palette;
import cataovo.utils.enums.FileExtension;
import cataovo.exceptions.DirectoryNotValidException;
import cataovo.exceptions.ImageNotValidException;

/**
 *
 * @author Bianca Leopoldo Ramos
 */
public interface EvaluationProcessorController {

    /**
     *
     * @param fileContentManual
     * @param fileContentAuto
     * @return
     */
    public String onEvaluateFileContentsOnPalette(String fileContentManual, String fileContentAuto);
    
    /**
     * 
     * @param method
     * @param value1
     * @param value2
     * @return 
     */
    public String getPercentageOf(int method, String value1, String value2);
    
    /**
     * 
     * @param method
     * @param value1
     * @param value2
     * @param value3
     * @param value4
     * @return 
     */
    public String getPercentageOf(int method, String value1, String value2, String value3, String value4);

    /**
     *
     * @param palette
     * @param savingDirectory
     * @param fileExtension
     * @param parentTabName
     * @return
     */
    public String onActionComandSaveEvaluationRelatory(Palette palette, String savingDirectory, FileExtension fileExtension, String parentTabName);

    /**
     * 
     * @param paletteDirectoryOnManual
     * @param paletteDirectoryOnAuto
     * @return
     * @throws DirectoryNotValidException 
     * @throws cataovo.exceptions.ImageNotValidException 
     */
    public Palette getPalettDirByReport(String paletteDirectoryOnManual, String paletteDirectoryOnAuto) throws DirectoryNotValidException, ImageNotValidException;

}
