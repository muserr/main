package duchess.storage;

import duchess.exceptions.DuchessException;
import duchess.model.Module;
import duchess.model.calendar.CalendarEntry;
import duchess.model.task.Event;
import duchess.model.task.Task;
import duchess.model.task.Todo;
import duchess.parser.Util;
import org.junit.jupiter.api.Test;

import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class StoreTest {
    private final String INVALID_FORMAT_MESSAGE = "Please enter dates in the format dd/mm/yyyy hhmm";

    @Test
    public void returnTaskList_emptyTaskList_success() {
        Store store = new Store();
        List<Task> testTaskList = new ArrayList<>();

        assertEquals(testTaskList, store.getTaskList());
        assertTrue(store.getTaskList().size() == 0);
    }

    @Test
    public void returnModuleList_emptyModuleList_success() {
        Store store = new Store();
        List<Module> testModuleList = new ArrayList<>();

        assertEquals(testModuleList, store.getModuleList());
        assertTrue(store.getModuleList().size() == 0);
    }

    @Test
    public void returnDuchessCalendar_emptyDuchessCalendar_success() {
        Store store = new Store();
        List<CalendarEntry> testDuchessCalendar = new ArrayList<>();

        assertEquals(testDuchessCalendar, store.getDuchessCalendar());
        assertTrue(store.getDuchessCalendar().size() == 0);
    }

    @Test
    public void isClashing_nonClashingTasks_false() {
        Store store = new Store();
        String startDateTime = "02/11/2019 1730";
        String endDateTime = "04/11/2019 1800";

        try {
            Task taskA = new Todo("Star jumps");
            Task taskB = new Todo("Jogging with friends.");

            Task taskC = new Event("Running"
                    , Util.parseDateTime(endDateTime)
                    , Util.parseDateTime(startDateTime));

            assertTrue(store.getTaskList().size() == 0);

            store.getTaskList().add(taskA);
            store.getTaskList().add(taskC);
            assertTrue(store.getTaskList().size() == 2);
            assertFalse(store.isClashing(taskB));

        } catch (DuchessException | DateTimeParseException | IndexOutOfBoundsException e) {
            assertEquals(e.getMessage(), INVALID_FORMAT_MESSAGE);
        }
    }

    @Test
    public void isClashing_clashingTasks_true() {
        Store store = new Store();
        String startDateTime = "02/11/2019 0800";
        String endDateTime = "04/11/2019 1800";

        try {
            Task taskA = new Event("Running"
                    , Util.parseDateTime(endDateTime)
                    , Util.parseDateTime(startDateTime));

            Task taskB = new Event("Jogging"
                    , Util.parseDateTime(endDateTime)
                    , Util.parseDateTime(startDateTime));

            assertTrue(store.getTaskList().size() == 0);
            store.getTaskList().add(taskA);

            assertTrue(store.getTaskList().size() == 1);
            assertTrue(store.isClashing(taskB));

        } catch (DuchessException | DateTimeParseException | IndexOutOfBoundsException e) {
            assertEquals(e.getMessage(), INVALID_FORMAT_MESSAGE);
        }
    }

    @Test
    public void findModuleByCode_validCodeString_nonNullOptionalValue() {
        Store store = new Store();
        Module moduleA = new Module("CS1231", "Discrete Mathematics");
        Module moduleB = new Module("CS2113T", "Software Engineering");

        store.getModuleList().add(moduleA);
        store.getModuleList().add(moduleB);

        Optional<Module> testModule = Optional.of(moduleA);
        assertEquals(testModule, store.findModuleByCode("CS1231"));
    }

    @Test
    public void findModuleByCode_inValidCodeString_nullOptionalValue() {
        Store store = new Store();
        Module moduleA = new Module("CS1231", "Discrete Mathematics");
        Module moduleB = new Module("CS2113T", "Software Engineering");

        store.getModuleList().add(moduleA);
        store.getModuleList().add(moduleB);

        Optional<Module> testModule = Optional.empty();
        assertEquals(testModule, store.findModuleByCode("CG2028"));
    }
}
