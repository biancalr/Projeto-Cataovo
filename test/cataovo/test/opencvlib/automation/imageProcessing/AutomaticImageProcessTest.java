/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cataovo.test.opencvlib.automation.imageProcessing;

import cataovo.utils.Constants;
import cataovo.utils.enums.FileExtension;
import cataovo.utils.fileUtils.writers.csv.csvWriter.CsvFileWriter;
import cataovo.wrappers.conversion.Conversions;
import cataovo.wrappers.opencv.MatWrapper;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Size;

/**
 *
 * @author Bianca Leopoldo Ramos
 */
public class AutomaticImageProcessTest {
    
    public static void main(String[] args) {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        
        final String orign = "C:\\Users\\bianc\\OneDrive\\Documentos\\Paletas\\Placa\\original.png";
        String dstn = "C:/Users/bianc/OneDrive/Documentos/Paletas/teste";
        final String frameName = "/frame_" + orign.substring(orign.lastIndexOf("\\") + 1, orign.lastIndexOf("."));
        File file = new File(dstn);
        int sizeTag = file.listFiles().length + 1;
        
        dstn = dstn + "/teste_" + sizeTag;
        file = new File(dstn);
        if (!file.exists()) {
            file.mkdir();
        }
        
//        ImageProcessUtils imageProcess = new ImageProcessUtilsImplements();
//        
//        Mat img = Imgcodecs.imread(orign);
//        
//        Core.copyMakeBorder(img, img, 4, 4, 4, 4, Core.BORDER_CONSTANT, new Scalar(255.0, 255.0, 255.0));
//        
//        Imgcodecs.imwrite(dstn + frameName + "_1_original.png", img);
//        
//        
//        Mat finalImage = img.clone();
//        
//        /*
//         *  Reduzir o brilho
//        */
//        img.convertTo(img, -1, 1, -15);
//        Imgcodecs.imwrite(dstn + frameName + "_2_brightness.png", img);
//        
//        /*
//         * Aplicar Abertura 
//         */
//        img = imageProcess.applyMorphOnImage(dstn + frameName + "_3_morph_init.png", 3, 3, 2, img);
//        
//        System.out.println("-------------------------------------" + img.type());
//        
//        img = imageProcess.applyBlurOnImage(dstn + frameName+ "_4_blur.png", img, 5, 5);
//        
//        System.out.println("-------------------------------------" + img.type());
//        
//        Mat im = getChannelImage(img, dstn + frameName + "_5_channel.png");
////        img = imageProcess.applyBinaryOnImage(dstn + frameName + "_5_binary.png", Conversions.getInstance().convertMatToPng(new MatWrapper(img, orign)).get());
//        
////        Mat im = getChannelImage(img, dstn + frameName + "_5_binary.png");
//
//        im = applyBinaryOnImage(dstn + frameName + "_6_binary.png", Conversions.getInstance().convertMatToPng(new MatWrapper(im, orign)).get());
//        
//        im = imageProcess.applyMorphOnImage(dstn + frameName + "_7_morph_1.png", 17, 40, 2, im);
//        im = imageProcess.applyMorphOnImage(dstn + frameName+ "_8_morph_2.png", 40, 17, 2, im);
////        img = imageProcess.applyMorphOnImage(dstn + "_morph_3.png", 17, 35, 2, img);
////        img = imageProcess.applyMorphOnImage(dstn + "_morph_4.png", 40, 17, 2, img);
//        
//
//        Core.copyMakeBorder(im, im, 1, 1, 1, 1, Core.BORDER_CONSTANT, new Scalar(255.0, 255.0, 255.0));
//        
//        finalImage = imageProcess.drawContoursOnImage(dstn + frameName + "_9_contours.png", finalImage, im, 800, 5500);
        
        System.out.println(frameName);
    }

    private static Mat applyBinaryOnImage(String savingPath, BufferedImage buffImgToBinary) {
        double[] WHITE = new double[] {255};
        double[] BLACK = new double[] {255};
        StringBuilder bf = new StringBuilder();
        Mat dstn = Mat.ones(new Size(buffImgToBinary.getWidth(), buffImgToBinary.getHeight()), CvType.CV_8UC1);
        for (int i = 0; i < buffImgToBinary.getWidth(); i++) {
            for (int j = 0; j < buffImgToBinary.getHeight(); j++) {
                Color color = new Color(buffImgToBinary.getRGB(i, j));
                double red = color.getRed();
//                double green = color.getGreen();
//                double red = color.getBlue();
                bf.append(red).append(Constants.QUEBRA_LINHA);
//                System.out.println("green: "+green);
//                System.out.println("blue: "+red);
                if (red != 252.0) {
                    dstn.put(j, i, BLACK);
                } else {
                    dstn.put(j, i, WHITE);
                }
            }
        }
        if (saveImage(dstn, savingPath)) {
            saveImageTones(bf);
            return dstn;
        } 
        return null;
    }
    
    /**
     * Transforms a three channels image to a single channel image.
     *
     * @param src the image to split the channels
     * @return a single channel result image
     */
    private static Mat getChannelImage(Mat src, String location) {
        BufferedImage image = new Conversions().convertMatToPng(new MatWrapper(src, location)).get();
        double[] WHITE = new double[] {255};
        double[] BLACK = new double[] {0};
        double limite = 44;
        StringBuilder bf = new StringBuilder("list").append(Constants.QUEBRA_LINHA);
        Mat matR = Mat.zeros(image.getHeight(),image.getWidth(), CvType.CV_8UC1);
        Mat m = new Mat(image.getHeight(),image.getWidth(), CvType.CV_8UC1);
        System.out.println(matR.size());
        System.out.println(image.getWidth() + "x" + image.getHeight());
        for (int i = 0; i < image.getWidth(); i++) {
            for (int j = 0; j < image.getHeight(); j++) {
                Color color = new Color(image.getRGB(i, j));
                double temp = color.getBlue();
                matR.put(j, i, temp);
                bf.append(temp).append(Constants.QUEBRA_LINHA);
                if (temp > limite) {
                    m.put(j, i, WHITE);
                } else {
                    m.put(j, i, BLACK);
                }
            }
        }
        
        saveImage(m, location.replace("_5_channel.png", "_6_channel.png"));
        saveImage(matR, location);
        saveImageTones(bf);
        return m;
    }
    
    private static boolean saveImage(Mat dstn, String savingPath) {
        try {
            BufferedImage image = new Conversions().convertMatToPng(new MatWrapper(dstn, savingPath)).get();
            ImageIO.write(image, FileExtension.PNG.toString().toLowerCase(), new File(savingPath));
            return true;
        } catch (IOException ex) {
            return false;
        }
    }

    private static void saveImageTones(StringBuilder bf) {
        String fileLocation = "C:/Users/bianc/OneDrive/Documentos/Paletas/teste/tones.csv";
        File f = new File(fileLocation);
        if (f.exists()) {
            f.delete();
        }
        try (FileWriter csvWriter = new FileWriter(fileLocation); 
                PrintWriter csvPrinter = new PrintWriter(csvWriter);) {

            
            csvPrinter.print(bf);
            
            
        } catch (Exception e) {
            Logger.getLogger(CsvFileWriter.class.getName()).log(Level.SEVERE, e.getMessage());
        }
    }
    
}
