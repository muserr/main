package duchess.storage;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;
import duchess.model.task.Task;
import duchess.model.Module;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class Store {
    private List<Task> taskList;
    private List<Module> moduleList;
    private Stack<String> undoStack;

    public Store() {
        this.taskList = new ArrayList<>();
        this.moduleList = new ArrayList<>();
        this.undoStack = new Stack<>();
    }

    public boolean isClashing(Task newTask) {
        return this.taskList.stream()
                .anyMatch(task -> task.clashesWith(newTask));
    }

    @JsonGetter("taskList")
    public List<Task> getTaskList() {
        return taskList;
    }

    @JsonSetter("taskList")
    public void setTaskList(List<Task> taskList) {
        this.taskList = taskList;
    }

    @JsonGetter("moduleList")
    public List<Module> getModuleList() {
        return moduleList;
    }

    @JsonSetter("moduleList")
    public void setModuleList(List<Module> moduleList) {
        this.moduleList = moduleList;
    }

    @JsonGetter("undoStack")
    public Stack<String> getUndoStack() {
        return undoStack;
    }

    @JsonSetter("undoStack")
    public void setUndoStack(Stack<String> undoStack) {
        this.undoStack = undoStack;
    }
}
