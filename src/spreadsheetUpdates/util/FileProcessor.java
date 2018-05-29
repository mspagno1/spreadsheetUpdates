package spreadsheetUpdates.util;

import spreadsheetUpdates.util.Logger;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.FileNotFoundException;

import java.io.IOException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.BufferedReader;

public class FileProcessor{
	/**
	 Int of the number of lines read
	 */
	private int linesRead = 0;
	/**
	 Filereader object to read input files
	 */
	private FileReader freader = null;
	/**
	 Buffer object to read in data from a file
	 */
	private BufferedReader bufReader = null;
	/**
	 Filewriter object to write to output files
	 */
	private FileWriter fwriter = null;
	/**
	 String of the file name.
	*/
	private String fileName;
	
	/**
	 * Constructs a fileProcessor object
	 * <p>: 
	 * Creates a fileProccessor object that opens an file to be ready to be read or written to. If the file does not exist then an exception is throw and the program exits. 
	 * <p>
	 * @param  fileName : Name of the file, boolean that shows if output file or input file     
	 * @return FileProccessor object 
	 */
	//http://www.avajava.com/tutorials/lessons/how-do-i-read-a-string-from-a-file-line-by-line.html
	public FileProcessor(String file, boolean trueForInput){
		try{
			fileName = file;
			if(trueForInput == true){
				freader = new FileReader(file);
				
				bufReader = new BufferedReader(freader);
				/*
				if(bufReader.readLine() == null){
					System.err.println("file is empty");
					System.exit(1);
				}
				*/
			}else{
				fwriter = new FileWriter(file);
			}
		}catch(IOException e){
			System.err.println("couldn't open file: " + file);
			e.printStackTrace();
			System.exit(1);
		}finally{
		
		}
	}
	/**
	 * Reads a line from a file/
	 * <p>: 
	 * Trys to read a line from the file. If first line is Null print an error empty file and exist. Otherwise keep
	 * reading until you hit NUll. Updates linesRead on each succesful call
	 * <p>
	 * @param  None       
	 * @return String Line from the file, Null if at the end of the file
	 */
	public String readOneLine(){
		try{
			String line = bufReader.readLine();
	
			if(line == null && linesRead == 0){
				System.err.println("file is empty");
				System.exit(1);
				return null;
			}else if(line != null){
				linesRead++;
				return line;
			}else{
				//System.out.println("eof");
				return null;
			}
		}catch(IOException ex){
			System.err.println(ex.getMessage());
			ex.printStackTrace();
			System.exit(1);
		}finally{}
		return null;
	}
	/**
	 * Writes a line to a file
	 * @param  String line you wish to write  
	 * @return None
	*/
	public void writeOneLine(String line){
		try{
			fwriter.write(line + "\n");
		}catch(IOException ex){
			System.err.println(ex.getMessage());
			ex.printStackTrace();
			System.exit(1);
		}finally{}
	}
	/**
	 * Closes an open read files
	 * <p>: 
	 * Closes open read buffer, if it cant throws an IOException
	 * <p>
	 * @param  None       
	 * @return None
	 */
	public void closeReadFile(){
		try{
			bufReader.close();
		}catch(IOException ex){
			System.err.println(ex.getMessage());
			ex.printStackTrace();
			System.exit(1);
		}finally{}
	}
	/**
	 * Closes an open write files
	 * <p>: 
	 * Closes open write buffer, if it cant throws an IOException
	 * <p>
	 * @param  None       
	 * @return None
	 */
	public void closeWriteFile(){
		try{
			fwriter.close();
		}catch(IOException ex){
			System.err.println(ex.getMessage());
			ex.printStackTrace();
			System.exit(1);
		}finally{};
	}
	
	/**
	 * Getter for fileName
	 * @param  None       
	 * @return String fileName
	 */
	public String getFileName(){
		return fileName;
	}
	/**
	 * Setter for fileName
	 * @param  String fileName       
	 * @return None
	 */
	public void setFileName(String name){
		fileName = name;
	}
		
	
	/**
	 * toString() for fileProccesser
	 * @param  None      
	 * @return String of the infomation of the fileProccessor
	 */
	public String toString(){
		
		
		String line = "";
		line = line + "fileName " + fileName + "\n";
		line = line  + "linesRead " + linesRead + "\n";
		line = line  + "freader " + freader + "\n";
		line = line  + "bufReader " + bufReader + "\n";
		line = line  + "fwriter " +fwriter + "\n";
		return line;
	}
	
		

}
