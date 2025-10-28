/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cataovo.events.implement;

import cataovo.automations.evaluate.BasicEvaluate;
import cataovo.automations.evaluate.EvaluateData;
import cataovo.automations.evaluate.EvaluateDataPixels;
import cataovo.automations.save.BasicSave;
import cataovo.automations.save.SaveEvaluationMode;
import cataovo.domain.Event;
import cataovo.entities.Palette;
import cataovo.resources.MainContext;
import cataovo.utils.Constants;
import cataovo.utils.enums.FileExtension;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import cataovo.events.EvaluationEvent;

/**
 *
 * @author Bianca Leopoldo Ramos
 */
public class EvaluationEventImpl implements EvaluationEvent {

    /**
     * Logging for EvaluationEventImpl
     */
    private static final Logger LOG = Logger.getLogger(EvaluationEventImpl.class.getName());
    private final MainContext mainContext;
    private Event event;

    public EvaluationEventImpl(final MainContext mainContext) {
        this.mainContext = mainContext;
    }

    @Override
    public Event execute(String fileContentManual, String fileContentAuto, boolean byPixel, final Palette palette, final String savingDirectory, final FileExtension fileExtension, final String tabName) {
        try {
            BasicEvaluate automation;
            Future<String> task;
            ExecutorService executorService = Executors.newSingleThreadExecutor();

            if (!byPixel) {
                automation = new EvaluateData(fileContentManual, fileContentAuto, mainContext);
            } else {
                automation = new EvaluateDataPixels(fileContentManual, fileContentAuto, mainContext);
            }

            task = executorService.submit(automation);
            String evaluationResult = task.get();
            executorService.awaitTermination(1, TimeUnit.MILLISECONDS);
            LOG.log(Level.INFO, "Calculating results in evaluation {0}", Arrays.toString(evaluationResult.split(Constants.QUEBRA_LINHA)));
            event = new Event(saveReport(palette, savingDirectory, fileExtension, tabName, evaluationResult), evaluationResult);
            return event;
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

    private String saveReport(final Palette palette, final String savingDirectory, final FileExtension fileExtension, final String tabName, String evaluationResult) {
        final String dateTime = getDateTime("dd-MM-yyyy_HH-mm-ss");
        try {
            String result;
            BasicSave automation;
            Future<String> task;
            ExecutorService executorService = Executors.newSingleThreadExecutor();

            automation = new SaveEvaluationMode(
                    palette,
                    savingDirectory,
                    fileExtension,
                    tabName,
                    evaluationResult,
                    dateTime);

            task = executorService.submit(automation);
            result = task.get();
            executorService.awaitTermination(1, TimeUnit.MILLISECONDS);
            LOG.log(Level.INFO, "Saving evaluation data in: {0}", result);
            return result;
        } catch (InterruptedException | ExecutionException ex) {
            LOG.log(Level.SEVERE, ex.getMessage(), ex);
            return "";
        }
    }
}
