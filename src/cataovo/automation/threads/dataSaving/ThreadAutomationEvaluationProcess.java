/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cataovo.automation.threads.dataSaving;

import cataovo.constants.Constants;
import cataovo.entities.Palette;
import cataovo.enums.FileExtension;
import cataovo.utils.evaluationUtils.EvaluationCalcType;
import cataovo.utils.evaluationUtils.PercentageCalcUtils;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Bianca Leopoldo Ramos
 */
public class ThreadAutomationEvaluationProcess extends DataSavingThreadAutomation {

    private static final Logger LOG = Logger.getLogger(ThreadAutomationEvaluationProcess.class.getName());
    private final String evaluationResult;

    public ThreadAutomationEvaluationProcess(Palette palette, String savingDirectory, FileExtension fileExtension, String parentTabName, String evaluationResult, String dateTime) {
        super(palette, savingDirectory, fileExtension, parentTabName, dateTime);
        this.evaluationResult = evaluationResult;
    }

    @Override
    protected StringBuffer createContent() {
//        evaluation[0]); // true positive
//        evaluation[2]); // false Positive
//        evaluation[1]); // false negative
//        evaluation[3]); // true negative
        // TVP = VP / (FN+VP)
        // TFP = FP / (VN+FP)
        // TVN = VN / (FP+VN)
        // TFN = FN / (VP+FN)
        // Prec = VP / (VP+FP)
        // Acc = (VP+VN) / (VP+VN+FP+FN)
        String[] resultLineSplitted = this.evaluationResult.split(Constants.QUEBRA_LINHA);
        LOG.log(Level.INFO, "Saving report at {0}", this.savingDirectory);
        StringBuffer sb = new StringBuffer(getPalette().getDirectory().getPath());
        sb.append(Constants.QUEBRA_LINHA);
        sb.append("Verdadeiro Positivo: ").append(resultLineSplitted[0]).append(Constants.QUEBRA_LINHA);
        sb.append("Falso Positivo: ").append(resultLineSplitted[2]).append(Constants.QUEBRA_LINHA);
        sb.append("Falso Negativo: ").append(resultLineSplitted[1]).append(Constants.QUEBRA_LINHA);
        sb.append("Verdadeiro Negativo: ").append(resultLineSplitted[3]).append(Constants.QUEBRA_LINHA);
        sb.append(Constants.QUEBRA_LINHA);
        sb.append("Recall: ").append(getPercentageOf(Constants.CALCULATE_METHOD_TRUE_POSITIVE, resultLineSplitted[0], resultLineSplitted[1])).append(Constants.QUEBRA_LINHA);
        sb.append("Falso Positivo: ").append(getPercentageOf(Constants.CALCULATE_METHOD_FALSE_POSITIVE, resultLineSplitted[2], resultLineSplitted[3])).append(Constants.QUEBRA_LINHA);
        sb.append("Falso Negativo: ").append(getPercentageOf(Constants.CALCULATE_METHOD_FALSE_NEGATIVE, resultLineSplitted[1], resultLineSplitted[0])).append(Constants.QUEBRA_LINHA);
        sb.append("Verdadeiro Negativo: ").append(getPercentageOf(Constants.CALCULATE_METHOD_TRUE_NEGATIVE, resultLineSplitted[3], resultLineSplitted[2])).append(Constants.QUEBRA_LINHA);
        sb.append("Acurácia: ").append(getPercentageOf(Constants.CALCULATE_METHOD_ACCURACY, resultLineSplitted[0], resultLineSplitted[3], resultLineSplitted[2], resultLineSplitted[1])).append(Constants.QUEBRA_LINHA);
        sb.append("Precisão: ").append(getPercentageOf(Constants.CALCULATE_METHOD_PRECISION, resultLineSplitted[0], resultLineSplitted[2])).append(Constants.QUEBRA_LINHA);
        LOG.info(sb.toString());
        return sb;
    }

    /**
     *
     * @param method
     * @param strValue1
     * @param strValue2
     * @return
     */
    public String getPercentageOf(final int method, final String strValue1, final String strValue2) {
        // TVP = VP / (FN+VP)
        // TFP = FP / (VN+FP)
        // TVN = VN / (FP+VN)
        // TFN = FN / (VP+FN)
        float value1 = Integer.parseInt(strValue1);
        float value2 = Integer.parseInt(strValue2);
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

    /**
     *
     * @param method
     * @param strValue1
     * @param strValue2
     * @param strValue3
     * @param strValue4
     * @return
     */
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

}
