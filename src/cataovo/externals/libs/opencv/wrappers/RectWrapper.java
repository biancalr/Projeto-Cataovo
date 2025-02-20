/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cataovo.externals.libs.opencv.wrappers;

import cataovo.entities.Point;
import cataovo.entities.Region;
import org.opencv.core.Rect;

/**
 * Wrapps a {@link org.opencv.core.Rect Rect}
 * @author Bianca Leopoldo Ramos
 */
public final class RectWrapper {
    
    private final Rect rect;
    private double area;

    public RectWrapper() {
        this.rect = new Rect();
        this.area = 0;
    }

    public RectWrapper(final Rect rect) {
        this.rect = rect;
        this.area = rect.area();
    }

    /**
     * 
     * @param region 
     */
    public RectWrapper(final Region region) {
        this.rect = new Rect(region.getInitialPoint().getX(), 
                             region.getInitialPoint().getY(),
                             region.getWidth(), 
                             region.getHeight());
        this.area = this.rect.area();
    }

    /**
     * 
     * @return 
     */
    public Region getRegion() {
        return new Region(rect.height, rect.width, new Point(rect.x, rect.y));
    }

    /**
     * 
     * @param region 
     */
    public void setRect(Region region) {
        this.rect.x = region.getInitialPoint().getX();
        this.rect.y = region.getInitialPoint().getY();
        this.rect.height = region.getHeight();
        this.rect.width = region.getWidth();
        this.area = this.rect.area();
    }
    
    /**
     * 
     * @param p
     * @return 
     */
    public boolean contains(PointWrapper p){
       return rect.x <= p.getPoint().getX() && p.getPoint().getX() < rect.x + rect.width 
               && rect.y <= p.getPoint().getY() && p.getPoint().getY() < rect.y + rect.height;
    }

    public Rect getOpencvRect() {
        return rect;
    }

    public double getArea() {
        return area;
    }
}
