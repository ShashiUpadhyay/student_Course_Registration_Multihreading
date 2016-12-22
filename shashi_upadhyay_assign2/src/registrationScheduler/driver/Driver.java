package registrationScheduler.driver;

import java.io.IOException;

import registrationScheduler.objectPoolRepository.SubjectAllocationPool;
import registrationScheduler.objectPoolRepository.SubjectAllocationPoolInterface;
import registrationScheduler.store.Result;
import registrationScheduler.store.SubjectAllocationImpl;
import registrationScheduler.threadMgmt.CreateWorkers;
import registrationScheduler.util.FileProcessor;
import registrationScheduler.util.Logger;
import registrationScheduler.util.Initializer;

/**
 * @author shashiupadhyay
 *
 */
public class Driver {

	public static void main(String args[]) throws IOException {

		SubjectAllocationPoolInterface subjectallocationPool = null;
		SubjectAllocationImpl subjectallocationimpl_ref = null;
		FileProcessor fileProcessor_ref = null;
		CreateWorkers createworkers_ref = null;
		Result result_ref = null;

		try {
			if (args != null) {
				if (Initializer.validatingInputArguments(args)) {

					if (Initializer.getPreference_Input_File() != null) {
						fileProcessor_ref = new FileProcessor(Initializer.getPreference_Input_File());
					}

					if (Initializer.getDEBUG_LEVEL() < 0 || Initializer.getDEBUG_LEVEL() > 4) {
						System.err.println("Acceptable value for DEBUG_LEVEL [0,1,2,3,4]");
					} else {
						Logger.setDebugValue(Initializer.getDEBUG_LEVEL());
					}

					subjectallocationPool = new SubjectAllocationPool(Initializer.getTotalSubjects(),
							Initializer.getCapacityeachcourse());

					subjectallocationimpl_ref = SubjectAllocationImpl.getsubjectAllocationImplInstance();
					result_ref = Result.getResultInstance();
					Result.initializeSubAllocationInstance(subjectallocationimpl_ref);

					createworkers_ref = new CreateWorkers(fileProcessor_ref, subjectallocationimpl_ref,
							subjectallocationPool);

					if (Initializer.getTHREAD_COUNT() > 0) {
						createworkers_ref.startWorker(Initializer.getTHREAD_COUNT());
					}

					subjectallocationimpl_ref.calculateAvgPreferenceScore();

					if (Initializer.getDEBUG_LEVEL() == 0) {
						result_ref.displayPreferenceScore();
					}  else {
						result_ref.writeScheduleToScreen();
						result_ref.writeScheduleToFile(Initializer.getOutput_file_name());
					}
				}
			} else {

			}
		} catch (Exception e) {
			System.err.println("Exception in Driver Class");
			e.printStackTrace();
			System.exit(0);
		}
	}

}