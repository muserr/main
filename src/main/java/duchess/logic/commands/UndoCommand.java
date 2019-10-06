package duchess.logic.commands;

import duchess.exceptions.DuchessException;
import duchess.storage.Storage;
import duchess.storage.Store;
import duchess.ui.Ui;

public class UndoCommand extends Command {

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
}