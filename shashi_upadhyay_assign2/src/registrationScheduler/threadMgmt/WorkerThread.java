package registrationScheduler.threadMgmt;

import java.util.ArrayList;
import java.util.Scanner;

import registrationScheduler.dao.Subject;
import registrationScheduler.dao.Student;
import registrationScheduler.objectPoolRepository.SubjectAllocationPoolInterface;
import registrationScheduler.store.SubjectAllocationImpl;
import registrationScheduler.util.FileProcessor;
import registrationScheduler.util.Logger;
import registrationScheduler.util.Logger.DebugLevel;
import registrationScheduler.util.Initializer;

/**
 * @author shashiupadhyay
 *
 */
public class WorkerThread implements Runnable {
	private FileProcessor fileProcessor;
	private SubjectAllocationImpl SubjectAllocationImpl;
	private SubjectAllocationPoolInterface subjectallocationpool;
	private String threadName;
	private ArrayList<Student> list_students_not_received_courses;
	private Scanner scanner = null;

	/**
	 * @param fileProcessor_in
	 * @param results_in
	 * @param coursePool_in
	 */
	public WorkerThread(FileProcessor fileProcessor_in, SubjectAllocationImpl results_in,
			SubjectAllocationPoolInterface coursePool_in) {
		Logger.writeMessage("Constructor called" + this.getClass().getName(), DebugLevel.CONSTRUCTOR_CALLED);
		this.fileProcessor = fileProcessor_in;
		this.SubjectAllocationImpl = results_in;
		this.subjectallocationpool = coursePool_in;
		list_students_not_received_courses = new ArrayList<Student>();
	}

	public void run() {
		Logger.writeMessage(Thread.currentThread().getName() + ": Executing", DebugLevel.THREAD_RUN_CALLED);
		Student student;
		try {
			String line = null;
			while ((line = fileProcessor.fetchNextLine()) != null) {
				student = scanningParsingInput(line);
				subjectAllotmentProcess(student);
			}
			reallotment();
		} catch (Exception e) {
			System.err.println("Exception in : Class - " + this.getClass().getName() + " Method - " + new Object() {
			}.getClass().getEnclosingMethod().getName() + " .\n");
			e.printStackTrace(System.err);
			System.exit(0);
		} finally {

		}
	}

	/**
	 * 
	 */
	private synchronized void reallotment() {
		try {
			while (!list_students_not_received_courses.isEmpty()) {
				subjects_allotment_secondround();
			}
		} catch (Exception e) {
			System.err.println("Exception in : Class - " + this.getClass().getName() + " Method - " + new Object() {
			}.getClass().getEnclosingMethod().getName() + " .\n");
			e.printStackTrace(System.err);
			System.exit(0);
		} finally {

		}
	}

	/**
	 * @param student
	 */
	private synchronized void subjectAllotmentProcess(Student student) {
		try {
			subject_allotment_firstround(student);
			if (student.doesStudentNeedSubjects()) {
				this.updateStudentResultSet(student);
			} else {
				list_students_not_received_courses.add(student);
			}
		} catch (Exception e) {
			System.err.println("Exception in : Class - " + this.getClass().getName() + " Method - " + new Object() {
			}.getClass().getEnclosingMethod().getName() + " .\n");
			e.printStackTrace(System.err);
			System.exit(0);
		} finally {

		}
	}

	/**
	 * @param line
	 * @return
	 */
	private synchronized Student scanningParsingInput(String line) {

		Student student = null;
		String student_reg;
		int[] student_preferences = new int[Initializer.getTotalSubjects()];
		try {
			scanner = new Scanner(line);
			if (scanner != null) {
				student_reg = scanner.next();
				for (int i = 0; i < student_preferences.length; i++) {
					int fetch_value = scanner.nextInt();
					student_preferences[i] = fetch_value;
				}
				if (student_reg != null && student_preferences != null) {
					student = new Student(student_reg, student_preferences);
				}
			}
		} catch (Exception e) {
			System.err.println("Error in scanning and Parsing preference input file data.\n");
			e.printStackTrace(System.err);
			System.exit(0);
		} finally {
			scanner.close();
		}
		return student;
	}

	/**
	 * @param student
	 */
	private synchronized void subject_allotment_firstround(Student student) {
		Subject subject;
		try {
			for (int i = 0; i < Initializer.getSubjectRequiredPerStudent(); i++) {
				int course_reg = (i + 1);
				if (course_reg < 1 || course_reg > Initializer.getTotalSubjects()) {
					System.err.println("Invalid preference : Preference should be between 0 to "
							+ Initializer.getTotalSubjects() + ".\n");
					break;
				} else {
					subject = student.sortSubjectPreference(course_reg);
					if (subjectallocationpool.courseAllocationPossibility(subject)) {
						student.allocateSubjects(subject);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace(System.err);
			System.exit(0);
		} finally {

		}
	}

	private synchronized void subjects_allotment_secondround() {
		try {
			if (list_students_not_received_courses.size() >= 1) {
				Student studentNeedSubject = (Student) list_students_not_received_courses.remove(0);
				if (studentNeedSubject != null) {
					while (!studentNeedSubject.doesStudentNeedSubjects()) {
						Student studentforcourseswap = null;
						studentforcourseswap = SubjectAllocationImpl.pickStudentForCourseSwap();
						if (studentforcourseswap != null) {
							Subject[] allocatedsublist = studentforcourseswap.getAllotedSubjects();
							if (allocatedsublist.length > 0) {
								for (Subject subject : allocatedsublist) {
									if (!studentNeedSubject.verifyingAssignedSubject(subject)) {
										Subject newCourseForSwappedStudent = null;
										if (studentforcourseswap != null) {
											newCourseForSwappedStudent = searchCourseForStudent(studentforcourseswap);
										}
										if (subject != null) {
											studentforcourseswap.deallocateCourse(subject);
											studentNeedSubject.allocateSubjects(subject);
										}
										if (newCourseForSwappedStudent != null) {
											studentforcourseswap.allocateSubjects(newCourseForSwappedStudent);
										}
										break;
									}
								}
								if (studentforcourseswap != null) {
									updateStudentResultSet(studentforcourseswap);
								}
							}
						}
					}
					if (studentNeedSubject != null) {
						updateStudentResultSet(studentNeedSubject);
					}
				}
			} else {
				System.err.println("Further allocation not possible");
			}
		} catch (Exception e) {
			e.printStackTrace(System.err);
			System.exit(0);
		} finally {

		}
	}

	/**
	 * @param studentneedcourse
	 * @return
	 */
	private synchronized Subject searchCourseForStudent(Student studentneedcourse) {
		for (Subject subject : Subject.values()) {
			if (subject != null) {
				if (subjectallocationpool.courseAllocationPossibility(subject)) {
					if (!studentneedcourse.verifyingAssignedSubject(subject)) {
						return subject;
					}
				}
			}
		}
		return null;
	}

	/**
	 * @param student
	 */
	private void updateStudentResultSet(Student student) {
		if (student != null) {
			SubjectAllocationImpl.placeAllocatedStudent(student);
		} else {
			System.err.println("Invalid Student");
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return this.getClass().getName() + "[" + ", threadName=" + threadName + ", Processing : "
				+ fileProcessor.getFilename();
	}

}