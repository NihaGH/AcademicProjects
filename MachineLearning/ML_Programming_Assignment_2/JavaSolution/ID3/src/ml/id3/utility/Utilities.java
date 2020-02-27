/**
 * 
 */
package ml.id3.utility;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Attr;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

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
					+ "The command line should be in the format: \n" + "java -Ddir=<<path_where_the_csv_file_resides>> "
					+ "-Ddata=<<file_name.csv>> -Doutput=<<file_name.xml>>"
					+ "-jar <<path_where_the_jar_file_resides/jar_file_name.jar>>" + "\n The arguments: "
					+ "\n -Ddir is optional."
					+ "\n -Ddata & -Doutput are the input and output file names with proper extentions, "
					+ "i.e. .csv and .xml respectively.";
			_mStaus = false;
		} else {
			
			String[] _temp = GlobalVariables.INPUT_FILE_NAME.split("\\.");
			String _input_file_ext = _temp[_temp.length - 1];
			_temp = GlobalVariables.OUTPUT_FILE_NAME.split("\\.");
			String _output_file_ext = _temp[_temp.length - 1];
			
			if(!_input_file_ext.equals("csv") || !_output_file_ext.equals("xml")) {

				_mMsg = "ERR :: " + "Error in setting the data and output file name. \n"
						+ "They should be proper format i.e. with .csv and .xml extensions respectively.";
				_mStaus = false;
			} else {
				_mMsg = "INPUT DIR :: " + GlobalVariables.PARENT_DIR + GlobalVariables.INPUT_FILE_NAME + "\nOUTPUT DIR :: "
						+ GlobalVariables.PARENT_DIR + GlobalVariables.OUTPUT_FOLDER + GlobalVariables.OUTPUT_FILE_NAME;

				_mStaus = true;
			}
		}
		return new Message(_mStaus, _mMsg);
	}
	
	// checks a value present in the distinct class label values. If yes returns the position else -1
	public static int ckeckClassLabelValue(String _val) {
		int _isPresent = -1;

		try {
			for (int k = 0; k < GlobalVariables.CLASS_LABEL_VALUES.length(); k++) {
				if (GlobalVariables.CLASS_LABEL_VALUES.getString(k).equals(_val))
					_isPresent = k;
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			System.err.println("Utilities.ckeckJSONKeys " + e.getMessage());
			// e.printStackTrace();
		}
		return _isPresent;
	}
	
	// fetches unique values from an Array of elements
	public static JSONArray fetchUniqueLabelValues(JSONArray _jArr) {
		// TODO Auto-generated method stub
		JSONArray _jAttrValue = new JSONArray();
		
		HashSet<String> _hsTemp = new HashSet<String>();
		int _size = _jArr.length();
		try {
			for (int i = 0; i < _size; i++) {
				_hsTemp.add(_jArr.getString(i));
			}
			
			Iterator<String> _itr = _hsTemp.iterator();
			while(_itr.hasNext()) {
				_jAttrValue.put(_itr.next());
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return _jAttrValue;
	}
	
	// find the class lable name for label with 0 entropy
	public static String findClassLable(JSONArray _jArr) {
		// TODO Auto-generated method stub
		String _classLabel = "";
		try {
			int _pos = 0;
			int _temp = _jArr.getInt(0);
			for (int i = 1; i < GlobalVariables.LOG_BASE; i++) {
				if (_jArr.getInt(i) > _temp)
					_pos = i;
			}
			_classLabel = GlobalVariables.CLASS_LABEL_VALUES.getString(_pos);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return _classLabel;
	}
	
	// fetch the label names from the dataset w.r.t. given label name
	@SuppressWarnings("rawtypes")
	public static JSONArray fetchLabels(String labelName, JSONObject _jObj) {
		// TODO Auto-generated method stub
		JSONArray _jArr = new JSONArray();
		Iterator _itrKeys = _jObj.sortedKeys();
		String _key;
		while (_itrKeys.hasNext()) {
			_key = _itrKeys.next().toString();
			if (labelName == null || !_key.equals(labelName)) {
				_jArr.put(_key);
			}
		}

		return _jArr;
	}
	
	// calculates the entropy
	public static double calculateEntropy(JSONArray _jArr) {
		// TODO Auto-generated method stub
		
		int _total = 0;
		double _entropy = 0.0;
		try {
			
			for (int i = 0; i < _jArr.length(); i++) {
				_total += _jArr.getInt(i);
			}

			double _prob = 0.0;
			double _log = 0.0;
			for (int i = 0; i < _jArr.length(); i++) {
				_prob = _jArr.getInt(i) / Double.valueOf(_total);
				if (_prob != 0.0)
					_log = (Math.log(_prob) / Math.log(GlobalVariables.LOG_BASE)) * (-1);
				_entropy += (_prob * _log);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return _entropy;
	}
	
	// sorts the nodes according to the entropy and returns the position value for creating XML file.
	public static JSONArray sortNodes(JSONObject jObj) {
		// TODO Auto-generated method stub
		JSONArray _jArr = null;
		try {
			JSONArray _jArrKey = fetchLabels("info_gain", jObj);
			int _size = _jArrKey.length();
			String[] _value = new String[_size];
			JSONArray _jTempArr;
			for(int i = 0; i < _size; i++) {
				_jTempArr = jObj.getJSONArray(_jArrKey.getString(i));
				String _val = _jTempArr.getString(_jTempArr.length()-1) + "," + _jArrKey.getString(i);
				_value[i] = _val;
			}
			
			//sort
			Arrays.sort(_value);
			
			// fetch Sorted Array of Attribute values as key
			_jArr = new JSONArray();
			for(int i = 0; i < _size; i++) {
				_jArr.put(_value[i].split(",")[1]);
			}
			
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return _jArr;
	}
	
	// for creating a XML file
	public static boolean createWriteXMLFile() {
		// TODO Auto-generated method stub
		boolean _status = false;
		try {

			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

			// root elements
			Document doc = docBuilder.newDocument();
			Element rootElement = doc.createElement(GlobalVariables.TREE);
			doc.appendChild(rootElement);

			// set attribute to tree element
			Attr _entropy = doc.createAttribute(GlobalVariables.ENTROPY);
			_entropy.setValue(GlobalVariables.OUTPUT_VAL.getString(GlobalVariables.ENTROPY));
			rootElement.setAttributeNode(_entropy);

			// set nodes of tree element
			Element node = setChildNodes(doc, rootElement, GlobalVariables.OUTPUT_VAL.getJSONArray(GlobalVariables.NODE));
			rootElement.appendChild(node);

			//
			String _output_dir = GlobalVariables.PARENT_DIR + GlobalVariables.OUTPUT_FOLDER;
			File _parentPath = new File(_output_dir);
			File _fileName = new File(_output_dir + GlobalVariables.OUTPUT_FILE_NAME);
			// check whether the parent folder is there or not
			if (!_parentPath.exists()) {
				_parentPath.mkdirs();
				if (_fileName.exists()) {
					_fileName.delete();
				}
			}
			_fileName.createNewFile();

			// write the content into xml file
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File(_output_dir + GlobalVariables.OUTPUT_FILE_NAME));

			// Output to console for testing
			// StreamResult result = new StreamResult(System.out);

			transformer.transform(source, result);

			_status = true;

		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			_status = false;
			System.err.println("ERROR :: In setting nodes to XML file " + e.getLocalizedMessage());
			//e.printStackTrace();
		} catch (TransformerConfigurationException e) {
			// TODO Auto-generated catch block
			_status = false;
			System.err.println("ERROR :: In setting nodes to XML file " + e.getLocalizedMessage());
			//e.printStackTrace();
		} catch (DOMException e) {
			// TODO Auto-generated catch block
			_status = false;
			System.err.println("ERROR :: In setting nodes to XML file " + e.getLocalizedMessage());
			//e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			_status = false;
			System.err.println("ERROR :: In setting nodes to XML file " + e.getLocalizedMessage());
			//e.printStackTrace();
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			_status = false;
			System.err.println("ERROR :: In setting nodes to XML file " + e.getLocalizedMessage());
			//e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			_status = false;
			System.err.println("ERROR :: In setting nodes to XML file " + e.getLocalizedMessage());
			//e.printStackTrace();
		}
		
		return _status;
	}
	
	// for creating the child nodes for the XML file
	private static Element setChildNodes(Document doc, Element rootElement, JSONArray jNodes) {
		// TODO Auto-generated method stub
		Element node = null;
		JSONObject _jNode;
		JSONArray _jKeys;
		try {
			int _nodeSize = jNodes.length();
			for (int i = 0; i < _nodeSize; i++) {
				_jNode = jNodes.getJSONObject(i);

				// nodes
				node = doc.createElement(GlobalVariables.NODE);
				
				_jKeys = fetchLabels(null, _jNode);
				
				Attr _nodeAttr = null;
				String _nodeAttrVal;
				int _nodeKeysSize = _jKeys.length();
				for(int j = 0; j < _nodeKeysSize; j++) {
					_nodeAttrVal = _jKeys.getString(j);			
					if (_nodeAttrVal.equals(GlobalVariables.LABEL))
						node.appendChild(doc.createTextNode(_jNode.getString(_nodeAttrVal)));
					else if (_nodeAttrVal.equals(GlobalVariables.NODE)) {
						// set child nodes
						setChildNodes(doc, node, _jNode.getJSONArray(_nodeAttrVal));
					} else {
						_nodeAttr = doc.createAttribute(_nodeAttrVal);
						_nodeAttr.setValue(_jNode.getString(_nodeAttrVal));
					}
					
					node.setAttributeNode(_nodeAttr);
				}
				
				rootElement.appendChild(node);
			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			System.err.println("ERROR :: In setting child nodes to XML file " + e.getLocalizedMessage());
			//e.printStackTrace();
		}

		return node;
	}	
}
