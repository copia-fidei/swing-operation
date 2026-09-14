module com.epau.util.swing.operation {
	requires java.desktop;
	requires java.logging;

	requires static org.jetbrains.annotations;
	requires com.formdev.flatlaf;
	requires transitive com.formdev.flatlaf.extras;
	requires com.github.weisj.jsvg;

	exports com.epau.util.swing.operation;
	exports com.epau.util.swing.operation.examples;

	requires com.epau.util.nls;
	opens com.epau.util.swing.operation to com.epau.util.nls;

	opens com.epau.util.swing.operation.icons.svgrepo;
}