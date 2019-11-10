package duchess.storage;

import duchess.exceptions.DuchessException;
import duchess.model.Module;
import duchess.model.task.Deadline;
import duchess.model.task.Task;
import duchess.model.task.Todo;
import duchess.parser.Parser;
import duchess.parser.Util;

import java.util.List;

/**
 * Add seed data when application is first started.
 */
public class Seed {
    /**
     * Seeds a given store with default data.
     *
     * @param store the store to seed
     */
    public static void execute(Store store) throws DuchessException {
        // Modules
        Module a = new Module("CS1231", "Discrete Mathematics");
        Module b = new Module("CS2040", "Algorithms");
        Module c = new Module("CS2113", "Software Engineering");
        Module d = new Module("CS1010", "Introduction to Programming");
        Module e = new Module("EE2026", "Digital Design");
        Module f = new Module("GES1010", "Nation Building");
        List.of(a, b, c, d, e, f).forEach(x -> store.getModuleList().add(x));

        // Todos
        Task g = new Todo("Reading");
        g.setModule(f);
        Task h = new Todo("Personal stuff");
        Task i = new Todo("Revision");
        i.setModule(c);
        Task j = new Todo("Community Service");
        Task k = new Todo("Consult prof on concept");
        k.setModule(e);
        List.of(g, h, i, j, k).forEach(x -> store.getTaskList().add(x));

        // Deadlines
        Task l = new Deadline("PPP submission", Util.parseDateTime("23/12/2019 0800"));
        Task m = new Deadline("Term Paper submission", Util.parseDateTime("02/12/2019 1400"));
        Task n = new Deadline("Buy Christmas gifts", Util.parseDateTime("24/12/2019 2359"));
        List.of(l, m, n).forEach(x -> store.getTaskList().add(x));
    }
}
