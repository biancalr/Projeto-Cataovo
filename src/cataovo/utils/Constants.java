/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cataovo.utils;

/**
 * Constants used in the project.
 *
 * @author Bianca Leopoldo Ramos
 */
public class Constants {
    
    public static final double[] WHITE = new double[]{255};
    public static final double[] BLACK = new double[]{0};

    /**
     * APPLICATION FOLDER
     */
    public static final String APPLICATION_FOLDER = "/cataovo/";
    
    /**
     * REPORT FILE NAME
     */
    public static final String REPORT_FILE_NAME = "Report";
    
    /**
     * FRAME ORIGINAL NAME TAG
     */
    public static final String FRAME_ORIGINAL_NAME_TAG = "original";
    
    /**
     * ACTION COMMAND ABRIR PASTA
     */
    public static final String ITEM_ACTION_COMMAND_OPEN_PALETTE_PT_BR = "Abrir Paleta";
    /**
     * ACTION COMMAND SELECIONAR PASTA DESTINO
     */
    public static final String ITEM_ACTION_COMMAND_SELECT_DESTINATION_FOLDER_PT_BR = "Selecionar Pasta Destino";

    /**
     * ACTION COMMAND SELECIONAR RELATORIO
     */
    public static final String ITEM_ACTION_COMMAND_SELECT_REPORT_PT_BR = "Selecionar Relatório";

    /**
     * TAB NAME MANUAL
     */
    public static final String TAB_NAME_MANUAL_PT_BR = "Gerador Padrão Ouro";
    /**
     * TAB NAME AUTOMATICO
     */
    public static final String TAB_NAME_AUTOMATIC_PT_BR = "Automático";
    /**
     * TAB NAME AVALIACAO
     */
    public static final String TAB_NAME_EVALUATION_PT_BR = "Avaliação";

    /**
     * NO PALETTE SELECTED
     */
    public static final String NO_PALETTE_SELECTED = "Escolha uma pasta";
    
    /**
     * NO ARTIFACT SELECTED
     */
    public static final String NO_ARTIFACT_SELECTED = "Escolha os Artefatos";

    /**
     * BREAK LINE.
     */
    public static final String QUEBRA_LINHA = "\n";

    /**
     * original.png
     */
    public static final String ORIGINAL_PNG = "/original.png";
    /**
     * blur.png
     */
    public static final String BLUR_PNG = "/blur.png";
    /**
     * binary.png
     */
    public static final String BINARY_PNG = "/binary.png";
    /**
     * morph.png
     */
    public static final String MORPH_PNG = "/morph.png";
    /**
     * contours.png
     */
    public static final String CONTOURS_PNG = "/contours.png";

    public static final int STEPS_TO_POINT_SAVING = 5;

    public static final int FRAME_SLOT_TO_PROCESS = 10;
    public static final int SLOT_FRAMES_TO_PROCESS_ON_PALETTE = 30;

    /**
     * Custom error message to REGION NOT VALID
     */
    public static final String ERROR_REGION_NOT_VALID_ENG_1 = "Axis(ies) with difference too small (less than 35px) or too large (more than 110px)";

    /**
     * Custom error message to DIRECTORY NOT VALID
     */
    public static final String ERROR_DIRECTORY_NOT_VALID_ENG_1 = "The file path does not represent a valid directory";

    /**
     * Custom error message to IMAGE NOT VALID
     */
    public static final String ERROR_IMAGE_NOT_VALID_ENG_1 = "The file does not represent a valid image: *.jpg or *.png";

    /**
     * Custom error message to TAB NOT VALID TO EVALUATE
     */
    public static final String ERROR_TAB_NOT_VALID_TO_EVALUATE_ENG_1 = "Must select Evaluation tab";

    /**
     * MINUMUM AXIS X DISTANCE ON REGION
     */
    public static final int MINUMUM_AXIS_X_DISTANCE_ON_REGION = 30;

    /**
     * MINUMUM AXIS Y DISTANCE ON REGION
     */
    public static final int MINUMUM_AXIS_Y_DISTANCE_ON_REGION = 30;

    /**
     * MAXIMUM AXIS X DISTANCE ON REGION
     */
    public static final int MAXIMUM_AXIS_X_DISTANCE_ON_REGION = 110;

    /**
     * MAXIMUM AXIS Y DISTANCE ON REGION
     */
    public static final int MAXIMUM_AXIS_Y_DISTANCE_ON_REGION = 110;

    /**
     * Error message when a folder wasn't created
     */
    public static final String NO_FOLDER_WAS_CREATED = "No folder was created";

    /**
     * Warning message when a report were aldeady selected but there is a trying
     * to add another one.
     */
    public static final String POSITION_OCCUPIED_EN = "position already occupied";

    /**
     * Warning message when both reports were aldeady selected but there is a
     * trying to add one.
     */
    public static final String REPORTS_ALREADY_SELECTED_PT_BR = "Relatórios já foram selecionados.";

    /**
     * Tag name added to help dealing with too small frame name tags.
     */
    public static final String FRAME_ID_TAG = "frame_";

    /**
     * When reports selected for the same Palette are not equal in number of
     * lines (Frames)
     */
    public static final String REPORTS_FRAME_QUANTITY_NOT_EQUAL_PT_BR = "Os relatórios não têm a mesma quatidade de Frames";

    /**
     * Rect format = 1
     */
    public static final int RECT = 1;

    /**
     * Circle format = 2
     */
    public static final int CIRCLE = 2;

    /**
     * PERCENTAGE OF TRUE POSITIVE
     */
    public static final int CALCULATE_METHOD_RECALL = 0;
    /**
     * PERCENTAGE OF FALSE POSITIVE
     */
    public static final int CALCULATE_METHOD_FALSE_POSITIVE = 1;
    /**
     * PERCENTAGE OF TRUE NEGATIVE
     */
    public static final int CALCULATE_METHOD_TRUE_NEGATIVE = 2;
    /**
     * PERCENTAGE OF FALSE NEGATIVE
     */
    public static final int CALCULATE_METHOD_FALSE_NEGATIVE = 3;
    /**
     * PERCENTAGE OF PRECISION
     */
    public static final int CALCULATE_METHOD_PRECISION = 4;
    /**
     * PERCENTAGE OF ACCURACY
     */
    public static final int CALCULATE_METHOD_ACCURACY = 5;
    
    /**
     * PERCENTAGE OF SPECIFICITY
     */
    public static final int CALCULATE_METHOD_SPECIFICITY = 6;
    
    /**
     * PERCENTAGE OF PRECISION
     */
    public static final int CALCULATE_METHOD_F1_SCORE = 7;
    
    
    /**
     * Type value indicating that the <code>JFileChooser</code> supports an
     * "Open" file operation.
     */
    public static final int OPEN_DIALOG = 0;

    /**
     * Type value indicating that the <code>JFileChooser</code> supports a
     * "Save" file operation.
     */
    public static final int SAVE_DIALOG = 1;
    
    /**
     * ROOT C FOLDER
     */
    public static final String ROOT_FOLDER="C:";
    
    /**
     * PROCESSING MODE NAME 'AUTO'
     */
    public static final String PROCESSING_MODE_NAME_AUTO = "auto"; 
    
    /**
     * DECIMAL FORMAT PATTERN
     */
    public static final String DECIMAL_FORMAT = "#,##0.00";
    
    /**
     * A sharp ('#') separator. Separetes an egg from other
     */
    public static String OBJECT_SEPARATOR = "#";
    
    /**
     * A comma (',') separator.
     */
    public static String SEPARATOR = ",";
    
    /**
     * Text measure by pixel
     */
    public static String CHECKBOX_EVALUATION_BY_PIXEL = "Medir por pixel";

}
