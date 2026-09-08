package com.epau.utilities.swing.operation.examples;

import com.epau.utilities.swing.operation.Operation;
import org.jetbrains.annotations.NonNls;

import static java.util.concurrent.TimeUnit.SECONDS;

/// An example operation for testing that waits for a specified amount of time.
///
/// The second constructor takes in a boolean parameter.
/// If true an exception will be thrown after half of the seconds have passed.
/// This simulates an error that occurred during the background task.
@NonNls
public class WaitOperation extends Operation {

	private static final int DEFAULT_SECONDS_TO_WAIT = 3;

	private final int     secondsToWait;
	private final boolean throwExceptionAtHalftime;

	public WaitOperation() {
		this(DEFAULT_SECONDS_TO_WAIT);
	}

	public WaitOperation(int secondsToWait) {
		this(secondsToWait, false);
	}

	public WaitOperation(String title, int secondsToWait, boolean throwExceptionAtHalftime) {
		this(title, "Wait for " + secondsToWait + " seconds", secondsToWait, throwExceptionAtHalftime);
	}

	public WaitOperation(int secondsToWait, boolean throwExceptionAtHalftime) {
		var text = "Wait for " + secondsToWait + " seconds";
		this(text, text, secondsToWait, throwExceptionAtHalftime);
	}

	/// @param title
	/// @param description
	/// @param secondsToWait
	/// @param throwExceptionAtHalftime will throw an exception after half of the seconds has passed
	public WaitOperation(String title, String description, int secondsToWait, boolean throwExceptionAtHalftime) {
		super(title, description); //NON-NLS

		this.secondsToWait            = secondsToWait;
		this.throwExceptionAtHalftime = throwExceptionAtHalftime;
	}

	@Override
	protected String doInBackground() throws Exception {
		for (int i = 1; i <= secondsToWait; i++) {
			if (isCancelled()) {
				return "Cancelled";
			}
			SECONDS.sleep(1);
			println("Waited for " + i + " seconds"); //NON-NLS
			int progress = i * 100 / secondsToWait;
			setProgress(progress);
			if (i > secondsToWait / 2 && throwExceptionAtHalftime) {
				throw new Exception("Error during execution");
			}
		}
		return "Done";
	}
}
