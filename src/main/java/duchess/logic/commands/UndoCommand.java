package duchess.logic.commands;

import duchess.exceptions.DuchessException;
import duchess.storage.Storage;
import duchess.storage.Store;
import duchess.ui.Ui;

import java.util.List;

public class UndoCommand extends Command {
    private int undoCounter;

    public UndoCommand(List<String> words) throws DuchessException {
        if (words.size() < 2) {
            throw new DuchessException("Usage: add modules <module code> <module name>");
        }

        undoCounter = Integer.parseInt(words.get(0));
    }

    @Override
    public void execute(Store store, Ui ui, Storage storage) throws DuchessException {
        ui.showUndo();

        // You obtain Store data from storage Stack
        storage.getLastSnapshot();
        Store prevStore = storage.getLastSnapshot();

        // Write to JSON DATA:
        storage.save(prevStore);

        // you are getting store from stack
        Store newStore = storage.load();
        store.setTaskList(newStore.getTaskList());
        store.setModuleList(newStore.getModuleList());
    }

    private void repeatUndoFor(int undoCounter) {

    }
}