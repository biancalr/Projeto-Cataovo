/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package cataovo.utils.mathUtils;

/**
 *
 * @author Bianca Leopoldo Ramos
 */
enum PercentMethods {

    RECALL() {
        @Override
        public float calculate(Float truePositive, Float falseNegative) {
            // TVP = VP / (FN+VP)
            return truePositive / (falseNegative + truePositive);
        }
    },
    PRECISION() {
        @Override
        public float calculate(Float truePositive, Float falsePositive) {
            // Prec = VP / (VP+FP)
            return truePositive / (truePositive + falsePositive);
        }
    },
    SPECIFICITY(){
        @Override
        public float calculate(Float trueNegative, Float falsePositive) {
            return trueNegative / (trueNegative + falsePositive);
        }
    };

    /**
     * TODO: Acrescentar detalhamento para ajudar a leitura do método
     *
     * @param value1
     * @param value2
     * @return
     */
    public abstract float calculate(Float value1, Float value2);

}
