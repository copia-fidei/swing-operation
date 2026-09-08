package com.epau.utilities.swing.operation;

import com.epau.utilities.swing.Icons;
import com.epau.utilities.nls.Nls;
import com.formdev.flatlaf.extras.FlatSVGIcon;

/// Gives you an icon or description for a {@link OperationStatus}.
public class OperationStatusPresentation {

	private final static int SIZE = 16;

	private final static Nls nls = new Nls(OperationStatusPresentation.class);

	public static FlatSVGIcon getIcon(OperationStatus status) {
		return switch (status) {
			case NOT_STARTED       -> Icons.hourglass(SIZE);
			case RUNNING           -> Icons.cogwheel(SIZE);
			case ERROR             -> Icons.warning(SIZE);
			case CANCELLED_BY_USER -> Icons.cancel(SIZE);
			case COMPLETED         -> Icons.check(SIZE);
		 };
	}

	public static String getDescription(OperationStatus status) {
		return switch (status) {
			case NOT_STARTED  	   -> nls.get("OperationStatusPresentation.Not_started");
			case RUNNING 		   -> nls.get("OperationStatusPresentation.Running");
			case ERROR 			   -> nls.get("OperationStatusPresentation.Failed");
			case CANCELLED_BY_USER -> nls.get("OperationStatusPresentation.Cancelled_by_user");
			case COMPLETED 		   -> nls.get("OperationStatusPresentation.Completed");
		};
	}
}
