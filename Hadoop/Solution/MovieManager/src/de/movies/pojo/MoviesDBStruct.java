/**
 * 
 */
package de.movies.pojo;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;

/**
 * @author nihatompi
 *
 */
public class MoviesDBStruct implements Writable {
	
	private Text m_id = new Text();
	private Text m_genres = new Text();
	private Text m_imdb_id = new Text();
	private Text m_original_title = new Text();
	private Text m_overview = new Text();
	private Text m_release_date = new Text();
	private Text m_cinema_revenue = new Text();
	private Text m_runtime = new Text();
	private Text m_title = new Text();
	
	/**
	 * 
	 */
	public MoviesDBStruct() {
		super();
	}
	
	

	/**
	 * @param m_id
	 * @param m_genres
	 * @param m_imdb_id
	 * @param m_original_title
	 * @param m_overview
	 * @param m_release_date
	 * @param m_cinema_revenue
	 * @param m_runtime
	 * @param m_title
	 */
	public MoviesDBStruct(Text m_id, Text m_genres, Text m_imdb_id, Text m_original_title, Text m_overview,
			Text m_release_date, Text m_cinema_revenue, Text m_runtime, Text m_title) {
		super();
		this.m_id = m_id;
		this.m_genres = m_genres;
		this.m_imdb_id = m_imdb_id;
		this.m_original_title = m_original_title;
		this.m_overview = m_overview;
		this.m_release_date = m_release_date;
		this.m_cinema_revenue = m_cinema_revenue;
		this.m_runtime = m_runtime;
		this.m_title = m_title;
	}

	public Text getM_id() {
		return m_id;
	}

	public void setM_id(Text _m_id) {
		this.m_id = _m_id;
	}

	public Text getM_genres() {
		return m_genres;
	}

	public void setM_genres(Text _m_genres) {
		this.m_genres = _m_genres;
	}

	public Text getM_imdb_id() {
		return m_imdb_id;
	}

	public void setM_imdb_id(Text _m_imdb_id) {
		this.m_imdb_id = _m_imdb_id;
	}

	public Text getM_original_title() {
		return m_original_title;
	}

	public void setM_original_title(Text _m_original_title) {
		this.m_original_title = _m_original_title;
	}

	public Text getM_overview() {
		return m_overview;
	}

	public void setM_overview(Text _m_overview) {
		this.m_overview = _m_overview;
	}
	public Text getM_release_date() {
		return m_release_date;
	}

	public void setM_release_date(Text _m_release_date) {
		this.m_release_date = _m_release_date;
	}

	public Text getM_cinema_revenue() {
		return m_cinema_revenue;
	}

	public void setM_cinema_revenue(Text _m_cinema_revenue) {
		this.m_cinema_revenue = _m_cinema_revenue;
	}

	public Text getM_runtime() {
		return m_runtime;
	}

	public void setM_runtime(Text _m_runtime) {
		this.m_runtime = _m_runtime;
	}

	public Text getM_title() {
		return m_title;
	}

	public void setM_title(Text _m_title) {
		this.m_title = _m_title;
	}

	//get
	@Override
	public void readFields(DataInput _dataIn) throws IOException {
		// TODO Auto-generated method stub
		this.m_id.readFields(_dataIn);
		this.m_genres.readFields(_dataIn);
		this.m_imdb_id.readFields(_dataIn);
		this.m_original_title.readFields(_dataIn);
		this.m_overview.readFields(_dataIn);
		this.m_release_date.readFields(_dataIn);
		this.m_cinema_revenue.readFields(_dataIn);
		this.m_runtime.readFields(_dataIn);
		this.m_title.readFields(_dataIn);
	}
	
	//set
	@Override
	public void write(DataOutput _dataOut) throws IOException {
		// TODO Auto-generated method stub
		this.m_id.write(_dataOut);
		this.m_genres.write(_dataOut);
		this.m_imdb_id.write(_dataOut);
		this.m_original_title.write(_dataOut);
		this.m_overview.write(_dataOut);
		this.m_release_date.write(_dataOut);
		this.m_cinema_revenue.write(_dataOut);
		this.m_runtime.write(_dataOut);
		this.m_title.write(_dataOut);
	}
	
}
