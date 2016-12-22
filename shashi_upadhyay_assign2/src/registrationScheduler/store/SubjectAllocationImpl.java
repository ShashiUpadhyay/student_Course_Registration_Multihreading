package registrationScheduler.store;

import java.util.List;
import java.text.DecimalFormat;
import java.util.ArrayList;

import registrationScheduler.dao.Student;
import registrationScheduler.util.Logger;
import registrationScheduler.util.Logger.DebugLevel;
import registrationScheduler.util.Initializer;

/**
 * @author shashiupadhyay
 *
 */
public class SubjectAllocationImpl {

	private volatile static SubjectAllocationImpl subjectallocation_ref = null;

	public static SubjectAllocationImpl getsubjectAllocationImplInstance() {
		if (subjectallocation_ref == null) {
			synchronized (SubjectAllocationImpl.class) {
				if (subjectallocation_ref == null)
					subjectallocation_ref = new SubjectAllocationImpl();
			}
		}
		return subjectallocation_ref;
	}

	private List<Student> student_list;

	private double avgPreferenceScore;

	public SubjectAllocationImpl() {
		Logger.writeMessage(this.getClass().getName() + " Constructor called ", DebugLevel.CONSTRUCTOR_CALLED);
		student_list = new ArrayList<Student>();
	}

	/**
	 * 
	 */
	public synchronized void calculateAvgPreferenceScore() {
		double totalpreference = 0;
		try {
			for (Student student : student_list) {
				student.calculateStudentPreferenceScore();
				totalpreference = totalpreference + student.getSubjectPreferenceScore();
			}
			this.avgPreferenceScore = totalpreference / Initializer.getTotalstudents();
		} catch (Exception e) {
			System.err.println("Exception in : Class - " + this.getClass().getName() + " Method - " + new Object() {
			}.getClass().getEnclosingMethod().getName() + " .\n");
			e.printStackTrace();
			System.exit(0);
		} finally {
		}
	}

	/**
	 * 
	 */
	private void fetchSubjectofAllStudent() {
		for (Student student : student_list) {
			Logger.writeMessage(student.fetchSubjectOfAStudent(), DebugLevel.DATA_STRUCTURE_RESULTS);
		}
	}

	public synchronized double getAvgPreferenceScore() {
		return Double.valueOf(new DecimalFormat("###.##").format(avgPreferenceScore));
	}

	/**
	 * @return the student_list
	 */
	public ArrayList<Student> getStudent_list() {
		return (ArrayList<Student>) student_list;
	}

	public synchronized Student pickStudentForCourseSwap() {
		Student student = null;
		if (student_list.size() >= 1) {
			student = (Student) student_list.get(0);
			if (student != null) {
				student_list.remove(student);
			}
		}
		return student;
	}

	/**
	 * @param studentIn
	 */
	public synchronized void placeAllocatedStudent(Student studentIn) {
		Logger.writeMessage(" Student record updated " + studentIn.toString(), DebugLevel.DATA_STRUCTURE_POPULATED);
		if (student_list.contains(studentIn)) {
			System.err.println("Record insertion not possible : Student already present");
		} else {
			student_list.add(studentIn);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		fetchSubjectofAllStudent();
		return "SubjectAllocationImpl [students=" + student_list + ", avgPreferenceScore=" + avgPreferenceScore + "]";
	}

	/**
	 * @param studentIn
	 */
	public synchronized void updateAllocatedStudentList(Student studentIn) {
		if (studentIn != null) {
			student_list.remove(studentIn);
			if (student_list.contains(studentIn)) {
				System.err.println("Cannot add student : Student already present");
			} else {
				student_list.add(studentIn);
				Logger.writeMessage("Updated Student record " + studentIn, DebugLevel.DATA_STRUCTURE_POPULATED);
			}
		}
	}
}