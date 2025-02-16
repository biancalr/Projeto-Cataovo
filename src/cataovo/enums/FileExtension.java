/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cataovo.enums;

/**
 *
 * @author Bianca Leopoldo Ramos
 */
public enum FileExtension {
    
    PNG("png"), CSV("csv"), JPG("jpg");

    /**
     * The value of the extension to work with.
     */
    private String extension;
    
    /**
     * 
     * @param extension the value of the extension to work with
     */
    private FileExtension(String extension) {
        this.extension = extension;
    }

    /**
     * 
     * @return the extension selected to work with
     */
    public String getExtension() {
        return extension;
    }

    /**
     * 
     * @param extension 
     */
    public void setExtension(String extension) {
        this.extension = extension;
    }
    
}
