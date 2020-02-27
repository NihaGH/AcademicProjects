/**
 * 
 */
package de.movies.mapreduce;

import java.io.IOException;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import de.movies.pojo.MoviesDBStruct;
import de.movies.utility.GlobalVariables;

/**
 * @author nihatompi
 *
 */
public class MoviesMapper extends Mapper<LongWritable, Text, Text, MoviesDBStruct> {
	
	public static int m_runTime = -1;

	@Override
	protected void map(LongWritable _key, Text _value,
			Mapper<LongWritable, Text, Text, MoviesDBStruct>.Context _context) {

		// TODO Auto-generated method stub
		try {
			// get configuration
			String _m_imdb_id = _context.getConfiguration().get(GlobalVariables.IMDB_ID);
			
			if (_key.get() > 0) {
				
				String[] _tokens = _value.toString().split("\t");
				
				if (_tokens.length != 22) {
					// print the ids
					// System.err.println(_tokens[0].trim() + "\t" + _tokens.length);
				} else {
					
					// for date formatting
					String _m_date = _tokens[10].trim();
					if (_m_date.contains("-")) {
						String[] _temp = _tokens[10].trim().split("-");
						_m_date = _temp[2] + "." + _temp[1] + "." + _temp[0];
					}
					if (_m_date.equals("")) {
						_m_date = "00.00.0000";
					}

					// run time formatting
					String _m_runTime = _tokens[12].trim().equals("") ? "0" : _tokens[12].trim();
					
					if (_tokens[3].trim().equals(_m_imdb_id)/* && _tokens[10].trim().equals(_m_release_data) */) {
						m_runTime = Integer.valueOf(_m_runTime);
					}

					String[] _genres = _tokens[2].trim().split("\\|");
					
					// for writing values into context
					for (String _genre : _genres) {

						MoviesDBStruct _m_dbs = new MoviesDBStruct(new Text(_tokens[0].trim()), new Text(_genre.trim()),
								new Text(_tokens[3].trim()), new Text(_tokens[5].trim()), new Text(_tokens[6].trim()),
								new Text(_m_date), new Text(_tokens[11].trim()), new Text(_m_runTime),
								new Text(_tokens[16].trim()));

						_context.write(new Text(_genre), _m_dbs);
					}
				}
			}
			
		} catch (IOException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
