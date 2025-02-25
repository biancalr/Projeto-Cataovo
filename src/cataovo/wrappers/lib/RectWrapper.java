/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cataovo.wrappers.lib;

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
    
    public Rect getRect() {
        final Rect region = new Rect();
        region.x = this.rect.width > 0 ? (this.rect.x - this.rect.width) : this.rect.x;
        region.y = this.rect.height > 0 ? (this.rect.y - this.rect.height) : this.rect.y;
        region.width = this.rect.width > 0 ? this.rect.width : Math.abs(this.rect.width);
        region.height = this.rect.height > 0 ? this.rect.height : Math.abs(this.rect.height);
        return region;
    }
}
