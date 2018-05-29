
#Compilation
From the folder: john_poblador_matthew_spagnoli/spreadsheetUpdates/src

ant clean
ant all


TO RUN:
From the folder: john_poblador_matthew_spagnoli/spreadsheetUpdates/src

ant run -Darg0=<input-file-name> -Darg1=<output-file-name> -Darg2=<debug_value>

Example:
ant run -Darg0=input.txt -Darg1=out.txt -Darg2=0

The files are read/written from/to here:
	john_poblador_matthew_spagnoli/spreadsheetUpdates/input.txt
	john_poblador_matthew_spagnoli/spreadsheetUpdates/out.txt
	
Logger debug level 0: no print statements
Logger debug level 1: prints all contructor calls
Logger debug level 2: prints each input line from the file
Logger debug level 3: prints every time an update occurs on a cell
Logger debug level 4: prints the <excel-like >table after the info is processed


CHOICE OF DATA STRUCTURE:
Data structure used is ... because the frequent operations are ... and the time complexity for avg case is ...
We used a matrix to store cell objects because access time is O(1). Also if the cell is not assigned a value in 
the file, we must initialize the undeclared cells to 0 to resolve dependencies. The space complexity is O(N) because
there are N cells in the table

Citations:
1. http://stackoverflow.com/questions/10358583/adding-char-and-int
-add an int to a char for ascii conversion
2.https://examples.javacodegeeks.com/core-java/character/how-to-convert-character-to-string-and-a-string-to-character-array-in-java/
-toString conversion for Character and Integer

