package duchess.logic.commands;

import duchess.exceptions.DuchessException;
import duchess.model.calendar.CalendarManager;
import duchess.model.task.Task;
import duchess.storage.Storage;
import duchess.storage.Store;
import duchess.ui.Ui;

import java.util.ArrayList;
import java.util.List;

public class DeleteLessonCommand extends Command {
    private String type;
    private String moduleCode;
    private String lessonDelete;

    public DeleteLessonCommand(String type, String moduleCode) {
        this.type = type;
        this.moduleCode = moduleCode;
        lessonDelete = type + " (" + moduleCode + ")";
    }

    @Override
    public void execute(Store store, Ui ui, Storage storage) throws DuchessException {
        boolean isDeleted = false;
        List<Integer> toDelete = new ArrayList<>();
        Task task;

        for (int i = 0; i < store.getTaskList().size(); i++) {
            task = store.getTaskList().get(i);

            if (task.getDescription().equalsIgnoreCase(this.lessonDelete)) {
                store.getTaskList().remove(i);
                toDelete.add(i);
                System.out.println(i);
                isDeleted = true;

                store.setDuchessCalendar(CalendarManager.deleteEntry(store.getDuchessCalendar(), task));

                // Decrement i by 1 every time removal is performed.
                i--;
            }
        }

        for (Integer index : toDelete) {
            // System.out.println("Index == " + index);
            // store.getTaskList().remove(index);
            //store.getTaskList().remove(integer);
        }

        ui.showTaskList(store.getTaskList());

        if (isDeleted) {
            ui.showDeletedLesson(moduleCode);
        } else {
            ui.showNoDeletedLesson(moduleCode);
        }

        storage.save(store);
    }
}
