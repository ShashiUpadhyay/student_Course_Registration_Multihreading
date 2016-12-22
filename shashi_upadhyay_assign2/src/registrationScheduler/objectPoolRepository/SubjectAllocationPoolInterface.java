package registrationScheduler.objectPoolRepository;

import registrationScheduler.dao.Subject;

/**
 * @author shashiupadhyay
 *
 */
public interface SubjectAllocationPoolInterface {
	/**
	 * @param subject
	 * @return
	 */
	public boolean courseAllocationPossibility(Subject subject);
	/**
	 * @param subject
	 * @return
	 */
	public boolean removingCourse(Subject subject);
	/**
	 * @param subject
	 * @return
	 */
	public boolean courseAvailabilityStatus(Subject subject);
}