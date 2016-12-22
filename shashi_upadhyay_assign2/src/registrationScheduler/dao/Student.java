package registrationScheduler.dao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import registrationScheduler.util.Logger;
import registrationScheduler.util.Logger.DebugLevel;
import registrationScheduler.util.Initializer;
import registrationScheduler.dao.Subject;

/**
 * @author shashiupadhyay
 *
 */
public class Student {
	private String studentRegistrationNumber;
	private int[] subject_preferences;
	private int subjectPreferenceScore;
	private List<Subject> subjectsAllocationToStudent;

	/**
	 * @param studentregistrationIn
	 * @param preferencesIn
	 */
	public Student(String studentregistrationIn, int[] preferencesIn) {
		Logger.writeMessage("Constructor called" + this.getClass().getName(), DebugLevel.CONSTRUCTOR_CALLED);
		if (studentregistrationIn == null || preferencesIn == null
				|| preferencesIn.length != Initializer.getTotalSubjects()) {
			System.err.println("Cannot proceed due to invalid values of preference or student registration");
		} else {
			try {
				this.studentRegistrationNumber = studentregistrationIn;
				this.subject_preferences = preferencesIn;
				this.subjectsAllocationToStudent = new ArrayList<Subject>();
				this.subjectPreferenceScore = -1;
			} catch (Exception e) {
				System.err.println(this.getClass().getName() + "Constructor failed.");
				e.printStackTrace(System.err);
				System.exit(0);
			}
		}
	}

	public String getStudentRegistrationNumber() {
		return studentRegistrationNumber;
	}

	public int[] getSubjectPreferences() {
		return subject_preferences;
	}

	public Subject[] getAllotedSubjects() {
		Subject[] subject = new Subject[subjectsAllocationToStudent.size()];
		return subjectsAllocationToStudent.toArray(subject);
	}

	public int getSubjectPreferenceScore() {
		return subjectPreferenceScore;
	}

	/**
	 * @param subjectIn
	 * @return
	 */
	public boolean verifyingAssignedSubject(Subject subjectIn) {
		if (subjectIn == null) {
			return false;
		} else {
			return this.subjectsAllocationToStudent.contains(subjectIn);
		}
	}

	/**
	 * @param subjectIn
	 * @return
	 */
	public boolean allocateSubjects(Subject subjectIn) {
		Logger.writeMessage(" Student record updated " + this.toString(), DebugLevel.DATA_STRUCTURE_POPULATED);
		if (subjectIn != null) {
			if (!this.verifyingAssignedSubject(subjectIn)) {
				return this.subjectsAllocationToStudent.add(subjectIn);
			} else {
				return false;
			}
		} else {
			System.err.println("Invalid subject : Unable to proceed");
			return false;
		}
	}

	/**
	 * @param subjectIn
	 * @return
	 */
	public boolean deallocateCourse(Subject subjectIn) {
		Logger.writeMessage(" Student record modified " + this.toString(), DebugLevel.DATA_STRUCTURE_POPULATED);
		if (subjectIn != null) {
			if (this.verifyingAssignedSubject(subjectIn)) {
				return this.subjectsAllocationToStudent.remove(subjectIn);
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	public boolean doesStudentNeedSubjects() {
		if (subjectsAllocationToStudent.size() == Initializer.getSubjectRequiredPerStudent()) {
			return true;
		} else {
			return false;
		}
	}

	public void calculateStudentPreferenceScore() {
		int totalPreference = 0;
		java.util.Iterator<Subject> iterator = subjectsAllocationToStudent.iterator();
		while (iterator.hasNext()) {
			Subject subject = iterator.next();
			if (subject != null) {
				totalPreference += subject_preferences[subject.getCoursePreference()];
			}
		}
		this.subjectPreferenceScore = totalPreference;
	}

	/**
	 * @param preferenceRank
	 * @return
	 */
	public Subject sortSubjectPreference(int preferenceRankIn) {
		int i;
		for (i = 0; i < subject_preferences.length; i++) {
			if (preferenceRankIn == subject_preferences[i]){
				break;
			}	
		}
		return Subject.fromValue(i);
	}

	public String fetchSubjectOfAStudent() {
		StringBuilder allotedsubjecttostudent = new StringBuilder(this.studentRegistrationNumber + "\t\t");
		for (Object subject_allocated : subjectsAllocationToStudent) {
			allotedsubjecttostudent.append((Subject) subject_allocated + "\t");
		}
		allotedsubjecttostudent.append("" + subjectPreferenceScore);
		return allotedsubjecttostudent.toString();
	}

	@Override
	public String toString() {
		return "Student [studentRegistrationNumber=" + studentRegistrationNumber + ", subject_preferences="
				+ Arrays.toString(subject_preferences) + ", subjectPreferenceScore=" + subjectPreferenceScore
				+ ", subjectsAllocationToStudent=" + subjectsAllocationToStudent + "]";
	}
}
