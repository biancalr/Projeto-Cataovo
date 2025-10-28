/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cataovo.events.implement;

import cataovo.automations.save.BasicSave;
import cataovo.automations.save.SaveManualMode;
import cataovo.domain.Event;
import cataovo.resources.MainContext;
import cataovo.utils.enums.FileExtension;
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
import cataovo.events.ManualEvent;

/**
 * Implements the manual processment controller.
 *
 * @author Bianca Leopoldo Ramos
 */
public class ManualEventImpl implements ManualEvent {

    private static final Logger LOG = Logger.getLogger(ManualEventImpl.class.getName());
    private final MainContext mainContext;

    public ManualEventImpl(MainContext mainContext) {
        this.mainContext = mainContext;
    }

    @Override
    public Event execute(final String parent, final String savingDarectory) {
        LOG.log(Level.INFO, "Final file save: start");
        String manualReportDestination;
        try {
            BasicSave newCreateRelatories;
            Future<String> task;
            ExecutorService executorService = Executors.newSingleThreadExecutor();
            final String dateTime = getDateTime("dd-MM-yyyy_HH-mm-ss");
            newCreateRelatories = new SaveManualMode(
                    mainContext.getPaletteToSave(),
                    savingDarectory,
                    FileExtension.CSV,
                    parent,
                    dateTime);
            task = executorService.submit(newCreateRelatories);
            manualReportDestination = task.get();
            executorService.awaitTermination(1, TimeUnit.MILLISECONDS);
            return new Event(manualReportDestination);
        } catch (InterruptedException | ExecutionException ex) {
            LOG.log(Level.SEVERE, ex.getMessage(), ex);
            return null;
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
