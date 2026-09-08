package com.epau.utilities.swing.operation.examples;

import com.epau.utilities.swing.operation.Operation;

import static java.util.concurrent.TimeUnit.SECONDS;

/// An example operations for testing that blocks forever.
public class BlockingOperation extends Operation {

	public BlockingOperation(String title, String description) {
		super(title, description);
	}

	@Override
	protected String doInBackground() throws Exception {
		//noinspection InfiniteLoopStatement
		while(true) {
			SECONDS.sleep(1);
		}
	}
}
