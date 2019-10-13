import duchess.exceptions.DuchessException;
import duchess.logic.commands.Command;
import duchess.logic.parser.Parser;
import duchess.model.task.DuchessLog;
import duchess.storage.Storage;
import duchess.storage.Store;
import duchess.ui.Ui;

public class Duke {

    private Storage storage;
    private Store store;
    private Ui ui;
    private DuchessLog duchessLog;

    /**
     * Creates an instant of Duke to be executed.
     *
     * @param filePath name of file to store tasks
     */
    private Duke(String filePath) {
        ui = new Ui();
        storage = new Storage(filePath);

        try {
            store = storage.load();

            // Adds the very first copy of store into undoStack
            storage.addToUndoStackPush(store);

        } catch (DuchessException e) {
            ui.showError(e.getMessage());
            store = new Store();
        }
    }

    /**
     * Begins the execution of Duke.
     */
    private void run() {
        ui.showWelcome();
        duchessLog = new DuchessLog();


        boolean isExit = false;
        while (!isExit) {
            try {
                String fullCommand = ui.readCommand();
                duchessLog.add(fullCommand);
                ui.beginBlock();
                Command c = Parser.parse(fullCommand);
                duchessLog.addValidCommands(c);


                c.execute(store, ui, storage);

                // Take snapshot here, you save copies of Store store here
                storage.addToUndoStackPush(store);

                // storage.setRedoStack();

                isExit = c.isExit();
            } catch (DuchessException e) {
                ui.showError(e.getMessage());
            } finally {
                ui.endBlock();
            }
        }
    }

    public static void main(String[] args) {
        new Duke("data.json").run();
    }
}