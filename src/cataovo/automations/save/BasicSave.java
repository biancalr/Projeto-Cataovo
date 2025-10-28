/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cataovo.automations.save;

import cataovo.entities.Palette;
import cataovo.exceptions.AutomationExecutionException;
import cataovo.utils.Constants;
import cataovo.utils.enums.FileExtension;
import cataovo.utils.enums.ProcessingMode;
import cataovo.utils.fileUtils.writers.csv.csvWriter.CsvFileWriter;
import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;
import cataovo.utils.fileUtils.writers.WriteFile;

/**
 *
 * @author Bianca Leopoldo Ramos
 */
public abstract class BasicSave implements Callable<String> {

    /**
     * Csv File WriteFile
     */
    private final WriteFile csvFileWriter;
    /**
     * Logging for CsvFileWriter
     */
    private static final Logger LOG = Logger.getLogger(BasicSave.class.getName());
    /**
     * The palette to be processed.
     */
    private final Palette palette;
    /**
     * The directory where the results will be saved.
     */
    protected final String savingDirectory;
    /**
     * Referes to the relatory's file extension where the text data will be
     * saved.
     */
    protected final FileExtension fileExtension;
    /**
     * Relates the tabName to the type of processing of a palette: Manual or
     * Automatic. Also helps to create folders of each processing type.
     */
    private final String parentTabName;
    /**
     * date and time captured when the process begin to create a relatory folder
     */
    protected final String dateTime;

    /**
     * 
     * @param palette
     * @param savingDirectory
     * @param fileExtension
     * @param parentTabName
     * @param dateTime 
     */
    public BasicSave(Palette palette, String savingDirectory, FileExtension fileExtension, String parentTabName, String dateTime) {
        this.palette = palette;
        this.savingDirectory = savingDirectory;
        this.fileExtension = fileExtension;
        this.parentTabName = parentTabName;
        this.dateTime = dateTime;
        this.csvFileWriter = new CsvFileWriter();
    }
    
    @Override
    public String call() throws Exception {
        return createFile(); 
    }
    
    /**
     * Responsable for creating the contents of each report needed to save the
     * resulted products
     *
     * @return the content of the file;
     * @throws cataovo.exceptions.AutomationExecutionException
     * @see
     * cataovo.automation.threads.dataSaving.NewThreadAutomationAutomaticProcess
     * @see
     * cataovo.automation.threads.dataSaving.NewThreadAutomationManualProcess
     */
    protected abstract String execute() throws AutomationExecutionException;
    
    /**
     * Creates the relatory wich saves the data of each type of processment.
     *
     * @return the filepath's relatory.
     * @see #execute()
     */
    private synchronized String createFile() throws Exception {
        
        StringBuffer sb = new StringBuffer();
        final String processingMode = (parentTabName == null ? Constants.TAB_NAME_MANUAL_PT_BR == null : parentTabName.equals(Constants.TAB_NAME_MANUAL_PT_BR)) ? ProcessingMode.MANUAL.getProcessingMode() : (parentTabName == null ? Constants.TAB_NAME_AUTOMATIC_PT_BR == null : parentTabName.equals(Constants.TAB_NAME_AUTOMATIC_PT_BR)) ? ProcessingMode.AUTOMATIC.getProcessingMode() : ProcessingMode.EVALUATION.getProcessingMode();
        final String dstn = palette.getDirectory().getName() + "/" + processingMode + "/" + dateTime;
        final String fileDirecto = savingDirectory + Constants.APPLICATION_FOLDER + dstn;
        final String createdFile = savingDirectory + Constants.APPLICATION_FOLDER + dstn + "/" + Constants.REPORT_FILE_NAME + "." + fileExtension.getExtension();
       
        sb.append(addModeSpecificities(processingMode, createdFile));
        
        sb.append(execute());
        
        LOG.log(Level.INFO, "the report will be created under the name: {0}", createdFile);
        return this.csvFileWriter.createFile(sb, createdFile, fileDirecto);
        
    }

    private StringBuffer addModeSpecificities(final String processingMode, final String createdFile) throws AutomationExecutionException {
        StringBuffer sb = new StringBuffer();
        switch (processingMode) {
            case Constants.PROCESSING_MODE_NAME_AUTO ->  {
                sb.append(csvFileWriter.verifyAndAppendFileAreadyExistent(createdFile, palette.getPathName()));
                break;
            }
            default ->  { break; }
        }
        return sb;
    }

    public Palette getPalette() {
        return palette;
    }
}
