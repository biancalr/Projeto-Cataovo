/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cataovo.entities;

import cataovo.constants.Constants;
import cataovo.exceptions.DirectoryNotValidException;
import java.io.File;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.Objects;
import java.util.Queue;

/**
 * Representation of an ovitrap palette, composed of several frames.
 *
 * @author Bianca Leopoldo Ramos
 */
public final class Palette implements Cloneable, Serializable {

    /**
     * The path where the palette is localized.
     */
    private String pathName;
    /**
     * The frames that composes a palette.
     */
    private Queue<Frame> frames;

    /**
     * The the directory as a file;
     */
    private File directory;

    /**
     * Create a Palette using the folder path
     *
     * @param pathName The path where the palette is localized. <strong>The file
     * has to be a directory</strong>
     * @throws cataovo.exceptions.DirectoryNotValidException if the file is not
     * a directory.
     */
    public Palette(String pathName) throws DirectoryNotValidException {
        if (verifyPathIsDirectory(pathName)) {
            this.pathName = pathName;
            this.frames = new LinkedList<>();
            this.directory = new File(pathName);
        } else {
            throw new DirectoryNotValidException(Constants.ERROR_DIRECTORY_NOT_VALID_ENG_1);
        }
    }

    /**
     * Create a Palette using the folder as a file
     *
     * @param file
     * @throws cataovo.exceptions.DirectoryNotValidException if the file is not
     * a directory.
     */
    public Palette(File file) throws DirectoryNotValidException {
        if (verifyPathIsDirectory(file.getAbsolutePath())) {
            this.pathName = file.getAbsolutePath();
            this.frames = new LinkedList<>();
            this.directory = file;
        } else {
            throw new DirectoryNotValidException(Constants.ERROR_DIRECTORY_NOT_VALID_ENG_1);
        }
    }

    /**
     * Empty constructor
     */
    public Palette() {
        directory = null;
        if (this.frames == null) {
            this.frames = new LinkedList<>();
        }
    }

    /**
     *
     * @return The path where the palette is localized.
     */
    public String getPathName() {
        return pathName;
    }

    /**
     *
     * @param pathName a path where a palette is localized.
     */
    public void setPathName(String pathName) {
        this.pathName = pathName;
    }

    /**
     *
     * @return The frames that composes a palette.
     */
    public Queue<Frame> getFrames() {
        return frames;
    }

    /**
     *
     * @param frames some frames that composes a palette.
     */
    public void setFrames(Queue<Frame> frames) {
        this.frames = frames;
    }

    /**
     *
     * @return the directory
     */
    public File getDirectory() {
        return directory;
    }

    /**
     *
     * @param directory the directory to be setted
     */
    public void setDirectory(File directory) {
        this.directory = directory;
    }

    /**
     * Calculates the total quantity of eggs in a pallete.
     *
     * @return the total number of eggs in a Palette
     */
    public int getTheTotalNumberOfEggsPalette() {
        int total = 0;
        if (frames == null || frames.isEmpty()) {
            total = 0;
        } else {
            total = frames.stream().map((frame) -> frame.getTotalNumberOfEggsFrame()).reduce(total, Integer::sum);
        }
        return total;
    }

    /**
     *
     * @return
     */
    public int getPaletteSize() {
        if (frames != null || !frames.isEmpty()) {
            return frames.size();
        }
        return 0;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append(pathName)
                .append(",")
                .append(getTheTotalNumberOfEggsPalette())
                .append(frames.toString());
        return sb.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        Palette pal = (Palette) obj;
        return Objects.equals(pathName, pal.pathName)
                && Objects.equals(frames, pal.frames);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + Objects.hashCode(this.pathName);
        hash = 97 * hash + Objects.hashCode(this.frames);
        return hash;
    }

    /**
     *
     * @param pathName
     * @return
     */
    private boolean verifyPathIsDirectory(String pathName) {
        File f = new File(pathName);
        return f.exists() && f.isDirectory();
    }

    @Override
    public Palette clone() throws CloneNotSupportedException {
        return (Palette) super.clone(); //To change body of generated methods, choose Tools | Templates.
    }

}
