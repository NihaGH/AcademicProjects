*************************************************************************************************************************
******************************************************** README *********************************************************
*************************************************************************************************************************

1.	Open the terminal or command prompt.
2.	The parameters needed are: 
		*	Data (-Ddata : the csv file name as input file)
		*	Output (-Doutput : the XML file name in which u want to store the output)
		*	Directory (-Ddir : the path means the parent directory where the csv file resides) 
					   This argument is optional, if both the jar file and csv file resides in the same location then no need of passing this argument.

3.	Run the following command
		java -Ddir=<<path_where_the_csv_file_resides>> -Ddata=<<input_file_name.csv>> -Doutput=<<output_file_name.xml>> 
		-jar <<path_where_the_jar_file_resides/jar_file_name.jar>>
		
		Example : (in ubuntu environment)
		*	With -Ddir argument
		java -Ddir=Desktop/DecissionTree/ -Ddata=nursery.csv -Doutput=nursery_output.csv -jar Desktop/ID3.jar
		
		*	Without -Ddir argument
		java -Ddata=nursery.csv -Doutput=nursery_output.csv -jar ID3.jar

4.	Output on console will be
		INPUT DIR :: /home/nihatompi/Desktop/decisiontree/nursery.csv
		OUTPUT DIR :: /home/nihatompi/Desktop/decisiontree/Solutions/nursery_output.xml
		INFO :: nursery.csv file is successfully read.
		INFO :: XML file successfully created in the given OUTOUT DIRECTORY.
		
5.	Afer successful completion go to the Solution folder and open the .xml file to see the output.

6.	The folder <<ID3>> contains the source files (in java).

*************************************************************************************************************************
*************************************************************************************************************************
*************************************************************************************************************************
