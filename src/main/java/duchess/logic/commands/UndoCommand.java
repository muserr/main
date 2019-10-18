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
            if (storage.getUndoStack().size() > 1) {
                System.out.println("UNDO COUNTER > 1");
                storage.addToRedoStack();
            }

            while (undoCounter > 0 && storage.getUndoStack().size() > 1) {
                System.out.println("Current counter == " + undoCounter);
                setToPreviousStore(undoCounter, store, storage);
                undoCounter--;
            }
        } else if (undoCounter == 1) {
            if (storage.getUndoStack().size() >= 1) {
                storage.addToRedoStack();
                setToPreviousStore(undoCounter, store, storage);
            }
        }

        // showUndo should only be placed after execution of undo.
        ui.showUndo(undoCounter);
    }

    private void setToPreviousStore(int undoCounter, Store store, Storage storage) throws DuchessException {
        storage.getLastSnapshot(undoCounter);
        storage.save(storage.peekUndoStackAsStore());

        // Obtaining store from stack
        Store newStore = storage.load();
        assert (store.equals(newStore));
        store.setTaskList(newStore.getTaskList());
        store.setModuleList(newStore.getModuleList());
        store.setDuchessCalendar(newStore.getDuchessCalendar());
    }
}