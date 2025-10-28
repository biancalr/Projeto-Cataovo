/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package cataovo.utils.fileUtils.writers;

import cataovo.exceptions.AutomationExecutionException;

/**
 *
 * @author Bianca Leopoldo Ramos
 */
public interface WriteFile {
    
    /**
     * 
     * @param content
     * @param fileLocation
     * @param folderLocation
     * @return
     * @throws Exception 
     */
    public String createFile(final StringBuffer content, final String fileLocation, final String folderLocation) throws Exception;
    
    /**
     * 
     * @param createdFile
     * @param palettePathName
     * @return
     * @throws AutomationExecutionException 
     */
    public StringBuffer verifyAndAppendFileAreadyExistent(final String createdFile, final String palettePathName) throws AutomationExecutionException;
    
}
