/**
 * 
 */
package com.lucene.ir.utility;

import java.nio.file.Path;

import com.lucene.ir.utility.Constants.RANKING_MODEL;

/**
 * @author nihatompi
 * This class is used to store all the values provided by the user as the input.
 */
public class GlobalVariables {
	
	public static String QUERY_STRING = null;
	public static Path DOCS_PATH = null;
	public static Path INDEX_PATH = null;
	public static RANKING_MODEL R_MODEL = RANKING_MODEL.OK;
	public static int N_TOP_REC = 10;	
}
