package com.tgc.appledora.habitica.streakCounter.db;

import android.content.Context;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.tgc.appledora.habitica.streakCounter.db.dao.TasksDao;
import com.tgc.appledora.habitica.streakCounter.db.entity.TaskEntity;

import java.util.Date;
import java.util.List;
import java.util.concurrent.Executor;


public class TasksRepository {

    private static TasksRepository sInstance = null;

    private TasksDao mTasksDao;
    //To execute operation off the UI thread
    private Executor executor;

    // Prevent direct instantiation.
    private TasksRepository(Context context) {
        mTasksDao = AppDatabase.getInstance(context).tasksDao();
    }

    /**
     * Helper method to initiate a singleton of TasksRepository
     *
     * @param context application or activity context
     */
    public static TasksRepository getInstance(Context context) {
        if (sInstance == null) {
            synchronized (TasksRepository.class) {
                if (sInstance == null) {
                    sInstance = new TasksRepository(context);
                }
            }
        }
        return sInstance;
    }

    /**
     * Gets tasks from local data source (SQLite)
     */
    public LiveData<List<TaskEntity>> getTasks() {
        return mTasksDao.getTasks();
    }

    /**
     * get single task, provided it's id
     *
     * @param taskId id of the task
     * @return single task wrapped in LiveData
     */
    public LiveData<TaskEntity> getTaskById(int taskId) {
        return mTasksDao.getTaskById(taskId);
    }

    /**
     * Insert task into database, in a separate background thread
     */
    public void insertTask(TaskEntity task) {
        new InsertAsyncTask(mTasksDao).execute(task);
    }

    private static class InsertAsyncTask extends AsyncTask<TaskEntity, Void, Void> {

        private TasksDao mAsyncTasksDao;

        InsertAsyncTask(TasksDao dao) {
            mAsyncTasksDao = dao;
        }


        @Override
        protected Void doInBackground(TaskEntity... task) {
            mAsyncTasksDao.insertTask(task[0]);
            return null;
        }
    }

    /**
     * Update the list of task
     */
    public void updateTasks(TaskEntity... tasks) {
        new UpdateAllAsyncTask(mTasksDao).execute(tasks);
    }

    private static class UpdateAllAsyncTask extends AsyncTask<TaskEntity, Void, Void> {

        private TasksDao mAsyncTasksDao;

        UpdateAllAsyncTask(TasksDao dao) {
            mAsyncTasksDao = dao;
        }


        @Override
        protected Void doInBackground(TaskEntity... tasks) {
            mAsyncTasksDao.updateTasks(tasks);
            return null;
        }
    }

    public void incrementStreak(int taskId, Date currentDate) {
        new IncrementStreakAsyncTask(mTasksDao).execute(new TaskEntity(taskId, "", "", 0, false, null, currentDate));

    }

    private static class IncrementStreakAsyncTask extends AsyncTask<TaskEntity, Void, Void> {

        private TasksDao mAsyncTasksDao;

        IncrementStreakAsyncTask(TasksDao dao) {
            mAsyncTasksDao = dao;
        }


        @Override
        protected Void doInBackground(TaskEntity... tasks) {
            mAsyncTasksDao.incrementStreak(tasks[0].getId(), tasks[0].getLastDate());
            return null;
        }
    }

    /**
     * Delete a task given task id
     */
    public void deleteTask(TaskEntity task) {
        new DeleteTaskAsyncTask(mTasksDao).execute(task);
    }

    private static class DeleteTaskAsyncTask extends AsyncTask<TaskEntity, Void, Void> {

        private TasksDao mAsyncTasksDao;

        DeleteTaskAsyncTask(TasksDao dao) {
            mAsyncTasksDao = dao;
        }


        @Override
        protected Void doInBackground(TaskEntity... tasks) {
            mAsyncTasksDao.deleteTasks(tasks);
            return null;
        }
    }


}
