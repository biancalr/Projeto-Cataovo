/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cataovo.controllers.implement;

import cataovo.automation.threads.dataSaving.DataSavingThreadAutomation;
import cataovo.automation.threads.dataSaving.ThreadAutomationManualProcess;
import cataovo.controllers.ManualProcessorController;
import cataovo.utils.enums.FileExtension;
import cataovo.exceptions.DirectoryNotValidException;
import cataovo.resources.MainContext;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Implements the manual processment controller.
 *
 * @author Bianca Leopoldo Ramos
 */
public class ManualProcessorControllerImplements implements ManualProcessorController{
    
    private static final Logger LOG = Logger.getLogger(ManualProcessorControllerImplements.class.getName());

    @Override
    public String onNewManualProcessPalette(final String parent, final String savingDarectory) {
        LOG.log(Level.INFO, "Final file save: start");
        String manualRelatoryDestination;
        try {
            DataSavingThreadAutomation newCreateRelatories;
            Future<String> task;
            ExecutorService executorService = Executors.newSingleThreadExecutor();
            final String dateTime = getDateTime("dd-MM-yyyy_HH-mm-ss");
            newCreateRelatories = new ThreadAutomationManualProcess(
                    MainContext.getInstance().getPaletteToSave(),
                    savingDarectory,
                    FileExtension.CSV,
                    parent, 
                    dateTime);
            task = executorService.submit(newCreateRelatories);
            manualRelatoryDestination = task.get();
            executorService.awaitTermination(1, TimeUnit.MILLISECONDS);
            return manualRelatoryDestination;
        } catch (DirectoryNotValidException | InterruptedException | ExecutionException ex) {
            LOG.log(Level.SEVERE, ex.getMessage(), ex);
            return "";
        }
    }
    
    /**
     * Calculates the date and the time.
     *
     * @param datePattern the pattern to return the date
     * @return date and time according to the the datePattern
     */
    private String getDateTime(final String datePattern) {
        DateFormat dateFormat = new SimpleDateFormat(datePattern);
        Date date = new Date();
        return dateFormat.format(date);
    }
    
}
