/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cataovo.resources;

/**
 * Responsable to support the tab changing.
 * 
 * @author Bianca Leopoldo Ramos
 */
public class PanelTabHelper {
    
    private boolean currentTabProcessing;
    private int tabIndex;
    private String tabName;

    public PanelTabHelper(boolean isActualTabProcessing, int tabIndex, String tabName) {
        this.currentTabProcessing = isActualTabProcessing;
        this.tabIndex = tabIndex;
        this.tabName = tabName;
    }

    public PanelTabHelper() {
    }

    public boolean isCurrentTabProcessing() {
        return currentTabProcessing;
    }

    public void setCurrentTabProcessing(boolean currentTabProcessing) {
        this.currentTabProcessing = currentTabProcessing;
    }

    public int getTabIndex() {
        return tabIndex;
    }

    public void setTabIndex(int tabIndex) {
        this.tabIndex = tabIndex;
    }

    public String getTabName() {
        return tabName;
    }

    public void setTabName(String tabName) {
        this.tabName = tabName;
    }

    
    
}
