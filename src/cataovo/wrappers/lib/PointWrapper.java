/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cataovo.wrappers.lib;

import org.opencv.core.Point;

/**
 * Wrapps a {@link org.opencv.core.Point Point}
 * @author Bianca Leopoldo Ramos.
 */
public final class PointWrapper {
    
    private Point point;

    public PointWrapper() {
        
    }
    
    /**
     * 
     * @param p cataovo.entities.Point
     */
    public PointWrapper(cataovo.entities.Point p) {
        point = new Point(p.getX(), p.getY());
    }

    /**
     * 
     * @return a cataovo.entities.Point
     */
    public cataovo.entities.Point getPoint() {
        return new cataovo.entities.Point((int)point.x, (int)point.y);
    }

    /**
     * 
     * @param point a cataovo.entities.Point to set
     */
    public void setPoint(cataovo.entities.Point point) {
        this.point.x = point.getX();
        this.point.y = point.getY();
    }
    
    /**
     * 
     * @param p a cataovo.entities.Point to set
     * @return 
     */
    public double dot(cataovo.entities.Point p){
        return point.dot(new Point(p.getX(), p.getY()));
    }
    
    public Point getOpencvPoint(){
        return point;
    }
}
