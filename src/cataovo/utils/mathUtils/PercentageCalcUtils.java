/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cataovo.utils.mathUtils;

import cataovo.utils.constants.Constants;
import java.math.RoundingMode;
import java.text.DecimalFormat;

/**
 *
 * @author Bianca Leopoldo Ramos
 */
public class PercentageCalcUtils {
    
    public String calculate(EvaluationCalcType calcType, Float value1, Float value2, Float value3, Float value4){
        final DecimalFormat decimalFormat = new DecimalFormat(Constants.DECIMAL_FORMAT);
        decimalFormat.setRoundingMode(RoundingMode.DOWN);
        
        return decimalFormat.format(calcType.calculate(value1, value2, value3, value4));
    }
    
}
