/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cataovo.automations.save;

import cataovo.automations.process.BasicProcess;
import cataovo.automations.process.ProcessAutomatic;
import cataovo.entities.Frame;
import cataovo.entities.Palette;
import cataovo.exceptions.AutomationExecutionException;
import cataovo.utils.Constants;
import cataovo.utils.enums.FileExtension;
import java.io.File;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Bianca Leopoldo Ramos
 */
public final class SaveAutomaticMode extends BasicSave {

    private static final Logger LOG = Logger.getLogger(SaveAutomaticMode.class.getName());
    /**
     * Defines the directory where the resultant images of each transformation
     * will be placed.
     */
    private final StringBuffer imagesDestination;
    /**
     * .
     * Controls the range of each subquerie related to the main querie.
     */
    private int slotRangeControl = 0;

    /**
     * <p>
     * The thread starts the processing of each frame of a palette.</p>
     *
     * @param palette the palette to be processed
     * @param savingDirectory the directory where the results will be saved.
     * @param extension referes to the relatory's file extension where the text
     * data will be saved.
     * @param parentTabName relates the tabName to the type of processing of a
     * palette: Manual or Automatic. Also helps to create folders of each
     * processing type.
     * @param dateTime date to separeate each analisys by the folder
     */
    public SaveAutomaticMode(final Palette palette, final String savingDirectory, final FileExtension extension, final String parentTabName, final String dateTime) {
        super(palette, savingDirectory, extension, parentTabName, dateTime);
        this.imagesDestination = new StringBuffer(savingDirectory).append("/cataovo/").append(palette.getDirectory().getName()).append("/auto/").append(dateTime);
    }

    @Override
    protected String execute() throws AutomationExecutionException {
        StringBuilder result = new StringBuilder();
        Queue<Frame> splitted;
        String destination;
        this.slotRangeControl = 0;
        do {
            splitted = split(getPalette().getFrames(), Constants.FRAME_SLOT_TO_PROCESS);
            try {
                for (Frame frame : splitted) {
                    destination = imagesDestination.toString() + "/" + frame.getName();
                    createImagesFolders(destination);
                }
                destination = imagesDestination.toString();
                synchronized (destination) {
                    result.append(processFrameImages(splitted, destination));
                }
            } catch (InterruptedException | ExecutionException ex) {
                LOG.log(Level.SEVERE, null, ex);
                throw new AutomationExecutionException("Error while executing automation on frame");
            }

        } while (slotRangeControl != getPalette().getPaletteSize());
        return result.toString();
    }

    /**
     * <p>
     * Calls the thread wich is going to processesescha frame.</p>
     *
     * @param frames collection of frames to be analysed.
     * @param destination folder to save the resulted images after the
     * transformations
     * @return a string containing the of quantity of eggs in a frame and a
     * percentage of the total points for later checks
     * @throws ExecutionException
     * @throws InterruptedException
     * @see #execute()
     * @see #split(java.util.Queue, int)
     */
    private StringBuffer processFrameImages(Queue<Frame> frames, String destination) throws ExecutionException, InterruptedException {
        StringBuffer result = new StringBuffer();
        Future<String> task;
        BasicProcess framesProcessor;
        ExecutorService executorService;
        for (Frame frame : frames) {
            executorService = Executors.newSingleThreadExecutor();
            framesProcessor = new ProcessAutomatic(frame, destination);
            task = executorService.submit(framesProcessor);
            synchronized (task) {
                result.append(task.get());
            }
            executorService.awaitTermination(1, TimeUnit.MICROSECONDS);
        }
        return result;
    }

    /**
     * <p>
     * Creates a folder corresponding to each frame in order to save their
     * resulted images transformations.</p>
     *
     * @param destination where the folders will be addressed
     * @return <code>True</code> if the folders were created successfully,
     * <code>False</code> otherwise.
     * @throws InterruptedException
     * @see #execute()
     */
    private synchronized boolean createImagesFolders(String destination) throws InterruptedException {
        File fileImagesDestination;
        fileImagesDestination = new File(destination);
        if (!fileImagesDestination.exists()) {
            fileImagesDestination.mkdirs();
            return true;
        }
        return false;
    }

    /**
     * <p>
     * Splits the main queue into subqueue according to the range.</p>
     *
     * @param toSplit the queue to split.
     * @param range the range of values in each subqueue to be created.
     * @return a subqueue based on the range.
     * @see #execute()
     */
    private Queue split(Queue toSplit, int range) {

        Queue<Frame> subQueue = new LinkedList<>();
        Object[] auxToSplit = toSplit.toArray();
        int limit = calculatRange(slotRangeControl, range, auxToSplit.length);
        if (slotRangeControl != limit) {
            for (int i = this.slotRangeControl; i < limit; i++) {
                Object object = auxToSplit[i];
                subQueue.offer((Frame) object);
            }
        }
        slotRangeControl = calculatRange(slotRangeControl, range, auxToSplit.length);
        return subQueue;
    }

    /**
     * 
     * @param range
     * @param length
     * @return 
     */
    private int calculatRange(int begin, int range, int length) {
        return (begin + range) <= length ? (begin + range) : length;
    }
}
