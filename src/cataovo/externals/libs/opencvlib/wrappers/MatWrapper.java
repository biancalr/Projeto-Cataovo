/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cataovo.externals.libs.opencvlib.wrappers;

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
public class MatWrapper{

    private static final int DEPTH = CvType.CV_8U;
    private int WIDTH = 640;
    private int HEIGHT = 480;
    private Mat mat;
    private String location;
    
    public MatWrapper() {
        location = null;
        mat = new Mat(new Size(WIDTH, HEIGHT), DEPTH);
    }
    
    public MatWrapper(Mat m, String location) {
        this.mat =  m;
        this.location = location;
    }
    
    public MatWrapper(Frame frame){
        this.location = frame.getPaletteFrame().getAbsolutePath();
        this.mat = Imgcodecs.imread(frame.getPaletteFrame().getAbsolutePath()).clone();
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

    public void setOpencvMat(Mat mat) {
        this.mat = mat;
    }

    public int getWIDTH() {
        return WIDTH;
    }

    public void setWIDTH(int WIDTH) {
        this.WIDTH = WIDTH;
    }

    public int getHEIGHT() {
        return HEIGHT;
    }

    public void setHEIGHT(int HEIGHT) {
        this.HEIGHT = HEIGHT;
    }
    
}
