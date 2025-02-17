/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cataovo.automation.threads.dataEvaluation;

import cataovo.entities.Point;
import cataovo.entities.Region;
import cataovo.utils.constants.Constants;
import cataovo.utils.conversionUtils.DataToFormatUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 *
 * @author bianc
 */
public class ThreadAutomationEvaluationPixel extends DataEvaluationThreadAutomation {

    private final DataToFormatUtils dataUtils;
    
    public ThreadAutomationEvaluationPixel(String fileContentManual, String fileContentAuto) {
        super(fileContentManual, fileContentAuto);
        this.dataUtils = new DataToFormatUtils();
    }

    @Override
    protected float[] evaluateFrame(final String regionsInFrame, final String pointsInFrame) throws NumberFormatException {
        int tp = 0;
        int tn = 0;
        int fp = 0;
        int fn = 0;
        float[] metrics = new float[4];
        List<Region> regions;
        List<String> eggsString;
        List<List<Point>> eggs = new ArrayList<>();
        // TODO: Subtrair as marcações manuais e automáticas
            //  recriar as regiões
            //Separar a as regioes pela vírgula
        regions = dataUtils.split(Constants.RECT_FORMAT, regionsInFrame.split(Constants.SEPARATOR));
        //Separa as áreas dos ovos pela cerquilha
        eggsString = new CopyOnWriteArrayList<>(List.of(pointsInFrame.split(Constants.OBJECT_SEPARATOR)));
        
        eggsString.forEach(p -> {
            eggs.add(dataUtils.split(Constants.CIRCLE_FORMAT, p.split(Constants.SEPARATOR)));
        });
        
            //  calcular a área das marcações
            
            
            //  subtrair os valores do total de pixels do frame
            
        
        metrics[0] = tp;
        metrics[1] = fn;
        metrics[2] = fp;
        metrics[3] = tn;
        return metrics;
    }
    
}
