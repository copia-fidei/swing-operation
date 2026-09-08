module com.epau.utilities.swing.operation {
	requires java.desktop;
	requires java.logging;

	requires static org.jetbrains.annotations;
	requires com.formdev.flatlaf;
	requires com.formdev.flatlaf.extras;
	requires com.github.weisj.jsvg;

	exports com.epau.utilities.swing.operation;
	exports com.epau.utilities.swing.operation.examples;

	requires com.epau.utilities.nls;
	opens com.epau.utilities.swing.operation to com.epau.utilities.nls;

	// TODO could lead to namespace conflicts
	opens com.epau.utilities.swing.icons.svgrepo;
}