/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cataovo.resources.fileChooser.handler;

import cataovo.exceptions.ImageNotValidException;
import cataovo.utils.enums.FileExtension;
import java.io.File;
import java.util.Collection;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Bianca Leopoldo Ramos
 * @param <T> The type of the elements to deal in the List
 */
public final class FileListHandler<T> {

    private static final Logger LOG = Logger.getLogger(FileListHandler.class.getName());

    public FileListHandler() {
    }

    /**
     *
     * @param file array of files. Onlu PNG Files.
     * @param fileExtension the supported FileExtesion
     * @return
     * @throws ImageNotValidException
     */
    public Collection<T> normalizeFiles(File[] file, FileExtension fileExtension) throws ImageNotValidException {
        Collection<T> fileList = new LinkedList<>();
        for (File file1 : file) {
            if (file1.exists() && file1.isFile() && getFileExtension(file1).equalsIgnoreCase(fileExtension.getExtension())) {
                fileList.add((T) file1);
                LOG.log(Level.INFO, "Adding following file: {0}", file1.getName());
            } else if (file1.isDirectory()) {
                normalizeFiles(file1.listFiles(), fileExtension);
            }
        }
        return fileList;
    }

    /**
     *
     * @param f the File
     * @return the file Extension
     */
    public final String getFileExtension(File f) {
        String fileExtension;
        // Verify which is the file extension.
        if (f.getAbsolutePath().substring(f.getAbsolutePath().indexOf(".")) != null) {
            fileExtension = f.getAbsolutePath().substring(f.getAbsolutePath().lastIndexOf(".") + 1);
        } else {
            fileExtension = "NONE";
        }
        return fileExtension;
    }

}
