package duchess.model;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeFrame implements Comparable<TimeFrame> {
    private Date time;
    private String task;
    private boolean isOngoing;
    private boolean isEmptyTimeFrame;

    /**
     * Constructor for empty schedules.
     */
    public TimeFrame() {
        this.isEmptyTimeFrame = true;
    }

    /**
     * Constructor for schedule.
     *
     * @param time     time to be shown in timetable
     * @param toString task details
     */
    public TimeFrame(Date time, String toString) {
        this.time = time;
        this.task = toString;
        this.isOngoing = false;
        this.isEmptyTimeFrame = false;
    }

    /**
     * Constructor for schedule. Tracks ongoing events.
     *
     * @param time     time to be shown in timetable
     * @param toString task details
     * @param ongoing  event ongoing
     */
    public TimeFrame(Date time, String toString, boolean ongoing) {
        this.time = time;
        this.task = toString;
        this.isOngoing = ongoing;
        this.isEmptyTimeFrame = false;
    }

    public Date getStart() {
        return time;
    }

    /**
     * Changes Date to String.
     *
     * @return string containing time of task
     */
    public String getStartString() {
        SimpleDateFormat formatter = new SimpleDateFormat("HHmm");
        formatter.setLenient(false);
        return formatter.format(time);
    }

    public String getTask() {
        return task;
    }

    public boolean getOngoing() {
        return isOngoing;
    }

    public boolean getEmptyTimeFrame() {
        return isEmptyTimeFrame;
    }

    @Override
    public int compareTo(TimeFrame timeFrame) {
        return time.compareTo(timeFrame.time);
    }
}
