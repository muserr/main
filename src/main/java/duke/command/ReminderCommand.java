package duke.command;

import duke.dukeexception.DukeException;
import duke.task.Deadline;
import duke.task.Task;
import duke.task.TaskList;

import java.util.ArrayList;
import java.util.List;

/**
 * Searches Tasklist and filters out deadline objects.
 */
public class ReminderCommand extends Command {
    private ArrayList<Task> reminderList;
    private Ui ui;

    /**
     * Instantiates reminderList and ui.
     */
    public ReminderCommand() {
        reminderList = new ArrayList<Task>();
        ui = new Ui();
    }

    /**
     * Displays Deadline objects to user.
     *
     * @param taskList List containing tasks
     * @param ui Userinterface object
     * @param storage Storage object
     * @throws DukeException Exception thrown when storage not found
     */
    @Override
    public void execute(TaskList taskList, Ui ui, Storage storage) throws DukeException {
        addDeadlines(taskList);
        display();
    }

    /**
     * Adds objects of type Deadline to reminderList.
     *
     * @param taskList of user inputs
     */
    public void addDeadlines(TaskList taskList) {
        for (Task task : taskList.getTasks()) {
            if (task instanceof Deadline) {
                reminderList.add(task);
            }
        }
    }

    /**
     * Displays deadlines to user.
     */
    public void display() {
        if (reminderList.size() == 0) {
            ui.showError("You have no pending deadlines.");
        } else {
            ui.printIndented("You currently have these deadlines.");
            displayReminderList();
        }
    }

    /**
     * Displays task of reminderList.
     */
    private void displayReminderList() {
        ui.showTasks(this.reminderList);
    }
}
