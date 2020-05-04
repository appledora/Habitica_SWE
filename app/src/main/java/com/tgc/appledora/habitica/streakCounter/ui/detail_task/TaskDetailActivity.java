
package com.tgc.appledora.habitica.streakCounter.ui.detail_task;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.maseno.franklinesable.lifereminder.R;
import com.maseno.franklinesable.lifereminder.databinding.ActivityTaskDetailBinding;

import static com.tgc.appledora.habitica.streakCounter.util.TaskStateAnnotation.TASK_COMPLETED;
import static com.tgc.appledora.habitica.streakCounter.util.TaskStateAnnotation.TASK_RESUMED;
import static com.tgc.appledora.habitica.streakCounter.util.TaskStateAnnotation.TASK_STARTED;
import static com.tgc.appledora.habitica.streakCounter.util.TaskStateAnnotation.TASK_STOPPED;

public class TaskDetailActivity extends AppCompatActivity {

    private TaskDetailViewModel taskDetailViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        init();
        setupLayoutWithDataBinding();
        setupToolbar();


        if (savedInstanceState == null) {
            loadTaskFromDb();
            setupFragment();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (!taskDetailViewModel.isResumeTaskAfterScreenOrientation() && taskDetailViewModel.getTaskRunningStatus().getValue() != null && (taskDetailViewModel.getTaskRunningStatus().getValue() != TASK_STOPPED || taskDetailViewModel.getTaskRunningStatus().getValue() != TASK_COMPLETED)) {
            taskDetailViewModel.saveTaskProgress();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        Log.d("streak", "Task id = " + intent.getIntExtra(TaskDetailFragment.ARG_TASK_ID, -1));
        if (taskDetailViewModel.getTaskRunningStatus() != null && taskDetailViewModel.getTaskRunningStatus().getValue() != null && taskDetailViewModel.getTaskRunningStatus().getValue() != TASK_RESUMED || taskDetailViewModel.getTaskRunningStatus().getValue() != TASK_STARTED) {
            Log.d("streak", "Task is not running");
            loadTaskFromDb();
            setupFragment();
        } else {
            Log.d("streak", "Task is running");
        }

    }

    /**
     * inflate R.menu.detail_menu to show delete icon on toolbar,
     * to allow user to delete current task
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.detail_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void init() {
        taskDetailViewModel = ViewModelProviders.of(this).get(TaskDetailViewModel.class); // Obtain the ViewModel component.
    }

    private void setupLayoutWithDataBinding() {
        // data binding
        ActivityTaskDetailBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_task_detail);
        // Specify the current activity as the lifecycle owner.
        binding.setLifecycleOwner(this);
        // connect ViewModel to data binding
        binding.setViewmodel(taskDetailViewModel);
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    private void loadTaskFromDb() {
        int mTaskId = getIntent().getIntExtra(TaskDetailFragment.ARG_TASK_ID, -1);
        taskDetailViewModel.setmTaskId(mTaskId);
    }

    private void setupFragment() {
        int mTaskId = getIntent().getIntExtra(TaskDetailFragment.ARG_TASK_ID, -1);
        Bundle arguments = new Bundle();
        arguments.putInt(TaskDetailFragment.ARG_TASK_ID,
                mTaskId);
        TaskDetailFragment fragment = new TaskDetailFragment();
        fragment.setArguments(arguments);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.item_detail_container, fragment)
                .commit();
    }

    @Override
    public void onBackPressed() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.item_detail_container);

        if (taskDetailViewModel.getTaskRunningStatus().getValue() != null && (taskDetailViewModel.getTaskRunningStatus().getValue() == TASK_STARTED || taskDetailViewModel.getTaskRunningStatus().getValue() == TASK_RESUMED) && fragment instanceof TaskDetailFragment) {
            ((TaskDetailFragment) fragment).showConfirmGoBackDialog();
        } else {
            super.onBackPressed();
        }
    }

}
