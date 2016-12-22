package registrationScheduler.util;

/**
 * @author shashiupadhyay
 *
 */
public class Logger {
	public static enum DebugLevel {
		NO_OUTPUT, DATA_STRUCTURE_POPULATED, DATA_STRUCTURE_RESULTS, THREAD_RUN_CALLED, CONSTRUCTOR_CALLED
	};

	private static DebugLevel debugLevel;

	/**
	 * @param DEBUG_VALUE
	 */
	public static void setDebugValue(int DEBUG_VALUE) {

		if (DEBUG_VALUE >= 0 && DEBUG_VALUE <= 4 ) {
			switch (DEBUG_VALUE) {
			case 0:
//				No output will be displayed
				debugLevel = DebugLevel.NO_OUTPUT;
				break;
//				Print the contents of the data structure
			case 1:
				debugLevel = DebugLevel.DATA_STRUCTURE_RESULTS;
				break;
//				Print each and everytime an entry is added to Result data structure
			case 2:
				debugLevel = DebugLevel.DATA_STRUCTURE_POPULATED;
				break;
//				Everytime Thread run method called
			case 3:
				debugLevel = DebugLevel.THREAD_RUN_CALLED;
				break;
//				When constructor is called
			case 4:
				debugLevel = DebugLevel.CONSTRUCTOR_CALLED;
				break;
			}
		}else{
			System.err.println("Accepted value for DEBUG_VALUE is between [0,1,2,3,4]");
		}
	}

	/**
	 * @param DEBUG_VALUEIn
	 */
	public static void setDebugValue(DebugLevel DEBUG_VALUEIn) {
		debugLevel = DEBUG_VALUEIn;
	}

	/**
	 * @param message
	 * @param DEBUG_VALUE
	 */
	public static void writeMessage(String messageIn, DebugLevel DEBUG_VALUEIn) {
		if(messageIn != null || DEBUG_VALUEIn != null){
			if (DEBUG_VALUEIn == debugLevel) {
				Logger.setDebugValue(DEBUG_VALUEIn);
				System.out.println(DEBUG_VALUEIn + " : " + messageIn);
			}
		}
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return "Enabled DUBEG LEVEL : " + debugLevel;
	}
}
