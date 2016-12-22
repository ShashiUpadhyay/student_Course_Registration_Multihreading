package registrationScheduler.util;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import registrationScheduler.util.Logger.DebugLevel;

/**
 * @author shashiupadhyay
 *
 */
public class FileProcessor {
	private String filename;
	FileReader fileReader;
	BufferedReader bufferedReader;

	/**
	 * @param filename_in
	 */
	public FileProcessor(String filename_in) {
		Logger.writeMessage("Constructor called" + this.getClass().getName(), DebugLevel.CONSTRUCTOR_CALLED);
		if (filename_in == null || filename_in.isEmpty()) {
			throw new IllegalArgumentException(filename_in);
		} else {
			setFilename(filename_in);
			try {
				bufferedReader = new BufferedReader(new FileReader(getFilename()));
			} catch (FileNotFoundException e) {
				e.printStackTrace(System.err);
				System.exit(0);
			} finally {
			}
		}

	}

	/**
	 * @return
	 */
	public synchronized String fetchNextLine() {
		String line = null;

		try {
			line = bufferedReader.readLine();
		} catch (IOException e) {
			e.printStackTrace(System.err);
			System.exit(0);
		} finally {
		}
		return line;
	}

	/**
	 * @param filename
	 */
	public void setFilename(String filename) {
		this.filename = filename;
	}

	/**
	 * @return the filename
	 */
	public String getFilename() {
		return filename;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return this.getClass().getName() + " [filename=" + filename + ", fileReader=" + fileReader + ", bufferedReader="
				+ bufferedReader + "]";
	}
}

