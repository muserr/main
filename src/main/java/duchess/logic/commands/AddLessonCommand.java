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
 *     /time <start_date> <start_time> /to <end_date> <end_time>
 */
public class AddLessonCommand extends Command {
    private String description;
    private LocalDate startDate;
    private LocalDateTime end;
    private LocalDateTime start;
    private String moduleCode;

    public AddLessonCommand(String description, LocalDateTime start, LocalDateTime end, String moduleCode) {
        this.description = description + " (" + moduleCode + ")";
        this.start = start;
        this.end = end;
        this.moduleCode = moduleCode;
        this.startDate = start.toLocalDate();
    }

    @Override
    public void execute(Store store, Ui ui, Storage storage) throws DuchessException {
        AcademicYear academicYear = new AcademicYear();
        // check if startDate is within AY. If not throw exception.
        if (!academicYear.isAcademicSemester(this.startDate)) {
            throw new DuchessException("Invalid lesson start, first day must be within AY.");
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

            for (int i = currentWeek; i <= 15; i++) {
                if (!academicYear.isSemesterBreak(currentWeek)) {
                    // Add classes similar to add events.
                    addLessons(store, ui, storage);

                    // Increment date to next week.
                    start.plusWeeks(1);
                }
            }
        }

    }

    private void addLessons(Store store, Ui ui, Storage storage) throws DuchessException {
        Event task = new Event(description, end, start);

        //if (store.isClashing(task)) {
        //    throw new DuchessException("Unable to add event - clash found.");
        //}
        if (!store.isClashing(task)) {
            store.getTaskList().add(task);
            ui.showTaskAdded(store.getTaskList(), task);
            store.setDuchessCalendar(CalendarManager.addEntry(store.getDuchessCalendar(), task));
            storage.save(store);
        }
    }
}
