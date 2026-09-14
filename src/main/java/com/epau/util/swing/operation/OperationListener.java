package com.epau.util.swing.operation;

import java.util.List;

/**
 * Listens to an operation's events.
 * Note: All methods are called in the event dispatch thread.
 */
public interface OperationListener {

	void statusChanged(OperationStatus status);

	void progressChanged(int progress);

	void intermediateResults(List<String> intermediateResults);
}
