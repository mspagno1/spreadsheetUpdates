
package spreadsheetUpdates.driver;

import java.lang.IllegalArgumentException;
import spreadsheetUpdates.util.FileProcessor;
import spreadsheetUpdates.util.Logger;
import spreadsheetUpdates.observer.Spreadsheet;

public class Driver{

	
	public static void main(String args[]) {
	    Driver dr = new Driver();
	    Logger logger = new Logger();
	    dr.validateArgs(args, logger);
	    
	  
	    

	    FileProcessor inputFile = new FileProcessor(args[0],true);
	    FileProcessor outputFile = new FileProcessor(args[1],false);
	    
	    // create an instance of this Driver class
	    // invoke validateArgs(...)
	    
	    //public Spreadsheet(FileProcessor in, int debug){
	    Spreadsheet spreadsht = new Spreadsheet(inputFile,outputFile, 0);
	    
	    spreadsht.readFile();
	    
	    spreadsht.addUpTotal();
	    spreadsht.printTable();
	    
	    inputFile.closeReadFile();
	    outputFile.closeWriteFile();

	} // end main(...)
	
	private void validateArgs(String args[], Logger logger){
		//validate number of arguments
		if(args.length==3){
		    // get file names

			int debugValue = 0;
			try{
				debugValue = Integer.parseInt(args[2]);
				logger.setDebugValue(debugValue);
			}catch(NumberFormatException e){
				System.err.println("Debugvalue is not an int");
				e.printStackTrace();
				System.exit(1);
			}finally{
				if(debugValue < 0 || debugValue > 4){
					System.err.println("Debug value is not between 0 and 4");
					System.exit(1);
				}
			}
		}else{
			System.err.println("Invalid number of arguments. Expected [FIXME: provide details here]");
			System.exit(1);
		}
		
		
	}
	

    

} // end public class Driver

