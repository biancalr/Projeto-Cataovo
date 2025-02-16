/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cataovo.automation.threads.dataProcessing;

import cataovo.constants.Constants;
import cataovo.entities.Frame;
import cataovo.externals.libs.opencvlib.converters.Converter;
import cataovo.externals.libs.opencvlib.wrappers.MatWrapper;
import java.util.logging.Logger;

/**
 * This thread is responsible for batch processing a
 * {@link cataovo.entities.Frame Frame}. This processment is responsible for find each
 * egg present in the frame.
 *
 * @author Bianca Leopoldo Ramos.
 */
public class ThreadAutomaticFramesProcessor extends DataProcessingThreadAutomation {

    /**
     * Logging for ThreadAutomaticFramesProcessor.
     */
    private static final Logger LOG = Logger.getLogger(ThreadAutomaticFramesProcessor.class.getName());
   
    public ThreadAutomaticFramesProcessor(final Frame frame, final String destination) {
        super(frame, destination);
    }

    /**
     * <p>
     * Sequence that processes each {@link cataovo.entities.Frame Frame} of a
     * {@link cataovo.entities.Palette Pelette}.</p>
     *
     * <p>
     * Explicação das operações a serem aplicadas. </p>
     *
     * @param frame the current frame to process
     * @return a text containing the quanity of eggs of Aedes found in the
     * frame, and a List of some of the points that make part of the eggs
     * contours.
     * @see cataovo.externals.libs.opencvlib.automation.imageProcessing.AutomaticImageProcess
     */
    @Override
    public StringBuffer startSequence(final Frame frame) {
        LOG.info("Starting to process the frame");
        MatWrapper current = new MatWrapper(frame);
        String dstny = destination + "/" + frame.getName();
        // blur
        current.setOpencvMat(imageProcess.applyBlurOnImage(dstny + Constants.BLUR_PNG,
                current.getOpencvMat(), 5, 5));

        // binary
        current.setOpencvMat(imageProcess.applyBinaryOnImage(dstny + Constants.BINARY_PNG,
                Converter.getInstance().convertMatToPng(current).get()));

        // morphology 
        current.setOpencvMat(imageProcess.applyMorphOnImage(dstny + Constants.MORPH_PNG,
                17, 35, 2, current.getOpencvMat()));
        current.setOpencvMat(imageProcess.applyMorphOnImage(dstny + Constants.MORPH_PNG,
                35, 17, 2, current.getOpencvMat()));
//        current.setOpencvMat(imageProcess.applyMorphOnImage(dstny + Constants.MORPH_PNG,
//                17, 35, 2, current.getOpencvMat()));
//        current.setOpencvMat(imageProcess.applyMorphOnImage(dstny + Constants.MORPH_PNG,
//                30, 17, 2, current.getOpencvMat()));

        // contours
        current.setOpencvMat(imageProcess.drawContoursOnImage(dstny + Constants.CONTOURS_PNG,
                new MatWrapper(frame).getOpencvMat(), current.getOpencvMat(), 800, 5000));

        LOG.info("Finising the processment of the frame");
        return imageProcess.generateAutoReport();
    }

}
