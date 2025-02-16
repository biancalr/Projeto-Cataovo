/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cataovo.automation.threads.dataEvaluation;

/**
 *
 * @author bianc
 */
public class ThreadAutomationEvaluationPixel extends DataEvaluationThreadAutomation {

    public ThreadAutomationEvaluationPixel(String fileContentManual, String fileContentAuto) {
        super(fileContentManual, fileContentAuto);
    }

    @Override
    protected float[] evaluateFrame(String regionsInFrame, String pointsInFrame) throws NumberFormatException {
        return null;
    }
    
}
