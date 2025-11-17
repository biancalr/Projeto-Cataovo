/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cataovo.events.implement;

import cataovo.automations.save.BasicSave;
import cataovo.automations.save.SaveAutomaticMode;
import cataovo.domain.Event;
import cataovo.entities.Frame;
import cataovo.entities.Palette;
import cataovo.exceptions.AutomationExecutionException;
import cataovo.exceptions.DirectoryNotValidException;
import cataovo.resources.MainContext;
import cataovo.utils.Constants;
import cataovo.utils.enums.FileExtension;
import cataovo.utils.fileUtils.readers.CsvFileReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import cataovo.events.AutomaticEvent;

/**
 * Implements the automatic processment controller
 *
 * @author Bianca Leopoldo Ramos
 */
public class AutomaticEventImpl implements AutomaticEvent {

    public AutomaticEventImpl(MainContext mainContext) {
        this.mainContext = mainContext;
    }

    private static final Logger LOG = Logger.getLogger(AutomaticEventImpl.class.getName());

    private final CsvFileReader reader = new CsvFileReader();
    private final MainContext mainContext;

    private Event event;

    /**
     * .
     * Controls the range of each subquerie related to the main querie.
     */
    private int slotRangeControl = 0;

    @Override
    public Event execute(final Palette currentPalette, final String savingFolderPath, final String tabName) throws DirectoryNotValidException, AutomationExecutionException {
        String result = "";
        BasicSave automation;
        final String dateTime = getDateTime("dd-MM-yyyy_HH-mm-ss");
        Future<String> task;
        mainContext.getPanelTabHelper().setCurrentTabProcessing(true);
        
        try {
            final List<Palette> splitted = split(currentPalette, Constants.SLOT_FRAMES_TO_PROCESS_ON_PALETTE);
            final ExecutorService executorService = Executors.newFixedThreadPool(splitted.size());

            for (Palette palette : splitted) {

                automation = new SaveAutomaticMode(
                        palette,
                        savingFolderPath,
                        FileExtension.CSV,
                        tabName, dateTime);
                task = executorService.submit(automation);
                executorService.awaitTermination(1, TimeUnit.MILLISECONDS);
                synchronized (task) {
                    result = task.get();
                }
            }
            executorService.shutdown();
            this.slotRangeControl = 0;
            event = new Event(result, wrapProcess(result));
            return event;
        } catch (DirectoryNotValidException ex) {
            LOG.log(Level.SEVERE, ex.getMessage(), ex);
            throw new DirectoryNotValidException(ex.getMessage());
        } catch (InterruptedException | ExecutionException | CloneNotSupportedException ex) {
            LOG.log(Level.SEVERE, null, ex);
            throw new AutomationExecutionException("Error while executing automation on palette");
        }
    }

    /**
     * <p>
     * Splits the main queue into subqueue according to the range.</p>
     *
     * @param toSplit the queue to split.
     * @param range the range of values in each subqueue to be created.
     * @return a subqueue based on the range.
     * @see #createContent()
     */
    private List<Palette> split(final Palette toSplit, final int range) throws CloneNotSupportedException, DirectoryNotValidException {
        List<Palette> list = new LinkedList<>();
        Palette subPalette;
        Object[] auxToSplit = toSplit.getFrames().toArray();
        Queue<Frame> subQueue;
        while (toSplit.getPaletteSize() != slotRangeControl) {
            subPalette = new Palette(toSplit.getDirectory());
            subQueue = new LinkedList<>();
            int limit = (slotRangeControl + range) <= auxToSplit.length ? (slotRangeControl + range) : auxToSplit.length;
            if (slotRangeControl != limit) {
                for (int i = this.slotRangeControl; i < limit; i++) {
                    Object object = auxToSplit[i];
                    subQueue.offer((Frame) object);
                }
            }
            slotRangeControl = (slotRangeControl + range) <= auxToSplit.length ? (slotRangeControl + range) : auxToSplit.length;
            subPalette.setFrames(subQueue);
            list.add(subPalette);
        }
        return list;
    }

    /**
     * Calculates the date and the time.
     *
     * @param datePattern the pattern to return the date
     * @return date and time according to the the datePattern
     */
    private String getDateTime(final String datePattern) {
        DateFormat dateFormat = new SimpleDateFormat(datePattern);
        Date date = new Date();
        return dateFormat.format(date);
    }

    /**
     *
     * @param result
     * @return the sum of total Of Eggs on a Palette
     */
    private String wrapProcess(String result) {
        List<String> content = reader.readContent(result + "\\" + Constants.REPORT_FILE_NAME + "." + FileExtension.CSV.getExtension());
        int totalOfEggs = 0;
        for (String line : content) {
            String information[] = line.split(Constants.OBJECT_SEPARATOR);
            String info = information[0];
            String splittedInfo[] = info.split(Constants.SEPARATOR);
            totalOfEggs += Integer.parseInt(splittedInfo[1]);
        }
        return Integer.toString(totalOfEggs);
    }

}
