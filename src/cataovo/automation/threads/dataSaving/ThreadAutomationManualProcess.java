/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cataovo.automation.threads.dataSaving;

import cataovo.utils.constants.Constants;
import cataovo.entities.Palette;
import cataovo.utils.enums.FileExtension;

/**
 *
 * @author Bianca Leopoldo Ramos
 */
public final class ThreadAutomationManualProcess extends DataSavingThreadAutomation{

    /**
     * <p>
     * The thread starts the processing of each frame of a palette.</p>
     *
     * @param palette the palette to be processed
     * @param savingDirectory the directory where the results will be saved.
     * @param fileExtension referes to the relatory's file extension where the text
     * data will be saved.
     * @param tabName relates the tabName to the type of processing of a
     * palette: Manual or Automatic. Also helps to create folders of each
     * processing type.
     * @param dateTime the date and time when the process begun to name the relatory
     */
    public ThreadAutomationManualProcess(final Palette palette, final String savingDirectory, final FileExtension fileExtension, final String tabName, final String dateTime) {
        super(palette, savingDirectory, fileExtension, tabName, dateTime);
    }

    @Override
    protected StringBuffer createContent() {
        StringBuffer sb = new StringBuffer(getPalette().getDirectory().getPath());
        sb.append(Constants.QUEBRA_LINHA);
        sb.append(getPalette().getTheTotalNumberOfEggsPalette());
        getPalette().getFrames().stream().forEach((f) -> {
            sb.append(Constants.QUEBRA_LINHA);
            sb.append(f.getName());
            if (!f.getRegionsContainingEggs().isEmpty()) {
                f.getRegionsContainingEggs().stream().map((r) -> {
                    sb.append(",");
                    sb.append(r.getInitialPoint().getX());
                    return r;
                }).map((r) -> {
                    sb.append(",");
                    sb.append(r.getInitialPoint().getY());
                    return r;
                }).map((r) -> {
                    sb.append(",");
                    sb.append(r.getWidth());
                    return r;
                }).forEachOrdered((r) -> {
                    sb.append(",");
                    sb.append(r.getHeight());
                });
            }
        });
        return sb;
    }
}
