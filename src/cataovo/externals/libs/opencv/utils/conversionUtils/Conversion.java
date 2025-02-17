/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cataovo.externals.libs.opencv.utils.conversionUtils;

import cataovo.entities.Frame;
import cataovo.entities.Point;
import cataovo.utils.enums.FileExtension;
import cataovo.externals.libs.opencv.wrappers.MatOfBytesWrapper;
import cataovo.externals.libs.opencv.wrappers.MatWrapper;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.imgcodecs.Imgcodecs;

/**
 * This class is responsable for the conversion between image formats to show
 * them on frame.
 *
 * @author Bianca Leopoldo Ramos
 */
public final class Conversion {

    /**
     * Logging
     */
    private static final Logger LOG = Logger.getLogger(Conversion.class.getName());
    /**
     * Instance
     */
    private static volatile Conversion CONVERTER;

    /**
     *
     * @return rules the operations of converting types
     */
    public static Conversion getInstance() {
        Conversion FORMAT_CONVERTER = Conversion.CONVERTER;
        if (FORMAT_CONVERTER == null) {
            synchronized (Conversion.class) {
                FORMAT_CONVERTER = Conversion.CONVERTER;
                if (FORMAT_CONVERTER == null) {
                    Conversion.CONVERTER = FORMAT_CONVERTER = new Conversion();
                }
            }
        }
        return FORMAT_CONVERTER;
    }

    /**
     * Converts a {@link org.opencv.core.MatOfPoint MatOfPoint} to a list of
     * {@link cataovo.entities.Point Point}
     *
     * @param matOfPoint
     * @return a list of {@link cataovo.entities.Point Point}
     */
    public List<Point> convertMatOfPointToList(MatOfPoint matOfPoint) {
        List<Point> points = new ArrayList<>();
        for (org.opencv.core.Point point : matOfPoint.toList()) {
            points.add(new Point((int) point.x, (int) point.y));
        }
        return points;
    }

    /**
     * Converts an image Frame to a
     * {@link cataovo.externals.libs.opencv.wrappers.MatWrapper Mat} in order to make
     * operations with a {@link org.opencv.core.Mat Opencv.Mat}.
     *
     * @param current the Frame to make the conversion.
     * @return a {@link cataovo.externals.libs.opencv.wrappers.MatWrapper MatWrapper} that
     * encapsulates a {@link org.opencv.core.Mat Mat}
     * @see org.opencv.imgcodecs.Imgcodecs#imread(java.lang.String)
     */
    public MatWrapper convertImageFrameToMat(Frame current) {
        Mat m = new Mat();
        Optional<Mat> optional = Optional.ofNullable(Imgcodecs.imread(current.getPaletteFrame().getPath()));
        optional.ifPresent((t) -> t.copyTo(m));
        MatWrapper wrapper = new MatWrapper(m.clone(), current.getPaletteFrame().getAbsolutePath());
        return wrapper;

    }

    /**
     * Converts the current {@link cataovo.externals.libs.opencv.wrappers.MatWrapper Mat} to
     * a JPG file.
     *
     * @param current the current frame as
     * {@link cataovo.externals.libs.opencv.wrappers.MatWrapper MatWrapper}
     * @return an Optional of the BufferedImage ".jpg"
     * @see #matToBuffedImageConvert(cataovo.opencvlib.wrappers.MatWrapper,
     * cataovo.resources.fileChooser.handler.FileExtension)
     */
    public Optional<Image> convertMatToImg(MatWrapper current) {
        return Optional.ofNullable(matToBuffedImageConvert(current, FileExtension.JPG));
    }

    /**
     * Converts the current {@link cataovo.externals.libs.opencv.wrappers.MatWrapper Mat} to
     * a PNG file.
     *
     * @param current the current frame as
     * {@link cataovo.externals.libs.opencv.wrappers.MatWrapper MatWrapper}
     * @return an Optional of the BufferedImage ".png"
     * @see #matToBuffedImageConvert(cataovo.opencvlib.wrappers.MatWrapper,
     * cataovo.resources.fileChooser.handler.FileExtension)
     */
    public Optional<BufferedImage> convertMatToPng(MatWrapper current) {
        return Optional.ofNullable(matToBuffedImageConvert(current, FileExtension.PNG));
    }

    /**
     * Encodes an image into a memory buffer.
     *
     * @param current the current frame as
     * {@link cataovo.externals.libs.opencv.wrappers.MatWrapper MatWrapper}
     * @param extension the type of desired extension for the frame.
     * @return the image as given extension.
     */
    private BufferedImage matToBuffedImageConvert(MatWrapper current, FileExtension extension) {
        LOG.log(Level.INFO, "Converting a MAT to: {0}", extension.name());
        MatOfBytesWrapper ofBytesWrapper = new MatOfBytesWrapper();
        boolean codeOk = Imgcodecs.imencode("." + extension.toString().toLowerCase(), current.getOpencvMat(), ofBytesWrapper);
        BufferedImage output = makeConversion(codeOk, ofBytesWrapper, extension);
        return output;

    }

    /**
     * Converts the current frame to a image of given extension.
     *
     * @param codeOk <code>true</code> if the encoding into a given extension
     * ran ok, <code>false</code> otherwise.
     * @param ofBytesWrapper compressor of the image and store it in the
     * internal memory buffer that is resized to fit the result
     * @param extension extension of the image
     * @return the image converted
     */
    private BufferedImage makeConversion(boolean codeOk, MatOfBytesWrapper ofBytesWrapper, FileExtension extension) {
        BufferedImage output = null;
        if (codeOk) {
            byte[] byteArray = ofBytesWrapper.toArray();
            InputStream in = new ByteArrayInputStream(byteArray);
            try {
                output = ImageIO.read(in);
            } catch (IOException ex) {
                LOG.log(Level.SEVERE, "Error while converting a MAT to: " + extension.toString(), ex);
            }
            return output;
        } else {
            return null;
        }

    }

}
