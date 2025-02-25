/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cataovo.externals.UI.swing.controllers;

import cataovo.exceptions.DirectoryNotValidException;
import cataovo.exceptions.ImageNotValidException;
import cataovo.exceptions.ReportNotValidException;
import java.io.File;
import java.io.FileNotFoundException;
import javax.swing.JTabbedPane;

/**
 * Controls the file selection from outside of the aplication to deal with it.
 *
 * @author Bianca Leopoldo Ramos.
 */
public interface FileSelectionController {

    /**
     * Selects an event and an action based on the parameters.
     *
     * @param isCurrentTabProcessing
     * @param actionCommand comand that defines a dialog showing actions.
     * @param parent the component
     * @param isADirectoryOnly <code>True</code> if the selection mode is a
     * <code>DIRECTORY_ONLY</code> or <code>False</code> if the selection mode
     * is a <code>FILES_AND_DIRECTORIES</code>
     * @return the selected file
     * @throws cataovo.exceptions.DirectoryNotValidException
     * @throws cataovo.exceptions.ImageNotValidException
     * @throws java.io.FileNotFoundException
     * @throws cataovo.exceptions.ReportNotValidException
     * @see cataovo.constants.Constants
     */
    public File onFileSelectionEvent(boolean isCurrentTabProcessing, String actionCommand, JTabbedPane parent, boolean isADirectoryOnly) throws DirectoryNotValidException, ImageNotValidException, FileNotFoundException, ReportNotValidException;

}
