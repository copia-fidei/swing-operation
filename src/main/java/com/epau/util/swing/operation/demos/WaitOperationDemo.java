package com.epau.util.swing.operation.demos;

import com.epau.util.swing.operation.Operation;
import com.epau.util.swing.operation.OperationListener;
import com.epau.util.swing.operation.OperationStatus;
import com.epau.util.swing.operation.examples.WaitOperation;
import org.jetbrains.annotations.NonNls;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

import static com.epau.util.swing.operation.OperationStatusPresentation.getDescription;
import static java.awt.BorderLayout.CENTER;
import static java.awt.BorderLayout.NORTH;
import static java.awt.BorderLayout.SOUTH;
import static java.lang.String.join;
import static java.lang.System.lineSeparator;
import static javax.swing.SwingUtilities.invokeLater;

@NonNls
class WaitOperationDemo {

	static void main() {
		class TestFrame extends JFrame implements OperationListener  {
			final JLabel progress = new JLabel("Progress: 0"); //NON-NLS
			final JTextArea textArea     = new JTextArea();
			final JButton   cancelButton = new JButton("Cancel"); //NON-NLS

			TestFrame(Operation operation) {
				setTitle("Waiting Operation Demo"); //NON-NLS
				setLayout(new BorderLayout());

				add(progress, NORTH);
				add(new JScrollPane(textArea), CENTER);
				add(cancelButton, SOUTH);

				operation.addListener(this);
				cancelButton.addActionListener(_ -> operation.cancel(true));

				textArea.setText(getDescription(operation.status()) + NEW_LINE);
			}

			@Override
			public void statusChanged(OperationStatus status) {
				textArea.append(getDescription(status) + NEW_LINE);

				switch (status) {
					case COMPLETED, CANCELLED_BY_USER, ERROR -> {
						textArea.append("Operation completed successfully." + NEW_LINE); //NON-NLS

						dispose();
					}
				}
			}

			@Override
			public void progressChanged(int progress) {
				this.progress.setText("Progress: " + progress); //NON-NLS
			}

			@Override
			public void intermediateResults(List<String> intermediateResults) {
				textArea.append(join(NEW_LINE, intermediateResults) + NEW_LINE);
			}
			private static final String NEW_LINE = lineSeparator();
		}
		var operation = new WaitOperation(10, true);
		invokeLater(() -> {
			var frame = new TestFrame(operation);
			frame.addWindowListener(new WindowAdapter() {
				@Override
				public void windowClosing(WindowEvent e) {
					operation.cancel(true);
				}
			});
			frame.pack();
			frame.setSize(new Dimension(400, 400));
			frame.setLocationRelativeTo(null);
			frame.setVisible(true);

			operation.execute();
		});
	}
}
