package duchess.logic.commands;

import duchess.model.task.DuchessLog;
import duchess.storage.Storage;
import duchess.logic.commands.exceptions.DukeException;
import duchess.storage.Store;
import duchess.ui.Ui;

public class ListCommand extends Command {
    @Override
    public void execute(Store store, Ui ui, Storage storage, DuchessLog duchessLog) throws DukeException {
        ui.showTaskList(store.getTaskList());
    }
}
