/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cataovo.automation.threads.dataEvaluation;

import cataovo.entities.Point;
import cataovo.entities.Region;
import cataovo.utils.constants.Constants;
import cataovo.exceptions.ReportNotValidException;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Bianca Leopoldo Ramos
 */
public abstract class DataEvaluationThreadAutomation implements Callable<String> {

    /**
     * Logging for DataEvaluationThreadAutomation
     */
    private static final Logger LOG = Logger.getLogger(DataEvaluationThreadAutomation.class.getName());

    private String fileContentManual;
    private String fileContentAuto;

    /**
     *
     * @param fileContentManual
     * @param fileContentAuto
     */
    public DataEvaluationThreadAutomation(String fileContentManual, String fileContentAuto) {
        this.fileContentManual = fileContentManual;
        this.fileContentAuto = fileContentAuto;
    }

    @Override
    public String call() throws NumberFormatException, ReportNotValidException {
        return evaluationAnalysis();
    }

    /**
     *
     * @param regionsInFrame
     * @param pointsInFrame
     * @return
     */
    protected abstract float[] evaluateFrame(String regionsInFrame, String pointsInFrame) throws NumberFormatException;

    /**
     *
     * @return
     */
    private synchronized String evaluationAnalysis() throws NumberFormatException, ReportNotValidException {
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
            float[] frameResult = evaluateFrame(regionsOnFrame[i], eggsOnFrame[i]);
            LOG.log(Level.INFO, "Resultado individual: TP({0}), FN({1}), FP({2}), TN({4})", new Object[]{frameResult[0], frameResult[1], frameResult[2], frameResult[3]});
            evaluationResult[0] += frameResult[0];
            evaluationResult[1] += frameResult[1];
            evaluationResult[2] += frameResult[2];
            evaluationResult[3] += frameResult[3];
            LOG.log(Level.INFO, "Resultado somado: TP({0}), FN({1}), FP({2}), TN({4})", new Object[]{evaluationResult[0], evaluationResult[1], evaluationResult[2], evaluationResult[3]});
        }

        for (int i = 0; i < evaluationResult.length; i++) {
            resultString.append(evaluationResult[i]).append(Constants.QUEBRA_LINHA);
        }

        return resultString.toString();
    }

    public String getFileContentManual() {
        return fileContentManual;
    }

    public void setFileContentManual(String fileContentManual) {
        this.fileContentManual = fileContentManual;
    }

    public String getFileContentAuto() {
        return fileContentAuto;
    }

    public void setFileContentAuto(String fileContentAuto) {
        this.fileContentAuto = fileContentAuto;
    }

}
