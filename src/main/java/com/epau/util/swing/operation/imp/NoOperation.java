package com.epau.util.swing.operation.imp;

import com.epau.util.swing.operation.Operation;

/// does nothing
public class NoOperation extends Operation {

	public NoOperation(String title, String description) {
		super(title, description);
	}

	public NoOperation() {
		super("", "");
	}

	@Override
	protected String doInBackground() {
		return "";
	}
}
