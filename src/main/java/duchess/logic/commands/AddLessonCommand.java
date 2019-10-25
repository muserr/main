package duchess.logic.commands;

import duchess.exceptions.DuchessException;
import duchess.model.calendar.AcademicYear;
import duchess.model.calendar.CalendarManager;
import duchess.model.task.Event;
import duchess.storage.Storage;
import duchess.storage.Store;
import duchess.ui.Ui;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Adds recurring lessons.
 *
 * lesson /add <module-code> /type <class-description>
 * /time <start_date> <start_time> /to <end_date> <end_time>
 *
 */
public class AddLessonCommand extends Command {
    private String description;
    private LocalDate startDate;
    private LocalDateTime end;
    private LocalDateTime start;
    private LocalDateTime endCopy;
    private LocalDateTime startCopy;
    private String moduleCode;
    private final int studyWeeks = 15;
    private final String invalidStartDate
            = "Invalid start date, start date provided must be within a semester.";

    public AddLessonCommand(String description, LocalDateTime start, LocalDateTime end, String moduleCode) {
        this.description = description + " (" + moduleCode + ")";
        this.start = start;
        this.end = end;
        this.startCopy = start;
        this.endCopy = end;
        this.moduleCode = moduleCode;
        this.startDate = start.toLocalDate();
    }

    @Override
    public void execute(Store store, Ui ui, Storage storage) throws DuchessException {
        AcademicYear academicYear = new AcademicYear();
        // check if startDate is within AY. If not throw exception.
        if (!academicYear.isAcademicSemester(this.startDate)) {
            throw new DuchessException(invalidStartDate);
        } else {
            // While AY not ended, and !recess_week && !reading_week. Add event.
            // Find out which Sem does date fall into first
            boolean isWithinSemOne = academicYear.isFirstSemester(startDate);
            boolean isWithinSemTwo = academicYear.isSecondSemester(startDate);

            LocalDate compareDate;
            assert (isWithinSemOne || isWithinSemTwo);
            if (isWithinSemOne) {
                compareDate = academicYear.getSemOneStart();
            } else {
                compareDate = academicYear.getSemTwoStart();
            }

            // Find the corresponding week for the semester.
            int currentWeek = academicYear.getWeekAsInt(compareDate, startDate);

            for (int i = currentWeek; i <= studyWeeks; i++) {
                if (academicYear.isSemesterBreak(i) == false) {
                    // Add classes similar to add events.
                    addLessons(store, ui, storage);

                    // Both startCopy and endCopy dates MUST BE INCREMENTED to next week.
                    startCopy = startCopy.plusWeeks(1);
                    endCopy = endCopy.plusWeeks(1);
                }
            }
        }
    }

    private void addLessons(Store store, Ui ui, Storage storage) throws DuchessException {
        Event task = new Event(description, endCopy, startCopy);
        if (!store.isClashing(task)) {
            store.getTaskList().add(task);
            ui.showTaskAdded(store.getTaskList(), task);
            store.setDuchessCalendar(CalendarManager.addEntry(store.getDuchessCalendar(), task));
            storage.save(store);
        }
    }
}
