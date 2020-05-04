package com.tgc.appledora.habitica.streakCounter.db.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.tgc.appledora.habitica.streakCounter.db.entity.TaskEntity;

import java.util.Date;
import java.util.List;

/**
 * Data Access Object for the tasks table.
 */
@Dao
public interface TasksDao {

    /**
     * Select all tasks from the tasks table.
     *
     * @return all tasks.
     */
    @Query("SELECT * FROM tasks")
    LiveData<List<TaskEntity>> getTasks();

    /**
     * Select a task by id.
     *
     * @param taskId the task id.
     * @return the task with taskId.
     */
    @Query("SELECT * FROM tasks WHERE id = :taskId")
    LiveData<TaskEntity> getTaskById(int taskId);

    /**
     * Insert a task in the database. If the task already exists, replace it.
     *
     * @param task the task to be inserted.
     * @return an array containing the rowId of tasks inserted
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] insertTask(TaskEntity... task);

    /**
     * Update a list of tasks. Can be a single task also
     *
     * @param tasks all the tasks that need to be updated
     * @return an integer indicating the number of rows updated in the database
     */
    @Update
    int updateTasks(TaskEntity... tasks);

    /**
     * Increment the streak by 1 and set lastDate to be current date, and reset elapsed time to be zero for next day
     *
     * @param taskId      id of the task
     * @param currentDate today's date
     */
    @Query("UPDATE tasks SET currentStreak = currentStreak + 1, lastDate = :currentDate, elapsedTimeInMilliSeconds = 0 WHERE id = :taskId")
    void incrementStreak(int taskId, Date currentDate);

    /**
     * Delete a list of tasks, could be single too
     *
     * @return an integer indicating the number of rows removed from the database
     */
    @Delete
    int deleteTasks(TaskEntity... tasks);


}
