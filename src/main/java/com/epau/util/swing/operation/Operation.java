package com.epau.util.swing.operation;

import javax.swing.SwingWorker;
import java.awt.GraphicsEnvironment;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.util.logging.Logger.getLogger;

/// An abstract SwingWorker with extra features, primarily intended for running terminal commands and capturing their stdout and stderr output.
///
/// Features:
/// 1. Logging:
/// - Uses SwingWorker's intermediate results.
/// - Captures stdout and stderr when running a terminal command. (Use the 'execute' methods)
/// - Converts exceptions into log messages.
/// 2. A status: Started, Running, Cancelled, Failed, Completed
/// 3. Listener support
/// 4. Title & description
public abstract class Operation extends SwingWorker<String, String> implements PropertyChangeListener {

	protected final Logger log = getLogger(getClass().getName());

	private OperationStatus status = OperationStatus.NOT_STARTED;

	private final List<OperationListener> listeners = new ArrayList<>();

	private final String title;
	private final String description;

	private final List<String> logs = Collections.synchronizedList(new ArrayList<>());

	public Operation(String title, String description) {
		this.title = title;
		this.description = description;

		this.addPropertyChangeListener(this);
	}

	public OperationStatus status() {
		return status;
	}

	public List<String> logs() {
		return new ArrayList<>(logs);
	}

	public void println(String line) {
		if (GraphicsEnvironment.isHeadless()) {
			IO.println(line);
		} else {
			publish(line + "\n");
		}
	}

	@Override
	protected void process(List<String> chunks) {
		logs.addAll(chunks);

		notifyAboutIntermediateResults(chunks);
	}

	private void notifyAboutIntermediateResults(List<String> chunks) {
		listeners.forEach(listener -> listener.intermediateResults(chunks));
	}

	public void addListener(OperationListener listener) {
		listeners.add(listener);
	}

	public void removeListener(OperationListener listener) {
		listeners.remove(listener);
	}

	private void notifyProgressChanged() {
		listeners.forEach(l -> l.progressChanged(getProgress()));
	}


	@Override
	protected void done() {
		if (isCancelled()) {
			fireOperationStatusChanged(OperationStatus.CANCELLED_BY_USER);
			return;
		}
		try {
			get();
			fireOperationStatusChanged(OperationStatus.COMPLETED);
		} catch (InterruptedException e) {
			fireOperationStatusChanged(OperationStatus.CANCELLED_BY_USER);
		} catch (ExecutionException e) {
			fireOperationStatusChanged(OperationStatus.ERROR);

			// This should prevent the exception type from being included in the message
			var cause = e.getCause();
			if (cause != null) {
				var localMessage = cause.getLocalizedMessage();
				if (localMessage == null) {
					log.log(Level.INFO, "Localized message of cause of exception is null", e); //NON-NLS
					localMessage = cause.getMessage();
					if (localMessage == null) {
						log.log(Level.INFO, "Message of cause of exception is null", e); //NON-NLS
						localMessage = cause.toString();
					}
				}
				process(localMessage);
			} else {
				log.log(Level.INFO, "Cause of exception is null", e); //NON-NLS
				String localMessage = e.getLocalizedMessage();
				if (localMessage == null) {
					log.log(Level.INFO, "Localized message of exception is null", e); //NON-NLS
					localMessage = e.getMessage();
					if (localMessage == null) {
						log.log(Level.INFO, "Message of exception is null", e); //NON-NLS
						localMessage = e.toString();
					}
				}
				process(localMessage);
			}
		}
	}

	private void process(String... chunks) {
		process(List.of(chunks));
	}

	protected void fireOperationStatusChanged(OperationStatus status) {
		this.status = status;

		listeners.forEach(listener -> listener.statusChanged(status));
	}

	/**
	 * Transform PropertyChangeEvents to OperationStatus and progress events.
	 */
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		Object newValue = evt.getNewValue();
		switch (evt.getPropertyName()) {
			case "state" -> {
				if (newValue == SwingWorker.StateValue.STARTED) {
					fireOperationStatusChanged(OperationStatus.RUNNING);
				}
				// other statuses are fired in done()
			}
			case "progress" -> notifyProgressChanged();
		}
	}

	public String title() {
		return title;
	}

	public String description() {
		return description;
	}

	protected void progress() {
		progress(1);
	}

	protected void progress(int by) {
		int newProgress = getProgress() + by;
		if (newProgress <= 100) {
			setProgress(newProgress);
		}
	}

	protected void print(String str) {
		if (GraphicsEnvironment.isHeadless()) {
			IO.print(str);
		} else {
			publish(str);
		}
	}

	/// execute the given process and send its outputs
	public Result execute(Process process) throws IOException, InterruptedException {
		var out = new CancellableWriter(this);
		var err = new CancellableWriter(this);
		try (var stdout = process.inputReader(); var stderr = process.errorReader()) {
			stdout.transferTo(out);
			stderr.transferTo(err);
		} catch (Cancelled e) {
			process.destroy();
			throw Cancelled.process(process, e);
		}
		return new Result(out, err, process.waitFor());
	}

	public Result execute(ProcessBuilder pb) throws IOException, InterruptedException {
		return execute(pb.start());
	}

	public Result execute(String... command) throws IOException, InterruptedException {
		return execute(new ProcessBuilder(command));
	}
}
