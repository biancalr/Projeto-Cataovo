
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cataovo.externals.UI.swing.controllers.implement;

import cataovo.utils.Constants;
import cataovo.entities.Frame;
import cataovo.entities.Point;
import cataovo.exceptions.DirectoryNotValidException;
import cataovo.exceptions.ImageNotValidException;
import cataovo.wrappers.UI.TabbedPane;
import cataovo.resources.MainContext;
import java.awt.Color;
import java.awt.Component;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import cataovo.externals.UI.swing.controllers.PageController;

/**
 * Controls how the Frames are seen and iterated.
 *
 * @author Bianca Leopoldo Ramos
 */
public class PageControllerImplements implements PageController {

    private static final Logger LOG = Logger.getLogger(PageControllerImplements.class.getName());
    private int frameCounter = 0;

    public PageControllerImplements() {
        frameCounter = 0;
    }

    /**
     * Heads to the next frame in Manual.
     *
     * @param parentName
     * @param parent
     * @throws ImageNotValidException
     * @throws DirectoryNotValidException
     */
    @Override
    public void onNextFrameInManual(JLabel parentName, JLabel parent) throws ImageNotValidException, DirectoryNotValidException, AssertionError {
        if (!MainContext.getInstance().getPalette().getFrames().isEmpty()) {
            Frame frame = MainContext.getInstance().getPalette().getFrames().poll();
            MainContext.getInstance().setCurrentFrame(frame);
            showFrameOnScreen(parentName, parent, frame);
        } else {
            LOG.log(Level.INFO, "The Palette was completed!");
            parentName.setText(Constants.NO_PALETTE_SELECTED);
            parent.setIcon(null);
            parent.setBorder(null);
        }

    }

    @Override
    public int onPreviousFrameInEvaluation(JLabel parentName, JLabel parent, File paletteDirectory, String[] reports) throws DirectoryNotValidException, ImageNotValidException {
        if (isReportValid(reports, paletteDirectory.getName())) { //verifica senão foi selecionada uma paleta diferente
            File[] frameResults = paletteDirectory.listFiles((pathname) -> pathname.isFile());
            LOG.log(Level.INFO, "Palette Size: ", String.valueOf(frameResults.length));
            if (frameResults.length > 0) {
                if (this.frameCounter > 0) {
                    this.frameCounter--;
                    setCurrentFrameOnEvaluationScreen(frameResults[this.frameCounter], parentName, parent);
                    return this.frameCounter;
                } else {
                    this.frameCounter = 0;
                    throw new ArrayIndexOutOfBoundsException("Atingiu o início da paleta");
                }
            }
        } else {
            throw new DirectoryNotValidException("The current Palette " + paletteDirectory.getName() + " couldn't be verified");

        }
        return -1;
    }

    /**
     *
     * @param frameResult
     * @param parentName
     * @param parent
     * @throws ImageNotValidException
     * @throws DirectoryNotValidException
     */
    private void setCurrentFrameOnEvaluationScreen(File frameResult, JLabel parentName, JLabel parent) throws ImageNotValidException, DirectoryNotValidException {
        Frame current = new Frame(frameResult.getAbsolutePath());
        MainContext.getInstance().setCurrentFrame(current);
        showFrameOnScreen(parentName, parent, current);
        LOG.log(Level.INFO, "Image Position: {0}", this.frameCounter);
    }

    /**
     *
     * @param parentName
     * @param parent
     * @param paletteDirectory
     * @param reports
     * @throws DirectoryNotValidException
     * @throws cataovo.exceptions.ImageNotValidException
     */
    @Override
    public void onNextFrameInEvaluation(JLabel parentName, JLabel parent, File paletteDirectory, String[] reports) throws DirectoryNotValidException, ImageNotValidException {
        if (isReportValid(reports, paletteDirectory.getName())) { //verifica senão foi selecionada uma paleta diferente
            File[] frameResults = paletteDirectory.listFiles((pathname) -> pathname.isFile());
            LOG.log(Level.INFO, "Tamanho da paleta: {0}", String.valueOf(frameResults.length));
            if (frameResults.length > 0) {
                if (this.frameCounter < (frameResults.length - 1)) {
                    this.frameCounter++;
                    setCurrentFrameOnEvaluationScreen(frameResults[this.frameCounter], parentName, parent);
                } else {
                    this.frameCounter = frameResults.length - 1;
                    throw new ArrayIndexOutOfBoundsException("Reached the end of the palette " + paletteDirectory.getName());
                }
            } else {
                throw new DirectoryNotValidException("The current Palette " + paletteDirectory.getName() + " couldn't be verified");
            }
        }
    }

    /**
     *
     * @param parentName
     * @param jTabbedPane
     * @throws ImageNotValidException
     * @throws DirectoryNotValidException
     */
    @Override
    public void onNextFrameInAutomatic(JLabel parentName, Component jTabbedPane, File paletteSavingFolder, String paletteName) throws ImageNotValidException, DirectoryNotValidException, ArrayIndexOutOfBoundsException {
        // Verificar as palavras-chave que formam o caminho de um diretório do processamento automático no projeto: (nome da paleta) e "auto". 
        if (!paletteSavingFolder.getAbsolutePath().contains(paletteName)
                || !paletteSavingFolder.getAbsolutePath().contains("auto")) {
            throw new DirectoryNotValidException("The current Palette " + paletteName + " couldn't be verified");
        } else {
            // Listando os diretórios correspondentes a cada frame
            File[] frameResults = paletteSavingFolder.listFiles((pathname) -> pathname.isDirectory());
            if (frameResults.length > 0) {
                if (this.frameCounter < (frameResults.length - 1)) {
                    this.frameCounter++;
                    // Buscar o diretório correspondente ao frame atual
                    File currentFrameDirectory = frameResults[this.frameCounter];
                    Frame current = putFileOnFrame(jTabbedPane, currentFrameDirectory, parentName);
                    MainContext.getInstance().setCurrentFrame(current);
                    setLabelText(parentName, currentFrameDirectory.getAbsolutePath().substring(currentFrameDirectory.getAbsolutePath().indexOf(Constants.FRAME_ID_TAG) + Constants.FRAME_ID_TAG.length()));
                    LOG.log(Level.INFO, "Image Position: {0}", this.frameCounter);
                } else {
                    this.frameCounter = frameResults.length - 1;
                    throw new ArrayIndexOutOfBoundsException("Reached the end of the Palette " + paletteName);
                }
            } else {
                throw new DirectoryNotValidException("Couldn't match the folders and the frames tag names (size = " + frameResults.length + ")");
            }
        }
    }

    @Override
    public void onPreviousFrameInAutomatic(JLabel parentName, Component jTabbedPane, File savingFolder, String paletteDirectoryName) throws ImageNotValidException, DirectoryNotValidException, ArrayIndexOutOfBoundsException {
        // Verificar as palavras-chave que formam o caminho de um diretório do processamento automático no projeto: (nome da paleta) e "auto". 
        if (!savingFolder.getAbsolutePath().contains(paletteDirectoryName)
                || !savingFolder.getAbsolutePath().contains("auto")) {
            throw new DirectoryNotValidException("The current Palette " + paletteDirectoryName + " couldn't be verified");
        } else {
            // Listando os diretórios correspondentes a cada frame
            File[] frameResults = savingFolder.listFiles((pathname) -> pathname.isDirectory());
            if (frameResults.length > 0) {
                if (this.frameCounter > 0) {
                    this.frameCounter--;
                    // Buscar o diretório correspondente ao frame atual
                    File currentFrameDirectory = frameResults[this.frameCounter];
                    Frame current = putFileOnFrame(jTabbedPane, currentFrameDirectory, parentName);
                    MainContext.getInstance().setCurrentFrame(current);
                    setLabelText(parentName, currentFrameDirectory.getAbsolutePath().substring(currentFrameDirectory.getAbsolutePath().indexOf(Constants.FRAME_ID_TAG) + Constants.FRAME_ID_TAG.length()));
                    LOG.log(Level.INFO, "Image Position: {0}", this.frameCounter);
                } else {
                    this.frameCounter = 0;
                    throw new ArrayIndexOutOfBoundsException("Reached the beggining of the palette " + paletteDirectoryName);
                }
            } else {
                throw new DirectoryNotValidException("Couldn't match the folders and the frames tag names (size = " + frameResults.length + ")");
            }
        }
    }

    /**
     *
     * @param tabbedPane
     * @param currentFrameDirectory
     * @param current
     * @param parentName
     * @return
     * @throws ImageNotValidException
     */
    private Frame putFileOnFrame(Component tabbedPane, File currentFrameDirectory, JLabel parentName) throws ImageNotValidException {
        TabbedPane pane = new TabbedPane(tabbedPane);
        // Recuperar as imagens resultantes e inserí-las nas abas correspondentes
        Frame current = new Frame();
        for (int i = 0; i < pane.getTabbedPane().getComponents().length; i++) {
            Component component = pane.getTabbedPane().getComponents()[i];
            Frame frame = new Frame(currentFrameDirectory.listFiles()[i].getAbsolutePath());
            if (frame.getName().equalsIgnoreCase(Constants.FRAME_ORIGINAL_NAME_TAG)) {
                current = frame;
            }
            // Posicionar as imagens nos componentes
            JLabel jlabel = (JLabel) component.getComponentAt(0, 0);
            presentImageFrameOnScreen(jlabel, parentName, frame);
        }
        return current;
    }

    /**
     * Setts the icons related to each JLabel (image or name)
     *
     * @param parentName
     * @param parent
     * @param frame
     * @return
     */
    @Override
    public boolean showFrameOnScreen(JLabel parentName, JLabel parent, Object frame) throws ImageNotValidException {
        switch (frame) {
            case Frame fr -> {
                LOG.log(Level.INFO, "Presenting the image {0} on screen...", fr.getName());
                if (presentImageFrameOnScreen(parent, parentName, fr)) {
                    return true;
                }
            }
            case Icon icon -> {
                presentImageIconOnScreen(parent, icon);
                return true;
            }
            case default -> {
            }
        }

        return false;
    }

    /**
     *
     * @param parent
     * @param icon
     */
    private void presentImageIconOnScreen(JLabel parent, Icon icon) {
        parent.setIcon(icon);
    }

    /**
     *
     * @param parent
     * @param parentName
     * @param fr
     * @return
     */
    private boolean presentImageFrameOnScreen(JLabel parent, JLabel parentName, Frame fr) {
        if (fr.getPaletteFrame() != null && fr.getPaletteFrame().exists()) {
            File f = (File) fr.getPaletteFrame();
            parent.setIcon(showImageFile(f));
            setLabelText(parentName, fr.getName());
            return true;
        }
        return false;
    }

    /**
     *
     * @param parentName
     */
    private void setLabelText(JLabel parentName, String text) {
        if (text.contains(Constants.FRAME_ID_TAG)) {
            text = text.replace(Constants.FRAME_ID_TAG, "");
        }
        parentName.setText(text);
    }

    /**
     * Receives a file and shows its image at jLabelImage.
     *
     * @param file the File image
     */
    private ImageIcon showImageFile(File file) {
        return new ImageIcon(file.getPath());
    }

    /**
     * When a Frame was finished its analysis, go to next Frame on Queue.
     *
     * @param parentName
     * @param parent
     * @throws cataovo.exceptions.ImageNotValidException
     * @throws cataovo.exceptions.DirectoryNotValidException
     */
    @Override
    public void onFrameFinishedManual(JLabel parentName, JLabel parent, Frame currentFrame) throws ImageNotValidException, DirectoryNotValidException {
        LOG.log(Level.INFO, "The frame was analysed. Charging next...");
        MainContext.getInstance().getPaletteToSave().getFrames()
                .offer(currentFrame);
        onNextFrameInManual(parentName, parent);
    }

    /**
     * From a given tab, it shows the frames selected
     *
     * @param tabComponent
     * @param parentNameLabel
     * @param parentLabel
     * @throws ImageNotValidException
     * @throws DirectoryNotValidException
     */
    @Override
    public void showFramesOnSelectedTabScreen(Component tabComponent, JLabel parentNameLabel, JLabel parentLabel, Object frame) throws ImageNotValidException, DirectoryNotValidException, UnsupportedOperationException, AssertionError {
        TabbedPane pane = new TabbedPane(tabComponent);
        this.frameCounter = -1;
        switch (pane.getTabbedPane().getSelectedIndex()) {
            case 0 -> {
                parentLabel.setText(null);
                parentLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                showFrameOnScreen(parentNameLabel, parentLabel, frame);
            }
            case 1 -> {
                showFrameOnScreen(parentNameLabel, parentLabel, frame);
            }
            case 2 -> {
                parentLabel.setText(null);
                parentLabel.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
                showFrameOnScreen(parentNameLabel, parentLabel, frame);
            }
            default ->
                throw new AssertionError("No tab with such index");
        }

    }

    @Override
    public void showSubFrameOnSelectedTabScreen(JLabel subframeNameLabel, JLabel subframeLabel, Icon frame, Point point) throws DirectoryNotValidException {
        subframeLabel.setText(null);
        presentImageIconOnScreen(subframeLabel, frame);
        String text = "(" + point.getX() + ", " + point.getY() + ")";
        setLabelText(subframeNameLabel, text);
    }

    /**
     *
     * @return @throws DirectoryNotValidException
     */
    private boolean isReportValid(String[] reports, String directoryName) throws DirectoryNotValidException {
        for (String report : reports) {
            if (!report.contains(directoryName)) {
                return false;
            }
        }
        return true;
    }

}
