/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cataovo.automation.threads.dataProcessing;

import cataovo.entities.Frame;
import cataovo.externals.libs.opencv.utils.ImageProcessImplements;
import cataovo.utils.Constants;
import cataovo.utils.libraryUtils.ImageProcessUtils;
import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Bianca Leopoldo Ramos
 */
public abstract class DataProcessingThreadAutomation implements Callable<StringBuffer> {

    /**
     * Logging for ThreadAutomaticFramesProcessor.
     */
    private static final Logger LOG = Logger.getLogger(DataProcessingThreadAutomation.class.getName());

    /**
     * The frame in a palette to be processed.
     */
    private final Frame frame;
    /**
     * Where the frames wll be saved.
     */
    protected final String destination;
    /**
     * Contains the methods to process a frame.
     */
    protected final ImageProcessUtils imageProcess;

    /**
     * The thread responsable for the automatic processing.
     *
     * @param frame
     * @param destination
     */
    public DataProcessingThreadAutomation(Frame frame, String destination) {
        this.frame = frame;
        this.destination = destination;
        this.imageProcess = new ImageProcessImplements();
    }

    /**
     * Starting the processment of the frame.
     *
     * @param f
     * @return
     */
    protected abstract StringBuffer startSequence(Frame f);

    /**
     * Initiates the sequence of steps stablished to fing the desired objects in
     * a {@link cataovo.entities.Frame} . The serired objects in this case are
     * the eggs of Aedes aegypti.
     *
     * @return the folder where the images and the relatory ware saved.
     * @throws Exception
     */
    @Override
    public StringBuffer call() throws Exception {
        LOG.log(Level.INFO, "Starting Sequence...");
        StringBuffer result = new StringBuffer();
        result.append(Constants.QUEBRA_LINHA).append(this.frame.getName()).append(",");
        result.append(startSequence(this.frame));
        return result;
    }

}
