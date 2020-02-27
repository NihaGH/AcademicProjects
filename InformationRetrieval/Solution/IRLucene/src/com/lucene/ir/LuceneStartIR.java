/**
 * This is just a basic simple flow
 * Need to change according to the Programming requirements
 * 
 */
package com.lucene.ir;

import com.lucene.ir.manager.LuceneIndexingManager;
import com.lucene.ir.manager.LuceneSearchingManager;
import com.lucene.ir.utility.Constants;
import com.lucene.ir.utility.Message;
import com.lucene.ir.utility.Utilities;

/**
 * @author nihatompi
 * The main class from where the entire program starts working
 */
public class LuceneStartIR {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		Message _mMsg = Utilities.checkSetArguments(args);

		if (_mMsg.is_status()) {
			System.out.println(Constants.USER_INPUT_MESSAGE);
			System.out.println(_mMsg.get_msg());
			System.out.print(Constants.NEW_LINE);
			System.out.println(Constants.WELCOME_MESSAGE);

			// call for indexing the files
			_mMsg = LuceneIndexingManager.startIndexing();

			if (_mMsg.is_status()) {
				System.out.println(_mMsg.get_msg());
				System.out.print(Constants.NEW_LINE);
				
				// call for searching the query in the given docs
				_mMsg = LuceneSearchingManager.startSearching();
				
				if (_mMsg.is_status()) {
					System.out.println(_mMsg.get_msg());
					System.out.print(Constants.NEW_LINE);
					System.out.println(Constants.EXITING);
				} else {
					System.err.println(_mMsg.get_msg());
					System.exit(0);
				}
				
			} else {
				System.err.println(_mMsg.get_msg());
				System.exit(0);
			}

		} else {
			System.err.println(_mMsg.get_msg());
			System.exit(0);
		}

	}

}
