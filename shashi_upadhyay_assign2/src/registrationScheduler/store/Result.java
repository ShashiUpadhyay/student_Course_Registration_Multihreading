package registrationScheduler.store;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import registrationScheduler.dao.Student;
import registrationScheduler.util.Logger;
import registrationScheduler.util.Logger.DebugLevel;

/**
 * @author shashiupadhyay
 *
 */
public class Result implements StdoutDisplayInterface, FileDisplayInterface {

	private static Result result_ref = null;
	private static SubjectAllocationImpl subjectallocationimpl = null;

	public static Result getResultInstance() {
		if (result_ref == null) {
			synchronized (Result.class) {
				if (result_ref == null)
					result_ref = new Result();
			}
		}
		return result_ref;
	}

	/**
	 * @param subjectallocationimplIn
	 */
	public static void initializeSubAllocationInstance(SubjectAllocationImpl subjectallocationimplIn) {
		if (subjectallocationimplIn != null) {
			subjectallocationimpl = subjectallocationimplIn;
		}
	}

	public synchronized void displayPreferenceScore() {
		System.out.println("Average preference score :\t" + subjectallocationimpl.getAvgPreferenceScore());
	}

	public synchronized void writeScheduleToScreen() {
		for (Student student : subjectallocationimpl.getStudent_list()) {
			Logger.writeMessage(student.fetchSubjectOfAStudent(), DebugLevel.DATA_STRUCTURE_RESULTS);
		}
		displayPreferenceScore();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * registrationScheduler.store.FileDisplayInterface#writeScheduleToFile(java
	 * .lang.String)
	 */
	public synchronized void writeScheduleToFile(String filename_in) {
		BufferedWriter bufferedwriter = null;
		try {
			File output_file = new File(filename_in);
			if (!output_file.exists()) {
				output_file.createNewFile();
			}
			bufferedwriter = new BufferedWriter(new FileWriter(output_file.getAbsolutePath()));
			for (Student student : subjectallocationimpl.getStudent_list()) {
				bufferedwriter.write(student.fetchSubjectOfAStudent() + "\n");
			}
			bufferedwriter.write("\nAverage preference score :\t" + subjectallocationimpl.getAvgPreferenceScore());
		} catch (IOException e) {
			System.err.println("Exception in : Class - " + this.getClass().getName() + " Method - " + new Object() {
			}.getClass().getEnclosingMethod().getName() + " .\n");
			e.printStackTrace(System.err);
			System.exit(0);
		} finally {
			try {
				bufferedwriter.close();
			} catch (IOException e) {
				System.err.println("Exception in : Class - " + this.getClass().getName() + " Method - " + new Object() {
				}.getClass().getEnclosingMethod().getName() + " .\n");
				e.printStackTrace(System.err);
				System.exit(0);
			}
		}
	}
}
