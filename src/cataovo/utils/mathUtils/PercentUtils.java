/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cataovo.utils.mathUtils;

import cataovo.utils.Constants;
import java.math.RoundingMode;
import java.text.DecimalFormat;

/**
 *
 * @author Bianca Leopoldo Ramos
 */
public class PercentUtils {

    /**
     * 
     * @param method
     * @param strValue1
     * @param strValue2
     * @return 
     */
    public String getPercentageOf(final int method, final String strValue1, final String strValue2) {
        float value1 = Float.parseFloat(strValue1);
        float value2 = Float.parseFloat(strValue2);
        final PercentCalculate calculate = new PercentCalculate();

        switch (method) {
            case Constants.CALCULATE_METHOD_RECALL -> { // TVP = VP / (FN+VP)
                return calculate.calculate(PercentMethods.RECALL, value1, value2);
            }
            case Constants.CALCULATE_METHOD_PRECISION -> {
                return calculate.calculate(PercentMethods.PRECISION, value1, value2);
            }
            case Constants.CALCULATE_METHOD_SPECIFICITY -> {
                return calculate.calculate(PercentMethods.SPECIFICITY, value1, value2);
            }
            default -> {
                return "";
            }
        }
    }

    /**
     * 
     * @param method
     * @param truePositive
     * @param trueNegative
     * @param falsePositive
     * @param falseNegative
     * @return 
     */
    public String getPercentageOf(final int method, final String truePositive, final String trueNegative, final String falsePositive, final String falseNegative) {
        float truePositiveNumber = Integer.parseInt(truePositive);
        float trueNegativeNumber = Integer.parseInt(trueNegative);
        float falsePositiveNumber = Integer.parseInt(falsePositive);
        float falseNegativeNumber = Integer.parseInt(falseNegative);

        final float result = getPercentageOf(method, truePositiveNumber, trueNegativeNumber, falsePositiveNumber, falseNegativeNumber);
        final DecimalFormat decimalFormat = new DecimalFormat(Constants.DECIMAL_FORMAT);
        decimalFormat.setRoundingMode(RoundingMode.DOWN);

        return decimalFormat.format(result);
    }

    private float getPercentageOf(final int method, float truePositiveNumber, float trueNegativeNumber, float falsePositiveNumber, float falseNegativeNumber) throws AssertionError {
        switch (method) {
            case Constants.CALCULATE_METHOD_ACCURACY -> {
                return (truePositiveNumber + trueNegativeNumber) / (truePositiveNumber + trueNegativeNumber + falsePositiveNumber + falseNegativeNumber);
            }
            case Constants.CALCULATE_METHOD_F1_SCORE -> {
                float precision = Float.parseFloat(getPercentageOf(Constants.CALCULATE_METHOD_PRECISION, Float.toString(truePositiveNumber), Float.toString(falsePositiveNumber)).replace(",", "."));
                float recall = Float.parseFloat(getPercentageOf(Constants.CALCULATE_METHOD_RECALL, Float.toString(truePositiveNumber), Float.toString(falseNegativeNumber)).replace(",", "."));
                return 2 * ((precision * recall) / (precision + recall));
            }
            default -> {
                return 0;
            }
        }
    }
}
