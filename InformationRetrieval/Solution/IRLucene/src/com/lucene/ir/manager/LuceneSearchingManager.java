
/**
 * 
 */
package com.lucene.ir.manager;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;

import com.lucene.ir.utility.Constants;
import com.lucene.ir.utility.GlobalVariables;
import com.lucene.ir.utility.Message;
import com.lucene.ir.utility.Utilities;

/**
 * @author nihatompi Used for initiating search process through all the indexes
 *         created earlier.
 */
public class LuceneSearchingManager {

	/**
	 * Stores a message in common
	 */
	private static Message mMessenger;

	/**
	 * Starts the searching process
	 */
	public static Message startSearching() {
		// TODO Auto-generated method stub
		boolean _isInvalid = GlobalVariables.QUERY_STRING.matches(Constants.SPECIAL_CHAR);
		if (_isInvalid) {
			mMessenger = new Message(false, Constants.IMPROPER_SEARCH_QUERY);
		} else {

			System.out.println(Constants.SEARCHING_START_MESSAGE + Constants.SINGLE_QUOTES
					+ GlobalVariables.QUERY_STRING + Constants.SINGLE_QUOTES + Constants.DOTS);
			try {
				Date _start = new Date();
				IndexReader _indexReader = DirectoryReader.open(FSDirectory.open(GlobalVariables.INDEX_PATH));
				IndexSearcher _indexSearcher = new IndexSearcher(_indexReader);
				_indexSearcher.setSimilarity(Utilities.getSimilarity());

				Analyzer _analyzer = new EnglishAnalyzer();
				QueryParser _queryParser = new QueryParser(Constants.FIELD_CONTENT, _analyzer);
				Query _query = _queryParser.parse(GlobalVariables.QUERY_STRING);
				search(_indexSearcher, _query);
				_indexReader.close();
				Date _end = new Date();

				// calculate the time taken for searching
				if (mMessenger.is_status()) {

					String _msg = mMessenger.get_msg() + Constants.NEW_LINE + Constants.TOTAL_SEARCHING_TIME
							+ Constants.COLON + (_end.getTime() - _start.getTime());

					// set the message back
					mMessenger.set_msg(_msg);
				}

			} catch (IOException | ParseException e) {
				// TODO Auto-generated catch block
				mMessenger = new Message(false, e.getMessage());
			}
		}

		return mMessenger;
	}

	private static void search(IndexSearcher indexSearcher, Query query) {
		// TODO Auto-generated method stub
		try {
			TopDocs _topDocs = indexSearcher.search(query, GlobalVariables.N_TOP_REC);
			ScoreDoc[] _scoreDocs = _topDocs.scoreDocs;
			int _totalHits = Math.toIntExact(_topDocs.totalHits);
			System.out.println(Constants.TOTAL_MATCHING_DOC + Constants.COLON + _totalHits);

			if (_totalHits > GlobalVariables.N_TOP_REC)
				System.out.println(
						GlobalVariables.N_TOP_REC + Constants.SPACE + Constants.MOST_RELEVANT_DOC + Constants.COLON);
			else
				System.out.println(_totalHits + Constants.SPACE + Constants.MOST_RELEVANT_DOC + Constants.COLON);

			System.out.println(Constants.TAB + "||" + Constants.SEPARATOR);
			System.out.println(Constants.TAB + "||" + Constants.TAB + Constants.DOCUMENT_RANK + Constants.TAB
					+ Constants.DOCUMENT_RELEVANCE_SCORE + Constants.TAB + Constants.DOCUMENT_NAME + Constants.TAB
					+ Constants.DOCUMENT_TITLE + Constants.TAB + Constants.TAB + Constants.DOCUMENT_LAST_MODIFIED
					+ Constants.TAB + Constants.DOCUMENT_PATH);

			Document _doc = new Document();
			SimpleDateFormat _simpleDateFormat = new SimpleDateFormat(Constants.DATE_FORMAT);

			for (int i = 0; i < GlobalVariables.N_TOP_REC; i++) { // GlobalVariables.N_TOP_REC; i++) {
																	// _scoreDocs.length; i++) {
				_doc = indexSearcher.doc(_scoreDocs[i].doc);
				String _path = _doc.get(Constants.FIELD_PATH);

				if (_path != null) { // if (!_path.equals(null)) {// -- not working

					System.out.print(Constants.TAB + "||" + Constants.TAB + (i + 1));
					System.out.print(Constants.TAB + Constants.TAB + _scoreDocs[i].score);
					System.out.print(Constants.TAB + Constants.TAB + _doc.get(Constants.FIELD_FILE_NAME));

					String _doc_title = _doc.get(Constants.FIELD_TITLE);
					_doc_title = _doc_title.isEmpty() ? Constants.TAB : _doc_title;
					System.out.print(Constants.TAB + _doc_title);

					Date date = new Date(Long.parseLong(_doc.get(Constants.FIELD_LAST_MODIFIED)));
					System.out.print(Constants.TAB + Constants.TAB + _simpleDateFormat.format(date));

					System.out.print(Constants.TAB + _path + Constants.NEW_LINE);

				} else {
					System.err.println(Constants.TAB + "||" + Constants.TAB + (i + 1) + Constants.TAB + Constants.TAB
							+ Constants.DOCUMENT_NO_PATH);
				}
			}
			System.out.println(Constants.TAB + "||" + Constants.SEPARATOR);
			mMessenger = new Message(true, Constants.SEARCHING_COMPLETED_MESSAGE);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			mMessenger = new Message(false, e.getMessage());
		}
	}

}
