package com.tgc.appledora.habitica.moodChart;


import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProviders;

import com.maseno.franklinesable.lifereminder.R;
import com.tgc.appledora.habitica.moodChart.data.AppDatabase;
import com.tgc.appledora.habitica.moodChart.data.AppExecutors;
import com.tgc.appledora.habitica.moodChart.data.Mood;
import com.tgc.appledora.habitica.moodChart.utils.Preferences;
import com.tgc.appledora.habitica.moodChart.utils.SpecialUtils;
import com.tgc.appledora.habitica.moodChart.viewmodel.AddDailyMoodViewModel;
import com.tgc.appledora.habitica.moodChart.viewmodel.AddDailyMoodViewModelFactory;
import com.tgc.appledora.habitica.moodChart.views.CellView;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.tgc.appledora.habitica.moodChart.MoodActivity.SELECTED_DAY_POSITION_KEY;


public class TodayActivity extends AppCompatActivity {
    private static final String IS_RESET_COLOR_BUTTON_VISIBLE_KEY = "reset_color_button_key";
    private static final String SELECTED_FIRST_COLOR_KEY = "first_color_key";
    private static final String SELECTED_SECOND_COLOR_KEY = "second_color_key";
    private static final String MOOD_ID_KEY = "mood_id_key";
    private static final int DEFAULT_VALUE = -1;
    private int mMoodId = DEFAULT_VALUE;
    private int mPosition = DEFAULT_VALUE;

    @BindView(R.id.selected_mood_color_view)
    CellView mSelectedColorView;
    @BindView(R.id.date_text_view)
    TextView mDateView;
    @BindView(R.id.mood_1_label)
    TextView mMood1LabelTextView;
    @BindView(R.id.mood_2_label)
    TextView mMood2LabelTextView;
    @BindView(R.id.mood_3_label)
    TextView mMood3LabelTextView;
    @BindView(R.id.mood_4_label)
    TextView mMood4LabelTextView;
    @BindView(R.id.mood_5_label)
    TextView mMood5LabelTextView;
    @BindView(R.id.mood_6_label)
    TextView mMood6LabelTextView;
    @BindView(R.id.mood_7_label)
    TextView mMood7LabelTextView;
    @BindView(R.id.mood_8_label)
    TextView mMood8LabelTextView;
    @BindView(R.id.mood_9_label)
    TextView mMood9LabelTextView;
    @BindView(R.id.mood_10_label)
    TextView mMood10LabelTextView;
    @BindView(R.id.mood_11_label)
    TextView mMood11LabelTextView;
    @BindView(R.id.mood_12_label)
    TextView mMood12LabelTextView;
    @BindView(R.id.reset_selected_color)
    ImageView mResetColorButton;

    @BindView(R.id.notes_label)
    TextView mNotesLabel;
    @BindView(R.id.notes_text_view)
    TextView mNotesTextView;
    @BindView(R.id.notes_edit_text)
    EditText mNotesEditText;
    @BindView(R.id.notes_button)
    Button mNotesButton;

    private AppDatabase mDb;
    private int mFirstColor = 0;
    private int mSecondColor = 0;
    private boolean isResetColorButtonVisible;
    private String mNotes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moodtoday);
        Toolbar toolbar = findViewById(R.id.today_toolbar);
        TextView mTitle = toolbar.findViewById(R.id.today_toolbar_title);
//        mTitle.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "Norican-Regular.ttf"));
        setSupportActionBar(toolbar);
        setTitle("");
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        ButterKnife.bind(this);

        if (savedInstanceState != null) {
            isResetColorButtonVisible = savedInstanceState.getBoolean(IS_RESET_COLOR_BUTTON_VISIBLE_KEY);
            mPosition = savedInstanceState.getInt(SELECTED_DAY_POSITION_KEY);
            mFirstColor = savedInstanceState.getInt(SELECTED_FIRST_COLOR_KEY);
            mSecondColor = savedInstanceState.getInt(SELECTED_SECOND_COLOR_KEY);
            mMoodId = savedInstanceState.getInt(MOOD_ID_KEY);
        }

        mDb = AppDatabase.getInstance(getApplicationContext());

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(SELECTED_DAY_POSITION_KEY)) {
            mPosition = intent.getIntExtra(SELECTED_DAY_POSITION_KEY, DEFAULT_VALUE);
        }

        if (mPosition != DEFAULT_VALUE && mMoodId == DEFAULT_VALUE) {
            AddDailyMoodViewModelFactory factory = new AddDailyMoodViewModelFactory(
                    mDb,
                    mPosition,
                    SpecialUtils.getCurrentYear());
            final AddDailyMoodViewModel viewModel = ViewModelProviders.of(this, factory).get(AddDailyMoodViewModel.class);
            viewModel.getDailyMoodDetails().observe(this, mood -> {
                if (mood != null) {
                    mMoodId = mood.getId();
                    mFirstColor = mood.getFirstColor();
                    mSecondColor = mood.getSecondColor();

                    if (mFirstColor != 0) {
                        setFirstMoodColor(mSelectedColorView, SpecialUtils.getColor(getApplicationContext(), mFirstColor));
                        mResetColorButton.setVisibility(View.VISIBLE);
                        isResetColorButtonVisible = true;
                        if (mSecondColor != 0) {
                            setSecondMoodColor(mSelectedColorView, SpecialUtils.getColor(getApplicationContext(), mSecondColor));
                        } else {
                            setSecondMoodColor(mSelectedColorView, Color.TRANSPARENT);
                        }
                    }

                    // Set colors for date text view and clear button, based on mFirstColor and mSecondColor
                    setViewColor(mDateView, mFirstColor);
                    if (mSecondColor == 0) {
                        setViewColor(mResetColorButton, mFirstColor);
                    } else {
                        setViewColor(mResetColorButton, mSecondColor);
                    }

                    mNotes = mood.getNotes();
                    setNotes(mNotes);
                }
            });
        }

        populateUI();

        // Set colors for mSelectedColorView background and triangle, based on mFirstColor and mSecondColor
        setFirstMoodColor(mSelectedColorView, SpecialUtils.getColor(getApplicationContext(), mFirstColor));
        if (mSecondColor != 0) {
            setSecondMoodColor(mSelectedColorView, SpecialUtils.getColor(getApplicationContext(), mSecondColor));
        } else {
            setSecondMoodColor(mSelectedColorView, Color.TRANSPARENT);
        }

        // Set a listener for reset button
        mResetColorButton.setOnClickListener(view -> {
            setFirstMoodColor(mSelectedColorView, Color.WHITE);
            setSecondMoodColor(mSelectedColorView, Color.TRANSPARENT);
            mFirstColor = 0;
            mSecondColor = 0;
            mDateView.setTextColor(Color.BLACK);
            mResetColorButton.setColorFilter(Color.BLACK);
            mResetColorButton.setVisibility(View.GONE);
            isResetColorButtonVisible = false;
        });
    }

    // Set a color to a selected view
    private void setViewColor(View view, int color) {
        // If color was set
        if (color != 0) {
            // If color is dark, set ResetColorButton white
            if (SpecialUtils.isDarkColor(SpecialUtils.getColor(getApplicationContext(), color))) {
                if (view instanceof ImageView) {
                    ((ImageView) view).setColorFilter(Color.WHITE);
                } else {
                    ((TextView) view).setTextColor(Color.WHITE);
                }
            } else {
                // Otherwise, set it black
                if (view instanceof ImageView) {
                    ((ImageView) view).setColorFilter(Color.BLACK);
                } else {
                    ((TextView) view).setTextColor(Color.BLACK);
                }
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(IS_RESET_COLOR_BUTTON_VISIBLE_KEY, isResetColorButtonVisible);
        outState.putInt(SELECTED_DAY_POSITION_KEY, mPosition);
        outState.putInt(SELECTED_FIRST_COLOR_KEY, mFirstColor);
        outState.putInt(SELECTED_SECOND_COLOR_KEY, mSecondColor);
        outState.putInt(MOOD_ID_KEY, mMoodId);
    }

    // Set default background colors, labels and click listeners for each mood option
    private void populateUI() {
        int day = mPosition / 13;
        int month = mPosition % 13;

        // TODO: see enumerations/plurals in android for st, nd and th
        // TODO: solve this case for other languages
        String dayString = String.valueOf(day);
        switch (day) {
            case 1:
                dayString = dayString.concat("st");
                break;
            case 2:
                dayString = dayString.concat("nd");
                break;
            case 3:
                dayString = dayString.concat("rd");
                break;
            case 21:
                dayString = dayString.concat("st");
                break;
            case 22:
                dayString = dayString.concat("nd");
                break;
            case 23:
                dayString = dayString.concat("rd");
                break;
            case 31:
                dayString = dayString.concat("st");
                break;
            default:
                dayString = dayString.concat("th");
                break;
        }

        // Set DateView text
        mDateView.setText(String.format(
                getString(R.string.format_date),
                SpecialUtils.getMonthName(this, month),
                dayString,
                Preferences.getSelectedYear(this)));

        // Set colors for DateView and ResetColorButton views, based on mFirstColor and mSecondColor
        setViewColor(mDateView, mFirstColor);
        setViewColor(mResetColorButton, mSecondColor);

        // Set ResetColor's visibility
        if (isResetColorButtonVisible) {
            mResetColorButton.setVisibility(View.VISIBLE);
        } else {
            mResetColorButton.setVisibility(View.GONE);
        }

        // Set font type for each mood option
        setTypefaceFont(mMood1LabelTextView);
        setTypefaceFont(mMood2LabelTextView);
        setTypefaceFont(mMood3LabelTextView);
        setTypefaceFont(mMood4LabelTextView);
        setTypefaceFont(mMood5LabelTextView);
        setTypefaceFont(mMood6LabelTextView);
        setTypefaceFont(mMood7LabelTextView);
        setTypefaceFont(mMood8LabelTextView);
        setTypefaceFont(mMood9LabelTextView);
        setTypefaceFont(mMood10LabelTextView);
        setTypefaceFont(mMood11LabelTextView);
        setTypefaceFont(mMood12LabelTextView);

        // Set font type for Notes label
        setTypefaceFont(mNotesLabel);

        // Set value and color for each mood option
        setMoodOption(
                (ConstraintLayout) findViewById(R.id.mood_1_layout),
                findViewById(R.id.mood_1),
                mMood1LabelTextView,
                Preferences.getMoodLabel(this, getString(R.string.pref_mood_1_label_key), getString(R.string.label_mood_1)),
                Preferences.getMoodColor(this, getString(R.string.pref_mood_1_color_key), ContextCompat.getColor(this, R.color.mood_color_1)),
                1);
        setMoodOption(
                (ConstraintLayout) findViewById(R.id.mood_2_layout),
                findViewById(R.id.mood_2),
                mMood2LabelTextView,
                Preferences.getMoodLabel(this, getString(R.string.pref_mood_2_label_key), getString(R.string.label_mood_2)),
                Preferences.getMoodColor(this, getString(R.string.pref_mood_2_color_key), ContextCompat.getColor(this, R.color.mood_color_2)),
                2);
        setMoodOption(
                (ConstraintLayout) findViewById(R.id.mood_3_layout),
                findViewById(R.id.mood_3),
                mMood3LabelTextView,
                Preferences.getMoodLabel(this, getString(R.string.pref_mood_3_label_key), getString(R.string.label_mood_3)),
                Preferences.getMoodColor(this, getString(R.string.pref_mood_3_color_key), ContextCompat.getColor(this, R.color.mood_color_3)),
                3);
        setMoodOption(
                (ConstraintLayout) findViewById(R.id.mood_4_layout),
                findViewById(R.id.mood_4),
                mMood4LabelTextView,
                Preferences.getMoodLabel(this, getString(R.string.pref_mood_4_label_key), getString(R.string.label_mood_4)),
                Preferences.getMoodColor(this, getString(R.string.pref_mood_4_color_key), ContextCompat.getColor(this, R.color.mood_color_4)),
                4);
        setMoodOption(
                (ConstraintLayout) findViewById(R.id.mood_5_layout),
                findViewById(R.id.mood_5),
                mMood5LabelTextView,
                Preferences.getMoodLabel(this, getString(R.string.pref_mood_5_label_key), getString(R.string.label_mood_5)),
                Preferences.getMoodColor(this, getString(R.string.pref_mood_5_color_key), ContextCompat.getColor(this, R.color.mood_color_5)),
                5);
        setMoodOption(
                (ConstraintLayout) findViewById(R.id.mood_6_layout),
                findViewById(R.id.mood_6),
                mMood6LabelTextView,
                Preferences.getMoodLabel(this, getString(R.string.pref_mood_6_label_key), getString(R.string.label_mood_6)),
                Preferences.getMoodColor(this, getString(R.string.pref_mood_6_color_key), ContextCompat.getColor(this, R.color.mood_color_6)),
                6);

        setMoodOption(
                (ConstraintLayout) findViewById(R.id.mood_7_layout),
                findViewById(R.id.mood_7),
                mMood7LabelTextView,
                Preferences.getMoodLabel(this, getString(R.string.pref_mood_7_label_key), getString(R.string.label_mood_7)),
                Preferences.getMoodColor(this, getString(R.string.pref_mood_7_color_key), ContextCompat.getColor(this, R.color.mood_color_7)),
                7);
        setMoodOption(
                (ConstraintLayout) findViewById(R.id.mood_8_layout),
                findViewById(R.id.mood_8),
                mMood8LabelTextView,
                Preferences.getMoodLabel(this, getString(R.string.pref_mood_8_label_key), getString(R.string.label_mood_8)),
                Preferences.getMoodColor(this, getString(R.string.pref_mood_8_color_key), ContextCompat.getColor(this, R.color.mood_color_8)),
                8);
        setMoodOption(
                (ConstraintLayout) findViewById(R.id.mood_9_layout),
                findViewById(R.id.mood_9),
                mMood9LabelTextView,
                Preferences.getMoodLabel(this, getString(R.string.pref_mood_9_label_key), getString(R.string.label_mood_9)),
                Preferences.getMoodColor(this, getString(R.string.pref_mood_9_color_key), ContextCompat.getColor(this, R.color.mood_color_9)),
                9);
        setMoodOption(
                (ConstraintLayout) findViewById(R.id.mood_10_layout),
                findViewById(R.id.mood_10),
                mMood10LabelTextView,
                Preferences.getMoodLabel(this, getString(R.string.pref_mood_10_label_key), getString(R.string.label_mood_10)),
                Preferences.getMoodColor(this, getString(R.string.pref_mood_10_color_key), ContextCompat.getColor(this, R.color.mood_color_10)),
                10);
        setMoodOption(
                (ConstraintLayout) findViewById(R.id.mood_11_layout),
                findViewById(R.id.mood_11),
                mMood11LabelTextView,
                Preferences.getMoodLabel(this, getString(R.string.pref_mood_11_label_key), getString(R.string.label_mood_11)),
                Preferences.getMoodColor(this, getString(R.string.pref_mood_11_color_key), ContextCompat.getColor(this, R.color.mood_color_11)),
                11);
        setMoodOption(
                (ConstraintLayout) findViewById(R.id.mood_12_layout),
                findViewById(R.id.mood_12),
                mMood12LabelTextView,
                Preferences.getMoodLabel(this, getString(R.string.pref_mood_12_label_key), getString(R.string.label_mood_12)),
                Preferences.getMoodColor(this, getString(R.string.pref_mood_12_color_key), ContextCompat.getColor(this, R.color.mood_color_12)),
                12);

        setNotes(mNotes);
    }

    private void setNotes(String notes) {
        if (!TextUtils.isEmpty(notes)) {
            mNotesTextView.setText(notes);
            mNotesTextView.setVisibility(View.VISIBLE);
            mNotesButton.setText(getString(R.string.edit_notes));
        } else {
            mNotesTextView.setVisibility(View.GONE);
            mNotesButton.setText(getString(R.string.add_notes));
        }
        mNotesEditText.setVisibility(View.GONE);
    }

    public void writeNote(View view) {
        if (mNotesTextView.getVisibility() == View.GONE && mNotesEditText.getVisibility() == View.GONE) {
            mNotesEditText.setVisibility(View.VISIBLE);
            mNotesButton.setText(getString(R.string.save_notes));
        } else if (mNotesTextView.getVisibility() == View.GONE && mNotesEditText.getVisibility() == View.VISIBLE) {
            mNotes = mNotesEditText.getText().toString();
            setNotes(mNotes);
        } else if (mNotesTextView.getVisibility() == View.VISIBLE && mNotesEditText.getVisibility() == View.GONE) {
            mNotesEditText.setText(mNotesTextView.getText());
            mNotesEditText.setSelection(mNotesTextView.getText().length());
            mNotesButton.setText(getString(R.string.save_notes));
            mNotesTextView.setVisibility(View.GONE);
            mNotesEditText.setVisibility(View.VISIBLE);
        }
    }

    private void setTypefaceFont(TextView view) {
//        view.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "Norican-Regular.ttf"));
    }

    private void setMoodOption(ConstraintLayout layout, View view, TextView textView, String label, int color, int moodId) {
        setMoodListeners(layout, color, moodId);
        setMoodColor(view, color);
        setMoodLabel(textView, label);
    }

    private void setMoodListeners(ConstraintLayout layout, final int color, final int moodId) {
        // Listener for the first color
        layout.setOnClickListener(view -> {
            setFirstMoodColor(mSelectedColorView, color);
            mFirstColor = moodId;

            // Set ResetColor image as visible and isResetColorButtonVisible as true
            mResetColorButton.setVisibility(View.VISIBLE);
            isResetColorButtonVisible = true;

            // Set DateView and ResetColorButton colors, based on mFirstColor and mSecondColor
            setViewColor(mDateView, mFirstColor);
            if (mSecondColor == 0) {
                setViewColor(mResetColorButton, mFirstColor);
            }
        });

        // Listener for the second color
        layout.setOnLongClickListener(v -> {
            setSecondMoodColor(mSelectedColorView, color);
            mSecondColor = moodId;

            // Set ResetColorButton color
            setViewColor(mResetColorButton, mSecondColor);

            // Set ResetColorButton image as visible and isResetColorButtonVisible as true
            mResetColorButton.setVisibility(View.VISIBLE);
            isResetColorButtonVisible = true;

            return true;
        });
    }

    private void setMoodColor(View view, int color) {
        GradientDrawable one = (GradientDrawable) view.getBackground().mutate();
        one.setColor(color);
    }

    private void setFirstMoodColor(CellView view, int color) {
        view.setBackgroundColor(color);
    }

    private void setSecondMoodColor(CellView view, int color) {
        view.setTriangleColor(color);
    }

    private void setMoodLabel(TextView textView, String label) {
        textView.setText(label);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // This adds menu items to the app bar
        getMenuInflater().inflate(R.menu.menu_today_mood, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Save" menu option
            case R.id.action_save:
                onSaveButtonClicked();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void onSaveButtonClicked() {
        double moodValue = 0;
        if (mFirstColor != 0) {
            moodValue = 1;
            if (mSecondColor != 0) {
                if (mFirstColor == mSecondColor) {
                    mSecondColor = 0;
                } else {
                    moodValue = 0.5;
                }
            }
        }

        final Mood mood = new Mood(
                SpecialUtils.getCurrentYear(),
                mPosition % 13,
                mPosition,
                mFirstColor,
                moodValue,
                mSecondColor,
                mNotes);

        AppExecutors.getInstance().diskIO().execute(() -> {
            if (mMoodId == DEFAULT_VALUE) {
                // Insert new contact
                mDb.moodsDao().insertMood(mood);
            } else {
                // Update contact
                mood.setId(mMoodId);
                mDb.moodsDao().updateMood(mood);
            }

            finish();
        });
    }
}
