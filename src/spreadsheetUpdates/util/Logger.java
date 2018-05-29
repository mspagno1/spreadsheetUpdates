
package  spreadsheetUpdates.util;

public class Logger{
    /*DEBUG_VALUE=4 [Print table after the inputfile + operations are processed]
      DEBUG_VALUE=3 [Print every time a cell is updated]
      DEBUG_VALUE=2 [Print every input line]
      DEBUG_VALUE=1 [Print constructor call]
      DEBUG_VALUE=0 [No output should be printed from the application, except the line "The average preference value is X.Y" ]
    */



    public static enum DebugLevel { EMPTY ,CTOR, INPUT_LINE, UPDATED_CELL, TABLE};

    private static DebugLevel debugLevel;


    public static void setDebugValue (int levelIn) {
	switch (levelIn) {
	  //case 4: debugLevel = DebugLevel.CONSTRUCTOR; break;
	      // add code for other cases
	  //case 0: debugLevel = DebugLevel.RELEASE; break;
	  case 0: debugLevel = DebugLevel.EMPTY; break;
	  case 1: debugLevel = DebugLevel.CTOR; break;
	  case 2: debugLevel = DebugLevel.INPUT_LINE; break;
	  case 3: debugLevel = DebugLevel.UPDATED_CELL; break;
	  case 4: debugLevel = DebugLevel.TABLE; break;
	}
    }

    public static void setDebugValue (DebugLevel levelIn) {
	debugLevel = levelIn;
    }

    // @return None
    public static void writeMessage (String  message  ,
                                     DebugLevel levelIn ) {
	if (levelIn == debugLevel)
	    System.out.println(message);
    }

    /**
	 * @return String
	 */
    public String toString() {
	return "Debug Level is " + debugLevel;
    }
}
