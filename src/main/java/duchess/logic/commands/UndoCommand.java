package duchess.logic.commands;

import duchess.exceptions.DuchessException;
import duchess.storage.Storage;
import duchess.storage.Store;
import duchess.ui.Ui;

import java.util.List;

public class UndoCommand extends Command {
    private int undoCounter;

    public UndoCommand(List<String> words) throws DuchessException {
        if (words.size() == 1) {
            undoCounter = Integer.parseInt(words.get(0));
        } else if (words.size() == 0) {
            undoCounter = 1;
        } else {
            throw new DuchessException("Usage: undo [number]");
        }
    }

    @Override
    public void execute(Store store, Ui ui, Storage storage) throws DuchessException {
        if(storage.getPreviousUndoStatus() == false) {
            // Update boolean to prevent next command to be undo.
            storage.setPreviousUndoTrue();

            // Perform undo function.
            ui.showUndo(undoCounter);

            if (undoCounter > 1) {
                storage.getLastSnapshot();

                while (undoCounter > 0 && storage.getUndoStack().size() > 0) {
                    getPreviousStore(store, storage);
                    undoCounter--;
                }
            } else {
                storage.getLastSnapshot();
                getPreviousStore(store, storage);
            }
        } else {
            throw new DuchessException("Last command cannot be undo. Use redo instead.");
        }
    }

    private void getPreviousStore(Store store, Storage storage) throws DuchessException {
        // You obtain Store data from storage Stack
        Store prevStore = storage.getLastSnapshot();

        // Write to JSON DATA:
        storage.save(prevStore);

        // you are getting store from stack
        Store newStore = storage.load();
        store.setTaskList(newStore.getTaskList());
        store.setModuleList(newStore.getModuleList());
    }
}