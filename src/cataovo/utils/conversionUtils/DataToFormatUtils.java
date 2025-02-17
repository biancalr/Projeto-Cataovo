/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cataovo.utils.conversionUtils;

import cataovo.entities.Point;
import cataovo.entities.Region;
import cataovo.utils.constants.Constants;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 *
 * @author bianc
 */
public class DataToFormatUtils {
    
    public DataToFormatUtils(){
        
    }
    
    public final List split(int ofFormat, String[] data) throws NumberFormatException {
        int jumpStep;
        int atStartPoint;
        switch (ofFormat) {
            case Constants.RECT_FORMAT -> {
                jumpStep = 4;
                atStartPoint = 1;
            }
            case Constants.CIRCLE_FORMAT -> {
                jumpStep = 2;
                atStartPoint = 0;
            }
            default ->
                throw new AssertionError();
        }
        return iterateOver(data, ofFormat, atStartPoint, jumpStep);

    }

    private List<?> iterateOver(String[] data, int ofFormat, int atStartPoint, int jumpStep) throws NumberFormatException {
        List formatList = new CopyOnWriteArrayList<>();
        for (int i = atStartPoint; i < data.length; i += jumpStep) {
            if (data[i] != null && !data[i].isBlank()) {
                switch (ofFormat) {
                    case Constants.RECT_FORMAT ->
                        formatList.add(addRegion(data, i));
                    case Constants.CIRCLE_FORMAT ->
                        formatList.add(addPoint(data, i));
                    default ->
                        throw new AssertionError();
                }

            }
        }
        return formatList;
    }

    /**
     * Converts a line of string data into {@link cataovo.entities.Point points}
     *
     * @param data
     * @return the list of points
     * @throws NumberFormatException
     */
    private Point addPoint(String[] data, int ofPosition) throws NumberFormatException {
        return new Point(
                Integer.parseInt(data[ofPosition].replace(".0", "").trim()),
                Integer.parseInt(data[ofPosition + 1].replace(".0", "").trim()));

    }

    /**
     * Converts a line of string data into
     * {@link cataovo.entities.Region regions}
     *
     * @param data
     * @return the list of regions
     * @throws NumberFormatException
     */
    private Region addRegion(String[] data, int ofPosition) throws NumberFormatException {
        return new Region(
                //Acrescentando correção em caso de valores negativos
                Integer.parseInt(data[ofPosition + 3]) > 0 ? Integer.parseInt(data[ofPosition + 3]) : Math.abs(Integer.parseInt(data[ofPosition + 3])),
                Integer.parseInt(data[ofPosition + 2]) > 0 ? Integer.parseInt(data[ofPosition + 2]) : Math.abs(Integer.parseInt(data[ofPosition + 2])),
                new Point(Integer.parseInt(data[ofPosition + 2]) > 0 ? (Integer.parseInt(data[ofPosition]) - Integer.parseInt(data[ofPosition + 2])) : Integer.parseInt(data[ofPosition]),
                        Integer.parseInt(data[ofPosition + 3]) > 0 ? (Integer.parseInt(data[ofPosition + 1]) - Integer.parseInt(data[ofPosition + 3])) : Integer.parseInt(data[ofPosition + 1])));

    }
    
}
