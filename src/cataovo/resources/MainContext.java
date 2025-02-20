/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cataovo.resources;

import cataovo.entities.Frame;
import cataovo.entities.Palette;
import cataovo.exceptions.DirectoryNotValidException;
import cataovo.externals.UI.swing.wrappers.FileChooserUI;
import cataovo.externals.UI.swing.wrappers.TabbedPane;
import cataovo.utils.Constants;
import java.awt.Component;
import java.io.File;
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
    private final FileChooserUI fileChooserUI;
    private Palette palette;
    private Palette paletteToSave;
    private Frame currentFrame;
    private File savingFolder;
    private static volatile MainContext MAIN_PAGE_RESOURCES;
    private final PanelTabHelper panelTabHelper;
    // Fixar ordem dos relatórios: file[0] deve ser o relatório de contagem manual, file[1] deve ser o relatório de contagem automática
    private String[] reports;

    public MainContext() throws DirectoryNotValidException {
        String homeDirectory = FileSystemView.getFileSystemView().getHomeDirectory().getPath();
        LOG.log(Level.INFO, "Selecting home directory: {0}", homeDirectory);
        savingFolder = getFileFolder(new File(homeDirectory));
        fileChooserUI = new FileChooserUI(new File(homeDirectory));
        panelTabHelper = new PanelTabHelper(false, 0, "Manual");
    }

    public static MainContext getInstance() throws DirectoryNotValidException {
        MainContext PAGE_RESOURCES = MainContext.MAIN_PAGE_RESOURCES;
        if (PAGE_RESOURCES == null) {
            synchronized (MainContext.class) {
                PAGE_RESOURCES = MainContext.MAIN_PAGE_RESOURCES;
                if (PAGE_RESOURCES == null) {
                    MainContext.MAIN_PAGE_RESOURCES = PAGE_RESOURCES = new MainContext();
                }
            }
        }
        return PAGE_RESOURCES;
    }

    public File getSavingFolder() throws DirectoryNotValidException {
        return savingFolder;
    }

    public void setSavingFolder(File savingFolder) throws DirectoryNotValidException {
        this.savingFolder = getFileFolder(savingFolder);
    }

    public FileChooserUI getFileChooserUI() {
        return fileChooserUI;
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

    public void addReport(String report, int position) throws ArrayIndexOutOfBoundsException {
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

    public void adjustPanelTab(Component pane, boolean isActualTabProcessing) {
        TabbedPane tb = new TabbedPane(pane);
        panelTabHelper.setIsActualTabProcessing(isActualTabProcessing);
        panelTabHelper.setTabIndex(tb.getTabbedPane().getSelectedIndex());
        panelTabHelper.setTabName(tb.getTabbedPane().getTitleAt(tb.getTabbedPane().getSelectedIndex()));

    }

    public void resetSavingFolder() throws DirectoryNotValidException {
        this.savingFolder = getFileFolder(new File(FileSystemView.getFileSystemView().getHomeDirectory().getPath()));
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

}
