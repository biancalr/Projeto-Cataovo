/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cataovo.controllers;

/**
 *
 * @author Bianca Leopoldo Ramos
 */
public interface ManualProcessorController {
    
    /**
     * Triggers the manual processment.
     * 
     * @param processingTabName
     * @param savingDirectory
     * @return 
     */
    public String onNewManualProcessPalette(final String processingTabName, final String savingDirectory);
    
}
