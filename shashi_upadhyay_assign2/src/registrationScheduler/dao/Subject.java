/**
 * reference: http://javahowto.blogspot.com/2008/04/java-enum-examples.html
 */
package registrationScheduler.dao;

public enum Subject {

	A(0), B(1), C(2), D(3), E(4), F(5), G(6);

	private int subjectPreference;

	private Subject(int subjectPreferenceIn) {
		this.subjectPreference = subjectPreferenceIn;
	}

	public int getCoursePreference() {
		return subjectPreference;
	}

	public static Subject fromValue(int subject) {
		try {
			return Subject.values()[subject];
		} catch (ArrayIndexOutOfBoundsException e) {
			throw new IllegalArgumentException("Unknown subject :" + subject);
		}
	}

}