/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cataovo.presentation;

import cataovo.controllers.FrameController;
import cataovo.controllers.PageController;
import cataovo.controllers.implement.FrameControllerImpl;
import cataovo.controllers.implement.PageControllerImpl;
import cataovo.domain.Event;
import cataovo.entities.Point;
import cataovo.events.AutomaticEvent;
import cataovo.events.EvaluationEvent;
import cataovo.events.FileEvent;
import cataovo.events.ManualEvent;
import cataovo.events.implement.AutomaticEventImpl;
import cataovo.events.implement.EvaluationEventImpl;
import cataovo.events.implement.FileEventImpl;
import cataovo.events.implement.ManualEventImpl;
import cataovo.exceptions.AutomationExecutionException;
import cataovo.exceptions.DirectoryNotValidException;
import cataovo.exceptions.ImageNotValidException;
import cataovo.exceptions.RegionNotValidException;
import cataovo.exceptions.ReportNotValidException;
import cataovo.exceptions.TabNotValidToEvaluationException;
import cataovo.resources.MainContext;
import cataovo.utils.Constants;
import cataovo.utils.enums.FileExtension;
import cataovo.utils.fileUtils.readers.FileUtils;
import cataovo.utils.mathUtils.PercentUtils;
import java.awt.Dimension;
import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Icon;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileSystemView;
import org.opencv.core.Core;

/**
 * Module that interacts with the user. This is the main face of this
 * application
 *
 * @author Bianca Leopoldo Ramos
 */
public class MainPage extends javax.swing.JFrame {

    private static final Logger LOG = Logger.getLogger(MainPage.class.getName());

    private File savingFolder = null;

    private FileUtils fileUtils = null;
    private MainContext mainContext = null;

    private PageController pageController = null;
    private FrameController frameController = null;

    private FileEvent fileEvent = null;
    private AutomaticEvent autoEvent = null;
    private EvaluationEvent evalEvent = null;
    private ManualEvent manualEvent = null;

    /**
     * Creates new form MainPage
     */
    public MainPage() {
        LOG.log(Level.INFO, "Initializing application");
        try {
            this.mainContext = new MainContext();
            this.fileUtils = new FileUtils();
            this.initComponents();

            this.resetInitialComponents();
            this.centralizeComponent();

            LOG.log(Level.INFO, "Selecting home directory {}", this.mainContext.getHomeDir());
            this.savingFolder = mainContext.getFileFolder(new File(this.mainContext.getHomeDir()));

            this.pageController = new PageControllerImpl(mainContext);
            this.frameController = new FrameControllerImpl(mainContext);

            this.fileEvent = new FileEventImpl(mainContext);
            this.autoEvent = new AutomaticEventImpl(mainContext);
            this.evalEvent = new EvaluationEventImpl(mainContext);
            this.manualEvent = new ManualEventImpl(mainContext);
        } catch (DirectoryNotValidException ex) {
            LOG.log(Level.SEVERE, ex.getMessage());
            JOptionPane.showMessageDialog(jTabbedPane1, ex.getMessage());
        }
    }

    /**
     * Modify the names of Buttons and actionItens to make ActionCommands and
     * methods easyer.
     */
    private void resetInitialComponents() {

        jLabel1.setText(Constants.NO_PALETTE_SELECTED);
        jLabel2.setText("");
        jLabel3.setText("");
        jLabel5.setText("");
        jLabel6.setText("");
        jLabel7.setText("");
        jLabel8.setText("");
        jLabel9.setText("");
        jLabel10.setText("");
        jLabel11.setText("");
        jLabel12.setText("");
        jLabel21.setText("");
        jLabel22.setText("");
        jLabel23.setText("");
        jLabel24.setText("");
        jLabel28.setText("");
        jLabel29.setText("");
        jLabel30.setText("");
        jLabel32.setText("");
        jLabel33.setText("");

        jButton1.setEnabled(false);
        jButton3.setEnabled(false);
        jButton2.setEnabled(false);
        jButton4.setEnabled(false);
        jButton5.setEnabled(false);
        jButton6.setEnabled(false);
        jButton7.setEnabled(false);

        jLabel9.setIcon(null);

        jCheckBox1.setText(Constants.CHECKBOX_EVALUATION_BY_PIXEL);

        jMenuItem1.setText(Constants.ITEM_ACTION_COMMAND_OPEN_PALETTE_PT_BR);
        jMenuItem5.setText(Constants.ITEM_ACTION_COMMAND_SELECT_DESTINATION_FOLDER_PT_BR);
        jMenuItem6.setText(Constants.ITEM_ACTION_COMMAND_SELECT_REPORT_PT_BR);

        jTabbedPane1.setTitleAt(0, Constants.TAB_NAME_MANUAL_PT_BR);
        jTabbedPane1.setTitleAt(1, Constants.TAB_NAME_AUTOMATIC_PT_BR);
        jTabbedPane1.setTitleAt(2, Constants.TAB_NAME_EVALUATION_PT_BR);

        jTabbedPane2.setSelectedIndex(4);
    }

    /**
     * Centralize the window
     */
    private void centralizeComponent() {
        Dimension ds = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension dw = getSize();
        setLocation((ds.width - dw.width) / 2, (ds.height - dw.height) / 2);
    }

    /**
     * Clean icons in labels.
     */
    private void cleanTabs() {
        jLabel2.setIcon(null);
        jLabel2.setBorder(null);
        jLabel3.setIcon(null);
        jLabel5.setIcon(null);
        jLabel6.setIcon(null);
        jLabel7.setIcon(null);
        jLabel8.setIcon(null);
        jLabel9.setIcon(null);
        jLabel12.setIcon(null);
        jLabel12.setBorder(null);
    }

    /**
     * Clean texts in labels
     */
    private void cleanTexts() {
        jLabel1.setText(Constants.NO_PALETTE_SELECTED);
        jLabel4.setText(Constants.NO_PALETTE_SELECTED);
        jLabel13.setText(Constants.NO_ARTIFACT_SELECTED);
        jLabel11.setText("");
        jLabel28.setText("");
        jLabel29.setText("");
        jLabel30.setText("");
        jLabel32.setText("");
        jLabel33.setText("");
        jLabel21.setText("");
        jLabel22.setText("");
        jLabel23.setText("");
        jLabel24.setText("");
    }

    /**
     *
     * @throws CloneNotSupportedException
     * @throws NumberFormatException
     * @throws ImageNotValidException
     * @throws DirectoryNotValidException
     * @throws FileNotFoundException
     */
    private void runEvaluation() throws CloneNotSupportedException, NumberFormatException, ImageNotValidException, DirectoryNotValidException, FileNotFoundException {
        String manualReport = mainContext.getReports()[0];
        String autoReport = mainContext.getReports()[1];
        mainContext.getPanelTabHelper().setCurrentTabProcessing(true);
        if (mainContext.getPalette() == null || mainContext.getPalette().getPathName() == null) {
            mainContext.setPalette(
                    this.mainContext.getPalettDirByReport(
                            this.fileUtils.readPaletteDirectoryFromReport(manualReport),
                            this.fileUtils.readPaletteDirectoryFromReport(autoReport)));
            pageController.showFramesOnSelectedTabScreen(jTabbedPane1, jLabel13, jLabel12, mainContext.getCurrentFrame());
        }
        loadEvalContents(manualReport, autoReport);
        final Event event = this.evalEvent.execute(
                this.fileUtils.readFullFileContent(manualReport).toString(),
                this.fileUtils.readFullFileContent(autoReport).toString(),
                this.jCheckBox1.isSelected(),
                mainContext.getPalette(),
                this.savingFolder.getAbsolutePath(),
                FileExtension.CSV,
                mainContext.getPanelTabHelper().getTabName());
        setLabelsEvaluationMod(event.getResult());
        mainContext.getPanelTabHelper().setCurrentTabProcessing(false);
        JOptionPane.showMessageDialog(jTabbedPane1, "The file was saved under the path: " + event.getFolder());
        LOG.log(Level.INFO, "Evaluation Finished!");
    }

    /**
     *
     * @param manualReport
     * @param autoReport
     * @throws FileNotFoundException
     * @throws CloneNotSupportedException
     * @throws NumberFormatException
     * @throws ImageNotValidException
     * @throws DirectoryNotValidException
     */
    private void loadEvalContents(String manualReport, String autoReport) throws FileNotFoundException, CloneNotSupportedException, NumberFormatException, ImageNotValidException, DirectoryNotValidException {
        this.pageController.onNextFrameInEvaluation(jLabel13, jLabel12,
                mainContext.getPalette().getDirectory(),
                mainContext.getReports());
        this.pageController.showFramesOnSelectedTabScreen(jTabbedPane1, jLabel13, jLabel12,
                (this.frameController.paintFormats(mainContext.getCurrentFrame().clone(),
                        this.fileUtils.getRegionsInFrameFile(mainContext.getCurrentFrame().getName(),
                                manualReport),
                        this.fileUtils.getPointsInFrameFile(mainContext.getCurrentFrame().getName(),
                                autoReport)))
        );
    }

    /**
     * Put the percent character on evaluations text labels
     *
     * @param evaluation
     */
    private void setLabelsEvaluationMod(String evaluation) {
        String[] dataEvaluationSplitted = evaluation.split(Constants.QUEBRA_LINHA);
        this.jLabel21.setText(dataEvaluationSplitted[0]); // true positive
        this.jLabel23.setText(dataEvaluationSplitted[2]); // false negative
        this.jLabel22.setText(dataEvaluationSplitted[1]); // false positive
        this.jLabel24.setText(dataEvaluationSplitted[3]); // true negative

        final PercentUtils percentCalculation = new PercentUtils();
        this.jLabel28.setText(percentCalculation.getPercentageOf(Constants.CALCULATE_METHOD_RECALL, dataEvaluationSplitted[0], dataEvaluationSplitted[1]));
        this.jLabel29.setText(percentCalculation.getPercentageOf(Constants.CALCULATE_METHOD_SPECIFICITY, dataEvaluationSplitted[3], dataEvaluationSplitted[1]));
        this.jLabel30.setText(percentCalculation.getPercentageOf(Constants.CALCULATE_METHOD_F1_SCORE, dataEvaluationSplitted[0], dataEvaluationSplitted[3], dataEvaluationSplitted[2], dataEvaluationSplitted[1]));
        this.jLabel33.setText(percentCalculation.getPercentageOf(Constants.CALCULATE_METHOD_PRECISION, dataEvaluationSplitted[0], dataEvaluationSplitted[2]));
        this.jLabel32.setText(percentCalculation.getPercentageOf(Constants.CALCULATE_METHOD_ACCURACY, dataEvaluationSplitted[0], dataEvaluationSplitted[3], dataEvaluationSplitted[2], dataEvaluationSplitted[1]));
    }

    /**
     * Setts the message when Evaluation resolves to a directory validation
     * error.
     *
     * @return the error message
     */
    private String onDirectoryErrorEvaluationMessage() {
        StringBuilder sb = new StringBuilder();
        sb.append("Não foram encontradas as pastas correspondentes ao processamento");
        sb.append(Constants.QUEBRA_LINHA);
        sb.append("É possível que uma nova paleta ou nenhuma tenha sido selecionada, portanto o diretório foi alterado ou ainda não existe.");
        sb.append(Constants.QUEBRA_LINHA);
        sb.append("Revise os arquivos e reinicie processamento para visualizar os devidos resultados.");
        return sb.toString();
    }

    /**
     * Setts the message when Automatic resolves to a directory validation error
     *
     * @return the error message
     */
    private String onDirectoryErrorAutomaticMessage() {
        StringBuilder sb = new StringBuilder("É possível que uma nova paleta ou nenhuma tenha sido selecionada, portanto o diretório de salvamento foi alterado ou ainda não existe.");
        sb.append(Constants.QUEBRA_LINHA);
        sb.append("Revise os arquivos e inicie um novo processamento para visualizar os devidos resultados.");
        return sb.toString();
    }

    /**
     * Setts the message when Evaluation resolves to a report validation error
     *
     * @return the error message
     */
    private String onReportErrorEvaluationMessage() {
        StringBuilder sb = new StringBuilder("Por favor, revisar os arquivos selecionados.");
        sb.append(Constants.QUEBRA_LINHA);
        sb.append("1º Selecionar a pasta da Paleta na Opçao 'Abrir Paleta'");
        sb.append(Constants.QUEBRA_LINHA);
        sb.append("2º Selecionar os dois relatórios CSV desta mesma Paleta em 'Selecionar Relatório'.");
        return sb.toString();
    }

    /**
     * Count the reports running through the array. Sums one if the position has
     * a report.
     *
     * @return the number of reports that contains a actual report
     * @throws DirectoryNotValidException
     */
    private int countEvaluationReports() throws DirectoryNotValidException {
        // With only two positions, doesn't have major problem to iterate over them
        int numberOfEvaluationReports = 0;
        for (String report : mainContext.getReports()) {
            if (report != null) {
                numberOfEvaluationReports++;
            }
        }
        return numberOfEvaluationReports;
    }

    public void resetSavingFolder() throws DirectoryNotValidException {
        this.savingFolder = mainContext.getFileFolder(new File(this.mainContext.getHomeDir()));
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTabbedPane1 = new javax.swing.JTabbedPane();
        jDesktopPane1 = new javax.swing.JDesktopPane();
        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jDesktopPane2 = new javax.swing.JDesktopPane();
        jTabbedPane2 = new javax.swing.JTabbedPane();
        jPanel2 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jLabel11 = new javax.swing.JLabel();
        jDesktopPane3 = new javax.swing.JDesktopPane();
        jPanel8 = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jButton6 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        jLabel32 = new javax.swing.JLabel();
        jLabel33 = new javax.swing.JLabel();
        jLabel35 = new javax.swing.JLabel();
        jLabel36 = new javax.swing.JLabel();
        jCheckBox1 = new javax.swing.JCheckBox();
        jLabel27 = new javax.swing.JLabel();
        jLabel30 = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem5 = new javax.swing.JMenuItem();
        jMenuItem6 = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jTabbedPane1.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jTabbedPane1StateChanged(evt);
            }
        });

        jPanel1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel2.setText("jLabel2");
        jLabel2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel2MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 640, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 10, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 480, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 8, Short.MAX_VALUE))
        );

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Escolha uma pasta");

        jButton2.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jButton2.setText("Remover");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jButton3.setText(" Finalizar Quadro");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel9.setText("jLabel9");

        jLabel10.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel10.setText("jLabel10");

        jDesktopPane1.setLayer(jPanel1, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jDesktopPane1.setLayer(jLabel1, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jDesktopPane1.setLayer(jButton2, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jDesktopPane1.setLayer(jButton3, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jDesktopPane1.setLayer(jLabel9, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jDesktopPane1.setLayer(jLabel10, javax.swing.JLayeredPane.DEFAULT_LAYER);

        javax.swing.GroupLayout jDesktopPane1Layout = new javax.swing.GroupLayout(jDesktopPane1);
        jDesktopPane1.setLayout(jDesktopPane1Layout);
        jDesktopPane1Layout.setHorizontalGroup(
            jDesktopPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDesktopPane1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jDesktopPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jDesktopPane1Layout.createSequentialGroup()
                        .addGap(18, 23, Short.MAX_VALUE)
                        .addGroup(jDesktopPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 310, Short.MAX_VALUE)
                            .addGroup(jDesktopPane1Layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap(15, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jDesktopPane1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jDesktopPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, 170, Short.MAX_VALUE))
                        .addGap(75, 75, 75))))
        );
        jDesktopPane1Layout.setVerticalGroup(
            jDesktopPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDesktopPane1Layout.createSequentialGroup()
                .addGap(44, 44, 44)
                .addGroup(jDesktopPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jDesktopPane1Layout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(126, 126, 126)
                        .addGroup(jDesktopPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButton2)
                            .addComponent(jButton3))
                        .addGap(78, 78, 78)
                        .addComponent(jLabel10)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(46, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Manual", jDesktopPane1);

        jTabbedPane2.setMinimumSize(new java.awt.Dimension(670, 490));
        jTabbedPane2.setPreferredSize(new java.awt.Dimension(671, 492));

        jPanel2.setPreferredSize(new java.awt.Dimension(671, 492));

        jLabel3.setText("jLabel3");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, 644, Short.MAX_VALUE)
                .addGap(0, 2, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, 484, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane2.addTab("Binarização", jPanel2);

        jLabel5.setText("jLabel3");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 640, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 6, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, 482, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane2.addTab("Blur", jPanel3);

        jLabel6.setText("jLabel3");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 640, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 6, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, 482, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane2.addTab("Final", jPanel4);

        jLabel7.setText("jLabel3");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 640, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 6, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, 482, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane2.addTab("Morfologia", jPanel5);

        jLabel8.setText("jLabel3");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 640, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 6, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, 482, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane2.addTab("Original", jPanel6);

        jLabel4.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("Escolha uma pasta");

        jButton4.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jButton4.setText("< Anterior");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jButton5.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jButton5.setText("Próxima >");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jButton1.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jButton1.setText("Iniciar");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel11.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel11.setText("jLabel11");

        jDesktopPane2.setLayer(jTabbedPane2, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jDesktopPane2.setLayer(jLabel4, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jDesktopPane2.setLayer(jButton4, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jDesktopPane2.setLayer(jButton5, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jDesktopPane2.setLayer(jButton1, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jDesktopPane2.setLayer(jLabel11, javax.swing.JLayeredPane.DEFAULT_LAYER);

        javax.swing.GroupLayout jDesktopPane2Layout = new javax.swing.GroupLayout(jDesktopPane2);
        jDesktopPane2.setLayout(jDesktopPane2Layout);
        jDesktopPane2Layout.setHorizontalGroup(
            jDesktopPane2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDesktopPane2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 646, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jDesktopPane2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jDesktopPane2Layout.createSequentialGroup()
                        .addGap(30, 30, 30)
                        .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 19, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jDesktopPane2Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jDesktopPane2Layout.createSequentialGroup()
                        .addGap(88, 88, 88)
                        .addGroup(jDesktopPane2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, 170, Short.MAX_VALUE)
                            .addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        jDesktopPane2Layout.setVerticalGroup(
            jDesktopPane2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDesktopPane2Layout.createSequentialGroup()
                .addGroup(jDesktopPane2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jDesktopPane2Layout.createSequentialGroup()
                        .addGap(23, 23, 23)
                        .addComponent(jTabbedPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 521, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jDesktopPane2Layout.createSequentialGroup()
                        .addGap(103, 103, 103)
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(123, 123, 123)
                        .addGroup(jDesktopPane2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButton4)
                            .addComponent(jButton5))
                        .addGap(92, 92, 92)
                        .addComponent(jButton1)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel11)))
                .addContainerGap(38, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Automático", jDesktopPane2);

        jPanel8.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jPanel8.setPreferredSize(new java.awt.Dimension(654, 492));

        jLabel12.setText("jLabel12");

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 640, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 10, Short.MAX_VALUE))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 480, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 8, Short.MAX_VALUE))
        );

        jLabel13.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        jLabel13.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel13.setText("Selecione os Artefatos");

        jButton6.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jButton6.setText("< Anterior");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        jButton7.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jButton7.setText("Próximo >");
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        jLabel14.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        jLabel14.setText("Métricas");

        jLabel15.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel15.setText("Valores Obtidos");

        jLabel16.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel16.setText("Valores Reais");

        jLabel17.setText("Positivo");

        jLabel18.setText("Negativo");

        jLabel19.setText("Positivo");

        jLabel20.setText("Negativo");

        jLabel21.setText("VP");

        jLabel22.setText("FP");

        jLabel23.setText("FN");

        jLabel24.setText("VN");

        jLabel25.setText("Recall");

        jLabel26.setText("Especificidade");

        jLabel28.setText("jLabel28");

        jLabel29.setText("jLabel29");

        jLabel32.setText("jLabel32");

        jLabel33.setText("jLabel33");

        jLabel35.setText("Acurácia");

        jLabel36.setText("Precisão");

        jCheckBox1.setText("Medir por Pixel");
        jCheckBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox1ActionPerformed(evt);
            }
        });

        jLabel27.setText("F1 Score");

        jLabel30.setText("jLabel29");

        jDesktopPane3.setLayer(jPanel8, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jDesktopPane3.setLayer(jLabel13, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jDesktopPane3.setLayer(jButton6, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jDesktopPane3.setLayer(jButton7, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jDesktopPane3.setLayer(jSeparator1, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jDesktopPane3.setLayer(jLabel14, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jDesktopPane3.setLayer(jLabel15, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jDesktopPane3.setLayer(jLabel16, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jDesktopPane3.setLayer(jLabel17, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jDesktopPane3.setLayer(jLabel18, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jDesktopPane3.setLayer(jLabel19, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jDesktopPane3.setLayer(jLabel20, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jDesktopPane3.setLayer(jLabel21, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jDesktopPane3.setLayer(jLabel22, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jDesktopPane3.setLayer(jLabel23, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jDesktopPane3.setLayer(jLabel24, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jDesktopPane3.setLayer(jLabel25, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jDesktopPane3.setLayer(jLabel26, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jDesktopPane3.setLayer(jLabel28, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jDesktopPane3.setLayer(jLabel29, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jDesktopPane3.setLayer(jLabel32, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jDesktopPane3.setLayer(jLabel33, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jDesktopPane3.setLayer(jLabel35, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jDesktopPane3.setLayer(jLabel36, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jDesktopPane3.setLayer(jCheckBox1, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jDesktopPane3.setLayer(jLabel27, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jDesktopPane3.setLayer(jLabel30, javax.swing.JLayeredPane.DEFAULT_LAYER);

        javax.swing.GroupLayout jDesktopPane3Layout = new javax.swing.GroupLayout(jDesktopPane3);
        jDesktopPane3.setLayout(jDesktopPane3Layout);
        jDesktopPane3Layout.setHorizontalGroup(
            jDesktopPane3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDesktopPane3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jDesktopPane3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jDesktopPane3Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(jDesktopPane3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jDesktopPane3Layout.createSequentialGroup()
                                .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jDesktopPane3Layout.createSequentialGroup()
                                .addComponent(jLabel16)
                                .addGap(17, 17, 17)
                                .addGroup(jDesktopPane3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel20)
                                    .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jDesktopPane3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jDesktopPane3Layout.createSequentialGroup()
                                        .addGroup(jDesktopPane3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGap(0, 8, Short.MAX_VALUE))
                                    .addComponent(jLabel23, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jDesktopPane3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel22, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addGroup(jDesktopPane3Layout.createSequentialGroup()
                                        .addComponent(jLabel18)
                                        .addGap(0, 42, Short.MAX_VALUE))
                                    .addComponent(jLabel24, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jDesktopPane3Layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(jLabel15)
                                .addGap(54, 54, 54))
                            .addGroup(jDesktopPane3Layout.createSequentialGroup()
                                .addGroup(jDesktopPane3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jDesktopPane3Layout.createSequentialGroup()
                                        .addGap(130, 130, 130)
                                        .addComponent(jLabel14))
                                    .addComponent(jCheckBox1)
                                    .addGroup(jDesktopPane3Layout.createSequentialGroup()
                                        .addGroup(jDesktopPane3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                            .addComponent(jLabel26, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(jLabel25, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(jLabel27, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(jDesktopPane3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(jLabel29, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(jLabel28)
                                            .addComponent(jLabel30, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                        .addGap(53, 53, 53)
                                        .addGroup(jDesktopPane3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(jLabel35)
                                            .addComponent(jLabel36))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addGroup(jDesktopPane3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(jLabel33, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(jLabel32, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                                .addGap(0, 0, Short.MAX_VALUE))))
                    .addGroup(jDesktopPane3Layout.createSequentialGroup()
                        .addGap(36, 36, 36)
                        .addGroup(jDesktopPane3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jLabel13, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 275, Short.MAX_VALUE)
                            .addComponent(jSeparator1, javax.swing.GroupLayout.Alignment.LEADING))))
                .addContainerGap())
        );
        jDesktopPane3Layout.setVerticalGroup(
            jDesktopPane3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDesktopPane3Layout.createSequentialGroup()
                .addGroup(jDesktopPane3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jDesktopPane3Layout.createSequentialGroup()
                        .addGap(47, 47, 47)
                        .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jDesktopPane3Layout.createSequentialGroup()
                        .addGap(85, 85, 85)
                        .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(39, 39, 39)
                        .addGroup(jDesktopPane3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButton6)
                            .addComponent(jButton7))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel14)
                        .addGap(18, 18, 18)
                        .addGroup(jDesktopPane3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel15)
                            .addComponent(jCheckBox1))
                        .addGroup(jDesktopPane3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jDesktopPane3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addGroup(jDesktopPane3Layout.createSequentialGroup()
                                    .addGap(35, 35, 35)
                                    .addComponent(jLabel22)
                                    .addGap(5, 5, 5)
                                    .addComponent(jLabel16)
                                    .addGap(5, 5, 5)
                                    .addComponent(jLabel24))
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jDesktopPane3Layout.createSequentialGroup()
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel17)
                                    .addGap(9, 9, 9)
                                    .addComponent(jLabel21)
                                    .addGap(26, 26, 26)
                                    .addComponent(jLabel23)))
                            .addGroup(jDesktopPane3Layout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addComponent(jLabel18)
                                .addGap(9, 9, 9)
                                .addComponent(jLabel19)
                                .addGap(26, 26, 26)
                                .addComponent(jLabel20)))
                        .addGap(48, 48, 48)
                        .addGroup(jDesktopPane3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jDesktopPane3Layout.createSequentialGroup()
                                .addComponent(jLabel25)
                                .addGap(20, 20, 20)
                                .addComponent(jLabel26)
                                .addGap(20, 20, 20)
                                .addComponent(jLabel27))
                            .addGroup(jDesktopPane3Layout.createSequentialGroup()
                                .addComponent(jLabel28)
                                .addGap(20, 20, 20)
                                .addComponent(jLabel29)
                                .addGap(20, 20, 20)
                                .addComponent(jLabel30))
                            .addGroup(jDesktopPane3Layout.createSequentialGroup()
                                .addComponent(jLabel32)
                                .addGap(20, 20, 20)
                                .addComponent(jLabel33))
                            .addGroup(jDesktopPane3Layout.createSequentialGroup()
                                .addComponent(jLabel35)
                                .addGap(20, 20, 20)
                                .addComponent(jLabel36)))))
                .addContainerGap(43, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Avaliação", jDesktopPane3);

        jMenu1.setText("File");
        jMenu1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenu1ActionPerformed(evt);
            }
        });

        jMenuItem1.setText("Abrir Pasta");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem1);

        jMenuItem5.setText("Selecionar Pasta Destino");
        jMenuItem5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem5ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem5);

        jMenuItem6.setText("Selecionar Relatório");
        jMenuItem6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem6ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem6);

        jMenuBar1.add(jMenu1);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 1007, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jTabbedPane1)
                .addGap(2, 2, 2))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        try {
            LOG.log(Level.INFO, "Removing the last created region: {0}", evt.getActionCommand());
            jLabel10.setText("");
            jLabel10.setIcon(null);
            jLabel9.setIcon(null);
            jLabel9.setText("");
            pageController.showFramesOnSelectedTabScreen(jTabbedPane1,
                    jLabel1,
                    jLabel2,
                    frameController.removeLastRegion(mainContext.getCurrentFrame()
                    ));
        } catch (DirectoryNotValidException | ImageNotValidException ex) {
            LOG.log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(jPanel1, ex.getMessage());
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        try {
            LOG.log(Level.INFO, evt.getActionCommand());
            final File file = fileEvent.onFileSelectionEvent(mainContext.getPanelTabHelper().isCurrentTabProcessing(), evt.getActionCommand(), true);

            if (file != null) {
                mainContext.choosePalette(file);
                if (mainContext.getCurrentFrame().getPaletteFrame().exists()) {
                    resetSavingFolder();
                    cleanTabs();
                    cleanTexts();
                    switch (jTabbedPane1.getSelectedIndex()) {
                        case 0 -> {
                            mainContext.getPalette().getFrames().poll();
                            mainContext.updateTabHelper(jTabbedPane1.getSelectedIndex(), jTabbedPane1.getTitleAt(jTabbedPane1.getSelectedIndex()), true);
                            jButton3.setEnabled(true);
                            jButton2.setEnabled(true);
                            mainContext.updateTabHelper(jTabbedPane1.getSelectedIndex(), jTabbedPane1.getTitleAt(jTabbedPane1.getSelectedIndex()), true);
                            pageController.showFramesOnSelectedTabScreen(jTabbedPane1, jLabel1, jLabel2, mainContext.getCurrentFrame());
                        }
                        case 1 -> {
                            mainContext.updateTabHelper(jTabbedPane1.getSelectedIndex(), jTabbedPane1.getTitleAt(jTabbedPane1.getSelectedIndex()), true);
                            jButton1.setEnabled(true);
                            jTabbedPane2.setSelectedIndex(4);
                            mainContext.updateTabHelper(jTabbedPane1.getSelectedIndex(), jTabbedPane1.getTitleAt(jTabbedPane1.getSelectedIndex()), true);
                            pageController.showFramesOnSelectedTabScreen(jTabbedPane1, jLabel4, jLabel8, mainContext.getCurrentFrame());
                        }
                        case 2 -> {
                            mainContext.setReports(new String[2]);
                            mainContext.setReportOrdered(file);
                            pageController.showFramesOnSelectedTabScreen(jTabbedPane1, jLabel13, jLabel12, mainContext.getCurrentFrame());
                        }
                        default ->
                            throw new AssertionError("No tab with such index");
                    }

                }
            } else {
                mainContext.getPanelTabHelper().setCurrentTabProcessing(false);
            }
        } catch (DirectoryNotValidException | FileNotFoundException | ImageNotValidException | ReportNotValidException | HeadlessException ex) {
            LOG.log(Level.SEVERE, ex.getMessage(), ex);
            JOptionPane.showMessageDialog(jPanel1, ex.getMessage());
        }
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void jMenuItem5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem5ActionPerformed
        try {
            LOG.log(Level.INFO, evt.paramString());
            final var newFolder = fileEvent.onFileSelectionEvent(mainContext.getPanelTabHelper().isCurrentTabProcessing(), evt.getActionCommand(), true);
            if (newFolder != null) {
                this.savingFolder = newFolder;
                JOptionPane.showMessageDialog(jPanel1, "Diretório de salvamento foi alterado com sucesso.");
            } else {
                JOptionPane.showMessageDialog(jPanel1, "Diretório não foi alterado. Por favor, verifique algumas possibilidades: a seleção da pasta foi cancelada, tentando alterar a pasta durante o processamento ou erro na seleção da pasta.");
            }
        } catch (DirectoryNotValidException | FileNotFoundException | ImageNotValidException | ReportNotValidException | HeadlessException ex) {
            LOG.log(Level.SEVERE, ex.getMessage(), ex);
            JOptionPane.showMessageDialog(jPanel1, ex.getMessage());
        }
    }//GEN-LAST:event_jMenuItem5ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        try {
            LOG.log(Level.INFO, evt.getActionCommand());
            if (mainContext.getPalette().getFrames().iterator().hasNext()) {
                jLabel10.setText("");
                jLabel10.setIcon(null);
                jLabel9.setIcon(null);
                jLabel9.setText("");
                pageController.onFrameFinishedManual(jLabel1, jLabel2, mainContext.getCurrentFrame());
            } else {
                LOG.info("You've reached the end of the Palette.");
                pageController.onFrameFinishedManual(jLabel1, jLabel2, mainContext.getCurrentFrame());
                jButton3.setEnabled(false);
                jButton2.setEnabled(false);
                jLabel10.setText("");
                jLabel9.setIcon(null);
                mainContext.getPanelTabHelper().setCurrentTabProcessing(false);
                final Event event = this.manualEvent.execute(Constants.TAB_NAME_MANUAL_PT_BR, this.savingFolder.getPath());
                if (event.getFolder().isBlank()) {
                    LOG.log(Level.WARNING, "It wasn't possible to save the Palette on a file");
                    JOptionPane.showMessageDialog(jPanel1, "It wasn't possible to save the Palette on a file");
                } else {
                    LOG.log(Level.INFO, "The palette was saved successfully!");
                    JOptionPane.showMessageDialog(jPanel1, "A paleta foi salva com sucesso em: " + event.getFolder()
                            + Constants.QUEBRA_LINHA + "Total de ovos: " + mainContext.getPaletteToSave().getTheTotalNumberOfEggsPalette());
                }
            }
        } catch (ImageNotValidException | DirectoryNotValidException | HeadlessException ex) {
            LOG.log(Level.SEVERE, ex.getMessage(), ex);
            JOptionPane.showMessageDialog(jPanel1, ex.getMessage());
        }
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jLabel2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel2MouseClicked
        LOG.log(Level.INFO, "Evento: {0}", evt.getPoint());
        Icon frame, subFrame;
        Point pointClick = new Point(evt.getX(), evt.getY());
        try {
            frame = frameController.paintFormats(pointClick, mainContext.getCurrentFrame());
            subFrame = frameController.captureSubframe(pointClick,
                    mainContext.getCurrentFrame());
            if (frame != null) {
                pageController.showFramesOnSelectedTabScreen(jTabbedPane1, jLabel1, jLabel2, frame);
                pageController.showSubFrameOnSelectedTabScreen(jLabel10, jLabel9, subFrame, pointClick);
                LOG.log(Level.INFO, "Presenting frame on screen");
                String eggs = this.jLabel1.getText();
                if (eggs.contains(": ")) {
                    eggs = eggs.split(":")[0];
                    eggs = eggs.concat(": ").concat("" + mainContext.getCurrentFrame().getTotalNumberOfEggsFrame());
                } else {
                    eggs = eggs.concat(": ").concat("" + mainContext.getCurrentFrame().getTotalNumberOfEggsFrame());
                }

                this.jLabel1.setText(eggs);
                LOG.log(Level.INFO, "Counting eggs in frame: {0}", mainContext.getCurrentFrame().getName());
            }
        } catch (DirectoryNotValidException | ImageNotValidException | RegionNotValidException | CloneNotSupportedException ex) {
            LOG.log(Level.WARNING, ex.getMessage());
            JOptionPane.showMessageDialog(jPanel1, ex.getMessage());
        }
    }//GEN-LAST:event_jLabel2MouseClicked

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        LOG.log(Level.INFO, evt.getActionCommand());
        try {
            pageController.onPreviousFrameInAutomatic(jLabel4,
                    jTabbedPane2,
                    this.savingFolder,
                    mainContext.getPalette().getDirectory().getName());
        } catch (ImageNotValidException ex) {
            LOG.log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(jPanel1, ex.getMessage());
        } catch (ArrayIndexOutOfBoundsException ex) {
            LOG.log(Level.SEVERE, "Atingiu o início da paleta", ex);
            JOptionPane.showMessageDialog(jPanel1, ex.getMessage());
        } catch (DirectoryNotValidException ex) {
            LOG.log(Level.SEVERE, "The saving directory doesn't exist", ex);
            JOptionPane.showMessageDialog(jPanel1, onDirectoryErrorAutomaticMessage());
        }
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        try {
            LOG.log(Level.INFO, evt.getActionCommand());
            pageController.onNextFrameInAutomatic(jLabel4,
                    jTabbedPane2,
                    this.savingFolder,
                    mainContext.getPalette().getDirectory().getName());
        } catch (ImageNotValidException ex) {
            LOG.log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(jPanel1, ex.getMessage());
        } catch (ArrayIndexOutOfBoundsException ex) {
            LOG.log(Level.SEVERE, "Atingiu o fim da paleta", ex);
            JOptionPane.showMessageDialog(jPanel1, ex.getMessage());
        } catch (DirectoryNotValidException ex) {
            LOG.log(Level.SEVERE, "The saving directory doesn't exist", ex);
            JOptionPane.showMessageDialog(jPanel1, onDirectoryErrorAutomaticMessage());
        }
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        LOG.log(Level.INFO, evt.getActionCommand());
        final Event event;
        final String mensagem;
        jButton1.setText("Processando...");
        try {
            event = autoEvent.execute(
                    mainContext.getPalette(),
                    this.savingFolder.getAbsolutePath(),
                    mainContext.getPanelTabHelper().getTabName());
            // Ao final do processamento, liberar os botões e a mudança de tab
            mainContext.getPanelTabHelper().setCurrentTabProcessing(false);
            this.jTabbedPane2.setSelectedIndex(2); // abre a aba dos ovos encontrados desenhados
            mensagem = "O diretório foi criado sob o nome: " + event.getFolder() + Constants.QUEBRA_LINHA + "Total de ovos: " + event.getResult();
            JOptionPane.showMessageDialog(jPanel1, mensagem);
            this.savingFolder = new File(event.getFolder());
            mainContext.getPanelTabHelper().setCurrentTabProcessing(false);
            pageController.onNextFrameInAutomatic(jLabel4,
                    jTabbedPane2,
                    this.savingFolder,
                    mainContext.getPalette().getDirectory().getName());
            jLabel11.setText("Total de ovos: " + event.getResult());
            jButton4.setEnabled(true);
            jButton5.setEnabled(true);
            jButton1.setText("Iniciar");
        } catch (DirectoryNotValidException | ImageNotValidException | ArrayIndexOutOfBoundsException ex) {
            LOG.log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(jPanel1, ex.getMessage());
        } catch (AutomationExecutionException ex) {
            LOG.log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(jPanel1, Constants.NO_FOLDER_WAS_CREATED);
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jTabbedPane1StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jTabbedPane1StateChanged
        LOG.log(Level.INFO, evt.toString());
        System.out.println(jTabbedPane1.getSelectedIndex());
        try {
            if (mainContext.getPanelTabHelper().isCurrentTabProcessing()) {
                LOG.log(Level.WARNING, "Can''t change to Tab {0} {1} while {2} {3} is still processing", new Object[]{jTabbedPane1.getSelectedIndex(), jTabbedPane1.getTitleAt(jTabbedPane1.getSelectedIndex()), mainContext.getPanelTabHelper().getTabIndex(), mainContext.getPanelTabHelper().getTabName()});
                jTabbedPane1.setSelectedIndex(mainContext.getPanelTabHelper().getTabIndex());
            } else {
                mainContext.updateTabHelper(jTabbedPane1.getSelectedIndex(), jTabbedPane1.getTitleAt(jTabbedPane1.getSelectedIndex()), false);
                mainContext.setPalette(null);
                mainContext.setPaletteToSave(null);
                mainContext.setCurrentFrame(null);
                mainContext.setReports(new String[2]);
                cleanTabs();
                cleanTexts();
            }
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(jPanel1, ex.getMessage());
        }
    }//GEN-LAST:event_jTabbedPane1StateChanged

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        LOG.log(Level.INFO, evt.getActionCommand());
        try {
            int numberOfEvaluationReports = countEvaluationReports();
            if (numberOfEvaluationReports == 2) {
                this.pageController.onNextFrameInEvaluation(jLabel13, jLabel12,
                        mainContext.getPalette().getDirectory(),
                        mainContext.getReports());
                pageController.showFrameOnScreen(jLabel13, jLabel12,
                        this.frameController.paintFormats(mainContext.getCurrentFrame().clone(),
                                fileUtils.getRegionsInFrameFile(mainContext.getCurrentFrame().getName(),
                                        mainContext.getReports()[0]),
                                fileUtils.getPointsInFrameFile(mainContext.getCurrentFrame().getName(),
                                        mainContext.getReports()[1]))
                );
            }
        } catch (FileNotFoundException | ImageNotValidException | HeadlessException | CloneNotSupportedException | NumberFormatException ex) {
            LOG.log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(jPanel1, ex.getMessage());
        } catch (ArrayIndexOutOfBoundsException ex) {
            LOG.log(Level.SEVERE, "Atingiu o fim da paleta");
            JOptionPane.showMessageDialog(jPanel1, ex.getMessage());
        } catch (DirectoryNotValidException ex) {
            LOG.log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(jPanel1, onDirectoryErrorEvaluationMessage());
        }
    }//GEN-LAST:event_jButton7ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        LOG.log(Level.INFO, evt.getActionCommand());
        try {
            int numberOfEvaluationReports = countEvaluationReports();
            if (numberOfEvaluationReports == 2) {
                this.pageController.onPreviousFrameInEvaluation(jLabel13, jLabel12,
                        mainContext.getPalette().getDirectory(),
                        mainContext.getReports());
                pageController.showFrameOnScreen(jLabel13, jLabel12,
                        this.frameController.paintFormats(mainContext.getCurrentFrame().clone(),
                                fileUtils.getRegionsInFrameFile(mainContext.getCurrentFrame().getName(),
                                        mainContext.getReports()[0]),
                                fileUtils.getPointsInFrameFile(mainContext.getCurrentFrame().getName(),
                                        mainContext.getReports()[1]))
                );
            }
        } catch (ImageNotValidException | FileNotFoundException | HeadlessException | CloneNotSupportedException | NumberFormatException ex) {
            LOG.log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(jPanel1, ex.getMessage());
        } catch (ArrayIndexOutOfBoundsException ex) {
            LOG.log(Level.SEVERE, "Atingiu o início da paleta");
            JOptionPane.showMessageDialog(jPanel1, ex.getMessage());
        } catch (DirectoryNotValidException ex) {
            LOG.log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(jPanel1, onDirectoryErrorEvaluationMessage());
        }
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jMenuItem6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem6ActionPerformed
        LOG.log(Level.INFO, evt.getActionCommand());
        try {

            if (mainContext.getPanelTabHelper().getTabIndex() != 2) {
                throw new TabNotValidToEvaluationException(Constants.ERROR_TAB_NOT_VALID_TO_EVALUATE_ENG_1);
            }

            final File file = this.fileEvent.onFileSelectionEvent(mainContext.getPanelTabHelper().isCurrentTabProcessing(), evt.getActionCommand(), false);

            if (file != null) {
                mainContext.setReportOrdered(file);
                mainContext.updateTabHelper(jTabbedPane1.getSelectedIndex(), jTabbedPane1.getTitleAt(jTabbedPane1.getSelectedIndex()), false);
            }

            switch (countEvaluationReports()) {
                case 1 -> {
                    String msg = "Relatório adicionado.";
                    JOptionPane.showMessageDialog(jDesktopPane3, msg);
                }
                case 2 -> {
                    String msg = "Relatório adicionado.";
                    JOptionPane.showMessageDialog(jDesktopPane3, msg);
                    jButton6.setEnabled(false);
                    jButton7.setEnabled(false);
                    mainContext.updateTabHelper(jTabbedPane1.getSelectedIndex(), jTabbedPane1.getTitleAt(jTabbedPane1.getSelectedIndex()), true);
                    runEvaluation();
                    jButton6.setEnabled(true);
                    jButton7.setEnabled(true);
                    mainContext.updateTabHelper(jTabbedPane1.getSelectedIndex(), jTabbedPane1.getTitleAt(jTabbedPane1.getSelectedIndex()), false);
                }
                default -> {
                }
            }
            mainContext.updateTabHelper(jTabbedPane1.getSelectedIndex(), jTabbedPane1.getTitleAt(jTabbedPane1.getSelectedIndex()), false);
        } catch (DirectoryNotValidException | ImageNotValidException | FileNotFoundException | TabNotValidToEvaluationException | HeadlessException | CloneNotSupportedException ex) {
            LOG.log(Level.SEVERE, ex.getMessage(), ex);
            JOptionPane.showMessageDialog(jPanel1, ex.getMessage());
        } catch (ReportNotValidException ex) {
            LOG.log(Level.SEVERE, ex.getMessage(), ex);
            JOptionPane.showMessageDialog(jPanel1, onReportErrorEvaluationMessage());
        }
    }//GEN-LAST:event_jMenuItem6ActionPerformed

    private void jMenu1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenu1ActionPerformed
        LOG.log(Level.INFO, evt.getActionCommand());
    }//GEN-LAST:event_jMenu1ActionPerformed

    private void jCheckBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox1ActionPerformed
        LOG.log(Level.INFO, evt.getActionCommand());
        LOG.log(Level.INFO, "Medir por Pixel: {0}", jCheckBox1.isSelected());
        try {

            if (mainContext.getPanelTabHelper().getTabIndex() != 2) {
                throw new TabNotValidToEvaluationException(Constants.ERROR_TAB_NOT_VALID_TO_EVALUATE_ENG_1);
            }

        } catch (TabNotValidToEvaluationException ex) {
            LOG.log(Level.SEVERE, ex.getMessage(), ex);
            JOptionPane.showMessageDialog(jPanel1, ex.getMessage());
        }
    }//GEN-LAST:event_jCheckBox1ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;

                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainPage.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            Locale.setDefault(Locale.of("pt", "BR"));
            System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
            new MainPage().setVisible(true);
        });
    }
//<editor-fold defaultstate="collapsed" desc="Variables">
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JDesktopPane jDesktopPane1;
    private javax.swing.JDesktopPane jDesktopPane2;
    private javax.swing.JDesktopPane jDesktopPane3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem5;
    private javax.swing.JMenuItem jMenuItem6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTabbedPane jTabbedPane2;
    // End of variables declaration//GEN-END:variables
//</editor-fold>
}
