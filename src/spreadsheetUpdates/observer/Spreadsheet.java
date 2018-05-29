package spreadsheetUpdates.observer;

import spreadsheetUpdates.util.Logger;

import java.util.ArrayList;
import spreadsheetUpdates.util.FileProcessor;
import spreadsheetUpdates.observer.Observer;
import spreadsheetUpdates.observer.Cell;

public class Spreadsheet implements Subject{
	/**
     	* FileProccessor inputfile
     	*/
	private FileProcessor inputFile = null;
	/**
     	* FileProccessor outputFile
     	*/
	private FileProcessor outputFile = null;
	/**
     	* int debugValue
     	*/
	private int debugValue;
	/**
     	* Table of observers
     	*/
	private Observer table[][];
	
	/**
	     * SpreadSheet Constructor
	     * @param  FileProcessor obj for input file, FileProcessor obj for output file, int for debug val
	     * @return Spreadsheet Object
        */
	public Spreadsheet(FileProcessor in, FileProcessor out, int debug){
		inputFile = in;
		outputFile = out;
		debugValue = debug;
		//init table to 0	
		table = new Cell[26][26];
		for(int i = 0; i < 26; i++){
			char letterIndex = 'a';
			//http://stackoverflow.com/questions/10358583/adding-char-and-int
			//link is for adding int and char for creating the cell name "a1" etc
			letterIndex = (char)(letterIndex + i);
			for(int j = 0; j < 26; j++){
				//table[i][j] = null;
				//https://examples.javacodegeeks.com/core-java/character/how-to-convert-character-to-string-and-a-string-to-character-array-in-java/
				//link above is for the toString() conversion
				String cellName = Character.toString(letterIndex);
				String numIndex = Integer.toString(j+1);
				cellName = cellName + numIndex;
				//System.out.println("cellName in ctor: " + cellName);
				table[i][j] = new Cell(cellName, this);
				((Cell)table[i][j]).setCellRow(i);
				((Cell)table[i][j]).setCellCol(j);
				
			}
		}
		Logger.writeMessage("Spreadsheet ctor is called", Logger.DebugLevel.CTOR);
	
	}
	
	
	/**
	     * reads and processes a line from the input file
	     * <p>
	     * reads a line from the input file and checks if the right hand vals
	     * of the cells are cell indices or number. Then it does the approriate 
	     * conversions for indices and checks for circular dependencies.
	     * if there are no circular dependecies it updates the cells in the table
	     * else it ignores the line and writes the line to the logger.
	     * then it reads in the next line of the file
	     * <p>
	     * @param  None
	     * @return None
        */
	public void readFile(){
		String line = inputFile.readOneLine();
		int i = 0;
		
		while(line != null){
			Logger.writeMessage("Current line being read is " + line, Logger.DebugLevel.INPUT_LINE);
			boolean validCell = false;
			String[] prefTokens = line.split("[=+]");
			String cellName = prefTokens[0];
			
			String data1 = prefTokens[1];
			String data2 = prefTokens[2];
			//System.out.println("operating on line: " + line);
			//System.out.println("cellName: " + cellName);
			//System.out.println("data1: " + data1);
			//System.out.println("data2: " + data2);
			int row = convertLetterIndex(cellName);
			int col = convertNumberIndex(cellName);

			Cell newCell = (Cell)table[row][col];
			
			//Old variables incase its 
			int oldVal1 = newCell.getVal1();
			int oldVal2 = newCell.getVal2();
			String oldData1 = newCell.getData1();
			String oldData2 = newCell.getData2();
			
			try{
				newCell.setVal1(Integer.parseInt(data1));
				//Set this equal to empty since a number is there
				newCell.setData1("EMPTY");
			}catch(NumberFormatException e){
				//its fine as a string
				newCell.setData1(data1);
				
			}finally{}
			
			
			try{
				newCell.setVal2(Integer.parseInt(data2));
				//Set this equal to empty since a number is there
				newCell.setData2("EMPTY");
			}catch(NumberFormatException e){
			
				newCell.setData2(data2);
			}finally{}
			
			//Two data cell string locations
			if(!newCell.getData1().equals("EMPTY") && !newCell.getData2().equals("EMPTY")){
				boolean circle = checkDependency(cellName, newCell);
				//If its true we have a dependency and we do nothing put print the line
				if(true == circle){

					outputFile.writeOneLine("Dependency for line: " + line);
				}
				//Else we update our table and broadcast
				else{
					validCell = true;
				}
				
				//Has to check for two circle dependencies
			}
			//First data is a cell loc and second is a number
			else if(!newCell.getData1().equals("EMPTY") && newCell.getData2().equals("EMPTY")){
				boolean circle = checkDependency(cellName, newCell);
				//If its true we have a dependency and we do nothing put print the line
				if(true == circle){
					outputFile.writeOneLine("Dependency for line: " + line);
					
				}
				//Else we update our table and broadcast
				else{
					validCell = true;
				}
			}
			//First value is a number and second is a cell loc
			else if(newCell.getData1().equals("EMPTY") && !newCell.getData2().equals("EMPTY")){
				boolean circle = checkDependency(cellName, newCell);
				//If its true we have a dependency and we do nothing put print the line
				if(true == circle){
					outputFile.writeOneLine("Dependency for line: " + line);
					
				}
				//Else we update our table and broadcast
				else{
					validCell = true;
				}
			}
			//Both are numbers
			else{
				//Nothing done here just addition
				validCell = true;
			}
			
			//print cellName
			//register observer
			if(true == validCell){
				
				//System.out.println("Line added" + line);
				registerObserver(newCell);
				notifyObserver();
			}
			else{
				//If we dont add the line we reset the data back to the original data members
				newCell.setVal1(oldVal1);
				newCell.setVal2(oldVal2);
				newCell.setData1(oldData1);
				newCell.setData2(oldData2);
			}
			//update here?
			
			line = inputFile.readOneLine();
			
		}

		//update table
	}
	
	/**
	     * Adds up the total values from all the cells
	     * @param  None
	     * @return None
        */
	public void addUpTotal(){
		int totalSum = 0;
		
		for(int i = 0; i < 26; i++){
			for(int j = 0; j < 26; j++){
				//Logger print each value here
				Cell currCell = (Cell)table[i][j];
				int cellValue = currCell.getTotal();
				totalSum = cellValue + totalSum;
			}
		}
		outputFile.writeOneLine("The total sum is : " + totalSum);
	}
				
	/**
	     * Checks to see if there is a dependency 
	     * <p>
	     * Checks to see if there is dependency by recursively calling this function until the two
	     * base cases occur. 
	     * Base 1 If both datas are empty it means we have two number and there is no dependecy
	     * Base 2 If one of the cells has the orignal cells name in the data we have our circle
	     * If we have two cell or one cell and its not the og cell name we recursively call on those
	     * cells til we hit one of our two base cases
	     * <p>
	     * @param  String orignal cell name, Cell current cell your working on
	     * @return false on no dependency true on dependency
        */
	
	public boolean checkDependency(String ogCell, Cell currCell){
		//Base case two numbers
		if(currCell.getData1().equals("EMPTY") && currCell.getData2().equals("EMPTY")){
			return false;
		}
		else if(currCell.getData1().equals(ogCell) || currCell.getData2().equals(ogCell)){
			String name1 = currCell.getData1();
			String name2 = currCell.getData2();
			//System.out.println(ogCell + " Dependency " +  name1+ " " + name2);
			return true;
		}
		else if(!currCell.getData1().equals("EMPTY") && currCell.getData2().equals("EMPTY")){
			//Has to check for one circle dependecy
			String name1 = currCell.getData1();
			String name2 = currCell.getData2();
			//System.out.println(ogCell + " Left Name " +  name1+ " " + name2);
			int dataRow1 = currCell.getData1Row();
			int dataCol1 = currCell.getData1Col();
			return checkDependency(ogCell, (Cell)table[dataRow1][dataCol1]);
		}
		//First value is a number and second is a cell loc
		else if(currCell.getData1().equals("EMPTY") && !currCell.getData2().equals("EMPTY")){
			String name1 = currCell.getData1();
			String name2 = currCell.getData2();
			//System.out.println(ogCell + " Right Name " +  name1+ " " + name2);
			int dataRow2 = currCell.getData2Row();
			int dataCol2 = currCell.getData2Col();
			return checkDependency(ogCell, (Cell)table[dataRow2][dataCol2]);
		}
		//Two dependency search
		else{
			String name1 = currCell.getData1();
			String name2 = currCell.getData2();
			//System.out.println(ogCell + " Two names " +  name1+ " " + name2);
			//Check if left side has dependency
			int dataRow1 = currCell.getData1Row();
			int dataCol1 = currCell.getData1Col();
			boolean checkData1 = checkDependency(ogCell, (Cell)table[dataRow1][dataCol1]);
			//Check if right side has dependency
			int dataRow2 = currCell.getData2Row();
			int dataCol2 = currCell.getData2Col();
			
			
			boolean checkData2 = checkDependency(ogCell, (Cell)table[dataRow2][dataCol2]);
			//If there a dependency from either then we return true
			if(true == checkData1 || true == checkData2){
				return true;
			}
			//Otherwise if there is none we return false
			else{
				return false;
			}
			
		}	
	}
	
	/**
	     * Checks to see if string contains a letter
	     * @param  String your working on
	     * @return true if contains letter false if no letter
        */
	public boolean containsLetter(String expr) {
    		return expr.matches("[a-zA-Z]+");
	}
	
	/**
	     * Registers an observer in our table
	     * @param  Observer you are registering
	     * @return None
        */
	public void registerObserver(Observer o){
		
		int cellRow = ((Cell)o).getCellRow();
		int cellCol = ((Cell)o).getCellCol();
		table[cellRow][cellCol] = (Cell)o;
		
	}
	
	/**
	     * Remove an observer in our table
	     * @param  Observer you are removing
	     * @return None
        */
	public void removeObserver(Observer o){
		if(o != null){
			int cellRow = ((Cell)o).getCellRow();
			int cellCol = ((Cell)o).getCellCol();
			table[cellRow][cellCol] = null;
			//System.out.println("removed observer");
		}else{
			//System.out.println("observer is null");
		}
	}
	/**
	     * Notify the observers that they have to update since something changed
	     * @param  None
	     * @return None
        */
	public void notifyObserver(){
		for(int i = 0; i < 26; i++){
			for(int j = 0; j < 26 ; j++){
				//table[i][j];
				
				table[i][j].update();
					
			}
		}
	}
	
	/**
	     * Converts a String of the letter of the cellName to the index form
	     * @param  String of the cellLoc (A-> [0])
	     * @return int index value
        */
	public int convertLetterIndex(String cellLoc){
		char letterIndex = cellLoc.charAt(0);
		letterIndex = Character.toLowerCase(letterIndex);
		
		int retVal = (int)letterIndex - 97;
		//System.out.println("converted char index: " + retVal); 	
		return retVal;
	}
	/**
	     * Converts a String of the number part of the cellName to an int
	     * @param  String of the number of cellName (1 -> [0])
	     * @return int index value
        */
	public int convertNumberIndex(String cellLoc){
		String numberIndex = cellLoc.substring(1);
		int retVal = 0;
		try{
			
			retVal = Integer.parseInt(numberIndex);
			//matrices start at 0
			retVal = retVal - 1;
		}catch(NumberFormatException e){
			e.printStackTrace();
			System.exit(1);
		}finally{}
		//System.out.println("converted num index: " + retVal);
		return retVal;
	}
	
	
	/**
	     * Prints the table of the cells if logger value is set to do so
	     * @param  None
	     * @return None
        */
	public void printTable(){
		for(int i = 0; i < 26; i++){
			for(int j = 0; j < 26; j++){
				int val1 = ((Cell)table[i][j]).getVal1();
				int val2 = ((Cell)table[i][j]).getVal2();
				int result = val1 + val2;
				char letterIndex = (char)('a' + i);
				//int numIndex = j;
				int numIndex = j+1;
				String strLetterInd = Character.toString(letterIndex);
				Logger.writeMessage("("+ strLetterInd + numIndex + ")" +"table[" + i + "][" + j + "] = " + result, Logger.DebugLevel.TABLE);
				
				//System.out.println("("+ strLetterInd + numIndex + ")" +"table[" + i + "][" + j + "] = " + result);
			}
		}
	}
	
	//Getters and setters
	
	/**
	     * Getter for table 
	     * @param  None
	     * @return Observer[][] table
        */
	public Observer[][] getTable(){
		return table;
	}
	/**
	     * Setter for table 
	     * @param  Observer[][] table
	     * @return None
        */
	public void setTable(Observer[][] tab){
		table = tab;
	}
	
	/**
	     * toString
	     * @para None
	     * @return String of data members
        */
	
	public String toString(){
		String retVal = "" + debugValue;
		retVal = retVal + " " + inputFile.toString();
		retVal = retVal + " " + outputFile.toString();
		return retVal;
		
		
	}
	
}



