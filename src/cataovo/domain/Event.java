/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cataovo.domain;

/**
 *
 * @author bianc
 */
public class Event {

    private final String folder;
    private String result;

    public Event(String folder) {
        this.folder = folder;
    }

    public Event(String folder, String result) {
        this.folder = folder;
        this.result = result;
    }

    public String getFolder() {
        return folder;
    }

    public String getResult() {
        return result;
    }
}
