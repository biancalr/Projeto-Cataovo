/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cataovo.externals.libs.swinglib.wrappers;

import java.awt.Component;
import javax.swing.JTabbedPane;

/**
 *
 * @author Bianca Leopoldo Ramos
 */
public class TabbedPane {

    private JTabbedPane jTabbedPane;

    public TabbedPane(Component tabbedPane) {
        switch (tabbedPane) {
            case JTabbedPane jtp -> {
                this.jTabbedPane = jtp;
            }
            default ->
                throw new AssertionError("Tab Component not Supported Yet. Only JTabbedPane allowed");
        }
    }

    public JTabbedPane getTabbedPane() {
        return jTabbedPane;
    }

    public void setTabbedPane(Component tabbedPane) {
        switch (tabbedPane) {
            case JTabbedPane jtp -> {
                this.jTabbedPane = jtp;
            }
            default ->
                throw new AssertionError("Tab Component not Supported Yet. Only JTabbedPane allowed");
        }

    }

}
