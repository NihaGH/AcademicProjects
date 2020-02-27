/**
 * 
 */
package com.lucene.ir.utility;

/**
 * @author nihatompi
 * All Static Data are defined here
 */
public interface Constants {
	
	// Ranking models
	public static enum RANKING_MODEL {
		VS, OK
	}
	
	// Constants argument names passed
	public static final String INDEX_PATH = "-index";
	public static final String DOC_PATH = "-docs";
	public static final String RANKING_MODEL = "-model";
	public static final String QUERY_STRING = "-query";
	//public static final String CHAR_SET = "UTF-8";

	// Basic Messages
	public static final String NEW_LINE = "\n";
	public static final String TAB = "\t";
	public static final String SINGLE_QUOTES = "'";
	public static final String SPACE = " ";
	public static final String DOTS = "...";
	public static final String COLON = ": ";
	public static final String SPECIAL_CHAR = "[-/@#$%^&_+=()!{};.*,<>?':|]";
	public static final String DATE_FORMAT = "MM/dd/yyyy HH:mm:ss";
	public static final String SEPARATOR = "============================================================";

	// General Messages
	public static final String USER_INPUT_MESSAGE = "****\tThe user inputs are as follows: \t****";
	public static final String WELCOME_MESSAGE = "****\tWelcome To Information Retieval System\t****";
	public static final String EXITING = "****\tExecution Successfully Completed\t****";
	
	// Warning Messages
	public static final String INVALID_NO_ARGUMENTS = "Invalid number of arguments passed.";
	public static final String PLEASE_TRY_AGAIN = "Please Try Again!!";
	public static final String ERROR_FINDING_ARGUMENT = "Error in finding the arguments in the command line.";
	public static final String ERROR_PASSING_ARGUMENT = "Error in passing the number arguments in the command line.";
	public static final String CMD_LINE_FORMAT = "The command line should be in the format: ";
	public static final String CMD = "java -jar <<jar_file_name.jar>> -docs=DOCS_PATH -index=INDEX_PATH -model=RANKING_MODEL -query=QUERY_STRING";
	public static final String OPT_FIELD = RANKING_MODEL + ": is optional field which will take OK ranking model as default one.";
	public static final String MANDAT_FIELD = DOC_PATH +": is the path of the files to be indexed.\n"
			+ INDEX_PATH + ": the path of the folder where the created indexes of the provided documents are stored.\n"
			+ QUERY_STRING + ": the query that need to be searched in the provided documents.";
	public static final String PROVIDE_VALID_PATH = "Please provide a valid path.";
	public static final String DOC_DIR_ERR_1 = "Document Directory '";
	public static final String DOC_DIR_ERR_2 = "' does not exists or is not readable.";
	
	// Ranking Model
	public static final String RANKING_MODEL_SELECTION = "Ranking Model Sucessfully selected.";
	public static final String VALID_RANKING_MODELS = "The valid Ranking Models are: ";	

	// Indexing Messages
	public static final String INDEXING_START_MESSAGE = "Started indexing of directory";
	public static final String UNABLE_TO_READ_FILE = "Unable to read file from the path";

	public static final String FIELD_PATH = "FilePath";
	public static final String FIELD_FILE_NAME = "FileName";
	public static final String FIELD_TITLE = "FileTitle";
	public static final String FIELD_LAST_MODIFIED = "LastModified";
	public static final String FIELD_CONTENT = "Content";

	public static final String ADD_INDEX_FILE = "Adding index file:";
	public static final String UPDATE_INDEX_FILE = "Updating index file:";
	public static final String INDEXING_COMPLETED_MESSAGE = "Indexing Completed.";

	public static final String TOTAL_INDEXING_TIME = "Total time for indexing (in MS)";

	// Searching Messages
	public static final String IMPROPER_SEARCH_QUERY = "Improper Search Query.";
	public static final String SEARCHING_START_MESSAGE = "Started searching for ";
	public static final String SEARCHING_COMPLETED_MESSAGE = "Searching completed with relevant data.";
	public static final String TOTAL_SEARCHING_TIME = "Total time for searching (in MS)";
	public static final String TOTAL_MATCHING_DOC = "Total number of matching documents";
	public static final String MOST_RELEVANT_DOC = "Most relevant documents";

	public static final String DOCUMENT_RANK = "Doc_Rank";
	public static final String DOCUMENT_PATH = "Doc_Path";
	public static final String DOCUMENT_NAME = "Doc_Name";
	public static final String DOCUMENT_TITLE = "Doc_Title";
	public static final String DOCUMENT_LAST_MODIFIED = "Doc_Last_Modified";
	public static final String DOCUMENT_RELEVANCE_SCORE = "Doc_Relevance_Score";
	public static final String DOCUMENT_NO_PATH = "No Path field available at search result";

}
