/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cataovo.automation.threads.dataEvaluation;

import cataovo.constants.Constants;
import cataovo.entities.Point;
import cataovo.entities.Region;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Bianca Leopoldo Ramos
 */
public class ThreadAutomationEvaluation extends DataEvaluationThreadAutomation {

    private static final Logger LOG = Logger.getLogger(ThreadAutomationEvaluation.class.getName());

    public ThreadAutomationEvaluation(String fileContentManual, String fileContentAuto) {
        super(fileContentManual, fileContentAuto);
    }

    @Override
    protected float[] evaluateFrame(String regionsLine, String eggsLine) throws NumberFormatException {
        int tp = 0;
        int tn = 0;
        int fp = 0;
        int fn;
        float[] metrics = new float[4];
        List<String> eggs;
        
        int regionsCounter;
        int eggsCounter;

        //Separar a as regioes pela vírgula
        List<Region> regions = split(Constants.RECT_FORMAT, regionsLine.split(Constants.SEPARATOR));
        regionsCounter = regions.size();
        //Separa as áreas dos ovos pela cerquilha
        eggs = new CopyOnWriteArrayList<>(List.of(eggsLine.split(Constants.OBJECT_SEPARATOR)));
        eggsCounter = eggs.size() - 1;
        // Posição zero contém apenas nome e a quantidade de ovos
//        eggs.remove(0);

        // Caso não haja ovos detectados pelo automático, a linha não será splitada
        if (eggs.size() >= 1) {

            // Posição zero contém apenas nome e a quantidade de ovos
            // Começar a partir do 1 pois essa posição não contém coordenadas de pontos
            List<Point> points;
            for (int e = 1; e < eggs.size(); e++) {
                String eggLine = eggs.get(e);
                // Separar os pontos do ovo pela vírgula
                points = split(Constants.CIRCLE_FORMAT, eggLine.split(Constants.SEPARATOR));
                Region rect;
                int totalPontos = points.size();
                LOG.log(Level.INFO, "Total of points {0}", points.size());
                // Comparação começa aqui
                // Percorrendo as marcações
                List<Point> pontosEncontrados = new CopyOnWriteArrayList<>();
                int Ymaximo = 0, Yminimo = 480, Xmaximo = 0, Xminimo = 640, distanciaYMancha, distanciaXMancha;
                for (int i = 0; i < regions.size(); i++) {
                    rect = regions.get(i);
                    //percorrendo os pontos
                    for (Point point : points) {
                        // se a região contém o ponto
                        if (rect.contains(point)) {
                            LOG.log(Level.INFO, "The point {0} was found in a region", point);
                            // acrescenta na quantidade de partes encontradas
//                            percentual++;
                            LOG.log(Level.INFO, "Points found in {0}: {1}", new Object[]{rect, point});
                            pontosEncontrados.add(point);
                            if (point.getX() > Xmaximo) {
                                Xmaximo = point.getX();
                            }
                            if (point.getX() < Xminimo) {
                                Xminimo = point.getX();
                            }
                            if (point.getY() > Ymaximo) {
                                Ymaximo = point.getY();
                            }
                            if (point.getY() < Yminimo) {
                                Yminimo = point.getY();
                            }
                        }
                    }
                    if (!pontosEncontrados.isEmpty() && ( (float) pontosEncontrados.size() / (float) points.size() > 0.3F)) {
                        LOG.log(Level.INFO, "Total de pontos encontrados em {0}: {1}", new Object[]{eggs.get(0), pontosEncontrados.size()});
                        distanciaYMancha = Ymaximo - Yminimo;
                        distanciaXMancha = Xmaximo - Xminimo;

                        float resultadoX = Math.abs((float) distanciaXMancha / (float) rect.getWidth());
                        float resultadoY = Math.abs((float) distanciaYMancha / (float) rect.getHeight());
                        
                        LOG.log(Level.INFO, "Largura X: {0}", distanciaXMancha);
                        LOG.log(Level.INFO, "Altura Y: {0}", distanciaYMancha);
                        LOG.log(Level.INFO, "Resultado distancia X: {0}", resultadoX);
                        LOG.log(Level.INFO, "Resultado distancia Y: {0}", resultadoY);
                        
                        if ((resultadoX > 0.5 && resultadoX <= 1.0) || (resultadoY > 0.5 && resultadoY <= 1.0)) {

                            regionsCounter--;
                            for (var pontoEncontrado : pontosEncontrados) {
                                points.remove(pontoEncontrado);
                            }
                            if (((float)points.size() / (float)totalPontos) >= 0.3F) {
                                i = i - 1;
                            } else {
                                eggsCounter--;
                            }
                            tp++;
                            LOG.log(Level.INFO, "A região de {0} contém um ovo", rect);
                        }
                        pontosEncontrados = new CopyOnWriteArrayList<>();
                    }
                }
                LOG.log(Level.INFO, "{0} Ovo(s) encontrado(s) para {1}", new Object[]{tp, eggs.get(0)});
            }

            // Caso tenha sobrado regiões que não foram detectados ovos ou foram detectadas incorretamente
            fn = regionsCounter;
            // Caso tenha sobrado ovos que não foram detectados ou foram detectados incorretamente
            // Reduzir 1 do pois a posição 1 não contém coordenadas de pontos
            fp = eggsCounter;
            
            LOG.log(Level.INFO, "Regiões remanescentes {0}", regions.size());
            LOG.log(Level.INFO, "Ovos remanescentes {0}", eggs.size() - 1);

        } else {
            // Incrementa os falsos negativos caso o automático não tenha encontrado ovos
            fn = regionsCounter;
        }

        metrics[0] = tp;
        metrics[1] = fn;
        metrics[2] = fp;
        metrics[3] = tn;
        return metrics;
    }

    private List split(int ofFormat, String[] data) throws NumberFormatException {
        int jumpStep;
        int atStartPoint;
        switch (ofFormat) {
            case Constants.RECT_FORMAT -> {
                jumpStep = 4;
                atStartPoint = 1;
            }
            case Constants.CIRCLE_FORMAT -> {
                jumpStep = 2;
                atStartPoint = 0;
            }
            default ->
                throw new AssertionError();
        }
        return iterateOver(data, ofFormat, atStartPoint, jumpStep);

    }

    private List iterateOver(String[] data, int ofFormat, int atStartPoint, int jumpStep) throws NumberFormatException {
        List formatList = new CopyOnWriteArrayList<>();
        for (int i = atStartPoint; i < data.length; i += jumpStep) {
            if (data[i] != null && !data[i].isBlank()) {
                switch (ofFormat) {
                    case Constants.RECT_FORMAT ->
                        formatList.add(addRegion(data, i));
                    case Constants.CIRCLE_FORMAT ->
                        formatList.add(addPoint(data, i));
                    default ->
                        throw new AssertionError();
                }

            }
        }
        return formatList;
    }

    /**
     * Converts a line of string data into {@link cataovo.entities.Point points}
     *
     * @param data
     * @return the list of points
     * @throws NumberFormatException
     */
    private Point addPoint(String[] data, int ofPosition) throws NumberFormatException {
        return new Point(
                Integer.parseInt(data[ofPosition].replace(".0", "").trim()),
                Integer.parseInt(data[ofPosition + 1].replace(".0", "").trim()));

    }

    /**
     * Converts a line of string data into
     * {@link cataovo.entities.Region regions}
     *
     * @param data
     * @return the list of regions
     * @throws NumberFormatException
     */
    private Region addRegion(String[] data, int ofPosition) throws NumberFormatException {
        return new Region(
                //Acrescentando correção em caso de valores negativos
                Integer.parseInt(data[ofPosition + 3]) > 0 ? Integer.parseInt(data[ofPosition + 3]) : Math.abs(Integer.parseInt(data[ofPosition + 3])),
                Integer.parseInt(data[ofPosition + 2]) > 0 ? Integer.parseInt(data[ofPosition + 2]) : Math.abs(Integer.parseInt(data[ofPosition + 2])),
                new Point(Integer.parseInt(data[ofPosition + 2]) > 0 ? (Integer.parseInt(data[ofPosition]) - Integer.parseInt(data[ofPosition + 2])) : Integer.parseInt(data[ofPosition]),
                        Integer.parseInt(data[ofPosition + 3]) > 0 ? (Integer.parseInt(data[ofPosition + 1]) - Integer.parseInt(data[ofPosition + 3])) : Integer.parseInt(data[ofPosition + 1])));

    }

}
