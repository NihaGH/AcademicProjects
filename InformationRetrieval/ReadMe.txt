************************************************************************************************************************************
************************************************************ README ****************************************************************
************************************************************************************************************************************
1. Open the terminal or command prompt.

2. The parameters needed are: 

      *-docs: Path to the folder containing HTML and TEXT documents as placed directly in to the folder or subfolders.
		
      *-index: Path to folder where the indexed files will be stored. If the folder does not exist, it will create a new one.
		
      *-model: Ranking model: user is given the option to choose a ranking scheme between Vector Space Model and Okapi BM25.
	  		   This field is optional. Default Ranking Model is OK. Other models not supported. 
  
         Write VS to choose Vector Space Model 
               OK to choose Okapi BM25 Model
     
      *-query: Query term to be searched for. 
          If there are two query words, enclose them with quotes.
          This fiels is optional. If not provided the user will be asked to enter it during runtime.


3.  Run the following command
		
   java -jar <<path_where_the_jar_file_resides/jar_file_name.jar>> -docs=<<path_where_the_files_resides>> 
        -index=<<path_where_indexed_files_stored>> -model=<<VS/OK>> -query=<<query_string_need_to_be_searched>> 
		
      Example : (in ubuntu environment)
      *	With all argument
        java -jar Desktop/IRLucene.jar -docs=Desktop/IRLuceneTestFile/ -index=Desktop/IRLuceneTestFileIndexes 
			 -model=VS -query='test query' 
	
      *	Without -model and -query argument
        java -jar Desktop/IRLucene.jar -docs=Desktop/IRLuceneTestFile/ -index=Desktop/IRLuceneTestFileIndexes


4.	The folder <<IRLucene>> contains the source files (in java).


*************************************************************************************************************************************
*************************************************************************************************************************************
*************************************************************************************************************************************