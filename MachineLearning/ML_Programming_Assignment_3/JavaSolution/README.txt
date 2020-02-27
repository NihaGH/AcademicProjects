*************************************************************************************************************************
******************************************************** README *********************************************************
*************************************************************************************************************************

1.	Open the terminal or command prompt.
2.	The parameters needed are: 
		*	Data (-Ddata : the csv file name as input file)
		*	Output (-Doutput : the XML file name in which u want to store the output)
		*	Directory (-Ddir : the path means the parent directory where the tsv file resides) 
					   This argument is optional, if both the jar file and tsv file resides in the same location then no need of passing this argument.

3.	Run the following command
		java -Ddir=<<path_where_the_tsv_file_resides>> -Ddata=<<input_file_name.tsv>> -Doutput=<<output_file_name.tsv>> 
		-jar <<path_where_the_jar_file_resides/jar_file_name.jar>>
		
		Example : (in ubuntu environment)
		*	With -Ddir argument
		java -Ddir=Desktop/perceptron/ -Ddata=Gauss.tsv -Doutput=Gauss_output.tsv -jar Desktop/Perceptron.jar
		
		*	Without -Ddir argument
		java -Ddata=Gauss.tsv -Doutput=Gauss_output.tsv -jar Perceptron.jar

4.	Output on console will be
		INPUT DIR :: /home/nihatompi/Desktop/decisiontree/Gauss.tsv
		OUTPUT DIR :: /home/nihatompi/Desktop/decisiontree/Solutions/Gauss_output.tsv
		INFO :: Gauss.tsv file is successfully read.
		INFO :: Data set successfully processed with both constant and annealing learning rates.
		INFO :: Iterations Successfully written to the output file. Please check the output folder for the file.
		
5.	Afer successful completion go to the Solution folder and open the .xml file to see the output.

6.	The folder <<Perceptron>> contains the source files (in java).

*************************************************************************************************************************
*************************************************************************************************************************
*************************************************************************************************************************
