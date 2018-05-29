package spreadsheetUpdates.observer;

import spreadsheetUpdates.util.Logger;

import spreadsheetUpdates.observer.Spreadsheet;
import java.lang.Character;

public class Cell implements Observer{
	/**
     	* Name of the cell
     	*/
	private String cellStr;
	/**
    	 * Int value of the cell
     	*/
	private int cellTotal;
	/**
     	* Cells row number in int form
     	*/
	private int cellRow;
	/**
    	 * Cells col number in int form
     	*/
	private int cellCol;
	/**
    	 * String name of first data member (EMPTY if number)
     	*/
	private String data1;
	/**
    	 * String name of second data member (EMPTY if number)
     	*/
	private String data2;
	/**
    	 * int of the first data (init to 0 if its a cell)
     	*/
	private int val1;
	/**
    	 * int of the second data (init to 0 if its a cell)
     	*/
	private int val2;
	/**
    	 * row location of the first data member( -1 if number)
     	*/
	private int data1Row;
	/**
    	 * col location of the first data member( -1 if number) 
     	*/
	private int data1Col;
	/**
    	 * row location of the second data member( -1 if number)
     	*/
	private int data2Row;
	/**
    	 * col location of the second data member( -1 if number) 
     	*/
	private int data2Col;
	/**
    	 * Spreadsheet object
     	*/
	private Subject spreadsht;
	
	 /**
     	 * Cell Constructor
    	 * @param  String name of cell<a1 etc> , subject object for observer pattern
    	 * @return Cell Object
    	 */
	public Cell(String cellInfo, Subject sub){
		Logger.writeMessage("Cell ctor is called", Logger.DebugLevel.CTOR);
		spreadsht = sub;
		//spreadsht.registerObserver(this);
		//System.out.println("registered obs in: " + cellInfo);
		cellStr = cellInfo;
		cellTotal = 0;
		cellRow = 0;
		cellCol = 0;
		data1 = "EMPTY";
		data2 = "EMPTY";
		//val1 and val2 are the 2 right hand vals of the cell equation
		val1 = 0;
		val2 = 0;
		data1Row = -1;
		data1Col = -1;
		data2Row = -1;
		data2Col = -1;
		
	}
	
	/**
	     * calculates and gets the total value of a cell
	     * <p>
	     * Checks if the right hand values of the cell are cell indices or numbers
	     * if they are indices we resolve them so that they are numbers
	     * <p>
	     * @param  table of cells, current cell object in the recursive call
	     * @return the total value of the cell
     	*/
	public int getCellValue(Observer[][] table, Cell cell){
		if(cell.getData1().equals("EMPTY") && cell.getData2().equals("EMPTY")){
			int a = cell.getVal1();
			int b = cell.getVal2();
			//System.out.println("CellName " + cell.getCellStr() + " val1 " + a + " val2 " + b);
			
			return cell.getVal1() + cell.getVal2();
		}
		else{
			int leftVal;
			int rightVal;
			
			//If data not empty then get then rec to find the value at that cell
			if(!cell.getData1().equals("EMPTY")){	
				int leftRow = cell.getData1Row();
				int leftCol = cell.getData1Col();
				leftVal =  cell.getCellValue(table,(Cell)table[leftRow][leftCol]);
			}
			else{
				leftVal = cell.getVal1();
			}
			//If data not empty then get then rec to find the value at that cell
			if(!cell.getData2().equals("EMPTY")){
				int rightRow = cell.getData2Row();
				int rightCol = cell.getData2Col();
				rightVal = cell.getCellValue(table,(Cell)table[rightRow][rightCol]);
			}
			else{
				rightVal = cell.getVal2();
			}

			cell.setTotal(rightVal + leftVal);
			return cell.getTotal();
		}
	} 
	
	
	/**
	     * update the cell in the subject's 2d matrix
	     * <p>
	     * calls getCellValue() and sets the return value of that function to the cell object
	     * <p>
	     * @param  None
	     * @return None
        */
	public void update(){
		Logger.writeMessage("Update cell on cell " + cellStr, Logger.DebugLevel.UPDATED_CELL);
		Observer table[][] = ((Spreadsheet)spreadsht).getTable();
		//System.out.println("Update called on " + getCellStr());
		
		if(!data1.equals("EMPTY")){
			int leftRow = getData1Row();
			int leftCol = getData1Col();
			val1 =  getCellValue(table,(Cell)table[leftRow][leftCol]);
		}
		if(!data2.equals("EMPTY")){
			int rightRow = getData2Row();
			int rightCol = getData2Col();
			val2 =  getCellValue(table,(Cell)table[rightRow][rightCol]);
		}
		
		
		cellTotal = val1 + val2;
		//System.out.println(cellStr + " Name1 " + data1 + " Name2 " + data2);
	}
	
	//getters and setters
	/**
	     * Getter for cellTotal val
	     * @param  None
	     * @return Int cellTotal
	*/
	public int getTotal(){
		return cellTotal;
	}
	
	/**
	     * Setter for cellTotal val
	     * @param  int total
	     * @return None
	*/
	public void setTotal(int total){
		cellTotal = total;
	}
	
	/**
	     * Getter for cellRow val
	     * @param  None
	     * @return cellRow
	*/
	public int getCellRow(){
		return cellRow;
	}
	/**
	     * Setter for cellRow val
	     * @param  int row
	     * @return None
	*/
	public void setCellRow(int row){
		cellRow = row;
	}
	
	/**
	     * Getter for cellCol val
	     * @param  None
	     * @return cellCol
	*/
	public int getCellCol(){
		return cellCol;
	}
	
	/**
	     * Setter for cellCol val
	     * @param  int col
	     * @return None
	*/
	public void setCellCol(int col){
		cellCol = col;
	}
	
	/**
	     * Getter for data1 val
	     * @param  None
	     * @return data1
	*/
	public String getData1(){
		return data1;
	}	
	
	/**
	     * Setter for data1 val
	     * @param  int data_in
	     * @return None
	*/
	public void setData1(String data_in){
		data1 = data_in;
		if(!data1.equals("EMPTY")){
			convertToIndex(data1, 1);
		}
		else{
			data1Row = -1;
			data1Col = -1;
			
		}
	}
	
	/**
	     * Getter for data2 val
	     * @param  None
	     * @return data2
	*/
	public String getData2(){
		return data2;
	}	
	
	/**
	     * Setter for data2 val
	     * @param  int data_in
	     * @return None
	*/
	public void setData2(String data_in){
		data2 = data_in;
		if(!data2.equals("EMPTY")){
			convertToIndex(data2, 2);
		}
		else{
			data2Row = -1;
			data2Col = -1;
		}
	}
	
	/**
	     * Getter for cellStr val
	     * @param  None
	     * @return cellStr
	*/
	public String getCellStr(){
		return cellStr;
	}	
	
	/**
	     * Setter for cellStr val
	     * @param  String str
	     * @return None
	*/
	public void setCellStr(String str){
		cellStr = str;
	}
	
	/**
	     * Getter for val1 val
	     * @param  None
	     * @return val1
	*/
	public int getVal1(){
		return val1;
	}
	
	/**
	     * Setter for val1 val
	     * @param  int value1
	     * @return None
	*/
	public void setVal1(int value1){
		val1 = value1;
	}
	
	/**
	     * Getter for val2 val
	     * @param  None
	     * @return val2
	*/
	public int getVal2(){
		return val2;
	}
	
	/**
	     * Setter for val2 val
	     * @param  int value2
	     * @return None
	*/
	public void setVal2(int value2){
		val2 = value2;
	}
	
	/**
	     * Getter for data1Row val
	     * @param  None
	     * @return data1Row
	*/
	public int getData1Row(){
		return data1Row;
	}
	/**
	     * Getter for data1Col val
	     * @param  None
	     * @return data1Col
	*/
	public int getData1Col(){
		return data1Col;
	}
	/**
	     * Getter for data2Row val
	     * @param  None
	     * @return data2Row
	*/
	public int getData2Row(){
		return data2Row;
	}
	
	/**
	     * Getter for data2Col val
	     * @param  None
	     * @return data2Col
	*/
	public int getData2Col(){
		return data2Col;
	}
	
	/**
	     * Setter for data1Row val
	     * @param  int row
	     * @return None
	*/
	public void setData1Row(int row){
		data1Row = row;
	}
	
	/**
	     * Setter for data1Col val
	     * @param  int col
	     * @return None
	*/
	public void setData1Col(int col){
		data1Col = col;
	}
	/**
	     * Setter for data2Row val
	     * @param  int row
	     * @return None
	*/
	public void setData2Row(int row){
		data2Row = row;
	}
	
	/**
	     * Setter for data2Col val
	     * @param  int col
	     * @return None
	*/
	public void setData2Col(int col){
		data1Col = col;
	}
	
	//end of getters and setters
	
	//for an existing cell(if you have it)
	/**
	     * converts a string to an index in subject's 2d matrix
	     * <p>
	     * converts a string to an index and assigns the index vals to the appropriate data 
	     * members based on the dataNum param<0 is cellRow/cellCol, 1 is data1Row/data1Col,
	     * 2 is data2Row/data2Col
	     * <p>
	     * @param  string to translate to index, int that specifies which data we are parsing
	     * @return None
        */
	public void convertToIndex(String cellLoc, int dataNum){
		char letterIndex = cellLoc.charAt(0);
		letterIndex = Character.toLowerCase(letterIndex);	
		String numberIndex = cellLoc.substring(1);
		
		try{
			//indices for current cell object
			if(0 == dataNum){
				
				//convert from ascii to row index in table[][]
				cellRow = (int)letterIndex - 97;
				cellCol = Integer.parseInt(numberIndex);
			}
			//indices for data1
			else if(1 == dataNum){
				data1Row = (int)letterIndex - 97;
				data1Col = Integer.parseInt(numberIndex) -1;
				//System.out.println("For loc " +cellLoc + " data1Row " + data1Row+ " data1col " + data1Col);
			}
			//indices for data2
			else if(2 == dataNum){
				data2Row = (int)(letterIndex - 97);
				data2Col = Integer.parseInt(numberIndex) -1;
				//System.out.println("For loc " +cellLoc + " data2Row " + data2Row+ " data2col " + data2Col);
			}
		
		}catch(NumberFormatException e){
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	/**
	     * ToString print a cell
	     * @param  None
	     * @return String (cellStr + cellTotal + cellRow + cellCol + data1
	     * + data2 + val1 + val2 + data1Row + data1Col + data2Row + data2Col)
        */
	public String toString(){

		String retVal = "" + cellStr;
		retVal = retVal + " " + cellTotal;
		retVal = retVal + " " + cellRow;
		retVal = retVal + " " + cellCol;
		retVal = retVal + " " + data1;
		retVal = retVal + " " + data2;
		retVal = retVal + " " + val1;
		retVal = retVal + " " + val2;
		retVal = retVal + " " + data1Row;
		retVal = retVal + " " + data1Col;
		retVal = retVal + " " + data2Row;
		retVal = retVal + " " + data2Col;
		return retVal;
	
	}
	
}

