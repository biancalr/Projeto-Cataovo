/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cataovo.resources;

import cataovo.entities.Frame;
import cataovo.entities.Palette;
import cataovo.exceptions.DirectoryNotValidException;
import cataovo.exceptions.ImageNotValidException;
import cataovo.exceptions.ReportNotValidException;
import cataovo.resources.fileChooser.handler.FileListHandler;
import cataovo.utils.Constants;
import cataovo.utils.enums.FileExtension;
import cataovo.utils.enums.ProcessingMode;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Queue;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.filechooser.FileSystemView;

/**
 * Resources used in the applications as a whole.
 *
 * @author Bianca Leopoldo Ramos
 */
public class MainContext {

    private static final Logger LOG = Logger.getLogger(MainContext.class.getName());
    private Palette palette;
    private Palette paletteToSave;
    private Frame currentFrame;
    private final PanelTabHelper panelTabHelper;
    // Fixar ordem dos relatórios: file[0] deve ser o relatório de contagem manual, file[1] deve ser o relatório de contagem automática
    private String[] reports;
    private String homeDir = null;


    public MainContext(final String homeDir) throws DirectoryNotValidException {
        this.homeDir = homeDir;
        panelTabHelper = new PanelTabHelper(false, 0, "Manual");
    }

    public String getHomeDir() {
        return homeDir;
    }

    public Palette getPalette() {
        if (this.palette == null) {
            this.palette = new Palette();
        }
        return palette;
    }

    public void setPalette(Palette palette) {
        if (this.palette == null) {
            this.palette = new Palette();
        }
        this.palette = palette;
    }

    public Palette getPaletteToSave() {
        if (this.paletteToSave == null) {
            this.paletteToSave = new Palette();
        }
        return paletteToSave;
    }

    public void setPaletteToSave(Palette paletteToSave) {
        if (this.paletteToSave == null) {
            this.paletteToSave = new Palette();
        }
        this.paletteToSave = paletteToSave;
    }

    public Frame getCurrentFrame() {
        if (this.currentFrame == null) {
            this.currentFrame = new Frame();
        }
        return currentFrame;
    }

    public void setCurrentFrame(Frame currentFrame) {
        if (this.currentFrame == null) {
            this.currentFrame = new Frame();
        }
        this.currentFrame = currentFrame;
    }

    public PanelTabHelper getPanelTabHelper() {
        return panelTabHelper;
    }

    public String[] getReports() {
        if (this.reports == null) {
            this.reports = new String[2];
        }
        return reports;
    }

    public void setReports(String[] reports) {
        if (this.reports == null) {
            this.reports = new String[2];
        }
        this.reports = reports;
    }

    private void addReport(String report, int position) throws ArrayIndexOutOfBoundsException {
        if (this.reports == null) {
            this.reports = new String[2];
        }
        if (position < reports.length) {
            if (reports[position] == null) {
                reports[position] = report;
                LOG.log(Level.INFO, "Selecting the report: {0}", report);
            } else {
                LOG.log(Level.WARNING, Constants.POSITION_OCCUPIED_EN);
                throw new ArrayIndexOutOfBoundsException(Constants.POSITION_OCCUPIED_EN);
            }
        } else {
            LOG.log(Level.WARNING, Constants.REPORTS_ALREADY_SELECTED_PT_BR);
            throw new ArrayIndexOutOfBoundsException(Constants.REPORTS_ALREADY_SELECTED_PT_BR);
        }
    }

    public void updateTabHelper(final int tabIndex, final String tabTitle, final boolean isCurrentTabProcessing) {
        panelTabHelper.setCurrentTabProcessing(isCurrentTabProcessing);
        panelTabHelper.setTabIndex(tabIndex);
        panelTabHelper.setTabName(tabTitle);

    }

    public final File getFileFolder(File file) throws DirectoryNotValidException {
        if (!file.exists()) {
            LOG.log(Level.SEVERE, "This method needs an existing file. The parameter cannot be null or inexistent");
            throw new DirectoryNotValidException("This method needs an existing file. The parameter cannot be null or inexistent");
        } else {
            if (file.isDirectory()) {
                LOG.log(Level.INFO, "Selecting a folder: {0}", file);
                return file;
            } else {
                LOG.log(Level.INFO, "Selecting the folder: {}", file);
                String selected = file.getAbsolutePath().substring(0, file.getAbsolutePath().lastIndexOf("\\") - 1);
                return new File(selected);
            }
        }
    }

    /**
     * Set the file to a Palette. Verify if the file is a valid one.
     *
     * @param selectedFile
     * @throws DirectoryNotValidException
     * @throws cataovo.exceptions.ImageNotValidException
     * @throws java.io.FileNotFoundException
     */
    public void choosePalette(File selectedFile) throws DirectoryNotValidException, ImageNotValidException, FileNotFoundException {
        LOG.log(Level.INFO, "Setting a new Palette...");
        final Palette pt;
        if (selectedFile.exists()) {
            if (selectedFile.isDirectory()) {
                pt = new Palette(selectedFile);
                pt.setFrames(setPaletteFrames(selectedFile.listFiles()));
                this.currentFrame = pt.getFrames().peek();
                this.paletteToSave = new Palette(pt.getDirectory());
                this.palette = pt;
            } else {
                throw new DirectoryNotValidException("The selected file is not a directory. Please, choose a directory.");
            }
            LOG.info(this.palette.toString());
            LOG.log(Level.INFO, "A new Palette was created with the amount of frames: {0}", this.palette.getFrames().size());

        } else {
            LOG.log(Level.WARNING, "The selected file doesn't exist. Please, select an existing file.");
            throw new FileNotFoundException("The selected file doesn't exist. Please, select an existing file.");
        }

    }

    /**
     * Set the Frames in a Palette. When a Palette is chosen, their frames must
     * be presented as a Queue. If the chosen file is a directory, there might
     * be nested directories. So these images must be normalized to a sigle
     * queue.
     *
     *
     * @param listFiles
     * @return
     */
    private Queue<Frame> setPaletteFrames(File[] listFiles) throws DirectoryNotValidException, ImageNotValidException {
        Queue<Frame> frames = new LinkedList<>();
        Collection<File> colection = new FileListHandler<File>().normalizeFiles(listFiles, FileExtension.PNG);
        Frame frame;
        for (File file1 : colection) {
            frame = new Frame(file1.getPath());
            frame.setName(Constants.FRAME_ID_TAG + frame.getName());
            frames.add(frame);
            LOG.log(Level.INFO, "Adding following frame: {0}", frame.getName());
        }
        return frames;
    }

    /**
     *
     * @param file
     * @throws ReportNotValidException
     * @throws DirectoryNotValidException
     */
    public void setReportOrdered(File file) throws ReportNotValidException, DirectoryNotValidException {
        // Fixar ordem dos relatórios: o primeiro deve ser o relatório de contagem manual
        // verificar se o primeiro relatório corresponde ao relatório da contagem manual
        // Receber os relatórios em qualquer ordem no array na posição correta
        int position = 0;
        if (file.getAbsolutePath().contains(ProcessingMode.MANUAL.getProcessingMode())) {
            position = 0;
        } else if (file.getAbsolutePath().contains(ProcessingMode.AUTOMATIC.getProcessingMode())) {
            position = 1;
        } else {
            throw new ReportNotValidException("Report not properly selected");
        }
        addReport(file.getPath(), position);

    }

    /**
     * Buscar paleta baseado nos caminhos dos arquivos nos relatórios.
     *
     * @param paletteDirectoryOnManual
     * @param paletteDirectoryOnAuto
     * @return
     * @throws DirectoryNotValidException
     * @throws cataovo.exceptions.ImageNotValidException
     */
    public Palette getPalettDirByReport(String paletteDirectoryOnManual, String paletteDirectoryOnAuto) throws DirectoryNotValidException, ImageNotValidException {
        if (new File(paletteDirectoryOnManual).exists() && new File(paletteDirectoryOnAuto).exists()) {
            final String reportName = paletteDirectoryOnManual.substring(paletteDirectoryOnManual.lastIndexOf("\\"), paletteDirectoryOnManual.length());
            if (paletteDirectoryOnAuto.contains(reportName)) {
                Palette currentPalette = new Palette(paletteDirectoryOnAuto);
                File frame = currentPalette.getDirectory().listFiles()[0];
                this.setCurrentFrame(new Frame(frame.getAbsolutePath()));
                return currentPalette;
            } else {
                throw new DirectoryNotValidException("The reports are not from the same Palette");
            }
        } else {
            throw new DirectoryNotValidException("One or more reports do not exist");
        }
    }
}
