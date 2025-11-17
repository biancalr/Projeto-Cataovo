/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cataovo.automations.evaluate;

import cataovo.exceptions.DirectoryNotValidException;
import cataovo.exceptions.ReportNotValidException;
import cataovo.resources.MainContext;
import cataovo.utils.Constants;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.IntStream;

/**
 *
 * @author Bianca Leopoldo Ramos
 */
public abstract class BasicEvaluate implements Callable<String> {

    /**
     * Logging for BasicEvaluate
     */
    private static final Logger LOG = Logger.getLogger(BasicEvaluate.class.getName());

    private final String fileContentManual;
    private final String fileContentAuto;
    private final MainContext mainContext;

    /**
     *
     * @param fileContentManual
     * @param fileContentAuto
     * @param mainContext
     */
    public BasicEvaluate(String fileContentManual, String fileContentAuto, MainContext mainContext) {
        this.fileContentManual = fileContentManual;
        this.fileContentAuto = fileContentAuto;
        this.mainContext = mainContext;
    }

    @Override
    public String call() throws ReportNotValidException, DirectoryNotValidException, NumberFormatException, InterruptedException {
        return evaluationAnalysis();
    }

    /**
     *
     * @param regionsInFrame
     * @param pointsInFrame
     * @return
     * @throws cataovo.exceptions.DirectoryNotValidException
     */
    protected abstract float[] execute(String regionsInFrame, String pointsInFrame) throws DirectoryNotValidException;

    /**
     *
     * @return
     */
    private synchronized String evaluationAnalysis() throws NumberFormatException, ReportNotValidException, DirectoryNotValidException, InterruptedException {
        LOG.log(Level.INFO, "Starting Evaluation...");

        //split both strings
        final String[] manualFrames = this.fileContentManual.split(Constants.QUEBRA_LINHA);
        final String[] autoFrames = this.fileContentAuto.split(Constants.QUEBRA_LINHA);
        StringBuilder resultString = new StringBuilder();
        int[] cumulative;

        if (manualFrames.length != autoFrames.length) {
            throw new ReportNotValidException(Constants.REPORTS_FRAME_QUANTITY_NOT_EQUAL_PT_BR);
        }
        
        ExecutorService executor = Executors.newCachedThreadPool();
        final List<Future<float[]>> evaluation = executor.invokeAll(createTasks(manualFrames, autoFrames));
        
        cumulative = getResult(evaluation);
        executor.shutdown();
        
        IntStream.range(0, cumulative.length).forEach(i -> {
            resultString.append(cumulative[i]).append(Constants.QUEBRA_LINHA);
        });

        return resultString.toString();
    }

    private List<Callable<float[]>> createTasks(final String[] manualFrames, final String[] autoFrames) {
        List<Callable<float[]>> tasks = new ArrayList();
        IntStream.range(0, manualFrames.length).forEach(i -> {
            tasks.add(() -> {
                float[] frameResult = execute(manualFrames[i], autoFrames[i]);
                LOG.log(Level.INFO, "Resultado individual: TP({0}), FN({1}), FP({2}), TN({3})", new Object[]{frameResult[0], frameResult[1], frameResult[2], frameResult[3]});
                return frameResult;
            });
        });
        return tasks;
    }

    private int[] getResult(final List<Future<float[]>> evaluation) {
        int[] cumulative = new int[4];
        evaluation.forEach(task -> {
            try {
                float[] result = task.get(1, TimeUnit.MILLISECONDS);
                cumulative[0] += result[0];
                cumulative[1] += result[1];
                cumulative[2] += result[2];
                cumulative[3] += result[3];
                LOG.log(Level.INFO, "Resultado somado: TP({0}), FN({1}), FP({2}), TN({3})", new Object[]{cumulative[0], cumulative[1], cumulative[2], cumulative[3]});
            } catch (InterruptedException | ExecutionException | TimeoutException ex) {
                LOG.log(Level.SEVERE, ex.getMessage(), ex);
            }
        });
        return cumulative;
    }

    protected MainContext getMainContext() {
        return mainContext;
    }
}
