/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cataovo.automations.evaluate;

import cataovo.exceptions.DirectoryNotValidException;
import cataovo.exceptions.ReportNotValidException;
import cataovo.resources.MainContext;
import cataovo.utils.Constants;
import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Bianca Leopoldo Ramos
 */
public abstract class BasicEvaluate implements Callable<String> {

    /**
     * Logging for BasicEvaluate
     */
    private static final Logger LOG = Logger.getLogger(BasicEvaluate.class.getName());

    private final String fileContentManual;
    private final String fileContentAuto;
    private final MainContext mainContext;

    /**
     *
     * @param fileContentManual
     * @param fileContentAuto
     * @param mainContext
     */
    public BasicEvaluate(String fileContentManual, String fileContentAuto, MainContext mainContext) {
        this.fileContentManual = fileContentManual;
        this.fileContentAuto = fileContentAuto;
        this.mainContext = mainContext;
    }

    @Override
    public String call() throws ReportNotValidException, DirectoryNotValidException {
        return evaluationAnalysis();
    }

    /**
     *
     * @param regionsInFrame
     * @param pointsInFrame
     * @return
     * @throws cataovo.exceptions.DirectoryNotValidException
     */
    protected abstract float[] execute(String regionsInFrame, String pointsInFrame) throws DirectoryNotValidException;

    /**
     *
     * @return
     */
    private synchronized String evaluationAnalysis() throws NumberFormatException, ReportNotValidException, DirectoryNotValidException {
        LOG.log(Level.INFO, "Starting Evaluation...");

        //split both strings
        final String[] regionsOnFrame = this.fileContentManual.split(Constants.QUEBRA_LINHA);
        final String[] eggsOnFrame = this.fileContentAuto.split(Constants.QUEBRA_LINHA);
        int[] evaluationResult;
        StringBuilder resultString = new StringBuilder();
        evaluationResult = new int[4];
        for (int i = 0; i < evaluationResult.length; i++) {
            evaluationResult[i] = 0;
        }

        if (regionsOnFrame.length != eggsOnFrame.length) {
            throw new ReportNotValidException(Constants.REPORTS_FRAME_QUANTITY_NOT_EQUAL_PT_BR);
        }

        for (int i = 0; i < regionsOnFrame.length; i++) {
            float[] frameResult = execute(regionsOnFrame[i], eggsOnFrame[i]);
            LOG.log(Level.INFO, "Resultado individual: TP({0}), FN({1}), FP({2}), TN({3})", new Object[]{frameResult[0], frameResult[1], frameResult[2], frameResult[3]});
            evaluationResult[0] += frameResult[0];
            evaluationResult[1] += frameResult[1];
            evaluationResult[2] += frameResult[2];
            evaluationResult[3] += frameResult[3];
            LOG.log(Level.INFO, "Resultado somado: TP({0}), FN({1}), FP({2}), TN({3})", new Object[]{evaluationResult[0], evaluationResult[1], evaluationResult[2], evaluationResult[3]});
        }

        for (int i = 0; i < evaluationResult.length; i++) {
            resultString.append(evaluationResult[i]).append(Constants.QUEBRA_LINHA);
        }

        return resultString.toString();
    }

    protected MainContext getMainContext() {
        return mainContext;
    }
}
