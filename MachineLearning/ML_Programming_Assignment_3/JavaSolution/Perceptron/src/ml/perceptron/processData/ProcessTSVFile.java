/**
 * 
 */
package ml.perceptron.processData;

import ml.perceptron.utility.GlobalVariables;
import ml.perceptron.utility.Message;

/**
 * @author nihatompi
 *
 */
public class ProcessTSVFile {

	public static Message processInputData() {

		int _rs = GlobalVariables.INPUT_VAL.size(); // No. of records
		int _ws = GlobalVariables.INPUT_VAL.get(0).length - 1; // No. of weights

		double[][] _t_err = new double[GlobalVariables.TYPE][_ws]; // error for calculating weights
		double[][] _weight = new double[GlobalVariables.TYPE][_ws]; // weights
		//double[] _learningRate = new double[GlobalVariables.TYPE]; // learning rates
		int[][] _output = new int[GlobalVariables.TYPE][GlobalVariables.EPOCHS + 1]; // error for calculating weights
		
		double[] _learningRate = {GlobalVariables.LEARNING_RATE, 1}; // 0- constant learning rate, 1- annealing learning rate
		for (int i = 0; i <= GlobalVariables.EPOCHS; i++) { // for number of iterations
			
			if(i != 0)
				_learningRate[1] = (GlobalVariables.LEARNING_RATE / i); // annealing learning rate
			
			// calculate the new weights for each iteration
			_weight = calculateWeights(_weight, _t_err, _learningRate);

			_t_err = new double[GlobalVariables.TYPE][_ws]; // initialize total error

			double[] _row;
			double[] _fx; // f(x) = w0x0 + w1x1 + .... + wNxN
			double[] _err; // err = y-y_cap // double _y_cap; // output of activation function =
							// perceptronOutput
			int[] _misCnt = new int[GlobalVariables.TYPE]; // mis-classified records count
			for (int j = 0; j < _rs; j++) { // for number of records

				_row = GlobalVariables.INPUT_VAL.get(j); // fetch each row
				_fx = calculateSummation(_weight, _row); // calculating f(x) for each row
				_err = calculateRowError(_fx, _row[_ws]); // calculate the error of each row

				// misclassified records count
				for (int k = 0; k < GlobalVariables.TYPE; k++) {
					if (_err[k] != 0) {
						_misCnt[k]++;
						_t_err[k] = calculateTotalError(_row, _err[k], _t_err[k]); // calculate total error
					}
				}
			}

			// add to the output variable
			for (int j = 0; j < GlobalVariables.TYPE; j++) {
				_output[j][i] = _misCnt[j];
			}
		}

		// add to the global output variable
		for (int i = 0; i < GlobalVariables.TYPE; i++) {
			GlobalVariables.OUTPUT_VAL.add(_output[i]);
		}
		
		

		// set the message
		boolean _status = false;
		String _msg = "Data set processing is unsuccessful.";
		if (GlobalVariables.OUTPUT_VAL.size() == 2
				&& GlobalVariables.OUTPUT_VAL.get(0).length == GlobalVariables.EPOCHS + 1) {
			_status = true;
			_msg = "Data set successfully processed with both constant and annealing learning rates.";
		}

		return new Message(_status, _msg);
	}

	private static double[][] calculateWeights(double[][] _weight, double[][] _t_err, double[] _learningRate) {
		// TODO Auto-generated method stub
		int _ws = _weight[0].length;
		for (int i = 0; i < GlobalVariables.TYPE; i++) {
			for (int j = 0; j < _ws; j++) {
				_weight[i][j] = _weight[i][j] + (_learningRate[i] * _t_err[i][j]);
			}
		}
		return _weight;
	}

	private static double[] calculateSummation(double[][] _weight, double[] _row) {
		// TODO Auto-generated method stub
		int _ws = _row.length - 1;
		double[] _fx = new double[GlobalVariables.TYPE];
		for (int i = 0; i < GlobalVariables.TYPE; i++) { // for number inputs
			for (int j = 0; j < _ws; j++) {
				_fx[i] = _fx[i] + (_weight[i][j] * _row[j]);
			}
		}
		return _fx;
	}

	private static double[] calculateRowError(double[] _fx, double y) {
		// TODO Auto-generated method stub
		double[] _err = new double[GlobalVariables.TYPE];
		for (int i = 0; i < GlobalVariables.TYPE; i++) {
			_err[i] = y - activationFunction(_fx[i]);
		}
		return _err;
	}

	private static double activationFunction(double _fx) {
		// TODO Auto-generated method stub
		double _y_cap = GlobalVariables.CLASS_ENUM.B.ordinal();
		if (_fx > 0)
			_y_cap = GlobalVariables.CLASS_ENUM.A.ordinal();

		return _y_cap;
	}

	private static double[] calculateTotalError(double[] _row, double _err, double[] _t_err) {
		// TODO Auto-generated method stub
		int _rc = _t_err.length;
		for (int i = 0; i < _rc; i++) {
			_t_err[i] = _t_err[i] + (_row[i] * _err);
		}
		return _t_err;
	}
}

//_learningRate[GlobalVariables.CLASS_TYPE_ENUM.CLR.ordinal()] =
// GlobalVariables.LEARNING_RATE;
// _learningRate[GlobalVariables.CLASS_TYPE_ENUM.ALR.ordinal()] = (i == 0) ? 0 :
// (GlobalVariables.LEARNING_RATE / i);

/*
// for printing
			System.out.print(i + " :: " +_learningRate[0] + " :: " + _misCnt[0] + " :: \t");

			for(int j = 0; j < _ws; j++ ) {
				System.out.print(_weight[0][j] + " :: \t");
			}
			for(int j = 0; j < _ws; j++ ) {
				System.out.print(_t_err[0][j] + " :: \t");
			}
			System.out.println();
			
*/
