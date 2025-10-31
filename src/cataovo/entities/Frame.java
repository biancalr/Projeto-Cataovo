/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cataovo.entities;

import cataovo.utils.Constants;
import cataovo.exceptions.ImageNotValidException;
import cataovo.utils.enums.FileExtension;
import java.io.File;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A frame represents a fraction of the various images that make up a palette.
 *
 * @author Bianca Leopoldo Ramos
 */
public final class Frame implements Cloneable, Serializable {

    /**
     * the frame identifier name
     */
    private String name;

    /**
     * The list of the regions wich indicates an egg of mosquito. A demarked
     * region must contain an egg in it
     */
    private Set<Region> regions;

    /**
     * The file of the frame;
     */
    private File paletteFrame;

    /**
     * Create a frame using the image path
     *
     * @param filePath the file path of this image
     * @throws cataovo.exceptions.ImageNotValidException if the file isn't a *.jpg or a *.png type
     */
    public Frame(String filePath) throws ImageNotValidException {
        if (fileIsAValidImageFrom(filePath)) {
            this.paletteFrame = new File(filePath);
            this.name = chopName(filePath);
            if (this.regions == null) {
                this.regions = new HashSet<>();
            }
        } else {
            throw new ImageNotValidException(Constants.ERROR_IMAGE_NOT_VALID_ENG_1);
        }

    }

    /**
     * Empty Constructor
     */
    public Frame() {
        this.regions = new HashSet<>();
        this.name = "";
    }

    /**
     *
     * @return the frame identifier name
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @param name a frame identifier name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     *
     * @return the regions with eggs in it
     */
    public Set<Region> getRegions() {
        return regions;
    }

    /**
     *
     * @param regions regions with eggs in it.
     */
    public void setRegions(Set<Region> regions) {
        this.regions = regions;
    }

    /**
     *
     * @return the palette frame File
     */
    public File getPaletteFrame() {
        return paletteFrame;
    }

    /**
     *
     * @param paletteFrame the palette frame to set
     */
    public void setPaletteFrame(File paletteFrame) {
        this.paletteFrame = paletteFrame;
    }

    /**
     * Calculates the total number of eggs in a frame.
     *
     * @return the quantity of eggs in a single frame if the list is not null or
     * Empty. 0 otherwise.
     */
    public int getTotalNumberOfEggsFrame() {
        if (this.regions == null || regions.isEmpty()) {
            return 0;
        } else {
            return regions.size();
        }
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append(",")
                .append(name)
                .append(regions.toString());
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

        Frame frame = (Frame) obj;
        return Objects.equals(frame.name, name)
                && Objects.equals(frame.regions, regions);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 11 * hash + Objects.hashCode(this.name);
        hash = 11 * hash + Objects.hashCode(this.regions);
        return hash;
    }

    /**
     *
     * @param pathName
     * @return true if the image is valid. False otherwise.
     * @throws ImageNotValidException if the image does not exists, is not a
     * file or is not a PNG/JPG image file.
     */
    private boolean fileIsAValidImageFrom(String pathName) throws ImageNotValidException {
        File f = new File(pathName);
        String fileExtension;

        fileExtension = getFileExtension(f);
        // Verify if this file exists, is a file and is a .png file type
        return (f.exists() && f.isFile() && (fileExtension.contains(FileExtension.PNG.getExtension()) || fileExtension.contains(FileExtension.JPG.getExtension())));
    }

    /**
     *
     * @param f the File
     * @return the file Extension
     */
    private String getFileExtension(File f) {
        String fileExtension;
        // Verify which is the file extension. Only png is valid.
        if (f.getAbsolutePath().substring(f.getAbsolutePath().indexOf(".")) != null) {
            fileExtension = f.getAbsolutePath().substring(f.getAbsolutePath().indexOf(".") + 1);
        } else {
            fileExtension = "NONE";
        }
        return fileExtension;
    }

    /**
     * Chopps the name to extract the image name.
     *
     * @param filePath the file path to extract the name tag
     * @return the name tag chopped
     */
    private String chopName(String filePath) {
        return filePath.substring(filePath.lastIndexOf("\\") + 1, filePath.lastIndexOf("."));
    }

    @Override
    public Frame clone() throws CloneNotSupportedException {
        return (Frame) super.clone(); //To change body of generated methods, choose Tools | Templates.
    }

}
