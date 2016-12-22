package registrationScheduler.objectPoolRepository;

import java.util.Arrays;

import registrationScheduler.dao.Subject;
import registrationScheduler.util.Logger;
import registrationScheduler.util.Logger.DebugLevel;

/**
 * @author shashiupadhyay
 *
 */
public class SubjectAllocationPool implements SubjectAllocationPoolInterface {
	private int totalSubjects;
	private int capacityPerSubject;
	private int[] subjectSeatAvailabilityStatus;

	/**
	 * @param totalSubjects_in
	 * @param capacityPerSubject_in
	 */
	public SubjectAllocationPool(int totalSubjects_in, int capacityPerSubject_in) {
		Logger.writeMessage("Constructor called" + this.getClass().getName(), DebugLevel.CONSTRUCTOR_CALLED);
		if (totalSubjects_in >= 1 || capacityPerSubject_in >= 1) {
			totalSubjects = totalSubjects_in;
			capacityPerSubject = capacityPerSubject_in;
			subjectSeatAvailabilityStatus = new int[totalSubjects];
			Arrays.fill(subjectSeatAvailabilityStatus, capacityPerSubject);
		}
	}

	/* (non-Javadoc)
	 * @see registrationScheduler.objectPoolRepository.SubjectAllocationPoolInterface#isAllocationPossible(registrationScheduler.dao.Subject)
	 */
	@Override
	public synchronized boolean courseAllocationPossibility(Subject subject) {
		if (subject != null) {
			int seatavailabilitystatus = subjectSeatAvailabilityStatus[subject.getCoursePreference()];
			if (seatavailabilitystatus > 0) {
				seatavailabilitystatus--;
				subjectSeatAvailabilityStatus[subject.getCoursePreference()] = seatavailabilitystatus;
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	/* (non-Javadoc)
	 * @see registrationScheduler.objectPoolRepository.SubjectAllocationPoolInterface#deAllocateCourse(registrationScheduler.dao.Subject)
	 */
	@Override
	public synchronized boolean removingCourse(Subject subject) {
		if (subject != null) {
			if (subjectSeatAvailabilityStatus[subject.getCoursePreference()] < capacityPerSubject) {
				++subjectSeatAvailabilityStatus[subject.getCoursePreference()];
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	/* (non-Javadoc)
	 * @see registrationScheduler.objectPoolRepository.SubjectAllocationPoolInterface#isCourseAvailable(registrationScheduler.dao.Subject)
	 */
	@Override
	public synchronized boolean courseAvailabilityStatus(Subject subject) {
		if (subject == null) {
			return false;
		} else {
			if (subjectSeatAvailabilityStatus[subject.getCoursePreference()] > 0) {
				return true;
			} else {
				return false;
			}
		}
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return this.getClass().getName() + " [totalSubjects=" + totalSubjects + ", capacityPerSubject="
				+ capacityPerSubject + ", seatCounters=" + Arrays.toString(subjectSeatAvailabilityStatus) + "]";
	}
}
