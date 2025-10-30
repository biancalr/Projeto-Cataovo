/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cataovo.automations.process;

import cataovo.entities.Frame;
import cataovo.utils.Constants;
import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Bianca Leopoldo Ramos
 */
public abstract class BasicProcess implements Callable<String> {

    /**
     * Logging for ThreadAutomaticFramesProcessor.
     */
    private static final Logger LOG = Logger.getLogger(BasicProcess.class.getName());

    /**
     * The frame in a palette to be processed.
     */
    private final Frame frame;

    /**
     * Where the frames wll be saved.
     */
    private final String destination;

    /**
     * The thread responsable for the automatic processing.
     *
     * @param frame
     * @param destination
     */
    public BasicProcess(Frame frame, String destination) {
        this.frame = frame;
        this.destination = destination;
    }

    /**
     * Starting the processment of the frame.
     *
     * @return
     */
    protected abstract String execute();

    /**
     * Initiates the sequence of steps stablished to fing the desired objects in
     * a {@link cataovo.entities.Frame} . The serired objects in this case are
     * the eggs of Aedes aegypti.
     *
     * @return the folder where the images and the relatory ware saved.
     * @throws Exception
     */
    @Override
    public String call() throws Exception {
        LOG.log(Level.INFO, "Starting Sequence...");
        StringBuilder result = new StringBuilder();
        result.append(Constants.QUEBRA_LINHA).append(this.frame.getName()).append(",");
        result.append(execute());
        return result.toString();
    }

    protected Frame getFrame() {
        return frame;
    }
    
    protected String getDestination() {
        return destination;
    }
}
