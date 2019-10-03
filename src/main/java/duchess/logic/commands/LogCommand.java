package duchess.logic.commands;

import duchess.logic.commands.exceptions.DukeException;
import duchess.model.task.DuchessLog;
import duchess.storage.Storage;
import duchess.storage.Store;
import duchess.ui.Ui;

/**
 * Command to display input history.
 */
public class LogCommand extends Command {

    @Override
    public void execute(Store store, Ui ui, Storage storage, DuchessLog duchessLog) throws DukeException {
        ui.showUserHistory(duchessLog.getLog());
    }
}
