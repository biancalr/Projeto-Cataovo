/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cataovo.utils.fileUtils.readers.csv.csvReader;

import cataovo.utils.constants.Constants;
import cataovo.utils.fileUtils.readers.Reader;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Bianca leopoldo Ramos
 */
public class CsvFileReader implements Reader{

    private static final Logger LOG = Logger.getLogger(CsvFileReader.class.getName());

    @Override
    public Optional<String> readLine(String lineToFind, String report) {
        Optional<String> line = null;
        try (InputStreamReader in = new InputStreamReader(new FileInputStream(report)); BufferedReader csvReader = new BufferedReader(in)) {

            line = csvReader.lines().filter((l) 
                    -> l.contains(lineToFind))
                    .findFirst();

        } catch (FileNotFoundException ex) {
            LOG.log(Level.SEVERE, ex.getMessage(), ex);
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, ex.getMessage(), ex);
        }
        return line;
    }
    
    @Override
    public List<String> readContent(String report) {
        List<String> content = null;
        
        try (InputStreamReader in = new InputStreamReader(new FileInputStream(report)); BufferedReader csvReader = new BufferedReader(in)) {
            
            content = csvReader.lines().filter((line) -> !line.contains(Constants.ROOT_FOLDER) && line.length() > 5 && !line.isBlank()).toList();
            
        } catch (FileNotFoundException ex) {
            LOG.log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, null, ex);
        }
        
        return content;
    }

}
