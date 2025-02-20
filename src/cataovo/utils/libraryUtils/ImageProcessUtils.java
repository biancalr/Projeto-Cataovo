/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package cataovo.utils.libraryUtils;

import cataovo.entities.Point;
import cataovo.wrappers.MatOfPointWrapper;
import cataovo.wrappers.MatWrapper;
import java.util.List;
import java.util.Map;

/**
 * Stablish the steps to detect Aedes eggs in a image.
 *
 * @author Bianca Leopoldo Ramos.
 */
public interface ImageProcessUtils {

    /**
     * Apply Blur On a Image
     *
     * @param imageMatToBlur the image to apply the blurring
     * @param savingPath the folder to save the image
     * @param ksize_width blurring kernel width
     * @param ksize_height blurring kernel height
     * @return the image blurred
     * @see org.opencv.imgproc.Imgproc#blur(org.opencv.core.Mat,
     * org.opencv.core.Mat, org.opencv.core.Size)
     */
    public MatWrapper applyBlurOnImage(String savingPath, MatWrapper imageMatToBlur, int ksize_width, int ksize_height);

    /**
     * Apply Binary On a Image
     *
     * @param savingPath the folder to save the image
     * @param imgToBinary the image to apply binarization
     * @return the image binarried
     */
    public MatWrapper applyBinaryOnImage(String savingPath, MatWrapper imgToBinary);

    /**
     * Apply Morphology On a Image
     *
     * @param savingPath the folder to save the image
     * @param structuringElementWidth Width of the structuring element. 
     * @param structuringElementHeight Height of the structuring element. 
     * @param morphologicalOperation the morphological operation used. The list bellow:
     * operations<ul><li><code>MORPH_ERODE = 0,</code></li><li><code>MORPH_DILATE = 1,</code></li>
     * <li><code>MORPH_OPEN = 2,</code></li><li><code>MORPH_CLOSE = 3,</code></li><li><code>MORPH_GRADIENT = 4,</code></li><li><code>MORPH_TOPHAT = 5,</code></li><li><code>MORPH_BLACKHAT = 6,</code></li><li><code>MORPH_HITMISS = 7;</code></li></ul>
     * @param imageToMorph the image to apply morphology
     * @return the image transformed based on the given morphological operation
     * @see org.opencv.imgproc.Imgproc#getStructuringElement(int,
     * org.opencv.core.Size)
     * @see org.opencv.imgproc.Imgproc#morphologyEx(org.opencv.core.Mat,
     * org.opencv.core.Mat, int, org.opencv.core.Mat)
     */
    public MatWrapper applyMorphOnImage(String savingPath, int structuringElementWidth, int structuringElementHeight, int morphologicalOperation, MatWrapper imageToMorph);

    /**
     * Draw Contours On a Image.
     *
     * @param savingPath the folder to save the image
     * @param imageToDraw the image to draw the contours upon.
     * @param imgToFindContours image transformed by the morphological operations to find the contours
     * @param minSizeArea the min sized area to be considered as an object of interest
     * @param maxSizeArea the min sized area to be considered as an object of interest
     * @return the image drawn based on the contours that were found.
     * @see Imgproc#findContours(org.opencv.core.Mat, java.util.List, org.opencv.core.Mat, int, int) 
     * @see Imgproc#drawContours(org.opencv.core.Mat, java.util.List, int, org.opencv.core.Scalar) 
     */
    public Map<Integer,List<List<Point>>> drawContoursOnImage(String savingPath, MatWrapper imageToDraw, MatWrapper imgToFindContours, int minSizeArea, int maxSizeArea);
    
    /**
     * Calculates the area.
     * @param contour
     * @return 
     */
    public double getArea(MatOfPointWrapper contour);

}
