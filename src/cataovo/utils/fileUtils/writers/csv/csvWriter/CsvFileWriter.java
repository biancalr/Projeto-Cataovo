/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cataovo.utils.fileUtils.writers.csv.csvWriter;

import cataovo.exceptions.AutomationExecutionException;
import cataovo.exceptions.FileCsvWriterException;
import cataovo.utils.Constants;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import cataovo.utils.fileUtils.writers.WriteFile;

/**
 *
 * @author Bianca Leopoldo Ramos
 */
public class CsvFileWriter implements WriteFile{

    /**
     * Logging for CsvFileWriter
     */
    private static final Logger LOG = Logger.getLogger(CsvFileWriter.class.getName());

    /**
     * Creates the relatory wich saves the data of each type of processment.
     *
     * @param content
     * @param fileLocation
     * @param folderLocation
     * @return the filepath's relatory.
     * @throws cataovo.exceptions.FileCsvWriterException
     * @see #createContent()
     */
    @Override
    public String createFile(final StringBuffer content, final String fileLocation, final String folderLocation) throws FileCsvWriterException {
        StringBuffer sb = new StringBuffer();
        File directory = new File(folderLocation);

        if (!fileLocation.contains(folderLocation)) {
            throw new FileCsvWriterException(fileLocation + " and " + folderLocation + "doesn't match.");
        }
        
        if (!directory.exists()) {
            directory.mkdirs();
        }

        try (FileWriter csvWriter = new FileWriter(fileLocation); PrintWriter csvPrinter = new PrintWriter(csvWriter);) {

            sb.append(content);
            csvPrinter.print(sb);
            LOG.log(Level.INFO, "The file will be saved under the name: {0}", fileLocation);
            return directory.getPath();
        } catch (Exception e) {
            LOG.log(Level.SEVERE, e.getMessage());
            throw new FileCsvWriterException(e.getMessage());
        }

    }

    /**
     * 
     * @param createdFile
     * @param palettePathName
     * @return
     * @throws AutomationExecutionException 
     */
    @Override
    public StringBuffer verifyAndAppendFileAreadyExistent(final String createdFile, final String palettePathName) throws AutomationExecutionException {
        StringBuffer sb = new StringBuffer();
        File f = new File(createdFile);
        LOG.info("Verifying if the file already exists.");
        sb.append(palettePathName);
        if (f.exists() && f.isFile()) {
            LOG.info("Reading the existing file to append its previous");
            sb.append(readFile(f, palettePathName));
        }
        return sb;
    }

    private StringBuffer readFile(final File createdFile, final String palettePathName) throws AutomationExecutionException {
        LOG.info("Starting to read the file");
        try (InputStreamReader in = new InputStreamReader(new FileInputStream(createdFile)); BufferedReader csvReader = new BufferedReader(in);) {
            return readContent(csvReader, palettePathName);
        } catch (Exception e) {
            LOG.log(Level.SEVERE, e.getMessage());
            JOptionPane.showMessageDialog(null, e.getMessage());
            throw new AutomationExecutionException("Error while reading an existing file.");
        }
    }

    private StringBuffer readContent(final BufferedReader csvReader, final String palettePathName) {
        StringBuffer sb = new StringBuffer();
        csvReader.lines().filter(l -> !l.contains(palettePathName) && l.length() > 4)
                .forEachOrdered(l -> sb.append(Constants.QUEBRA_LINHA).append(l));
        return sb;
    }

}
