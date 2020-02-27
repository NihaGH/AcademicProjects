/**
 * 
 */
package com.lucene.ir.utility;

import java.nio.file.Files;
import java.nio.file.Paths;

import org.apache.lucene.search.similarities.BM25Similarity;
import org.apache.lucene.search.similarities.ClassicSimilarity;
import org.apache.lucene.search.similarities.Similarity;

import com.lucene.ir.utility.Constants.RANKING_MODEL;

/**
 * @author nihatompi
 * This class is used to create diifferent utility methods for this application
 */
public class Utilities {

	/**
	 * Checking and validating the parameters passed in the console.
	 * 
	 * @param args
	 * @return _mMessenger
	 */	
	public static Message checkSetArguments(String[] args) {

		Message _mMessenger = null;

		boolean _mStatus = false;
		String _mMsg = "";
		
		int argsLen = args.length;
		if (argsLen == 4 || argsLen == 3) {
			for (int i = 0; i < argsLen; i++) {
				String[] _arg = args[i].split("=");
				if (Constants.INDEX_PATH.equals(_arg[0])) {
					GlobalVariables.INDEX_PATH = Paths.get(_arg[1].trim());
				} else if (Constants.DOC_PATH.equals(_arg[0])) {
					GlobalVariables.DOCS_PATH = Paths.get(_arg[1].trim());
				} else if (Constants.RANKING_MODEL.equals(_arg[0])) {
					_mMessenger = validateRankingModel(_arg[1].trim().toUpperCase());
				} else if (Constants.QUERY_STRING.equals(_arg[0])) {
					GlobalVariables.QUERY_STRING = _arg[1].trim();
				}
			}
			
			// conditional check after validating Ranking Model
			if (_mMessenger == null || _mMessenger.is_status()) {
				if (GlobalVariables.INDEX_PATH == null || GlobalVariables.DOCS_PATH == null 
						|| GlobalVariables.QUERY_STRING == null) {
					
					_mMsg = Constants.ERROR_FINDING_ARGUMENT + Constants.NEW_LINE 
							+ Constants.CMD_LINE_FORMAT + Constants.NEW_LINE 
							+ Constants.CMD + Constants.NEW_LINE 
							+ Constants.MANDAT_FIELD + Constants.NEW_LINE
							+ Constants.OPT_FIELD + Constants.NEW_LINE
							+ Constants.PLEASE_TRY_AGAIN;
					_mStatus = false;
				} else {

					// validate the doc dirctory
					if (!Files.isReadable(GlobalVariables.DOCS_PATH)) {
						_mMsg = Constants.DOC_DIR_ERR_1 + GlobalVariables.DOCS_PATH.toAbsolutePath()
								+ Constants.DOC_DIR_ERR_2 + Constants.NEW_LINE + Constants.PROVIDE_VALID_PATH;
						_mStatus = false;
					} else {

						_mMsg = "INDEX_PATH :: " + GlobalVariables.INDEX_PATH 
								+ "\nDOCS_PATH :: " + GlobalVariables.DOCS_PATH
								+ "\nRANKING MODEL :: " + GlobalVariables.R_MODEL
								+ "\nQUERY_STRING :: " + GlobalVariables.QUERY_STRING;

						_mStatus = true;
					}
				}

				_mMessenger = new Message(_mStatus, _mMsg);
			}
			
		} else {
			_mMsg = Constants.ERROR_PASSING_ARGUMENT + Constants.NEW_LINE 
					+ Constants.CMD_LINE_FORMAT + Constants.NEW_LINE 
					+ Constants.CMD + Constants.NEW_LINE 
					+ Constants.MANDAT_FIELD + Constants.NEW_LINE
					+ Constants.OPT_FIELD + Constants.NEW_LINE
					+ Constants.PLEASE_TRY_AGAIN;
			_mStatus = false;
			_mMessenger = new Message(_mStatus, _mMsg);
		}

		return _mMessenger;
	}
	
	/**
	 * Checks whether the input ranking model is a valid one.
	 * 
	 * @param rankingModel
	 * @return _mMessenger
	 */	
	private static Message validateRankingModel(String rankingModel) {
		// TODO Auto-generated method stub

		Message _mMessenger = null;

		try {
			GlobalVariables.R_MODEL = RANKING_MODEL.valueOf(rankingModel);
			_mMessenger = new Message(true, Constants.RANKING_MODEL_SELECTION);
		} catch (IllegalArgumentException e) {
			// TODO: handle exception
			String _msg = e.getLocalizedMessage() + Constants.NEW_LINE + Constants.VALID_RANKING_MODELS;
			for (RANKING_MODEL rm : RANKING_MODEL.values()) {
				_msg += rm + ", ";
			}

			_mMessenger = new Message(false, _msg.substring(0, _msg.length() - 2));
		}

		return _mMessenger;
	}
	
	/**
	 * Return the similarity to be set for indexing and searching as per the user
	 * input
	 * 
	 * @return Similarity
	 */
	public static Similarity getSimilarity() {
		Similarity _similarity;
		switch(GlobalVariables.R_MODEL) {
		case VS:
			_similarity =  new ClassicSimilarity();
			break;
		case OK:
		default:
			_similarity = new BM25Similarity();
			break;
		}		
		return _similarity;
	}
}
