/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cataovo.automations.evaluate;

import cataovo.entities.Point;
import cataovo.entities.Region;
import cataovo.exceptions.DirectoryNotValidException;
import cataovo.resources.MainContext;
import cataovo.utils.Constants;
import cataovo.utils.frameUtils.FrameUtils;
import cataovo.wrappers.opencv.MatWrapper;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Bianca Leopoldo Ramos
 */
public class EvaluateData extends BasicEvaluate {

    private static final Logger LOG = Logger.getLogger(EvaluateData.class.getName());

    private final FrameUtils frameUtils;

    public EvaluateData(String fileContentManual, String fileContentAuto, MainContext mainContext) {
        super(fileContentManual, fileContentAuto, mainContext);
        this.frameUtils = new FrameUtils();
    }

    /**
     *
     * @param regionsInFrame
     * @param pointsInFrame
     * @return
     * @throws NumberFormatException
     */
    @Override
    protected float[] execute(final String regionsInFrame, final String pointsInFrame) throws DirectoryNotValidException {
        int tp = 0;
        int tn = 0;
        int fp = 0;
        int fn = 0;
        float[] metrics = new float[4];
        List<String> eggs;

        int regionsCounter;
        int eggsCounter;
        final MatWrapper currentFrame = new MatWrapper(getMainContext().getCurrentFrame());

        //Separar a as regioes pela vírgula
        final List<Region> regions = frameUtils.split(Constants.RECT, regionsInFrame.split(Constants.SEPARATOR));
        regionsCounter = regions.size();
        //Separa as áreas dos ovos pela cerquilha
        eggs = new CopyOnWriteArrayList<>(List.of(pointsInFrame.split(Constants.OBJECT_SEPARATOR)));
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
                points = frameUtils.split(Constants.CIRCLE, eggLine.split(Constants.SEPARATOR));
                Region rect;
                int totalPontos = points.size();
                LOG.log(Level.INFO, "Total of points {0}", points.size());
                // Comparação começa aqui
                // Percorrendo as marcações
                List<Point> pontosEncontrados = new CopyOnWriteArrayList<>();
                int Ymaximo = 0, Yminimo = currentFrame.getHEIGHT(), Xmaximo = 0, Xminimo = currentFrame.getWIDTH(), distanciaYMancha, distanciaXMancha;
                for (int i = 0; i < regions.size(); i++) {
                    rect = regions.get(i);
                    //percorrendo os pontos
                    for (Point point : points) {
                        // se a região contém o ponto
                        if (rect.contains(point)) {
//                            LOG.log(Level.INFO, "The point {0} was found in a region", point);
                            // acrescenta na quantidade de partes encontradas
//                            percentual++;
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
                    // Forçar o acúmulo de uma quantidade significativa da área de um ovo dentro da marcação
                    if (!pontosEncontrados.isEmpty() && ((float) pontosEncontrados.size() / (float) points.size() > 0.3F)) {
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
                            if (((float) points.size() / (float) totalPontos) >= 0.3F) {
                                i = i - 1;
                            } else {
                                eggsCounter--;
                            }
                            tp++;
                            LOG.log(Level.INFO, "A região de {0} contém um ovo", rect);
                            LOG.log(Level.INFO, "Points found in {0}: {1}", new Object[]{rect, pontosEncontrados.toString()});
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

}
