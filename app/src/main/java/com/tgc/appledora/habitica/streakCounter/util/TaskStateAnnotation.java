package com.tgc.appledora.habitica.streakCounter.util;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class TaskStateAnnotation {

    public static final int TASK_STOPPED = 671; //task hasn't been started yet and is not complete
    public static final int TASK_STARTED = 603; //task has been started and is currently running
    public static final int TASK_PAUSED = 20; //task has been paused
    public static final int TASK_RESUMED = 704; //task was paused earlier and now resumed and running
    public static final int TASK_COMPLETED = 735; //task has been completed for this day

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({TASK_STOPPED, TASK_STARTED, TASK_PAUSED, TASK_RESUMED, TASK_COMPLETED})
    public @interface TaskState {
    }
}
