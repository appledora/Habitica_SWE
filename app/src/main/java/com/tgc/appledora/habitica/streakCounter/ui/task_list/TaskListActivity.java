package com.tgc.appledora.habitica.streakCounter.ui.task_list;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.ShareActionProvider;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.maseno.franklinesable.lifereminder.R;
import com.maseno.franklinesable.lifereminder.databinding.ActivityTaskListBinding;
import com.tgc.appledora.habitica.streakCounter.db.entity.TaskEntity;
import com.tgc.appledora.habitica.streakCounter.ui.configure_task.ConfigureTaskActivity;
import com.tgc.appledora.habitica.streakCounter.util.DateUtils;
import com.tgc.appledora.habitica.streakCounter.util.GridSpacingItemDecoration;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * An activity representing a list of task.
 */
// TODO: 11/21/2018 Clean the code
public class TaskListActivity extends AppCompatActivity {

    private TaskListViewModel taskListViewModel;
    private List<TaskEntity> taskList;
    private TaskAdapter adapter;
    private TextToSpeech textToSpeech;

    private Toolbar toolbar;
    private CoordinatorLayout coordinatorLayout;
    private RecyclerView taskRecyclerView;

    private ShareActionProvider shareActionProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        init();
        setupLayoutWithDataBinding();
        initView();
        setupToolbar();
        setupTaskRecyclerView();
        setupClickListener();
        observeDataFromViewModel();

    }

    @Override
    protected void onResume() {
        super.onResume();
        textToSpeech = new TextToSpeech(getApplicationContext(), status -> {
            if (status != TextToSpeech.ERROR) {
                // TODO: 11/1/2018 If localization is done, set locale according to that
                textToSpeech.setLanguage(Locale.UK);
            }
        });

    }

    @Override
    public void onPause() {
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
        super.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.list_menu, menu);
//        shareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(menu.findItem(R.id.action_share_app));
//        shareActionProvider.setShareIntent(getShareIntent());
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_feedback:
                sendFeedbackViaMail();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private Intent getShareIntent() {

        return null;
    }

    private void sendFeedbackViaMail() {
        Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", "sanjeevy133@email.address", null));
        intent.putExtra(Intent.EXTRA_SUBJECT, "Feedback");
        intent.putExtra(Intent.EXTRA_TEXT, "Hi Streaky team, ");
        startActivity(Intent.createChooser(intent, ""));
    }


    private void init() {
        // Obtain the ViewModel component.
        taskListViewModel = ViewModelProviders.of(this).get(TaskListViewModel.class);
    }

    private void setupLayoutWithDataBinding() {
        // data binding
        ActivityTaskListBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_task_list);
        // Specify the current activity as the lifecycle owner.
        binding.setLifecycleOwner(this);
        // connect ViewModel to data binding
        binding.setViewmodel(taskListViewModel);
    }


    private void initView() {
        toolbar = findViewById(R.id.toolbar);
        taskRecyclerView = findViewById(R.id.item_list);
        coordinatorLayout = findViewById(R.id.coordinator);
    }


    private void setupToolbar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false); //Showing TextView instead of toolbar default title, to position title in middle
        }
    }

    private void setupTaskRecyclerView() {

        RecyclerView.LayoutManager layoutManager = getLayoutManager();
        taskList = new ArrayList<>();
        adapter = new TaskAdapter(this, taskList);
        taskRecyclerView.setLayoutManager(layoutManager);
        taskRecyclerView.setAdapter(adapter);
        taskRecyclerView.setHasFixedSize(true);
        taskRecyclerView.addItemDecoration(new GridSpacingItemDecoration(
                getResources().getInteger(R.integer.list_column_count),
                (int) getResources().getDimension(R.dimen.list_gutter))
        );
    }

    private RecyclerView.LayoutManager getLayoutManager() {

        int SPAN_COUNT = getResources().getInteger(R.integer.list_column_count);
        if (SPAN_COUNT == 1) {
            return new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        } else {
            return new GridLayoutManager(this, SPAN_COUNT, RecyclerView.VERTICAL, false);
        }
    }

    private void setupClickListener() {
        findViewById(R.id.fab).setOnClickListener(view -> startActivity(new Intent(TaskListActivity.this, ConfigureTaskActivity.class)));
    }

    private void observeDataFromViewModel() {
        taskListViewModel.getTaskList().observe(this, this::onTaskListChanged);
    }

    private void onTaskListChanged(List<TaskEntity> newTaskList) {

        //if list is empty, show message on screen, else populate RecyclerView
        if (newTaskList.size() > 0) {
            findViewById(R.id.empty_view).setVisibility(View.GONE);
            // FIXME: 10/14/2018 This should be handle either in ViewModel or Repository
            List<TaskEntity> updatedList = calculateStreakLost(newTaskList);
            if (updatedList.size() > 0) {
                showDialogStreakLost(updatedList);
                taskListViewModel.updateTasks(updatedList);
            } else {
                //no modification, show the list
                // TODO: 11/1/2018 Should be using DiffUtil library
                this.taskList.clear();
                this.taskList.addAll(newTaskList);
                adapter.notifyDataSetChanged();
            }
        } else {
            findViewById(R.id.empty_view).setVisibility(View.VISIBLE);
        }

        showMessageOnTaskListChanged(newTaskList.size());

    }

    private void showMessageOnTaskListChanged(int size) {
        if (taskListViewModel.getNumberOfTask() != -1) {
            if (taskListViewModel.getNumberOfTask() < size) {
                showSnackBar(getString(R.string.new_task_added_msg));
            } else if (taskListViewModel.getNumberOfTask() > size) {
                showSnackBar(getString(R.string.task_deleted_msg));
            }
        }
        taskListViewModel.setNumberOfTask(size);
    }

    /**
     * Show SnackBar with message
     *
     * @param message need to be shown
     */
    private void showSnackBar(String message) {
        if (message != null) {
            Snackbar.make(coordinatorLayout, message, Snackbar.LENGTH_SHORT).show();
        }
    }

    /**
     * Find out whether user has lost and streak,
     *
     * @param newTaskList contains the list of all the tasks
     * @return list of task in which user has lost streaks
     */
    private List<TaskEntity> calculateStreakLost(List<TaskEntity> newTaskList) {

        List<TaskEntity> streakLostTaskList = new ArrayList<>();

        if (newTaskList == null || newTaskList.size() < 1) return streakLostTaskList;

        Date currentDate = DateUtils.getCurrentDateWithoutTime();
        Date yesterdayDate = DateUtils.getYesterdayDateWithoutTime();

        //calculate whether user has lost streak
        for (TaskEntity taskEntity : newTaskList) {
            if (taskEntity.getCurrentStreak() != 0) {
                long numberOfDays = DateUtils.subtractDates(currentDate, taskEntity.getLastDate());
                if (numberOfDays > 1) {
                    taskEntity.setCurrentStreak(0);
                    taskEntity.setLastDate(yesterdayDate); //setting it to yesterday so tat user can complete today streak, and make count to 1
                    streakLostTaskList.add(taskEntity);
                }
            }
        }

        return streakLostTaskList;
    }

    /**
     * Show dialog showing all the task in which streak has lost
     *
     * @param list of task in which has streak has lost
     */
    private void showDialogStreakLost(List<TaskEntity> list) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AlertDialogLight);
        builder.setTitle(getResources().getQuantityString(R.plurals.streakLost, list.size()));
        builder.setItems(getTaskTitleArray(list), null);
        // Add the buttons
        builder.setPositiveButton(R.string.ok, (dialog, id) -> {
            // User clicked OK button
            dialog.dismiss();
        });

        AlertDialog dialog = builder.create();
        dialog.setOnShowListener(dialogInterface -> dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(this, R.color.colorAccent)));

        dialog.show();
    }

    /**
     * return a string array containing the titles of all the task,
     * in which streak has been lost
     *
     * @param list in which streak has lost
     * @return streing array containing title of tasks
     */
    private String[] getTaskTitleArray(List<TaskEntity> list) {
        String[] taskTitles = new String[list.size()];
        for (int i = 0; i < list.size(); i++) {
            taskTitles[i] = list.get(i).getTitle();
        }

        return taskTitles;
    }

    /**
     * Convert text to speech
     *
     * @param text which will be converted to speech
     */
    public void convertTextToSpeech(String text) {
        textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null);
    }

    /**
     * Function used for debugging purpose
     */
    private void printScreenDimensions(View view) {
        final String TAG = "SANJEEV";
        float density = this.getResources()
                .getDisplayMetrics()
                .density;

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;

        Log.d(TAG, "density: " + density);
        Log.d(TAG, "height: " + height);
        Log.d(TAG, "width: " + width);
    }
}
