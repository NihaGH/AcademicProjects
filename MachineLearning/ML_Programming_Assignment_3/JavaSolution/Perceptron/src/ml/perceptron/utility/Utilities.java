/**
 * 
 */
package ml.perceptron.utility;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * @author nihatompi
 *
 */
public class Utilities {

	/**
	 * 
	 */

	public static Message checkSetPassingParameters() {

		boolean _mStaus = false;
		String _mMsg = "";

		GlobalVariables.PARENT_DIR = System.getProperty("dir", "");
		GlobalVariables.INPUT_FILE_NAME = System.getProperty("data");
		GlobalVariables.OUTPUT_FILE_NAME = System.getProperty("output");

		if (GlobalVariables.PARENT_DIR == null || GlobalVariables.INPUT_FILE_NAME == null
				|| GlobalVariables.OUTPUT_FILE_NAME == null) {

			_mMsg = "ERR :: " + "Error in passing the arguments in the command line. "
					+ "The command line should be in the format: \n" + "java -Ddir=<<path_where_the_tsv_file_resides>> "
					+ "-Ddata=<<file_name.tsv>> -Doutput=<<file_name.tsv>>"
					+ "-jar <<path_where_the_jar_file_resides/jar_file_name.jar>>" + "\n The arguments: "
					+ "\n -Ddir is optional."
					+ "\n -Ddata & -Doutput are the input and output file names with proper extentions, "
					+ "i.e. .tsv.";
			_mStaus = false;
		} else {

			String[] _temp = GlobalVariables.INPUT_FILE_NAME.split("\\.");
			String _input_file_ext = _temp[_temp.length - 1];
			_temp = GlobalVariables.OUTPUT_FILE_NAME.split("\\.");
			String _output_file_ext = _temp[_temp.length - 1];

			if (!_input_file_ext.equals("tsv") || !_output_file_ext.equals("tsv")) {

				_mMsg = "ERR :: " + "Error in setting the data and output file name. \n"
						+ "They should be proper format i.e. with .tsv extension.";
				_mStaus = false;
			} else {
				_mMsg = "INPUT DIR :: " + GlobalVariables.PARENT_DIR + GlobalVariables.INPUT_FILE_NAME
						+ "\nOUTPUT DIR :: " + GlobalVariables.PARENT_DIR + GlobalVariables.OUTPUT_FOLDER
						+ GlobalVariables.OUTPUT_FILE_NAME;

				_mStaus = true;
			}
		}
		return new Message(_mStaus, _mMsg);
	}

	public static Message readFromTSVFile() {

		Message _m_message = null;
		BufferedReader _m_br = null;
		String _m_line = "";
		String _m_cvsSplitBy = "\t";

		try {
			_m_br = new BufferedReader(new FileReader(GlobalVariables.PARENT_DIR + GlobalVariables.INPUT_FILE_NAME));

			String[] _rows;
			double[] _row_vals;
			int _cnt;
			while ((_m_line = _m_br.readLine()) != null) {

				_rows = _m_line.split(_m_cvsSplitBy);
				_cnt = _rows.length;
				_row_vals = new double[_cnt + 1];

				_row_vals[0] = 1;
				for (int i = 1; i < _cnt; i++) {
					_row_vals[i] = Double.valueOf(_rows[i]);
				}
				if (_rows[0].equals(GlobalVariables.CLASS_ENUM.A.name()))
					_row_vals[_cnt] = Double.valueOf(GlobalVariables.CLASS_ENUM.A.ordinal());
				else
					_row_vals[_cnt] = Double.valueOf(GlobalVariables.CLASS_ENUM.B.ordinal());

				GlobalVariables.INPUT_VAL.add(_row_vals);
			}

			if (GlobalVariables.INPUT_VAL.size() > 0)
				_m_message = new Message(true, GlobalVariables.INPUT_FILE_NAME + " file is successfully read.");
			else
				_m_message = new Message(false, GlobalVariables.INPUT_FILE_NAME + " file doesn't have any inputs.");

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return _m_message;
	}

	public static Message writeToTSVFile() {

		Message _m_msg = null;
		try {
			// output file creation
			File _parentPath = new File(GlobalVariables.PARENT_DIR + GlobalVariables.OUTPUT_FOLDER);
			File _fileName = new File(
					GlobalVariables.PARENT_DIR + GlobalVariables.OUTPUT_FOLDER + GlobalVariables.OUTPUT_FILE_NAME);
			// check whether the parent folder is there or not
			if (!_parentPath.exists()) {
				_parentPath.mkdirs();
				if (_fileName.exists()) {
					_fileName.delete();
				}
			}
			_fileName.createNewFile();
			
			// write to file
			FileWriter _fw = new FileWriter(_fileName.getAbsoluteFile());
			BufferedWriter _bw = new BufferedWriter(_fw);
			
			// write to the output file
			for(int i = 0; i < GlobalVariables.OUTPUT_VAL.size(); i++) {
				
				int[] _output = GlobalVariables.OUTPUT_VAL.get(i);
				String _temp = "";
				for(int j = 0; j <= GlobalVariables.EPOCHS; j++) {
					_temp = _temp + _output[j] + "\t";
				}
				_bw.write(_temp);
				_bw.newLine();
			}
			_bw.close();
			
			_m_msg = new Message(true, "Iterations Successfully written to the output file. "
					+ "Please check the output folder for the file.");

		} catch (Exception e) {
			// TODO: handle exception
			// e.printStackTrace();
			_m_msg = new Message(false, e.getLocalizedMessage());
		}

		return _m_msg;
	}
	
}
