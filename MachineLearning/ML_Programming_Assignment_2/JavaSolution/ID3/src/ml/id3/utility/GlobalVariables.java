/**
 * 
 */
package ml.id3.utility;

import org.json.JSONArray;
import org.json.JSONObject;

public class GlobalVariables {

	public static String PARENT_DIR;
	public static String INPUT_FILE_NAME;
	public static String OUTPUT_FILE_NAME;
	public static String OUTPUT_FOLDER = "Solutions/";
	
	public static String ATTRIBUTE = "attr";
	public static String TARGET_ATTRIBUTE = "target_attr";
	
	public static JSONObject INPUT_VAL = new JSONObject();
	public static JSONObject OUTPUT_VAL = new JSONObject();
	
	public static int LOG_BASE = 2;
	
	public static JSONArray CLASS_LABEL_VALUES = new JSONArray();
	
	// for creating the nodes
	public static String ENTROPY = "entropy";
	public static String FEATURE = "feature";
	public static String VALUE = "value";
	public static String LABEL = "label";
	public static String NODE = "node";
	public static String TREE = "tree";
	
}