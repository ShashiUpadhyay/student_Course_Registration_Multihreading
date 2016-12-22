package registrationScheduler.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Properties;

/**
 * @author shashiupadhyay
 *
 */
public class Initializer {
	
	private static String property_file = "src/RegistrationRecords.properties";
	private static int totalsubjects;
	private static int capacityeachsubject;
	private static int totalstudents;
	private static int courserequiredperstudent;
	private static int DEBUG_LEVEL;
	private static int THREAD_COUNT;
	private static String preference_Input_File;
	private static String output_file_name;
	
	static{
		populatePreDefinedValues();
	}

	/**
	 * @return the capacityeachsubject
	 */
	public static int getCapacityeachcourse() {
		return capacityeachsubject;
	}
	
	/**
	 * @return the courserequiredperstudent
	 */
	public static int getSubjectRequiredPerStudent() {
		return courserequiredperstudent;
	}
	

	/**
	 * @return the dEBUG_LEVEL
	 */
	public static int getDEBUG_LEVEL() {
		return DEBUG_LEVEL;
	}

	/**
	 * @return the output_file_name
	 */
	public static String getOutput_file_name() {
		return output_file_name;
	}

	/**
	 * @return the preference_Input_File
	 */
	public static String getPreference_Input_File() {
		return preference_Input_File;
	}

	/**
	 * @return the property_file
	 */
	public static String getProperty_file() {
		return property_file;
	}

	/**
	 * @return the tHREAD_COUNT
	 */
	public static int getTHREAD_COUNT() {
		return THREAD_COUNT;
	}

	/**
	 * @return the totalsubjects
	 */
	public static int getTotalSubjects() {
		return totalsubjects;
	}

	/**
	 * @return the totalstudents
	 */
	public static int getTotalstudents() {
		return totalstudents;
	}

	public static void populatePreDefinedValues(){
		try {
			FileInputStream fileinputstream_ref = new FileInputStream(new File(Initializer.property_file));
			Properties properties = new Properties();
			properties.load(fileinputstream_ref);
			fileinputstream_ref.close();
			Enumeration<?> enuKeys = properties.keys();
			while (enuKeys.hasMoreElements()) {
				String key = (String) enuKeys.nextElement();
				Integer value = Integer.parseInt(properties.getProperty(key));
				if(key.equalsIgnoreCase("totalstudents")){
					Initializer.setTotalstudents(value);
				}
				if(key.equalsIgnoreCase("capacityeachsubject")){
					Initializer.setCapacityeachcourse(value);
				}
				if(key.equalsIgnoreCase("totalsubjects")){
					Initializer.setTotalcourses(value);
				}
				if(key.equalsIgnoreCase("subjectsrequiredperstudent")){
					Initializer.setSubjectsRequiredperStudent(value);
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param capacityeachsubject the capacityeachsubject to set
	 */
	public static void setCapacityeachcourse(int capacityeachcourse) {
		Initializer.capacityeachsubject = capacityeachcourse;
	}

	/**
	 * @param courserequiredperstudent the courserequiredperstudent to set
	 */
	public static void setSubjectsRequiredperStudent(int courserequiredperstudent) {
		Initializer.courserequiredperstudent = courserequiredperstudent;
	}

	/**
	 * @param dEBUG_LEVEL the dEBUG_LEVEL to set
	 */
	public static void setDEBUG_LEVEL(int dEBUG_LEVEL) {
		DEBUG_LEVEL = dEBUG_LEVEL;
	}

	/**
	 * @param output_file_name the output_file_name to set
	 */
	public static void setOutput_file_name(String output_file_name) {
		Initializer.output_file_name = output_file_name;
	}

	/**
	 * @param preference_Input_File the preference_Input_File to set
	 */
	public static void setPreference_Input_File(String preference_Input_File) {
		Initializer.preference_Input_File = preference_Input_File;
	}

	/**
	 * @param property_file the property_file to set
	 */
	public static void setProperty_file(String property_file) {
		Initializer.property_file = property_file;
	}

	/**
	 * @param tHREAD_COUNT the tHREAD_COUNT to set
	 */
	/**
	 * @param tHREAD_COUNT
	 */
	public static void setTHREAD_COUNT(int tHREAD_COUNT) {
		THREAD_COUNT = tHREAD_COUNT;
	}

	/**
	 * @param totalsubjects the totalsubjects to set
	 */
	public static void setTotalcourses(int totalcourses) {
		Initializer.totalsubjects = totalcourses;
	}

	/**
	 * @param totalstudents the totalstudents to set
	 */
	public static void setTotalstudents(int totalstudents) {
		Initializer.totalstudents = totalstudents;
	}

	/**
	 * @param args
	 * @return
	 */
	public static boolean validatingInputArguments(String[] args) {
		File input_file;
		boolean returnvalue = false;
		try {
			if (args.length == 4) {

				if (args[0] != null && !args[0].isEmpty()) {
					input_file = new File(args[0]);
					if (input_file.canRead()) {
						Initializer.setPreference_Input_File(args[0]);
					} else {
						System.err.println("Input file name invalid or missing");
						System.exit(0);
					}
				}

				if (args[1] != null && !args[1].isEmpty()) {
					Initializer.setOutput_file_name(args[1]);
				} else {
					System.err.println("Output file name invalid or missing");
					System.exit(0);
				}

				try {
					if (args[2] != null && !args[2].isEmpty()) {
						Initializer.setTHREAD_COUNT(Integer.parseInt(args[2]));
					}
					if (args[3] != null && !args[3].isEmpty()) {
						Initializer.setDEBUG_LEVEL(Integer.parseInt(args[3]));
					}

				} catch (NumberFormatException e) {
					System.err.println("Exception : Invalid values for DEBUG_LEVEL or THREAD_COUNT");
					System.exit(0);
				}
				if (Initializer.getTHREAD_COUNT() <= 0) {
					System.err.println("Acceptable values for thread count > 0");
					returnvalue = false;
				} else {
					returnvalue = true;
				}
			} else {
				StringBuilder sb = new StringBuilder();
				sb.append("Input arguments missing \n");
				sb.append("Four inputs are required for execution \n");
				sb.append("Execution format : <Compiled_Java_File> <Input_File> <Output_File> <Thread_Count> <Debug_Level> \n");
				sb.append("Example : RC6 input.txt output.txt 2 0");
				System.err.println(sb.toString());
				System.exit(0);
			}

		} catch (Exception e) {
			e.printStackTrace(System.err);
			System.exit(0);
		} finally {

		}
		return returnvalue;
	}
	
	
}
