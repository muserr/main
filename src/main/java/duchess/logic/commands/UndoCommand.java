package duchess.logic.commands;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.ObjectMapper;
import duchess.exceptions.DukeException;
import duchess.storage.Storage;
import duchess.storage.Store;
import duchess.ui.Ui;

import java.io.IOException;

public class UndoCommand extends Command {

    @Override
    public void execute(Store store, Ui ui, Storage storage) throws DukeException {
        ui.showUndo();
        String prevStoreJSON = store.getUndoStack().peek();
        store.getUndoStack().pop();
        ObjectMapper mapper = new ObjectMapper();

        try {
            Store newStore = mapper.readValue(prevStoreJSON, Store.class);
            store.setTaskList(newStore.getTaskList());
            store.setModuleList(newStore.getModuleList());
        } catch(JsonParseException e) {
            throw new DukeException("JSON parsing issue");
        } catch(IOException e) {
            throw new DukeException("Stack input error");
        }
    }
}