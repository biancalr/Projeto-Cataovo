/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cataovo.automations.save;

import cataovo.entities.Palette;
import cataovo.utils.Constants;
import cataovo.utils.enums.FileExtension;
import cataovo.utils.mathUtils.PercentUtils;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Bianca Leopoldo Ramos
 */
public class SaveEvaluationMode extends BasicSave {

    private static final Logger LOG = Logger.getLogger(SaveEvaluationMode.class.getName());
    private final String evaluationResult;
    private final PercentUtils percentCalculation;

    public SaveEvaluationMode(Palette palette, String savingDirectory, FileExtension fileExtension, String parentTabName, String evaluationResult, String dateTime) {
        super(palette, savingDirectory, fileExtension, parentTabName, dateTime);
        this.evaluationResult = evaluationResult;
        this.percentCalculation = new PercentUtils();
    }

    @Override
    protected String execute() {
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
        StringBuilder sb = new StringBuilder(getPalette().getDirectory().getPath());
        sb.append(Constants.QUEBRA_LINHA);
        sb.append("Verdadeiro Positivo: ").append(resultLineSplitted[0]).append(Constants.QUEBRA_LINHA);
        sb.append("Falso Positivo: ").append(resultLineSplitted[2]).append(Constants.QUEBRA_LINHA);
        sb.append("Falso Negativo: ").append(resultLineSplitted[1]).append(Constants.QUEBRA_LINHA);
        sb.append("Verdadeiro Negativo: ").append(resultLineSplitted[3]).append(Constants.QUEBRA_LINHA);
        sb.append(Constants.QUEBRA_LINHA);
        sb.append("Recall: ").append(percentCalculation.getPercentageOf(Constants.CALCULATE_METHOD_TRUE_POSITIVE, resultLineSplitted[0], resultLineSplitted[1])).append(Constants.QUEBRA_LINHA);
        sb.append("Falso Positivo: ").append(percentCalculation.getPercentageOf(Constants.CALCULATE_METHOD_FALSE_POSITIVE, resultLineSplitted[2], resultLineSplitted[3])).append(Constants.QUEBRA_LINHA);
        sb.append("Falso Negativo: ").append(percentCalculation.getPercentageOf(Constants.CALCULATE_METHOD_FALSE_NEGATIVE, resultLineSplitted[1], resultLineSplitted[0])).append(Constants.QUEBRA_LINHA);
        sb.append("Verdadeiro Negativo: ").append(percentCalculation.getPercentageOf(Constants.CALCULATE_METHOD_TRUE_NEGATIVE, resultLineSplitted[3], resultLineSplitted[2])).append(Constants.QUEBRA_LINHA);
        sb.append("Acurácia: ").append(percentCalculation.getPercentageOf(Constants.CALCULATE_METHOD_ACCURACY, resultLineSplitted[0], resultLineSplitted[3], resultLineSplitted[2], resultLineSplitted[1])).append(Constants.QUEBRA_LINHA);
        sb.append("Precisão: ").append(percentCalculation.getPercentageOf(Constants.CALCULATE_METHOD_PRECISION, resultLineSplitted[0], resultLineSplitted[2])).append(Constants.QUEBRA_LINHA);
        LOG.info(sb.toString());
        return sb.toString();
    }
}
