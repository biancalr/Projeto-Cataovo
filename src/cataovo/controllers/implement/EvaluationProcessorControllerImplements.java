/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cataovo.controllers.implement;

import cataovo.automation.threads.dataEvaluation.DataEvaluationThreadAutomation;
import cataovo.automation.threads.dataEvaluation.ThreadAutomationEvaluation;
import cataovo.automation.threads.dataSaving.DataSavingThreadAutomation;
import cataovo.automation.threads.dataSaving.ThreadAutomationEvaluationProcess;
import cataovo.constants.Constants;
import cataovo.controllers.EvaluationProcessorController;
import cataovo.entities.Frame;
import cataovo.entities.Palette;
import cataovo.enums.FileExtension;
import cataovo.exceptions.DirectoryNotValidException;
import cataovo.exceptions.ImageNotValidException;
import cataovo.resources.MainResources;
import cataovo.utils.evaluationUtils.EvaluationCalcType;
import cataovo.utils.evaluationUtils.PercentageCalcUtils;
import java.io.File;
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

/**
 *
 * @author Bianca Leopoldo Ramos
 */
public class EvaluationProcessorControllerImplements implements EvaluationProcessorController {

    /**
     * Logging for EvaluationProcessorControllerImplements
     */
    private static final Logger LOG = Logger.getLogger(EvaluationProcessorControllerImplements.class.getName());
    private String evaluationResult;

    public EvaluationProcessorControllerImplements() {

    }

    @Override
    public String onEvaluateFileContentsOnPalette(String fileContentManual, String fileContentAuto) {
        try {
            DataEvaluationThreadAutomation automation;
            Future<String> task;
            ExecutorService executorService = Executors.newSingleThreadExecutor();

            automation = new ThreadAutomationEvaluation(fileContentManual, fileContentAuto);

            task = executorService.submit(automation);
            this.evaluationResult = task.get();
            executorService.awaitTermination(1, TimeUnit.MILLISECONDS);
            executorService.shutdown();
            LOG.log(Level.INFO, "Calculating results in evaluation {0}", Arrays.toString(this.evaluationResult.split(Constants.QUEBRA_LINHA)));
            return this.evaluationResult;
        } catch (InterruptedException | ExecutionException ex) {
            LOG.log(Level.SEVERE, ex.getMessage(), ex);
            return "";
        }
    }

    @Override
    public String getPercentageOf(final int method, final String strValue1, final String strValue2) {
        // TVP = VP / (FN+VP)
        // TFP = FP / (VN+FP)
        // TVN = VN / (FP+VN)
        // TFN = FN / (VP+FN)
        float value1 = Float.parseFloat(strValue1);
        float value2 = Float.parseFloat(strValue2);
        final PercentageCalcUtils calcUtils = new PercentageCalcUtils();

        switch (method) {
            case Constants.CALCULATE_METHOD_TRUE_POSITIVE -> { // TVP = VP / (FN+VP)
                return calcUtils.calculate(EvaluationCalcType.RECALL, value1, value2, null, null);
            }
            case Constants.CALCULATE_METHOD_FALSE_POSITIVE -> { // TFP = FP / (VN+FP)
                return calcUtils.calculate(EvaluationCalcType.BASIC_FORMULA, value1, value2, null, null);
            }
            case Constants.CALCULATE_METHOD_TRUE_NEGATIVE -> { // TVN = VN / (FP+VN)
                return calcUtils.calculate(EvaluationCalcType.BASIC_FORMULA, value1, value2, null, null);
            }
            case Constants.CALCULATE_METHOD_FALSE_NEGATIVE -> { // TFN = FN / (VP+FN)
                return calcUtils.calculate(EvaluationCalcType.BASIC_FORMULA, value1, value2, null, null);
            }
            case Constants.CALCULATE_METHOD_PRECISION -> {
                return calcUtils.calculate(EvaluationCalcType.PRECISION, value1, value2, null, null);
            }
            default -> {
                throw new AssertionError();
            }
        }
    }

    @Override
    public String getPercentageOf(final int method, final String strValue1, final String strValue2, final String strValue3, final String strValue4) {
        float value1 = Integer.parseInt(strValue1);
        float value2 = Integer.parseInt(strValue2);
        float value3 = Integer.parseInt(strValue3);
        float value4 = Integer.parseInt(strValue4);
        final PercentageCalcUtils calcUtils = new PercentageCalcUtils();

        switch (method) {
            case Constants.CALCULATE_METHOD_ACCURACY -> {
                return calcUtils.calculate(EvaluationCalcType.ACCURACY, value1, value2, value3, value4);
            }
            default -> {
                throw new AssertionError();
            }
        }

    }

    @Override
    public String onActionComandSaveEvaluationRelatory(final Palette palette, final String savingDirectory, final FileExtension fileExtension, final String tabName) {
        if (this.evaluationResult != null) {
            final String dateTime = getDateTime("dd-MM-yyyy_HH-mm-ss");
            try {
                String result;
                DataSavingThreadAutomation automation;
                Future<String> task;
                ExecutorService executorService = Executors.newSingleThreadExecutor();

                automation = new ThreadAutomationEvaluationProcess(
                        palette,
                        savingDirectory,
                        fileExtension,
                        tabName,
                        this.evaluationResult,
                        dateTime);

                task = executorService.submit(automation);
                result = task.get();
                executorService.awaitTermination(1, TimeUnit.MILLISECONDS);
                executorService.shutdown();
                LOG.log(Level.INFO, "Saving evaluation data in: {0}", result);
                return result;
            } catch (InterruptedException | ExecutionException ex) {
                LOG.log(Level.SEVERE, ex.getMessage(), ex);
                return null;
            }
        } else {
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

    /**
     *
     * @param paletteDirectoryOnManual
     * @param paletteDirectoryOnAuto
     * @return
     * @throws DirectoryNotValidException
     */
    @Override
    public Palette getPalettDirByReport(String paletteDirectoryOnManual, String paletteDirectoryOnAuto) throws DirectoryNotValidException, ImageNotValidException{
        if (new File(paletteDirectoryOnManual).exists() && new File(paletteDirectoryOnAuto).exists()) {
             String reportName = paletteDirectoryOnManual.substring(paletteDirectoryOnManual.lastIndexOf("\\"), paletteDirectoryOnManual.length());
             if (paletteDirectoryOnAuto.contains(reportName)) {
                 Palette palette = new Palette(paletteDirectoryOnAuto);
                 File frame = palette.getDirectory().listFiles()[0];
                 MainResources.getInstance().setCurrentFrame(new Frame(frame.getAbsolutePath()));
                 return palette;
             } else {
                 throw new DirectoryNotValidException("The reports are not from the same Palette");
             }
         } else {
             throw new DirectoryNotValidException("One or more reports do not exist");
         }
    }

}
