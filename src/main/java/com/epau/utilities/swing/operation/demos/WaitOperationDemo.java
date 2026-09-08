package com.epau.utilities.swing.operation.demos;

import com.epau.utilities.swing.operation.Operation;
import com.epau.utilities.swing.operation.OperationListener;
import com.epau.utilities.swing.operation.OperationStatus;
import com.epau.utilities.swing.operation.OperationStatusPresentation;
import com.epau.utilities.swing.operation.examples.WaitOperation;
import org.jetbrains.annotations.NonNls;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.List;

import static java.awt.BorderLayout.CENTER;
import static java.awt.BorderLayout.NORTH;
import static java.awt.BorderLayout.SOUTH;
import static java.lang.System.lineSeparator;
import static javax.swing.SwingUtilities.invokeLater;
import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

@NonNls
class WaitOperationDemo {

	static void main() {
		class TestFrame extends JFrame implements OperationListener {

			final JLabel    progress     = new JLabel("Progress: 0"); //NON-NLS
			final JTextArea textArea     = new JTextArea();
			final JButton   cancelButton = new JButton("Cancel"); //NON-NLS

			TestFrame(Operation operation) {
				setTitle("Waiting Operation Demo"); //NON-NLS
				setDefaultCloseOperation(EXIT_ON_CLOSE);
				setLayout(new BorderLayout());

				add(progress, NORTH);
				add(new JScrollPane(textArea), CENTER);
				add(cancelButton, SOUTH);

				operation.addListener(this);
				cancelButton.addActionListener(_ -> operation.cancel(true));

				textArea.setText(OperationStatusPresentation.getDescription(operation.getStatus()) + lineSeparator());
			}

			@Override
			public void statusChanged(OperationStatus status) {
				textArea.append(OperationStatusPresentation.getDescription(status) + lineSeparator());
			}

			@Override
			public void progressChanged(int progress) {
				this.progress.setText("Progress: " + progress); //NON-NLS
			}

			@Override
			public void intermediateResults(List<String> intermediateResults) {
				textArea.append(String.join(lineSeparator(), intermediateResults) + lineSeparator());
			}
		}

		invokeLater(() -> {
			var operation = new WaitOperation(10, true);
			var frame     = new TestFrame(operation);
			frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
			frame.pack();
			frame.setSize(new Dimension(400, 400));
			frame.setLocationRelativeTo(null);
			frame.setVisible(true);

			operation.execute();
		});
	}
}
