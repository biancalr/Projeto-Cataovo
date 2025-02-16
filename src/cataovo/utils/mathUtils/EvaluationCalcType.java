/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package cataovo.utils.mathUtils;

/**
 *
 * @author Bianca Leopoldo Ramos
 */
public enum EvaluationCalcType {

    BASIC_FORMULA() {
        @Override
        public float calculate(Float value1, Float value2, Float value3, Float value4) {
            // TVP = VP / (FN+VP)
            // TFP = FP / (VN+FP)
            // TVN = VN / (FP+VN)
            // TFN = FN / (VP+FN)
            return value1 / ((value1 + value2) == 0.0f ? 1.0f : (value1 + value2));
        }
    },
    RECALL() {
        @Override
        public float calculate(Float truePositive, Float falseNegative, Float value3, Float value4) {
            // TVP = VP / (FN+VP)
            return truePositive / (falseNegative + truePositive);
        }
    },
    ACCURACY() {
        @Override
        public float calculate(Float truePositive, Float trueNegative, Float falsePositive, Float falseNegative) {
            // Acc = (VP+VN) / (VP+VN+FP+FN)
            return (truePositive + trueNegative) / (truePositive + trueNegative + falsePositive + falseNegative);
        }
    },
    PRECISION() {
        @Override
        public float calculate(Float truePositive, Float falsePositive, Float value3, Float value4) {
            // Prec = VP / (VP+FP)
            return truePositive / (truePositive + falsePositive);
        }
    };

    /**
     * TODO: Acrescentar detalhamento para ajudar a leitura do método
     *
     * @param value1
     * @param value2
     * @param value3
     * @param value4
     * @return
     */
    public abstract float calculate(Float value1, Float value2, Float value3, Float value4);

}
