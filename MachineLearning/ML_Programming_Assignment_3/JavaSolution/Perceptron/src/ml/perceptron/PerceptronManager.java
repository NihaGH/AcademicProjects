/**
 * 
 */
package ml.perceptron;

import ml.perceptron.processData.ProcessTSVFile;
import ml.perceptron.utility.Message;
import ml.perceptron.utility.Utilities;

/**
 * @author nihatompi
 *
 */
public class PerceptronManager {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		Message _mMsg = Utilities.checkSetPassingParameters();
		
		if(_mMsg.is_status()) {
			System.out.println(_mMsg.get_msg());
			// read the tsv file row wise 
			_mMsg = Utilities.readFromTSVFile();
			if(_mMsg.is_status()) {
				System.out.println("INFO :: " + _mMsg.get_msg());
				
				// process the tsv data
				_mMsg = ProcessTSVFile.processInputData();
				if(_mMsg.is_status()) {
					System.out.println("INFO :: " + _mMsg.get_msg());
					
					// write to tsv file
					_mMsg = Utilities.writeToTSVFile();
					if(_mMsg.is_status())
						System.out.println("INFO :: " + _mMsg.get_msg());
					else
						System.err.println(_mMsg.get_msg());
				} else
					System.err.println(_mMsg.get_msg());
			}
			else
				System.err.println(_mMsg.get_msg());
			
		} else
			System.err.println(_mMsg.get_msg());
	}

}
