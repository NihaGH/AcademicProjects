/**
 * 
 */
package com.lucene.ir.manager;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Date;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.jsoup.Jsoup;

import com.lucene.ir.utility.Constants;
import com.lucene.ir.utility.GlobalVariables;
import com.lucene.ir.utility.Message;
import com.lucene.ir.utility.Utilities;

/**
 * Handles indexing of files Index all text files and HTML files under a given
 * directory.
 * 
 * @author nihatompi
 * 
 */
public class LuceneIndexingManager {

	private static Message mMessenger;

	/**
	 * Here the indexing process starts
	 * 
	 * @return mMessenger
	 */
	public static Message startIndexing() {
		System.out.println(Constants.INDEXING_START_MESSAGE + Constants.SPACE + Constants.SINGLE_QUOTES
				+ GlobalVariables.DOCS_PATH + Constants.SINGLE_QUOTES + Constants.DOTS);
		try {

			Date _start = new Date();
			Analyzer _analyzer = new EnglishAnalyzer();
			IndexWriterConfig _iwc = new IndexWriterConfig(_analyzer);
			if (Files.notExists(GlobalVariables.INDEX_PATH)) {
				// Create a new index in the directory, removing any previously
				// indexed documents
				_iwc.setOpenMode(OpenMode.CREATE);
			} else {
				// Add new documents to an existing index
				_iwc.setOpenMode(OpenMode.CREATE_OR_APPEND);
			}

			/*
			 * Creating index in directory
			 */
			Directory _dir = FSDirectory.open(GlobalVariables.INDEX_PATH);
			_iwc.setSimilarity(Utilities.getSimilarity());

			IndexWriter _iw = new IndexWriter(_dir, _iwc);
			indexFiles(_iw);
			_iw.close();
			Date _end = new Date();

			// calculate the time taken for indexing
			if (mMessenger.is_status()) {

				String _msg = mMessenger.get_msg() + Constants.NEW_LINE + Constants.TOTAL_INDEXING_TIME
						+ Constants.COLON + (_end.getTime() - _start.getTime());

				// set the message back
				mMessenger.set_msg(_msg);
			}

		} catch (IOException e) {
			setMessenger(false, e.getMessage());
		}

		return mMessenger;
	}

	/**
	 * Indexes the given file using the given writer, or if a directory is given,
	 * recurses over files and directories found under the given directory.
	 * 
	 * @param indexWriter Writer to the index where the given file/dir info will be
	 *                    stored
	 */
	private static void indexFiles(IndexWriter indexWriter) {
		// TODO Auto-generated method stub
		try {
			if (Files.isDirectory(GlobalVariables.DOCS_PATH)) {
				Files.walkFileTree(GlobalVariables.DOCS_PATH, new SimpleFileVisitor<Path>() {

					@Override
					public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
						// TODO Auto-generated method stub
						if (Files.isReadable(file)) {
							indexFile(indexWriter, file, attrs.lastModifiedTime().toMillis());
						} else {
							String _msg = Constants.UNABLE_TO_READ_FILE + Constants.COLON + file.toAbsolutePath();
							setMessenger(false, _msg);
						}
						return super.visitFile(file, attrs);
					}
				});
			} else {
				if (Files.isReadable(GlobalVariables.DOCS_PATH)) {
					indexFile(indexWriter, GlobalVariables.DOCS_PATH,
							Files.getLastModifiedTime(GlobalVariables.DOCS_PATH).toMillis());
				} else {
					String _msg = Constants.UNABLE_TO_READ_FILE + Constants.COLON
							+ GlobalVariables.DOCS_PATH.toAbsolutePath();
					setMessenger(false, _msg);
				}
			}
		} catch (IOException e) {
			setMessenger(false, e.getMessage());
		}

	}

	/**
	 * Index each document
	 */
	protected static void indexFile(IndexWriter indexWriter, Path filePath, long lastModifiedDate) {

		try {
			InputStream _stream;
			String _fileTitle = "";
			String _fileName = filePath.getFileName().toString();
			
			if(_fileName.endsWith("html") | _fileName.endsWith("htm")) {
				org.jsoup.nodes.Document _docJsoup = Jsoup.parse(new File(filePath.toString()), "UTF-8");
				_fileTitle = _docJsoup.title();
				String _content = _fileTitle + "\n" + _docJsoup.body().text();
				_stream = new ByteArrayInputStream(_content.getBytes(StandardCharsets.UTF_8));
			} else {
				_stream = Files.newInputStream(filePath);
			}
			
			// document represents a virtual document with Fields
			Document _doc = new Document();
			
			// Field represents the key value pair relationship where a key is
			// used to identify the value to be indexed.
			// Create fields in the document.
			Field _fieldPath = new StringField(Constants.FIELD_PATH, filePath.toString(), Field.Store.YES);
			Field _lastModifiedData = new StringField(Constants.FIELD_LAST_MODIFIED, String.valueOf(lastModifiedDate),
					Field.Store.YES);
			Field _fieldName = new StringField(Constants.FIELD_FILE_NAME, _fileName, Field.Store.YES);
			Field _fieldTitle = new StringField(Constants.FIELD_TITLE, _fileTitle, Field.Store.YES);
			Field _content = new TextField(Constants.FIELD_CONTENT,
					new BufferedReader(new InputStreamReader(_stream, StandardCharsets.UTF_8)));
			
			// Adding created fields to the document.
			_doc.add(_fieldPath);
			_doc.add(_lastModifiedData);
			_doc.add(_fieldName);
			_doc.add(_fieldTitle);
			_doc.add(_content);
			
			if (indexWriter.getConfig().getOpenMode() == OpenMode.CREATE) {
				// New index, adding new document:
				System.out.println(Constants.TAB + Constants.ADD_INDEX_FILE + Constants.SPACE + filePath);
				indexWriter.addDocument(_doc);
			} else if (indexWriter.getConfig().getOpenMode() == OpenMode.CREATE_OR_APPEND) {
				System.out.println(Constants.TAB + Constants.UPDATE_INDEX_FILE + Constants.SPACE + filePath);
				indexWriter.updateDocument(new Term(Constants.FIELD_PATH, filePath.toString()), _doc);// Delete
			}
			setMessenger(true, Constants.INDEXING_COMPLETED_MESSAGE);
		} catch (Exception e) {
			setMessenger(false, e.getMessage());
		}
	}

	private static void setMessenger(boolean b, String message) {
		// TODO Auto-generated method stub
		mMessenger = new Message(b, message);
	}
}
