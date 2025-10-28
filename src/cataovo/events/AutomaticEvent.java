/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package cataovo.events;

import cataovo.domain.Event;
import cataovo.entities.Palette;
import cataovo.exceptions.AutomationExecutionException;
import cataovo.exceptions.DirectoryNotValidException;

/**
 * Controls the automatic processment.
 *
 * @author Bianca Leopoldo Ramos
 */
public interface AutomaticEvent {
       
    /**
     * Starts the automatic processing of the {@link cataovo.entities.Palette} as a {@link java.util.concurrent.Callable}.
     * 
     * @param currentPalette
     * @param savingFolderPath
     * @param tabName
     * @return the final relatory's path
     * @throws cataovo.exceptions.DirectoryNotValidException
     * @throws cataovo.exceptions.AutomationExecutionException
     */
    public Event execute(Palette currentPalette, String savingFolderPath, String tabName) throws DirectoryNotValidException, AutomationExecutionException;
    
}
