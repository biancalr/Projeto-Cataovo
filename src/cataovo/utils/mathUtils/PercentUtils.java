/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cataovo.utils.mathUtils;

import cataovo.utils.Constants;

/**
 *
 * @author bianc
 */
public class PercentUtils {
    
    public String getPercentageOf(final int method, final String strValue1, final String strValue2) {
        // TVP = VP / (FN+VP)
        // TFP = FP / (VN+FP)
        // TVN = VN / (FP+VN)
        // TFN = FN / (VP+FN)
        float value1 = Float.parseFloat(strValue1);
        float value2 = Float.parseFloat(strValue2);
        final PercentCalculate calcUtils = new PercentCalculate();

        switch (method) {
            case Constants.CALCULATE_METHOD_TRUE_POSITIVE -> { // TVP = VP / (FN+VP)
                return calcUtils.calculate(PercentMethods.RECALL, value1, value2, null, null);
            }
            case Constants.CALCULATE_METHOD_FALSE_POSITIVE -> { // TFP = FP / (VN+FP)
                return calcUtils.calculate(PercentMethods.BASIC_FORMULA, value1, value2, null, null);
            }
            case Constants.CALCULATE_METHOD_TRUE_NEGATIVE -> { // TVN = VN / (FP+VN)
                return calcUtils.calculate(PercentMethods.BASIC_FORMULA, value1, value2, null, null);
            }
            case Constants.CALCULATE_METHOD_FALSE_NEGATIVE -> { // TFN = FN / (VP+FN)
                return calcUtils.calculate(PercentMethods.BASIC_FORMULA, value1, value2, null, null);
            }
            case Constants.CALCULATE_METHOD_PRECISION -> {
                return calcUtils.calculate(PercentMethods.PRECISION, value1, value2, null, null);
            }
            default -> {
                throw new AssertionError();
            }
        }
    }
    
    public String getPercentageOf(final int method, final String strValue1, final String strValue2, final String strValue3, final String strValue4) {
        float value1 = Integer.parseInt(strValue1);
        float value2 = Integer.parseInt(strValue2);
        float value3 = Integer.parseInt(strValue3);
        float value4 = Integer.parseInt(strValue4);
        final PercentCalculate calcUtils = new PercentCalculate();

        switch (method) {
            case Constants.CALCULATE_METHOD_ACCURACY -> {
                return calcUtils.calculate(PercentMethods.ACCURACY, value1, value2, value3, value4);
            }
            default -> {
                throw new AssertionError();
            }
        }

    }
}
