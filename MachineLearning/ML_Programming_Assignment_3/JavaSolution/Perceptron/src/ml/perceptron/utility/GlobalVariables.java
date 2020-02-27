/**
 * 
 */
package ml.perceptron.utility;

import java.util.ArrayList;
import java.util.List;

public class GlobalVariables {

	public static String PARENT_DIR;
	public static String INPUT_FILE_NAME;
	public static String OUTPUT_FILE_NAME;
	public static String OUTPUT_FOLDER = "Solutions/";
	public static int EPOCHS = 100;
	
	//public static enum CLASS_TYPE_ENUM {CLR, ALR};
	public static int TYPE = 2;
	public static enum CLASS_ENUM {B, A};
	
	public static double LEARNING_RATE = 1.0;
	
	
	// 
	public static List<double[]> INPUT_VAL = new ArrayList<>();
	public static List<int[]> OUTPUT_VAL = new ArrayList<>();	
}