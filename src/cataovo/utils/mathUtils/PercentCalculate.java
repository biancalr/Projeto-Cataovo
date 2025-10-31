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
class PercentCalculate {

    String calculate(PercentMethods calcType, Float value1, Float value2) {
        final DecimalFormat decimalFormat = new DecimalFormat(Constants.DECIMAL_FORMAT);
        decimalFormat.setRoundingMode(RoundingMode.DOWN);

        return decimalFormat.format(calcType.calculate(value1, value2));
    }

}
