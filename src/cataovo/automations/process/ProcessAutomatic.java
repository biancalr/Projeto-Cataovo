/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cataovo.automations.process;

import cataovo.entities.Frame;
import cataovo.entities.Point;
import cataovo.utils.Constants;
import cataovo.wrappers.opencv.MatWrapper;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * This thread is responsible for batch processing a
 * {@link cataovo.entities.Frame Frame}. This processment is responsible for
 * find each egg present in the frame.
 *
 * @author Bianca Leopoldo Ramos.
 */
public class ProcessAutomatic extends BasicProcess {

    /**
     * Logging for ProcessAutomatic.
     */
    private static final Logger LOG = Logger.getLogger(ProcessAutomatic.class.getName());

    public ProcessAutomatic(final Frame frame, final String destination) {
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
     * @return a text containing the quanity of eggs of Aedes found in the
     * frame, and a List of some of the points that make part of the eggs
     * contours.
     * @see cataovo.utils.libraryUtils.ImageTransformationUtils
     */
    @Override
    public String execute() {
        LOG.info("Starting to process the frame");
        MatWrapper current = new MatWrapper(getFrame());
        String dstny = getDestination() + "/" + getFrame().getName();
        // blur
        current = imageTransform().applyBlurOnImage(dstny + Constants.BLUR_PNG,
                current, 5, 5);

        // binary
        current = imageTransform().applyBinaryOnImage(dstny + Constants.BINARY_PNG,
                current);

        // morphology 
        current = imageTransform().applyMorphOnImage(dstny + Constants.MORPH_PNG,
                17, 35, 2, current);
        current = imageTransform().applyMorphOnImage(dstny + Constants.MORPH_PNG,
                35, 17, 2, current);

        // contours
        final var result = imageTransform().drawContoursOnImage(dstny + Constants.CONTOURS_PNG,
                new MatWrapper(getFrame()), current, 800, 5000);

        LOG.info("Finising the processing");
        return report(result);
    }

    private String report(final Map<Integer, List<List<Point>>> result) {
        final StringBuffer builder = new StringBuffer();

        result.entrySet().forEach(entry -> {
            var key = entry.getKey();
            var val = entry.getValue();

            builder.append(Integer.toString(key));

            List<Point> auxlist;
            List<Point> mainPoints = new ArrayList<>();
            for (var i = 0; i < val.size(); i++) {

                auxlist = val.get(i);
                for (int j = 0; j < auxlist.size(); j++) {

                    // Saves a point each given steps
                    if ((j % Constants.STEPS_TO_POINT_SAVING) == 0) {
                        mainPoints.add(auxlist.get(j));
                    }
                }

                for (var j = 0; j < mainPoints.size(); j++) {
                    if (j != 0) {
                        builder.append(Constants.SEPARATOR);
                    } else {
                        builder.append(Constants.OBJECT_SEPARATOR);
                    }
                    builder.append(mainPoints.get(j).getX());
                    builder.append(",");
                    builder.append(mainPoints.get(j).getY());
                }

                mainPoints = new ArrayList<>();

            }
        });
        return builder.toString();
    }

}
