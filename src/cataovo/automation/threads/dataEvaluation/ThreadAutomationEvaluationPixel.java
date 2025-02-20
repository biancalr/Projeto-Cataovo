/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cataovo.automation.threads.dataEvaluation;

import cataovo.entities.Point;
import cataovo.entities.Region;
import cataovo.exceptions.DirectoryNotValidException;
import cataovo.externals.libs.opencv.utils.processUtils.ProcessUtils;
import cataovo.externals.libs.opencv.utils.processUtils.ProcessUtilsImplements;
import cataovo.externals.libs.opencv.wrappers.MatOfPointWrapper;
import cataovo.externals.libs.opencv.wrappers.MatWrapper;
import cataovo.resources.MainContext;
import cataovo.utils.constants.Constants;
import cataovo.utils.conversionUtils.DataToFormatUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author bianc
 */
public class ThreadAutomationEvaluationPixel extends DataEvaluationThreadAutomation {

    private static final Logger LOG = Logger.getLogger(ThreadAutomationEvaluationPixel.class.getName());

    private final DataToFormatUtils dataUtils;
    private final ProcessUtils processUtils;

    public ThreadAutomationEvaluationPixel(String fileContentManual, String fileContentAuto) {
        super(fileContentManual, fileContentAuto);
        this.dataUtils = new DataToFormatUtils();
        this.processUtils = new ProcessUtilsImplements();
    }

    @Override
    protected float[] evaluateFrame(final String regionsInFrame, final String pointsInFrame) throws DirectoryNotValidException {
        int totalOfPixels;
        int regionsCounter;
        int eggsCounter;
        int totalRegionsInPixels;

        int tp = 0;
        int tn = 0;
        int fp = 0;
        int fn = 0;

        float[] metrics = new float[4];

        //Separar a as regioes pela vírgula
        final List<Region> regions = new CopyOnWriteArrayList<>(dataUtils.split(Constants.RECT_FORMAT, regionsInFrame.split(Constants.SEPARATOR)));
        final List<Region> regionsAux = new CopyOnWriteArrayList<>(regions);
        //Separa as áreas dos ovos pela cerquilha
        final List<String> eggsString = new CopyOnWriteArrayList<>(List.of(pointsInFrame.split(Constants.OBJECT_SEPARATOR)));
        final MatWrapper currentFrame = new MatWrapper(MainContext.getInstance().getCurrentFrame());

        totalOfPixels = currentFrame.getWIDTH() * currentFrame.getHEIGHT();
        regionsCounter = regions.size();
        eggsCounter = eggsString.size() - 1;

        // Caso não haja ovos detectados pelo automático, ele não será avaliado
        if (eggsString.size() >= 1) {

            int totalPointsOfSingleEgg;
            int totalEggsInPixels;
            int totalFoundEggs = 0;
            int Ymaximo = 0, Yminimo = currentFrame.getHEIGHT(), Xmaximo = 0, Xminimo = currentFrame.getWIDTH(), distanciaYMancha, distanciaXMancha;
            String eggLine;

            List<Point> egg;
            List<Point> foundEgg;

            Region rect;

            List<Point> removed = new ArrayList<>();
            List<Point> pontosEncontrados = new ArrayList<>();
            List<Point> pontosEncontradosAux = new ArrayList<>();

            List<List<Point>> foundEggs = new CopyOnWriteArrayList<>();
            List<List<Point>> remanescent = new CopyOnWriteArrayList<>();

            // Posição zero contém apenas nome e a quantidade de ovos
            // Começar a partir do 1 pois essa posição não contém coordenadas de pontos
            for (int e = 1; e < eggsString.size(); e++) {
                eggLine = eggsString.get(e);
                // Separar os pontos do ovo pela vírgula
                egg = dataUtils.split(Constants.CIRCLE_FORMAT, eggLine.split(Constants.SEPARATOR));
                foundEggs.add(new CopyOnWriteArrayList<>(egg));
                totalPointsOfSingleEgg = egg.size();
                LOG.log(Level.INFO, "Total of points {0}", egg.size());
                for (int i = 0; i < regions.size(); i++) {
                    rect = regions.get(i);
                    //percorrendo os pontos
                    for (Point point : egg) {
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
                    // Força o acúmulo de uma quantidade significativa da área de um ovo dentro da marcação
                    if (!pontosEncontrados.isEmpty() && ((float) pontosEncontrados.size() / (float) egg.size() > 0.3F)) {
                        LOG.log(Level.INFO, "Total de pontos encontrados em {0}: {1}", new Object[]{eggsString.get(0), pontosEncontrados.size()});
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
                            regionsAux.remove(rect);
                            for (var pontoEncontrado : pontosEncontrados) {
                                egg.remove(pontoEncontrado);
                            }

                            if (((float) egg.size() / (float) totalPointsOfSingleEgg) >= 0.3F) {
                                i = i - 1;
                            } else {
                                eggsCounter--;
                            }
                            pontosEncontradosAux.addAll(pontosEncontrados);
                            totalFoundEggs++;
                            LOG.log(Level.INFO, "A região de {0} contém um ovo", rect.toString());
                        }
                        pontosEncontrados = new ArrayList<>();
                    }

                }

                // Calcular a área da de cada um dos ovos
                // deixar apenas os pontos contidos nas marcações manuais
                if (totalFoundEggs > 0) {

                    for (var i = 0; i < foundEggs.size(); i++) {
                        foundEgg = foundEggs.get(i);

                        for (var j = 0; j < foundEgg.size(); j++) {
                            final var rem = foundEgg.get(j);
                            if (!pontosEncontradosAux.contains(rem)) {
                                foundEgg.remove(rem);
                                removed.add(rem);
                            }
                        }
                        remanescent.add(removed);
                    }

                    totalEggsInPixels = 0;
                    totalEggsInPixels = foundEggs.stream().map(rem -> (int) processUtils.getArea(new MatOfPointWrapper(rem).getMatOfPoint())).reduce(totalEggsInPixels, Integer::sum);
                    totalOfPixels -= totalEggsInPixels;
                    tp = totalEggsInPixels;
                    LOG.log(Level.INFO, "{0} Ovo(s) encontrado(s) para {1}", new Object[]{totalFoundEggs, eggsString.get(0)});
                }
            }
            // Caso tenha sobrado regiões que não foram detectados ovos ou foram detectadas incorretamente
            // transformar para pixel
            if (!regionsAux.isEmpty()) {
                totalRegionsInPixels = 0;
                totalRegionsInPixels = regionsAux.stream().map(Region::getArea).reduce(totalRegionsInPixels, Integer::sum);
                totalOfPixels -= totalRegionsInPixels;
                fn = totalRegionsInPixels;
            }

            // Caso tenha sobrado ovos que não foram detectados ou foram detectados incorretamente
            // remover todos os pontos pertencentes a ovos encontrados encontrados corretamente
            if (!remanescent.isEmpty()) {
                totalEggsInPixels = 0;
                totalEggsInPixels = remanescent.stream().map(e -> (int) processUtils.getArea(new MatOfPointWrapper(e).getMatOfPoint())).reduce(totalEggsInPixels, Integer::sum);
                totalOfPixels -= totalEggsInPixels;
                fp = totalEggsInPixels;
            }

            LOG.log(Level.INFO, "Regiões remanescentes {0}", regions.size());
            LOG.log(Level.INFO, "Ovos remanescentes {0}", eggsString.size() - 1);

        } else {
            // Incrementa os falsos negativos caso o automático não tenha encontrado ovos
            // calcular a quantidade de pixels de cada região;
            if (regionsCounter > 0) {
                totalRegionsInPixels = 0;
                totalRegionsInPixels = regions.stream().map(Region::getArea).reduce(totalRegionsInPixels, Integer::sum);
                totalOfPixels -= totalRegionsInPixels;
                fn = totalRegionsInPixels;
            }
        }

        //  subtrair os valores do total de pixels do frame
        tn = totalOfPixels;

        metrics[0] = tp;
        metrics[1] = fn;
        metrics[2] = fp;
        metrics[3] = tn;
        return metrics;
    }

}
