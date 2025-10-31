/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cataovo.automations.process;

import cataovo.entities.Frame;
import cataovo.entities.Point;
import cataovo.utils.Constants;
import cataovo.wrappers.opencv.MatWrapper;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
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
        try {
            LOG.info("Starting to process the frame");
            MatWrapper current = new MatWrapper(getFrame());
            String dstny = getDestination() + "/" + getFrame().getName();
            // blur
            LOG.log(Level.INFO, "Applying blur...");
            current = current.applyBlur(dstny + Constants.BLUR_PNG, 5, 5);

            // binary
            LOG.log(Level.INFO, "Applying binary...");
            current = current.applyBinary(dstny + Constants.BINARY_PNG);

            // morphology
            LOG.log(Level.INFO, "Applying morphology...");
            current = current.applyMorph(dstny + Constants.MORPH_PNG,
                    17, 35, 2);
            current = current.applyMorph(dstny + Constants.MORPH_PNG,
                    35, 17, 2);

            // contours
            LOG.log(Level.INFO, "Drawing contours...");
            final Map<Integer, List<List<Point>>> result = current.drawContours(dstny + Constants.CONTOURS_PNG,
                    new MatWrapper(getFrame()), 800, 5000);

            LOG.info("Finising the processing");
            return report(result);
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, "Error while processing", ex);
            return "";
        }
    }

    private String report(final Map<Integer, List<List<Point>>> result) {
        final StringBuffer builder = new StringBuffer();

        result.entrySet().forEach(entry -> {
            Integer key = entry.getKey();
            List<List<Point>> val = entry.getValue();

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
