package duchess.logic.commands;

import duchess.exceptions.DuchessException;
import duchess.storage.Storage;
import duchess.storage.Store;
import duchess.ui.Ui;

import java.util.List;

public class UndoCommand extends Command {
    private int undoCounter;

    /**
     * Checks if undo command contains additional parameters.
     *
     * @param words additional parameters for undo
     * @throws DuchessException throws exceptions if invalid command
     */
    public UndoCommand(List<String> words) throws DuchessException {
        if (words.size() != 1 && words.size() != 0) {
            throw new DuchessException("Usage: undo [number]");
        } else if (words.size() == 1) {
            undoCounter = Integer.parseInt(words.get(0));
        } else if (words.size() == 0) {
            undoCounter = 1;
        }
    }

    @Override
    public void execute(Store store, Ui ui, Storage storage) throws DuchessException {

        if (undoCounter > 1) {
            storage.getLastSnapshot();

            while (undoCounter > 0 && storage.getUndoStack().size() > 0) {
                // setToPreviousStore(store, storage);
                undoCounter--;
            }
        } else {

            if (storage.getUndoStack().size() == 2) {
                System.out.println("UNDOSTACK == SIZE OF 1!@!@!@!");
                storage.getLastSnapshot();
                Store temp = storage.peekUndoStackAsStore();
                storage.save(temp);
                storage.displayStore(temp);

                // Obtaining store from stack
                Store newStore = storage.load();
                assert (store == newStore);
                store.setTaskList(newStore.getTaskList());
                store.setModuleList(newStore.getModuleList());

                ui.showTaskList(store.getTaskList());

            } else if (storage.getUndoStack().size() > 1) {
                System.out.println("ONLY 1 UNDO COMMAND");
                // Pops end of stack.
                // setToPreviousStore(store, storage);

                // Already polls once. (YOU MUST POLL AT LEAST ONCE).
                storage.getLastSnapshot();

                ui.showTaskList(store.getTaskList());

                // Get lastSnapshot round 2
                storage.save(storage.getLastSnapshot());

                // Obtaining store from stack
                Store newStore = storage.load();
                assert (store == newStore);
                store.setTaskList(newStore.getTaskList());
                store.setModuleList(newStore.getModuleList());

                ui.showTaskList(store.getTaskList());
            }
        }

        // showUndo should only be placed after execution of undo.
        ui.showUndo(undoCounter);
    }

    private void setToPreviousStore(Store store, Storage storage) throws DuchessException {
        // Obtain Store data from storage Stack
        Store prevStore = storage.getLastSnapshot();

        // Write to JSON file
        storage.save(prevStore);

        // Obtaining store from stack
        Store newStore = storage.load();
        store.setTaskList(newStore.getTaskList());
        store.setModuleList(newStore.getModuleList());
    }
}