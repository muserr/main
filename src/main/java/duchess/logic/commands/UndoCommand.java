package duchess.logic.commands;

import duchess.exceptions.DuchessException;
import duchess.storage.Storage;
import duchess.storage.Store;
import duchess.ui.Ui;

public class UndoCommand extends Command {

    @Override
    public void execute(Store store, Ui ui, Storage storage) throws DuchessException {
        ui.showUndo();

        // Undo time
        if (storage.getUndoStack().size() > 0) {
            // You obtain Store data from storage Stack
            Store prevStoreJSON = storage.getUndoStack().peek();
            Storage.getUndoStack().pop();

            // Serialize store to data.json
            // Write to JSON DATA:
            storage.save(prevStoreJSON);

            // you are getting store from stack
            Store newStore = storage.load();

            store.setTaskList(newStore.getTaskList());
            store.setModuleList(newStore.getModuleList());
        }
    }
}