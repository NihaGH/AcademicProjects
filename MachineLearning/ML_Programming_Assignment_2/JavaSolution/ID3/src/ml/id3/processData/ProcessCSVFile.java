/**
 * 
 */
package ml.id3.processData;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import ml.id3.utility.GlobalVariables;
import ml.id3.utility.Message;
import ml.id3.utility.Utilities;

/**
 * @author nihatompi
 *
 */
public class ProcessCSVFile {

	/**
	 * 
	 */
	public ProcessCSVFile() {
		// TODO Auto-generated constructor stub
	}

	public Message readCSVFile() {
		// TODO Auto-generated method stub
		Message _mMsg = null;

		BufferedReader _mBr = null;
		String _mLine = "";
		String _mCSVSplitBy = ",";
		int _mCount = 0;

		String[] _rows;
		int _mRowCnt = 0;

		try {
			_mBr = new BufferedReader(new FileReader(GlobalVariables.PARENT_DIR + GlobalVariables.INPUT_FILE_NAME));

			while ((_mLine = _mBr.readLine()) != null) {
				
				_rows = _mLine.split(_mCSVSplitBy);
				_mRowCnt = _rows.length - 1;

				if (_mCount == 0) {
					// setting the JSONObject keys
					for (int i = 0; i < _mRowCnt; i++) { // key names
						GlobalVariables.INPUT_VAL.put(GlobalVariables.ATTRIBUTE + i, new JSONArray());
					}
					// setting last column as the target attribute
					GlobalVariables.INPUT_VAL.put(GlobalVariables.TARGET_ATTRIBUTE, new JSONArray());
				}

				if (_mRowCnt > 0) {
					// set values to JSONArray and then adding to JSONObject
					for (int i = 0; i < _mRowCnt; i++) {
						GlobalVariables.INPUT_VAL.getJSONArray(GlobalVariables.ATTRIBUTE + i).put(_rows[i]);
					}
					// set target attribute
					GlobalVariables.INPUT_VAL.getJSONArray(GlobalVariables.TARGET_ATTRIBUTE).put(_rows[_mRowCnt]);
				}

				_mCount++;
			}

			if (GlobalVariables.INPUT_VAL.length() > 0)
				_mMsg = new Message(true, GlobalVariables.INPUT_FILE_NAME + " file is successfully read.");
			else
				_mMsg = new Message(false, GlobalVariables.INPUT_FILE_NAME + " file doesn't have any inputs.");

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			_mMsg = new Message(false, e.getMessage());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			_mMsg = new Message(false, "PrepareCSVData.readCSVFile IOException\n" + e.getMessage());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			_mMsg = new Message(false, "PrepareCSVData.readCSVFile JSONException\n" + e.getMessage());
		}

		return _mMsg;
	}

	public Message processCSVData() {
		Message _msg = null;
		JSONArray _jArr = processingCSVData(null, null, GlobalVariables.INPUT_VAL);
		try {
			if(_jArr != null) {
				GlobalVariables.OUTPUT_VAL.put(GlobalVariables.NODE, _jArr);
				
				// create XML FILE
				if(Utilities.createWriteXMLFile())
					_msg = new Message(true, "XML file successfully created in the given OUTPUT DIRECTORY.");
				else
					_msg = new Message(false, "There is some error in the process. Please review the source code.");
			} else {
				_msg = new Message(false, "Some error encountered while processing Data.\nNo Decission Tree is formed.");
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			_msg = new Message(false, e.getLocalizedMessage()); 
			e.printStackTrace();
		}
		return _msg;
	}

	private JSONArray processingCSVData(String attrKey, String attrVal, JSONObject _jObj) {
		
		JSONArray _jNodeArr = null;
		// fetch the data set to process upon
		JSONObject _jObjDataSet = fetchDataSet(attrKey, attrVal, _jObj);
		
		if(_jObjDataSet != null) {
			// count occurrence of each record w.r.t. target attribute
			JSONObject _jTempObj = countOccurance(_jObjDataSet);
			
			if(_jTempObj != null) {
				// calculate entropy and information gain
				_jNodeArr = calculation(_jObjDataSet, _jTempObj);
			} else
				System.err.println("Some error occured while performing calculation on data set.");
		} else 
			System.err.println("Some error occured while fetching the reqired data set for processing.");
		
		return _jNodeArr;
	}
	
	// fetching the dataset w.r.t. LABEL and LABEL_VALUE from the existing dataset
	private JSONObject fetchDataSet(String _LABEL, String _LABEL_VALUE, JSONObject _jObjMain) {
		// TODO Auto-generated method stub
		
		JSONObject _jObjDataSet = null; // stores the fetched dataset

		try {

			if (_LABEL == null) {
				GlobalVariables.CLASS_LABEL_VALUES = Utilities.fetchUniqueLabelValues(_jObjMain.getJSONArray(GlobalVariables.TARGET_ATTRIBUTE));
				GlobalVariables.LOG_BASE = GlobalVariables.CLASS_LABEL_VALUES.length();
				_jObjDataSet = _jObjMain;

			} else {
				
				_jObjDataSet = new JSONObject(); // intialize the dataSet
				JSONArray LABELS = Utilities.fetchLabels(_LABEL, _jObjMain);// fetch all LABEL names except the parent LABEL
				JSONArray _jParentLabelValues = _jObjMain.getJSONArray(_LABEL); // fetch the parent LABEL values

				int _keySize = LABELS.length();

				// fetch required LABEL values for further iterations
				String _tempPLV; // stores the Parent Label Value temporarily
				JSONArray _jIndexes = new JSONArray();
				int _size = _jParentLabelValues.length();
				for (int i = 0; i < _size; i++) {
					_tempPLV = _jParentLabelValues.getString(i);
					if (_tempPLV.equals(_LABEL_VALUE))
						_jIndexes.put(i);
				}

				_size = _jIndexes.length();
				int _pos;
				for (int i = 0; i < _size; i++) {
					_pos = _jIndexes.getInt(i); // position of the value to be fetches
					for (int j = 0; j < _keySize; j++) {
						String _key = LABELS.getString(j);
						if (i == 0)
							_jObjDataSet.put(_key, new JSONArray());

						_tempPLV = _jObjMain.getJSONArray(_key).getString(_pos); // fetch the LABEL value of the position
						_jObjDataSet.getJSONArray(_key).put(_tempPLV); // set the LABEL value to that LABEL
					}
				}
			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			System.err.println("Some error occured while processing data to fetch the data set to work on.");
		}

		return _jObjDataSet;
	}
	
	// count the occurrence of CLASS_LABEL and each LABEL w.r.t. CLASS_LABEL
	private JSONObject countOccurance(JSONObject _jObjMain) {
		// TODO Auto-generated method stub
		
		JSONObject _jObjCountDataset = null;

		try {
			_jObjCountDataset = new JSONObject(); // initialize for storing count of the target attribute and other attributes w.r.t. TA
			JSONArray _CLASS_LABEL_VALUES = _jObjMain.getJSONArray(GlobalVariables.TARGET_ATTRIBUTE);
			JSONArray _LABEL_NAMES = Utilities.fetchLabels(null, _jObjMain); // returns all LABEL_NAMES 

			int _size = _LABEL_NAMES.length() - 1; // size other than CLASS_LABEL
			String _labelName;
			for (int i = 0; i < _size; i++) {
				_labelName = _LABEL_NAMES.getString(i); // fetch the label name from list of LABEL_NAMES
				JSONArray _LABEL_VALUES = _jObjMain.getJSONArray(_labelName); // fetch the values w.r.t. the _labelName
				JSONObject _jTempObj = new JSONObject();// initialize count to 0 for each label value
				
				for (int j = 0; j < _LABEL_VALUES.length(); j++) {
					// create JSONArray for each unique label value
					if(j == 0) {
						JSONArray _jTempLabelValAsKey = Utilities.fetchUniqueLabelValues(_LABEL_VALUES);
						for(int k = 0; k < _jTempLabelValAsKey.length(); k++) {
							JSONArray _jTempArr = new JSONArray();
							for (int l = 0; l < GlobalVariables.LOG_BASE; l++) {
								_jTempArr.put(0);
							}
							_jTempObj.put(_jTempLabelValAsKey.getString(k), _jTempArr);
						}
					}
					
					// increments the position of the array w.r.t. CLASS_LABEL_VALUES and the position
					// _jTempObj = {"med":[192, 16, 28, 0]} 
					int _pos = Utilities.ckeckClassLabelValue(_CLASS_LABEL_VALUES.getString(j));
					String _labelValue = _LABEL_VALUES.getString(j);
					int _classLabelValCnt = _jTempObj.getJSONArray(_labelValue).getInt(_pos) + 1;
					_jTempObj.getJSONArray(_labelValue).put(_pos, _classLabelValCnt);
				}
				
				// sets the LABEL_NAME with corresponding count of CLASS_LABEL_VALUES
				// {"attr1":{"med":[192, 16, 28, 0]}}
				_jObjCountDataset.put(_labelName, _jTempObj);
			}
			
			// for CLASS_LABEL
			// set the counter with respect to CLASS_LABEL_VALUES
			JSONArray _jTempArr = new JSONArray();
			_size = _CLASS_LABEL_VALUES.length();
			for (int j = 0; j < _size; j++) {
				if(j == 0) {
					for (int k = 0; k < GlobalVariables.LOG_BASE; k++) {
						_jTempArr.put(0);
					}
				}
				
				int _pos = Utilities.ckeckClassLabelValue(_CLASS_LABEL_VALUES.getString(j));
				_jTempArr.put(_pos, _jTempArr.getInt(_pos) + 1);
				
			}
			_jObjCountDataset.put(GlobalVariables.TARGET_ATTRIBUTE, _jTempArr);

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			System.out.println("COUNT FUNCTION :: " + e.getLocalizedMessage());
		}

		return _jObjCountDataset;
	}
	
	// the main calculation of ID3 algorithm
	private JSONArray calculation(JSONObject _jObjDataSet, JSONObject _jObjCount) {
		// TODO Auto-generated method stub
		
		JSONArray _jNodes = null;
		String _rootNodeAttr = null;
		double _finalIG = 0.0;

		try {
			
			// for target attribute
			JSONArray _jTargetAttr = _jObjCount.getJSONArray(GlobalVariables.TARGET_ATTRIBUTE);
			double _entropy = Utilities.calculateEntropy(_jTargetAttr);
			_jTargetAttr.put(_entropy);

			// set initial entropy to the to OUTPUT_VAL
			if (!GlobalVariables.OUTPUT_VAL.has(GlobalVariables.ENTROPY))
				GlobalVariables.OUTPUT_VAL.put(GlobalVariables.ENTROPY, String.valueOf(_entropy));
			
			// total count of records
			int _recCnt = _jObjDataSet.getJSONArray(GlobalVariables.TARGET_ATTRIBUTE).length();
			
			// fetch attribute names or labels
			JSONArray _LABELS = Utilities.fetchLabels(null, _jObjDataSet);
			int _size = _LABELS.length() - 1; // size other than CLASS_LABEL
			
			// for attributes
			double _infoGain = 0.0;
			for (int i = 0; i < _size; i++) {

				String _labelName = _LABELS.getString(i);
				JSONObject _jLabelValCntAsObj = _jObjCount.getJSONObject(_labelName);
				
				JSONArray _jLabelValAsKeys = Utilities.fetchLabels(null, _jLabelValCntAsObj);
				int _attrValAsKeySize = _jLabelValAsKeys.length();
				_infoGain = _jTargetAttr.getDouble(GlobalVariables.LOG_BASE);
				

				String _tempAttrValAsKey = null;
				JSONArray _jTempAttrValCount;
				for (int j = 0; j < _attrValAsKeySize; j++) {
					_tempAttrValAsKey = _jLabelValAsKeys.getString(j);
					_jTempAttrValCount = _jLabelValCntAsObj.getJSONArray(_tempAttrValAsKey);
					_entropy = Utilities.calculateEntropy(_jTempAttrValCount); // calculate entropy
					_jTempAttrValCount.put(String.valueOf(_entropy)); // add entropy

					// calculate information gain
					double _total = 0.0;
					for (int k = 0; k < GlobalVariables.LOG_BASE; k++) {
						_total += _jTempAttrValCount.getDouble(k);
					}

					_infoGain = _infoGain - ((_total / _recCnt) * _entropy);
				}
				
				// set info gain to the Object
				_jLabelValCntAsObj.put("info_gain", _infoGain);

				// fetch the highest information gain attribute
				if (_finalIG == 0 || _infoGain > _finalIG) {
					_rootNodeAttr = _labelName;
					_finalIG = _infoGain;
				}
			}

			// call to add root node and successive nodes
			if (!_rootNodeAttr.equals(null)) {
				_jNodes = makeNode(_jObjDataSet, _rootNodeAttr, _jObjCount);
			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			System.out.println("CALCULATION :: " + e.getLocalizedMessage());
		}

		return _jNodes;
	}
	
	// makes a node to the OUTPUT_VAL
	private JSONArray makeNode(JSONObject _jObjDataSet, String _rootNodeAttr, JSONObject _jObjCount) {
		// TODO Auto-generated method stub
		JSONArray _jArrOutput = null;

		try {
			// fetch the root node Object 
			JSONObject _jObjRootNode = _jObjCount.getJSONObject(_rootNodeAttr);
			
			// sort by entropy and value
			JSONArray _jKeys = Utilities.sortNodes(_jObjRootNode);
			int _size = _jKeys.length();
			for (int i = 0; i < _size; i++) {
				
				//
				if(i == 0)
					_jArrOutput = new JSONArray();

				String _key = _jKeys.getString(i);
				JSONArray _jArrTemp = _jObjRootNode.getJSONArray(_key);
				double _entropy = _jArrTemp.getDouble(GlobalVariables.LOG_BASE);

				//
				JSONObject _jObjTemp = new JSONObject();
				_jObjTemp.put(GlobalVariables.ENTROPY, String.valueOf(_entropy));
				_jObjTemp.put(GlobalVariables.FEATURE, _rootNodeAttr);
				_jObjTemp.put(GlobalVariables.VALUE, _key);

				// find class lable if entropy is 0
				if (_entropy == 0.0) {
					//String _label = Utilities.findClassLable(_jArrTemp);
					_jObjTemp.put(GlobalVariables.LABEL, Utilities.findClassLable(_jArrTemp));
				} else {
					JSONArray _jTemp = processingCSVData(_rootNodeAttr, _key, _jObjDataSet);
					_jObjTemp.put(GlobalVariables.NODE, _jTemp);
				}

				// extra values for checking
				/*_jArrTemp.remove(_jArrTemp.length() - 1);
				_jObjTemp.put("COUNT_ENTROPY", _jArrTemp);
				_jObjTemp.put("CLASS_LABEL_VALUES", CLASS_LABEL_VALUES);
				_jObjTemp.put("TOTAL_RECORD_COUNT", recCount);
				_jObjTemp.put("TARGET_ATTRIBUTE", TARGET_ATTRIBUTE);*/

				_jArrOutput.put(_jObjTemp);
			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return _jArrOutput;
	}

}
