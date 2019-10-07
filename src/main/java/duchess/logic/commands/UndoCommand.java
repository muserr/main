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
            throw new DuchessException("Usage: undo <number>");
        }
    }

    @Override
    public void execute(Store store, Ui ui, Storage storage) throws DuchessException {

        ui.showUndo(undoCounter);
        //while(undoCounter > 0 && storage.getUndoStack().size() > 0) {
        // You obtain Store data from storage Stack
        storage.getLastSnapshot();
        Store prevStore = storage.getLastSnapshot();

        // Write to JSON DATA:
        storage.save(prevStore);

        // you are getting store from stack
        Store newStore = storage.load();
        store.setTaskList(newStore.getTaskList());
        store.setModuleList(newStore.getModuleList());

        undoCounter--;
        //}
    }

    // private void repeatUndoFor(Store store, Ui ui, Storage storage, int counter) throws DuchessException {
    //     while(storage.getUndoStack().size() > 0) {
    //         // You obtain Store data from storage Stack
    //         storage.getLastSnapshot();
    //         Store prevStore = storage.getLastSnapshot();

    //         // Write to JSON DATA:
    //         storage.save(prevStore);

    //         // you are getting store from stack
    //         Store newStore = storage.load();
    //         store.setTaskList(newStore.getTaskList());
    //         store.setModuleList(newStore.getModuleList());
    //     }
    // }
}