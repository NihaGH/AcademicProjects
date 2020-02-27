/**
 * 
 */
package ml.id3;

import ml.id3.processData.ProcessCSVFile;
import ml.id3.utility.Message;
import ml.id3.utility.Utilities;

/**
 * @author nihatompi
 *
 */
public class ID3Manager {

	/**
	 * 
	 */
	public ID3Manager() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Message _mMsg = Utilities.checkSetPassingParameters();

		if (_mMsg.is_status()) {
			System.out.println("INFO :: " + _mMsg.get_msg());
			
			ProcessCSVFile _process = new ProcessCSVFile();
			_mMsg = _process.readCSVFile();
			if (_mMsg.is_status()) {
				System.out.println("INFO :: " + _mMsg.get_msg());
				
				_mMsg = _process.processCSVData();
				if(_mMsg.is_status())
					System.out.println("INFO :: " + _mMsg.get_msg());
				else
					System.err.println("ERROR :: " + _mMsg.get_msg());
			} else
				System.err.println("ERROR :: " + _mMsg.get_msg());

		} else
			System.err.println("ERROR :: " + _mMsg.get_msg());
	}

}
