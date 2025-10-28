/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package cataovo.events;

import cataovo.entities.Palette;
import cataovo.domain.Event;
import cataovo.utils.enums.FileExtension;

/**
 *
 * @author Bianca Leopoldo Ramos
 */
public interface EvaluationEvent {

    /**
     *
     * @param fileContentManual
     * @param fileContentAuto
     * @param evaluateByPixel
     * @param palette
     * @param savingDirectory
     * @param fileExtension
     * @param tabName
     * @return
     */
    public Event execute(String fileContentManual, String fileContentAuto, boolean evaluateByPixel, final Palette palette, final String savingDirectory, final FileExtension fileExtension, final String tabName);
}
