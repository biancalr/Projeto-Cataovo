/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cataovo.wrappers.conversion;

import cataovo.utils.enums.FileExtension;
import cataovo.wrappers.lib.MatOfBytesWrapper;
import cataovo.wrappers.lib.MatWrapper;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import org.opencv.imgcodecs.Imgcodecs;

/**
 * This class is responsable for the conversion between image formats to show
 * them on frame.
 *
 * @author Bianca Leopoldo Ramos
 */
public final class Conversions {

    /**
     * Logging
     */
    private static final Logger LOG = Logger.getLogger(Conversions.class.getName());

    public Conversions() {
    }

    /**
     * Converts the current {@link cataovo.wrappers.lib.MatWrapper Mat} to a JPG
     * file.
     *
     * @param current the current frame as
     * {@link cataovo.wrappers.lib.MatWrapper MatWrapper}
     * @return an Optional of the BufferedImage ".jpg"
     * @see #matToBuffedImageConvert(cataovo.opencvlib.wrappers.MatWrapper,
     * cataovo.resources.fileChooser.handler.FileExtension)
     */
    public Optional<Image> convertMatToImg(MatWrapper current) {
        return Optional.ofNullable(matToBuffedImageConvert(current, FileExtension.JPG));
    }

    /**
     * Converts the current {@link cataovo.wrappers.lib.MatWrapper Mat} to a PNG
     * file.
     *
     * @param current the current frame as
     * {@link cataovo.wrappers.lib.MatWrapper MatWrapper}
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
     * {@link cataovo.wrappers.lib.MatWrapper MatWrapper}
     * @param extension the type of desired extension for the frame.
     * @return the image as given extension.
     */
    private BufferedImage matToBuffedImageConvert(MatWrapper current, FileExtension extension) {
        LOG.log(Level.INFO, "Converting a MAT to: {0}", extension.name());
        final MatOfBytesWrapper ofBytesWrapper = new MatOfBytesWrapper();
        boolean codeOk = Imgcodecs.imencode("." + extension.toString().toLowerCase(), current.getOpencvMat(), ofBytesWrapper);
        final BufferedImage output = makeConversion(codeOk, ofBytesWrapper, extension);
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

        }
        return output;

    }

}
