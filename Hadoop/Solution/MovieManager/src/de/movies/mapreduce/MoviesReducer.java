/**
 * 
 */
package de.movies.mapreduce;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.HashSet;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import de.movies.pojo.MoviesDBStruct;
import de.movies.utility.GlobalVariables;

/**
 * @author nihatompi
 *
 */
public class MoviesReducer extends Reducer<Text, MoviesDBStruct, Text, Text> {

	private String[] m_terms;
	private StringBuilder m_firstStrBuilder, m_secondStrBuilder, m_thirdStrBuilder;
	private Date m_releaseDate;
	private int m_runTime, m_termSize;
	private String m_releaseDateStr, m_imdbId;
	private HashSet<Integer> m_hsSecond;
	private HashSet<Integer> m_hsThird;
	private String m_resSplitor = "\t";

	@Override
	protected void setup(Reducer<Text, MoviesDBStruct, Text, Text>.Context _context) {
		// TODO Auto-generated method stub
		try {
			super.setup(_context);

			// get configuration
			Configuration _m_conf = _context.getConfiguration();

			m_firstStrBuilder = new StringBuilder();

			m_runTime = MoviesMapper.m_runTime;
			if (m_runTime != -1) {
				m_imdbId = _m_conf.get(GlobalVariables.IMDB_ID);
				m_releaseDateStr = _m_conf.get(GlobalVariables.RELEASE_DATE);
				m_releaseDate = GlobalVariables.SDF.parse(m_releaseDateStr);
				m_hsSecond = new HashSet<Integer>();
				m_secondStrBuilder = new StringBuilder();
			}
			
			String _m_TermStr = _m_conf.get(GlobalVariables.TERMS,"");
			if(_m_TermStr.equals("") || _m_TermStr == null) {
				m_termSize = 0;
			} else {
				m_terms = _m_TermStr.split("\\|");
				m_termSize = m_terms.length;
				if (m_termSize > 0) {
					m_hsThird = new HashSet<Integer>();
					m_thirdStrBuilder = new StringBuilder();
				}
			}
			

		} catch (IOException | InterruptedException | ParseException e) {
			// TODO Auto-generated catch block
			System.out.println("REDUCER EXCEPTION : " + e.getMessage());
		}
	}

	@Override
	protected void reduce(Text _key, Iterable<MoviesDBStruct> _value,
			Reducer<Text, MoviesDBStruct, Text, Text>.Context _context) {
		// TODO Auto-generated method stub
		try {
			// super.reduce(_key, _value, _context);
			String _m_movieName = null;
			long _m_revenue, _m_totalRevenue = 0, _m_count = 0, _m_highestRevenue = -1;

			for (MoviesDBStruct _m_DBS : _value) {

				int _m_id = Integer.valueOf(_m_DBS.getM_id().toString());

				// first question requirement
				_m_revenue = Long.valueOf(_m_DBS.getM_cinema_revenue().toString());
				_m_totalRevenue += _m_revenue;
				_m_count++;
				if (_m_highestRevenue < _m_revenue) {
					_m_highestRevenue = _m_revenue;
					_m_movieName = _m_DBS.getM_title().toString();
				} else if (_m_highestRevenue == _m_revenue) {
					_m_movieName = (_m_movieName.equals(null)) ? _m_DBS.getM_title().toString()
							: _m_movieName + "|" + _m_DBS.getM_title().toString();
				}

				// second question requirement
				if (m_runTime != -1) {
					String _m_currentDateStr = _m_DBS.getM_release_date().toString();
					Date _m_currentDate = GlobalVariables.SDF.parse(_m_currentDateStr);
					int _m_currentRunTime = Integer.valueOf(_m_DBS.getM_runtime().toString());

					if ((m_releaseDate.compareTo(_m_currentDate) <= 0) && ((_m_currentRunTime == (m_runTime + 10))
							|| (_m_currentRunTime == (m_runTime - 10)) || (_m_currentRunTime == m_runTime))) {
						String _temp = _m_id + m_resSplitor + _m_DBS.getM_runtime() + m_resSplitor
								+ _m_DBS.getM_original_title() + m_resSplitor + _m_currentDateStr;

						if (!m_hsSecond.contains(_m_id)) {
							m_hsSecond.add(_m_id);
							m_secondStrBuilder.append(_temp + "\n\t");
						}
					}
				}

				// third question requirement
				if (m_termSize > 0) {
					String[] _m_overview = _m_DBS.getM_overview().toString().replaceAll("\t", " ").split(" ");
					boolean _add = false;
					String _temp = String.valueOf(_m_id);
					for (String _term : m_terms) {
						int _cnt = 0;
						for (String _word : _m_overview) {
							_cnt = (_term.toUpperCase()).equals(_word.trim().toUpperCase()) ? _cnt + 1 : _cnt;
						}
						_temp += m_resSplitor + _cnt;
						if (_add || _cnt > 0) {
							_add = true;
						}
					}
					if (_add) {
						if (!m_hsThird.contains(_m_id)) {
							m_hsThird.add(_m_id);
							m_thirdStrBuilder.append(_temp + "\n\t");
						}
					}
				}
			}

			m_firstStrBuilder.append(_key.toString() + m_resSplitor + (_m_totalRevenue / _m_count) + m_resSplitor
					+ _m_movieName + " {" + _m_highestRevenue + "}\n\t");

		} catch (Exception e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
			System.out.println("REDUCER EXCEPTION : " + e.getMessage());
			e.printStackTrace();
		}
	}

	@Override
	public void run(Reducer<Text, MoviesDBStruct, Text, Text>.Context _context) {
		// TODO Auto-generated method stub
		try {
			super.run(_context);

			// Set values into context for output
			String _temp = "Average Revenue per Genre with Movie Name having highest revenue in that Genre.";
			_context.write(new Text("QUES_1"), new Text(_temp));
			_temp = "Gerne" + m_resSplitor + "AverageRevenue" + m_resSplitor + "MovieName {Revenue}";
			_context.write(new Text("PART_1"), new Text(_temp));
			_context.write(new Text("SOL__1"), new Text(m_firstStrBuilder.append("\n\n").toString()));

			if (m_runTime != -1) {
				_temp = "List of Movie Id's with Original Title whose Release Date is greater than equal to "
						+ m_releaseDateStr
						+ " and Runtime equal to +/- 10 minutes the runtime of the movie with IMBD_ID: " + m_imdbId;
				_context.write(new Text("QUES_2"), new Text(_temp));
				_temp = "Id" + m_resSplitor + "RunTime" + m_resSplitor + "OriginalTitle" + m_resSplitor + "ReleaseDate";
				_context.write(new Text("PART_2"), new Text(_temp));
				_context.write(new Text("SOL__2"), new Text(m_secondStrBuilder.append("\n\n").toString()));
			}

			if (m_termSize > 0) {
				String _temp1 = "";
				_temp = "List of Movie Id's containg terms: ";
				for (String _term : m_terms) {
					_temp += _term + ", ";
					_temp1 += m_resSplitor + "CNT(" + _term + ")";
				}
				_temp = _temp.substring(0, (_temp.length() - 2));
				_temp += " in the Movie Overview field with their respective counts.";
				_temp1 = "Id" + _temp1;
				_context.write(new Text("QUES_3"), new Text(_temp));
				_context.write(new Text("PART_3"), new Text(_temp1));
				_context.write(new Text("SOL__3"), new Text(m_thirdStrBuilder.toString()));
			}

		} catch (IOException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
