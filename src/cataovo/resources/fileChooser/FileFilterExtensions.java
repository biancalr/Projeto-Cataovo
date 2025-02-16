/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cataovo.resources.fileChooser;

import cataovo.utils.enums.FileExtension;
import java.io.File;
import javax.swing.filechooser.FileFilter;

/**
 *
 * @author Bianca Leopoldo Ramos
 */
public class FileFilterExtensions extends FileFilter {

    /**
     * The file extension to work with.
     */
    public FileExtension extension;

    /**
     *
     * @param extension the extension needed
     */
    public FileFilterExtensions(FileExtension extension) {
        this.extension = extension;
    }

    /**
     * Empty Constructor
     */
    public FileFilterExtensions() {

    }

    @Override
    public boolean accept(File f) {
        return f.isDirectory()
                || f.getAbsolutePath()
                        .endsWith("." + extension.getExtension());
    }

    @Override
    public String getDescription() {
        return "*." + extension.toString().toLowerCase();
    }

    /**
     *
     * @param extension
     */
    public void setExtension(FileExtension extension) {
        this.extension = extension;
    }

    /**
     *
     * @return the extension
     */
    public FileExtension getExtension() {
        return extension;
    }

}
