/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cataovo.events;

import cataovo.domain.Event;

/**
 *
 * @author Bianca Leopoldo Ramos
 */
public interface ManualEvent {
    
    /**
     * Triggers the manual processment.
     * 
     * @param processingTabName
     * @param savingDirectory
     * @return 
     */
    public Event execute(final String processingTabName, final String savingDirectory);
    
}
