/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package cataovo.utils.fileUtils.readers;

import java.util.List;
import java.util.Optional;

/**
 *
 * @author Bianca Leopoldo Ramos
 */
public interface Reader {
    
    /**
     * 
     * @param lineToFind
     * @param report
     * @return 
     */
    public Optional<String> readLine(String lineToFind, String report);
    
    /**
     * 
     * @param report
     * @return 
     */
    public List<String> readContent(String report);
    
}
