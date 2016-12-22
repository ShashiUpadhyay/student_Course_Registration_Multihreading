package registrationScheduler.threadMgmt;

import registrationScheduler.objectPoolRepository.SubjectAllocationPoolInterface;
import registrationScheduler.store.SubjectAllocationImpl;
import registrationScheduler.util.FileProcessor;
import registrationScheduler.util.Logger;
import registrationScheduler.util.Logger.DebugLevel;

/**
 * @author shashiupadhyay
 *
 */
public class CreateWorkers {
	private int totalThreads;
	private Thread[] threads;
	private FileProcessor fileProcessor;
	private SubjectAllocationImpl SubjectAllocationImpl;
	private SubjectAllocationPoolInterface subjectallocationPool;

	/**
	 * @param fileProcessor_in
	 * @param results_in
	 * @param subjectallocationPool_in
	 */
	public CreateWorkers(FileProcessor fileProcessor_in, SubjectAllocationImpl results_in,
			SubjectAllocationPoolInterface subjectallocationPool_in) {
		Logger.writeMessage("Constructor called" + this.getClass().getName(), DebugLevel.CONSTRUCTOR_CALLED);
		if (fileProcessor_in != null || results_in != null || subjectallocationPool_in != null) {
			threads = null;
			totalThreads = -99;
			fileProcessor = fileProcessor_in;
			subjectallocationPool = subjectallocationPool_in;
			SubjectAllocationImpl = results_in;
		}
	}

	/**
	 * @param numThreadsIn
	 */
	public void startWorker(int numThreadsIn) {
		if (numThreadsIn > 0) {
			try {
				totalThreads = numThreadsIn;
				threads = new Thread[totalThreads];
				for (int i = 0; i < threads.length; i++) {
					WorkerThread workerthread = new WorkerThread(fileProcessor, SubjectAllocationImpl, subjectallocationPool);
					threads[i] = new Thread(workerthread);
					threads[i].setName("Worker_Thread_"+i);
				}
				for (int i = 0; i < threads.length; i++) {
					threads[i].start();
				}
				for (int i = 0; i < threads.length; i++) {
					threads[i].join();
				}
			} catch (Exception e) {
				System.err.println(this.getClass().getName() + " : Exception in startWorker method ");
				e.printStackTrace(System.err);
				System.exit(0);
			} finally {
			}
		}
	}

	@Override
	public String toString() {
		return this.getClass().getName() + " [Number of Threads =" + totalThreads + "]";
	}

}