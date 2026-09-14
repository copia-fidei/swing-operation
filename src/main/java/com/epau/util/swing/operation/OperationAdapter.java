package com.epau.util.swing.operation;

import java.util.List;

/// Like MouseAdapter but for OperationListener.
public class OperationAdapter implements OperationListener {

	@Override
	public void statusChanged(OperationStatus status) {}

	@Override
	public void progressChanged(int progress) {}

	@Override
	public void intermediateResults(List<String> intermediateResults) {}
}
