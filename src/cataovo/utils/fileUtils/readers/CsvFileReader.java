/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cataovo.utils.fileUtils.readers;

import cataovo.entities.Point;
import cataovo.entities.Region;
import cataovo.exceptions.DirectoryNotValidException;
import cataovo.utils.Constants;
import cataovo.utils.enums.ProcessingMode;
import cataovo.wrappers.opencv.PointWrapper;
import cataovo.wrappers.opencv.RectWrapper;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 *
 * @author Bianca Leopoldo Ramos
 */
public class CsvFileReader {

    private static final Logger LOG = Logger.getLogger(CsvFileReader.class.getName());

    public CsvFileReader() {
    }

    public List<RectWrapper> getRegionsInFrameFile(String frameName, String report) throws FileNotFoundException, NumberFormatException {
        List<RectWrapper> regions = new ArrayList<>();

        try {
            Optional<String> optLine = readLine(frameName, report);

            optLine.ifPresent(line -> {

                LOG.log(Level.INFO, "LINE: {0}", optLine.get());

                String[] data = line.split(Constants.SEPARATOR);
                if (data.length > 2) {
                    // ignorando posição 0 pois representa o nome do frame, o qual não é necessário
                    for (int i = 1; i < data.length; i += 4) {
                        String string = data[i];
                        if (string != null) {
                            RectWrapper current = new RectWrapper(
                                    new Region(
                                            new Point(
                                                    Integer.parseInt(data[i]),
                                                    Integer.parseInt(data[i + 1])), Integer.parseInt(data[i + 2]), Integer.parseInt(data[i + 3])));
                            regions.add(current);
                        }
                    }
                }

            });

        } catch (NumberFormatException ex) {
            LOG.log(Level.SEVERE, ex.getMessage(), ex);
            throw new NumberFormatException("Error while converting a data string to number");
        }
        LOG.log(Level.INFO, "Regions size: {0}", regions.size());
        return regions;
    }

    public List<List<PointWrapper>> getPointsInFrameFile(String frameName, String report) throws FileNotFoundException, NumberFormatException {
        List<List<PointWrapper>> eggs = new ArrayList<>();

        try {

            // encontrando o frame filtrando pelo nome
            Optional<String> optLine = readLine(frameName, report);

            LOG.log(Level.INFO, "LINE: {0}", optLine.get());

            optLine.ifPresent(line -> {

                String[] majorData = line.split(Constants.OBJECT_SEPARATOR);
                // Maior que 1 pois existem frames sem ovos, nesse caso a linha não splitta
                if (majorData.length > 1) {
                    // ignorando posição 0 pois representa o nome do frame e a quantidade de ovos, os quais não são necessários
                    for (int i = 1; i < majorData.length; i++) {
                        String eggObject = majorData[i];
                        if (!eggObject.isBlank()) {
                            eggs.add(getPoints(eggObject));

                        }
                    }
                }
            });

        } catch (NumberFormatException ex) {
            LOG.log(Level.SEVERE, ex.getMessage(), ex);
            throw new NumberFormatException("Error while converting a data string to number");
        }
        LOG.log(Level.INFO, "Points size: {0}", eggs.size());
        return eggs;
    }

    /**
     *
     * @param object
     * @return
     * @throws NumberFormatException
     */
    private List<PointWrapper> getPoints(String object) throws NumberFormatException {
        List<PointWrapper> pointsAux = new ArrayList<>();
        String[] data = object.split(Constants.SEPARATOR);
        if (data.length > 3) {
            // Não é necessário ler todos os pontos salvos ou pode poluir o visual do frame por isso j pula 4
            for (int j = 2; j < data.length; j += 4) {
                String string = data[j];
                if (string != null) {
                    PointWrapper current = new PointWrapper(
                            new Point(
                                    Integer.parseInt(data[j].replace(".0", "").trim()),
                                    Integer.parseInt(data[j + 1].replace(".0", "").trim())));
                    pointsAux.add(current);
                }
            }

        }
        return pointsAux;
    }

    public StringBuilder readFullFileContent(String report) {
        StringBuilder builder = new StringBuilder();
        // remove as linhas que não contém necessariamente os pontos e as regiões
        readContent(report).forEach((line) -> {
            builder.append(line);
            builder.append(Constants.QUEBRA_LINHA);
        });

        return builder;
    }

    public String readPaletteDirectoryFromReport(String report) throws DirectoryNotValidException {
        boolean valid = (report.toLowerCase().contains(ProcessingMode.MANUAL.getProcessingMode())
                || report.toLowerCase().contains(ProcessingMode.AUTOMATIC.getProcessingMode()));
        if (valid) {
            return readLine("C:", report).get();
        } else {
            throw new DirectoryNotValidException("No valid reports were selected");
        }
    }

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
