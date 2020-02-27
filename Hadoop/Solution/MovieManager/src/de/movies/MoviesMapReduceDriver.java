/**
 * 
 */
package de.movies;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

import de.movies.mapreduce.MoviesMapper;
import de.movies.mapreduce.MoviesReducer;
import de.movies.pojo.MoviesDBStruct;
import de.movies.utility.GlobalVariables;

/**
 * @author nihatompi
 *
 */
public class MoviesMapReduceDriver extends Configured implements Tool {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int m_exitCode = 0;

		try {
			m_exitCode = ToolRunner.run(new MoviesMapReduceDriver(), args);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.exit(m_exitCode);
	}

	/**
	 * Thsese variables are used to store the arguments that are passed in the
	 * command line.
	 */
	private String m_parentDir, m_inFolder, m_outFolder = "Solution", m_imdbId = "", m_releaseDate = "", m_terms = "";

	@Override
	public int run(String[] args) {
		// TODO Auto-generated method stub
		int _m_returnValue = 0;
		Path _m_inputPath = null, _m_outputPath = null;

		checkArguments(args);

		if ((m_parentDir == null || m_inFolder == null)
				|| ((m_imdbId == "" && m_releaseDate != "") || (m_imdbId != "" && m_releaseDate == ""))) {
			System.err.println("MovieManager required the following params:\n"
					+ "[-parentDir=PARENT_DIR] [-inFolder=INPUT_FOLDER_NAME] [-outFolder=OUTPUT_FOLDER_NAME] "
					+ "[-terms=TERMS_TO_BE_SEARCHED_SEPARATED_BY_PIPELINE_(|)]"
					+ "[-imdbId=IMDB_ID] [-releaseDate=<<dd.mm.yyyy>>]\n"
					+ "-imdbID & -releaseDate are optional but are dependent.");
		} else { // Fetch the parameter passed through command line
			if (m_terms == "") {
				System.out.println("No terms are passed to find it count in the overview field of the movie. "
						+ "\nIf you want to find the count of a particular term, then please run again the jar"
						+ " file the the terms.");
			}
			_m_inputPath = new Path(m_parentDir + m_inFolder);
			_m_outputPath = new Path(m_parentDir + m_outFolder);

			deleteOutputFileIfExists(_m_outputPath);

			Configuration _m_conf = new Configuration();
			_m_conf.set(GlobalVariables.IMDB_ID, m_imdbId);
			_m_conf.set(GlobalVariables.RELEASE_DATE, m_releaseDate);
			_m_conf.set(GlobalVariables.TERMS, m_terms);

			// Creates new Jar and set the driver(this) class as the main class of jar
			try {
				Job _m_job = Job.getInstance(_m_conf, "Movies Gist");
				_m_job.setJobName("MoviesGist"); // Set name of the job for the later monitoring
				_m_job.setJarByClass(MoviesMapReduceDriver.class);

				// Define Mapper and Reducer Classes
				_m_job.setMapperClass(MoviesMapper.class);
				_m_job.setReducerClass(MoviesReducer.class);

				_m_job.setMapOutputKeyClass(Text.class);
				_m_job.setMapOutputValueClass(MoviesDBStruct.class);
				_m_job.setOutputKeyClass(Text.class);
				_m_job.setOutputValueClass(MoviesDBStruct.class);

				// Set the input and the output path from the arguments
				FileInputFormat.addInputPath(_m_job, _m_inputPath);
				FileOutputFormat.setOutputPath(_m_job, _m_outputPath);

				// Run the job and wait for its completion
				_m_returnValue = _m_job.waitForCompletion(true) ? 0 : 1;

				if (_m_job.isSuccessful()) {
					System.out.println("Job was successful");
				} else if (!_m_job.isSuccessful()) {
					System.out.println("Job was not successful");
				}
			} catch (IOException | ClassNotFoundException | InterruptedException e) {
				// TODO Auto-generated catch block
				System.out.println("MAPREDUCER EXCEPTION : " + e.getMessage());
			}
		}

		return _m_returnValue;
	}

	/**
	 * For fetching the passed parameters as argument in the command line.
	 * 
	 * @param args the arguments passed in the command
	 */
	private void checkArguments(String[] args) {
		// TODO Auto-generated method stub
		for (int i = 0; i < args.length; i++) {
			String[] _temp = args[i].split("=");
			if ("-parentDir".equals(_temp[0])) {
				m_parentDir = _temp[1];
			} else if ("-inFolder".equals(_temp[0])) {
				m_inFolder = _temp[1];
			} else if ("-outFolder".equals(_temp[0])) {
				m_outFolder = _temp[1];
			} else if ("-imdbId".equals(_temp[0])) {
				m_imdbId = _temp[1];
			} else if ("-releaseDate".equals(_temp[0])) {
				m_releaseDate = _temp[1];
			} else if ("-terms".equals(_temp[0])) {
				m_terms = _temp[1];
			}
		}

	}

	/**
	 * For deleting the output folder in each run.
	 * 
	 * @param _m_outputPath the path of the Output folder
	 */
	private void deleteOutputFileIfExists(Path _m_outputPath) {
		// TODO Auto-generated method stub
		try {
			FileSystem.get(_m_outputPath.toUri(), getConf()).delete(_m_outputPath, true);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("MAPREDUCER EXCEPTION-1 : " + e.getMessage());
		}
	}

}
