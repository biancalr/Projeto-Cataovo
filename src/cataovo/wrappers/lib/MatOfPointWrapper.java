/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cataovo.wrappers.lib;

import cataovo.entities.Point;
import java.util.List;
import org.opencv.core.MatOfPoint;

/**
 * Wrapps a {@link org.opencv.core.MatOfPoint MatOfPoint}
 *
 * @author Bianca Leopoldo Ramos
 */
public final class MatOfPointWrapper {
    
    private final MatOfPoint matOfPoint;
    
    public MatOfPointWrapper(final List<Point> listOfPoints) {
        this.matOfPoint = new MatOfPoint(toOpencvMatOfPoint(listOfPoints));
    }

    private org.opencv.core.Point[] toOpencvMatOfPoint(final List<Point> listOfPoint) {
        org.opencv.core.Point[] cvPoints = new org.opencv.core.Point[listOfPoint.size()];
        for (int i = 0; i < listOfPoint.size(); i++) {
            cvPoints[i] = new org.opencv.core.Point(listOfPoint.get(i).getX(), listOfPoint.get(i).getY());
        }
        return cvPoints;
    }

    public MatOfPoint getMatOfPoint() {
        return matOfPoint;
    }
    
}
