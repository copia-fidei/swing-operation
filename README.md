Includes a subclass of SwingWorker (called Operation) intended to run background tasks (in the terminal) and forward their output (standard output/error) to the GUI.

Added functionality

| `SwingWorker`                                                                             | `Operation`                                                                                                     |
| ----------------------------------------------------------------------------------------- | --------------------------------------------------------------------------------------------------------------- |
| Supports generic types for intermediate results and the final result of a background task | Both intermediate results and the final result are `String`s, intended for log messages (standard output/error) |
| Requires references to GUI components in order to update them                             | Provides increased decoupling through listener support                                                          |
| Has no built-in status                                                                    | Provides a status such as Not Started, Running, Cancelled, etc.                                                 |
|                                                                                           | Additional helper methods to run terminal commands and capture their outputs                                    |
