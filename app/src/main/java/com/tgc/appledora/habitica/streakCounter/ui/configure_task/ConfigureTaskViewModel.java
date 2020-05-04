package com.tgc.appledora.habitica.streakCounter.ui.configure_task;


import android.app.Application;
import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.maseno.franklinesable.lifereminder.R;
import com.tgc.appledora.habitica.streakCounter.db.TasksRepository;
import com.tgc.appledora.habitica.streakCounter.db.entity.TaskEntity;
import com.tgc.appledora.habitica.streakCounter.util.DateUtils;
import com.tgc.appledora.habitica.streakCounter.util.NotificationUtils;
import com.tgc.appledora.habitica.streakCounter.util.StreakyPreferences;

public class ConfigureTaskViewModel extends AndroidViewModel {

    public static final String INSERT_MODE = "createNewTaskAndInsert";
    public static final String EDIT_MODE = "editTaskAndUpdate";

    private LiveData<TaskEntity> task; //task to be configured
    private String configurationMode;
    private TasksRepository taskRepository;
    private MutableLiveData<Integer> statusFinishActivity;// Set it to 1 to notify activity to finish itself
    private MutableLiveData<String> time; //Will store formatted string h:mm a from TimePickerDialog, to show changes in UI
    private MutableLiveData<String> validationErrorMsg; //Will show validation message AddTaskActivity
    private MutableLiveData<String> minutesForTask; //Have to use this variable to avoid showing default 0 value in EditText, once user click ok button, it will be saved into the task
    private Context mContext;

    public ConfigureTaskViewModel(@NonNull Application application) {
        super(application);
        mContext = application;
        task = new MutableLiveData<>();
        taskRepository = TasksRepository.getInstance(application);
        time = new MutableLiveData<>();
        minutesForTask = new MutableLiveData<>();
        //time.setValue(application.getString(R.string.time_tag));
        validationErrorMsg = new MutableLiveData<>();
        statusFinishActivity = new MutableLiveData<>();
    }

    /**
     * Set mode whether we are creating new task or editing an existing task
     *
     * @param taskId == -1 => Creating new task else Editing an existing task
     */
    public void setConfigurationMode(int taskId) {
        if (taskId == -1) {
            //Create a new task
            configurationMode = INSERT_MODE;
            MutableLiveData<TaskEntity> emptyTask = new MutableLiveData<>();
            emptyTask.setValue(new TaskEntity());
            task = emptyTask;
            //time.setValue(mContext.getString(R.string.time_tag));
        } else {
            //Edit an existing task
            configurationMode = EDIT_MODE;
            task = taskRepository.getTaskById(taskId);
            //time.setValue(task.getValue().getTimeStr());
        }
    }

    /**
     * OnClick function bind to Button[@+id/mbtn_save_task] in activity_add_task.xml
     * fixme : This code is becoming very chaotic, clean it
     */
    public void onClickSave(View view) {
        if (validateTask()) {
            try {
                task.getValue().setMinutes(Integer.parseInt(minutesForTask.getValue()));
            } catch (NumberFormatException e) {
                e.printStackTrace();
                task.getValue().setMinutes(0); //set default to zero
            }
            formatInputText();

            if (INSERT_MODE.equals(configurationMode)) {
                //Insert a new task
                setDefaultParameters();
                taskRepository.insertTask(task.getValue());
            } else {
                //Update an existing task
                taskRepository.updateTasks(task.getValue());
            }
            if (task.getValue().isShowNotification()) {
                // REMAINDER: Since we are setting id just above, we need to call this line always after setting id
                NotificationUtils.scheduleAlarmToTriggerNotification(mContext, task.getValue());
            } else {
                NotificationUtils.cancelAlarmToTriggerNotification(mContext, task.getValue());
            }

            // FIXME: 10/23/2018 This should be managed from TaskRepository, once the operation has been performed successfully, It should set the status from there
            statusFinishActivity.setValue(1);//notify activity to finish itself
        }
    }

    /**
     * Make text from EditText lower case and trim
     */
    private void formatInputText() {
        task.getValue().setTitle(task.getValue().getTitle().trim().toLowerCase());
        task.getValue().setLocation(task.getValue().getLocation().trim().toLowerCase());
    }

    /**
     * Set unique id to act as a primary key
     * current streak to zero,
     * lastDate to be yesterday date
     */
    private void setDefaultParameters() {
        task.getValue().setId(StreakyPreferences.getNextUniqueId(mContext));
        task.getValue().setLastDate(DateUtils.getYesterdayDateWithoutTime());
        ;
        task.getValue().setCurrentStreak(0);
    }

    /**
     * Helper function to check validity of inputs and
     * check if fields are not null, minute field can be left blank,
     * if minute field left blank set it to 0 by default
     *
     * @return true => if all inputs in {@link ConfigureTaskActivity} are not null and valid
     */
    private boolean validateTask() {
        if (task.getValue().getTitle() == null || "".equals(task.getValue().getTitle())) {
            validationErrorMsg.setValue(mContext.getString(R.string.task_title_empty_error));
            return false;
        }

        if (task.getValue().getTime() == null) {
            validationErrorMsg.setValue(mContext.getString(R.string.task_time_empty_error));
            return false;
        }

        if (task.getValue().getLocation() == null || "".equals(task.getValue().getLocation())) {
            validationErrorMsg.setValue(mContext.getString(R.string.task_location_empty_error));
            return false;
        }

        return true;
    }

    /**
     * ========================================== Getters and setters =========================================
     **/

    public LiveData<TaskEntity> getTask() {
        return task;
    }

    public MutableLiveData<String> getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time.setValue(time);
    }

    public MutableLiveData<String> getValidationErrorMsg() {
        return validationErrorMsg;
    }

    /**
     * Used to consume event only one time,
     * this will avoid firing of same event,
     * if configuration change
     * <p>
     * but also if snackbar is visible and configuration change happen, it won't be shown
     */
    public void consumedErrorMessage() {
        this.validationErrorMsg.setValue(null);
    }

    public MutableLiveData<String> getMinutesForTask() {
        return minutesForTask;
    }

    public void setMinutesForTask(String minutesForTask) {
        this.minutesForTask.setValue(minutesForTask);
    }

    public MutableLiveData<Integer> getStatusFinishActivity() {
        return statusFinishActivity;
    }
}
