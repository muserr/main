package duchess.model.task;

import java.util.ArrayList;
import java.util.List;

public class DuchessLog {
    private List<String> log;

    public DuchessLog() {
        this.log = new ArrayList<>();
    }

    public void add(String input) {
        this.log.add(input);
    }

    public List<String> getLog() {
        return this.log;
    }
}
