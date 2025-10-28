/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cataovo.wrappers.opencv;

import cataovo.entities.Frame;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;

/**
 * Wrapps a {@link org.opencv.core.Mat Mat}
 *
 * @author Bianca Leopoldo Ramos
 */
public final class MatWrapper{

    private static final int DEPTH = CvType.CV_8U;
    private int WIDTH;
    private int HEIGHT;
    private Mat mat;
    private String location;
    
    public MatWrapper() {
        location = null;
        this.WIDTH = 0;
        this.HEIGHT = 0;
        mat = new Mat(new Size(WIDTH, HEIGHT), DEPTH);
    }
    
    public MatWrapper(final Mat m, final String location) {
        this.mat =  m;
        this.location = location;
        this.WIDTH = this.mat.width();
        this.HEIGHT = this.mat.height();
    }
    
    public MatWrapper(final Frame frame){
        this.location = frame.getPaletteFrame().getAbsolutePath();
        this.mat = Imgcodecs.imread(frame.getPaletteFrame().getAbsolutePath()).clone();
        this.WIDTH = this.mat.width();
        this.HEIGHT = this.mat.height();
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Mat getOpencvMat() {
        return mat;
    }

    public void setOpencvMat(final Mat mat) {
        this.mat = mat;
        this.WIDTH = mat.width();
        this.HEIGHT = mat.height();
    }

    public int getWIDTH() {
        return WIDTH;
    }

    public int getHEIGHT() {
        return HEIGHT;
    }
    
    public int getArea() {
        return this.getWIDTH() * this.getHEIGHT();
    }
    
}
