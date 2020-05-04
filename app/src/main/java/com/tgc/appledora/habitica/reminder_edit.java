package com.tgc.appledora.habitica;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.CalendarContract;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.DecelerateInterpolator;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.maseno.franklinesable.lifereminder.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

public class reminder_edit extends AppCompatActivity {

    private static final int ARG_DATE_DIALOG = 32;
    private static final int ARG_TIME_DIALOG = 43;
    // private static final int ARG_ENTER_LOCATION = 69;
    private Calendar mCalendar = Calendar.getInstance();
    private Button timeButton, dateButton, locationButton;
    private EditText title_Edit, body_Edit;
    private app_database dbHandler;
    public static final String TIME_FORMAT_WORDS = "hh:mm a";
    public static final String DATE_TIME_FORMAT = "MM/dd/yy HH:mm:ss";
    public static final String DATE_TIME_FORMAT_WORDS = "cccc, MMMM dd, yyyy hh:mm a";
    //private static final int RETRIEVE_LOCATION_DATA = 73900518;
    private Long mRowId;
    private Context context;
    private Typeface apple_Font;
    private Toolbar toolbar;
    private final int color_button_IDs[] = {R.id.purple_button, R.id.green_button, R.id.blue_button, R.id.yellow_button, R.id.brown_button, R.id.red_button, R.id.orange_button};
    private final int color_buttons[] = {R.color.purple_button, R.color.green_button, R.color.blue_button,
            R.color.yellow_button, R.color.brown_button, R.color.red_button, R.color.orange_button};
    private ImageView color_icon[] = new ImageView[color_button_IDs.length];
    private int reminder_color;
    private Random randomPick = new Random();
    private int picked_Color_Dot;
    private boolean addToFavourite = false;
    private boolean setTimeWithAi = true;
    private TextView priorityHeader, chooseRepeatMode, repeatHeader;
    private int prioritySet = 0;
    private int mSelectedItem = 0;
    private static final int REQUEST_TAKE_PHOTO = 31;
    private ImageView defaultImg;
    private Uri mCurrentPhotoPath = null;
    private Animator mCurrentAnimator;
    private int mShortAnimationDuration;
    private static final int SELECT_PHOTO = 12;
    private LinearLayout dockingMenu;
    private ImageView expandedImageView;
    private static final int imageArray[] = {R.drawable.default_save_icon, R.drawable.instagram, R.drawable.slidea, R.drawable.slidec, R.drawable.slided};
    private int imageSwitchPosition = 1;
    private String realImagePath = "null";
    private File imageFromPickedGallery;
    private boolean defaultDrawable = true, assertVisibility = false;
    private int picTint;
    private ImageView switchImageButton;
    private AnimationDrawable animationDrawable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder_edit);

        context = this.getApplicationContext();
        applyDefaultSettings();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);


        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setSubtitle(R.string.create_Info);
        setSupportActionBar(toolbar);

        apple_Font = Typeface.createFromAsset(getAssets(), getString(R.string.appleFont));

        expandedImageView = (ImageView) findViewById(R.id.expanded_image);
        timeButton = (Button) findViewById(R.id.time_Button);
        dateButton = (Button) findViewById(R.id.date_Button);

        title_Edit = (EditText) findViewById(R.id.description_Edit);
        body_Edit = (EditText) findViewById(R.id.body_Edit);
        Spinner prioritySpinner = (Spinner) findViewById(R.id.prioritySpinner);
        priorityHeader = (TextView) findViewById(R.id.priorityHeader);
        repeatHeader = (TextView) findViewById(R.id.chooseRepeatMode);
        chooseRepeatMode = (TextView) findViewById(R.id.textRepeat);


        for (int i = 0; i < color_button_IDs.length; i++) {

            color_icon[i] = (ImageView) findViewById(color_button_IDs[i]);
        }


        // Retrieve and cache the system's default "short" animation time.
        mShortAnimationDuration = getResources().getInteger(
                android.R.integer.config_shortAnimTime);

        timeButton.setTypeface(apple_Font);
        dateButton.setTypeface(apple_Font);
        title_Edit.setTypeface(apple_Font);
        body_Edit.setTypeface(apple_Font);
        priorityHeader.setTypeface(apple_Font);
        repeatHeader.setTypeface(apple_Font);
        setupActionBar();

        dbHandler = new app_database(context);
        mRowId = savedInstanceState != null ? savedInstanceState.getLong(app_database.tb_Struct.KEY_ROW_ID) : null;

        picked_Color_Dot = randomPick.nextInt(color_button_IDs.length);
        reminder_color = color_buttons[picked_Color_Dot];

        final String[] priorityTag = {"Low  !", "Medium !!", "High  !!!"};
        ArrayAdapter<CharSequence> spinnerPriorities = new ArrayAdapter<CharSequence>(this, android.R.layout.simple_spinner_dropdown_item, priorityTag);
        prioritySpinner.setAdapter(spinnerPriorities);
        prioritySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                switch (position) {
                    case 0:
                        priorityHeader.setCompoundDrawablesWithIntrinsicBounds(R.drawable.flag_normal, 0, 0, 0);
                        break;
                    case 1:
                        priorityHeader.setCompoundDrawablesWithIntrinsicBounds(R.drawable.flag_medium, 0, 0, 0);
                        break;
                    case 2:
                        priorityHeader.setCompoundDrawablesWithIntrinsicBounds(R.drawable.flag_high, 0, 0, 0);
                        break;
                }
                prioritySet = position;

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {/*ignore*/}
        });
    }

    public void setRepeatMode(View v) {
        final String arrayRepeat[] = {"Never", "Every Day", "Every Week", "Every 2 Weeks", "Every Month", "Every Year"};
        final String selectedRepeat[] = {"Never", "Daily", "Weekly", "Biweekly", "Monthly", "Yearly"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Repeat")
                .setSingleChoiceItems(arrayRepeat, mSelectedItem, (dialog, which) -> {

                    dialog.dismiss();
                    chooseRepeatMode.setText(selectedRepeat[which]);
                    mSelectedItem = which;
                }).setCancelable(false).setIcon(R.drawable.recurring_appointment_filled_50px);
        builder.create().show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_reminder_edit, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int itemId = item.getItemId();
        View focusView;

        if (itemId == R.id.action_save) {

            String entered_title = title_Edit.getText().toString();

            if (TextUtils.isEmpty(entered_title)) {
                title_Edit.setError("Enter at least a description!");
                focusView = title_Edit;
                focusView.requestFocus();

            } else {

                saveState();
            }

        } else if (itemId == R.id.action_favourites) {

            if (!addToFavourite) {
                item.setIcon(R.drawable.favoriteon);
                Toast.makeText(this, "Will be added to favourites", Toast.LENGTH_SHORT).show();
                addToFavourite = true;
            } else {

                item.setIcon(R.drawable.favoriteoff);
                addToFavourite = false;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    public void action_SetDateTime(View v) {

        int itemId = v.getId();

        if (itemId == R.id.date_Button) {
            showDialog(ARG_DATE_DIALOG);
        } else if (itemId == R.id.time_Button) {
            showDialog(ARG_TIME_DIALOG);
        }
    }

    @Override
    protected Dialog onCreateDialog(int id) {

        if (id == ARG_DATE_DIALOG) {

            return datePickerDialog();
        } else if (id == ARG_TIME_DIALOG) {

            return timePickerDialog();
        }
        return super.onCreateDialog(id);
    }

    private DatePickerDialog datePickerDialog() {

        setTimeWithAi = false;
        return new DatePickerDialog(this, (view, year, monthOfYear, dayOfMonth) -> {
            mCalendar.set(Calendar.YEAR, year);
            mCalendar.set(Calendar.MONTH, monthOfYear);
            mCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateDateButton();
        }, mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH), mCalendar.get(Calendar.DAY_OF_MONTH));
    }

    public TimePickerDialog timePickerDialog() {

        return new TimePickerDialog(this, (view, hourOfDay, minute) -> {
            mCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
            mCalendar.set(Calendar.MINUTE, minute);
            // mCalendar.set(Calendar.SECOND, 0);
            updateTimeButton();
        }, mCalendar.get(Calendar.HOUR_OF_DAY), mCalendar.get(Calendar.MINUTE), false);
    }


    public void updateDateButton() {

        SimpleDateFormat dateFormat = new SimpleDateFormat("LLL-dd-yy", Locale.US);
        dateButton.setText(dateFormat.format(mCalendar.getTime()));
    }

    public void updateTimeButton() {

        SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm:ss a", Locale.US);
        timeButton.setText(timeFormat.format(mCalendar.getTime()));
    }

    private void saveState() {

        final String titleEd, bodyEd;

        titleEd = title_Edit.getText().toString();
        if (body_Edit.getText().toString().length() < 1) {

            bodyEd = " ";
        } else {
            bodyEd = body_Edit.getText().toString();
        }
        SimpleDateFormat dateTimeFormat = new SimpleDateFormat(DATE_TIME_FORMAT, Locale.US);
        SimpleDateFormat dateTimeFormatWords = new SimpleDateFormat(DATE_TIME_FORMAT_WORDS, Locale.US);
        setDefaultTime(setTimeWithAi);
        final String reminderDateTime = dateTimeFormat.format(mCalendar.getTime());
        final String reminderDateTimeWords = dateTimeFormatWords.format(mCalendar.getTime());
        final String saveFavourite = Boolean.toString(addToFavourite);

        final String hasExpired = "false";

        new Thread(() -> {
            dbHandler.open();
            new Handler(getMainLooper()).post(() -> new Thread(() -> {

                String imageToStore = String.valueOf(mCurrentPhotoPath);

                if (imageToStore == null) {
                    imageToStore = "null";
                } else if (realImagePath == null) {
                    realImagePath = "null";
                }

                if (mRowId == null) {
                    long id = dbHandler.insertDb(titleEd, bodyEd, getCurrentDateStore(), reminderDateTime, reminderDateTimeWords, getCurrentTime(), hasExpired, reminder_color, saveFavourite, prioritySet, mSelectedItem, imageToStore, realImagePath);
                    if (id > 0) {
                        mRowId = id;
                    }
                } else {
                    dbHandler.updateDb(mRowId, titleEd, bodyEd, getCurrentDateStore(), reminderDateTime, reminderDateTimeWords, getCurrentTime(), hasExpired, reminder_color, saveFavourite, prioritySet, mSelectedItem, imageToStore, realImagePath);
                }
                new Handler(getMainLooper()).post(() -> {

                    dbHandler.close();
                    new reminder_Manager(context).setReminderAlarm(mRowId, mCalendar, true);
                    setResult(RESULT_OK);
                    Toast.makeText(context, "saved", Toast.LENGTH_SHORT).show();
                    recordOnCalendar(titleEd, bodyEd, mCalendar);
                    finish();
                });
            }).start());
        }).start();
    }

    private String getCurrentDateStore() {

        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_TIME_FORMAT, Locale.US);
        return dateFormat.format(new Date());
    }

    private String getCurrentTime() {

        SimpleDateFormat dateFormat = new SimpleDateFormat(TIME_FORMAT_WORDS, Locale.US);
        return dateFormat.format(new Date());
    }

    private void setRowIdFromIntent() {

        if (mRowId == null) {
            Bundle extras = getIntent().getExtras();
            mRowId = extras != null ? extras.getLong(app_database.tb_Struct.KEY_ROW_ID) : null;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        // dbHandler.close();
    }

    @Override
    protected void onResume() {
        super.onResume();
        toolbar.setTitle("Create Reminder");
        applyDefaultColor();
        setRowIdFromIntent();
        populateFields();

    }

    public void populateFields() {

        if (mRowId != null) {

            toolbar.setTitle("Edit Reminder");
            toolbar.setSubtitle(R.string.edit_info);
            populateFieldsAsync async = new populateFieldsAsync();
            async.execute();
        }
        // spinningBackground();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        try {
            outState.putLong(app_database.tb_Struct.KEY_ROW_ID, mRowId);
        } catch (Exception ignored) {
        }
    }

    public void setContentsColor(View v) {

        ImageView img = (ImageView) v;

        for (int i = 0; i < color_button_IDs.length; i++) {

            color_icon[i].setImageResource(R.drawable.ic_color_button);
            if (v.getId() == color_button_IDs[i]) {

                body_Edit.setTextColor(getResources().getColor(color_buttons[i]));
                title_Edit.setTextColor(body_Edit.getTextColors());
                reminder_color = color_buttons[i];
            }
        }
        img.setImageResource(R.drawable.ic_color_button22);

    }

    public void applyDefaultColor() {

        ImageView imgColor = (ImageView) findViewById(color_button_IDs[picked_Color_Dot]);
        imgColor.setImageResource(R.drawable.ic_color_button22);
        body_Edit.setTextColor(getResources().getColor(reminder_color));
        title_Edit.setTextColor(body_Edit.getTextColors());
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem fav_Item = menu.findItem(R.id.action_favourites);

        if (addToFavourite) {
            fav_Item.setIcon(R.drawable.favoriteon);
        } else {

            fav_Item.setIcon(R.drawable.favoriteoff);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    public void setDefaultTime(boolean set) {

        if (set) {

            int currentDate = mCalendar.get(Calendar.DAY_OF_MONTH);
            int currentMonth = mCalendar.get(Calendar.MONTH);
            int daysTotalMonth = mCalendar.getActualMaximum(Calendar.DATE);
            int setDate = currentDate + 7;

            if (setDate > daysTotalMonth) {

                setDate = setDate - daysTotalMonth;
                currentMonth += 1;
                mCalendar.set(Calendar.DAY_OF_MONTH, setDate);
                mCalendar.set(Calendar.MONTH, currentMonth);
            } else {
                mCalendar.set(Calendar.DAY_OF_MONTH, setDate);

            }
        }
    }

    public void applyDefaultSettings() {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        SettingsFragment fragment = new SettingsFragment();
        fragment.setRetainInstance(true);//retain the fragment when it has been created
        fragmentTransaction.add(fragment, "settings_fragment");
        fragmentTransaction.commit();
    }

    private class populateFieldsAsync extends AsyncTask<Void, Cursor, Cursor> {
        @Override
        protected Cursor doInBackground(Void... params) {
            dbHandler.open();
            Cursor rem_Cursor = dbHandler.readDb(app_database.tb_Struct.KEY_ROW_ID + "=" + mRowId, null, null);
            startManagingCursor(rem_Cursor);
            return rem_Cursor;
        }

        @Override
        protected void onPostExecute(Cursor cursor) {

            title_Edit.setText(cursor.getString(cursor.getColumnIndexOrThrow(app_database.tb_Struct.KEY_TITLE)));
            body_Edit.setText(cursor.getString(cursor.getColumnIndexOrThrow(app_database.tb_Struct.KEY_BODY)));
            reminder_color = cursor.getInt(cursor.getColumnIndexOrThrow(app_database.tb_Struct.KEY_COLOR));
            dbHandler.close();

            addToFavourite = Boolean.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(app_database.tb_Struct.KEY_FAVOURITE)));

            title_Edit.setTextColor(getResources().getColor(reminder_color));
            body_Edit.setTextColor(title_Edit.getTextColors());

            SimpleDateFormat dateTimeFmt = new SimpleDateFormat(DATE_TIME_FORMAT, Locale.US);
            String dateString = cursor.getString(cursor.getColumnIndexOrThrow(app_database.tb_Struct.KEY_SCHEDULED_DATE));

            try {
                Date date = dateTimeFmt.parse(dateString);
                mCalendar.setTime(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            updateTimeButton();
            updateDateButton();

            for (int i = 0; i < color_buttons.length; i++) {

                ImageView colorImage = (ImageView) findViewById(color_button_IDs[i]);
                colorImage.setImageResource(R.drawable.ic_color_button);
                if (reminder_color == color_buttons[i]) {
                    colorImage = (ImageView) findViewById(color_button_IDs[i]);
                    colorImage.setImageResource(R.drawable.ic_color_button22);
                }
            }
            cursor.close();
        }
    }

    public void recordOnCalendar(String title, String body, final Calendar time) {

        Intent calendarIntent = new Intent(Intent.ACTION_INSERT, CalendarContract.Events.CONTENT_URI);
        int intermediate = time.get(Calendar.HOUR_OF_DAY);
        intermediate += 3;
        time.set(Calendar.HOUR_OF_DAY, intermediate);

        calendarIntent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, time.getTimeInMillis());
        calendarIntent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, time.getTimeInMillis());
        calendarIntent.putExtra(CalendarContract.Events.TITLE, title + ", : " + body);
    }

    private void setTheme() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        if (!sharedPreferences.getAll().isEmpty()) {
            int appTheme = Integer.parseInt(sharedPreferences.getString(preference_activity.KEY_THEME, ""));
            if (appTheme == 1) {
                setTheme(R.style.DarkTheme);
                picTint = Color.rgb(48, 48, 48);
                return;
            }
            picTint = Color.rgb(250, 250, 250);
        }

    }

    public void launchCamera(View v) {

        if (getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)) {
            dispatchTakePictureIntent();
        }
    }

    public void dispatchTakePictureIntent() {

        Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePicture.resolveActivity(getPackageManager()) != null) {

            File photoFile = null;
            try {
                photoFile = createImageFile();

            } catch (IOException e) {
                Toast.makeText(reminder_edit.this, "Error!", Toast.LENGTH_LONG).show();
            }

            if (photoFile != null) {
                takePicture.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(photoFile));
                startActivityForResult(takePicture, REQUEST_TAKE_PHOTO);
            }

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
            defaultDrawable = false;
            switchImageButton.setVisibility(View.INVISIBLE);
            try {
                decodeExpanded();
                decodeThumbnail();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        } else if (requestCode == SELECT_PHOTO && resultCode == RESULT_OK) {
            defaultDrawable = false;
            switchImageButton.setVisibility(View.INVISIBLE);
            //  mCurrentPhotoPath = ;
            try {
                realImagePath = RealPathURI.getRealPathFromURI_API19(this, data.getData());
            } catch (Exception e) {
                try {
                    realImagePath = RealPathURI.getRealPathFromURI_API11to18(this, data.getData());
                } catch (Exception e2) {
                    // SDK < API11
                    realImagePath = RealPathURI.getRealPathFromURI_BelowAPI11(this, data.getData());
                }
            } finally {
                try {
                    copyImageFile(new File(realImagePath), imageFromPickedGallery);
                    decodeExpanded();
                    decodeThumbnail();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    public File createImageFile() throws IOException {

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmSS", Locale.US).format(new Date());
        final String imageFileName = "JPEG_" + timeStamp + "_";
        File imageFile = null;

        File storageDir = Environment.getExternalStorageDirectory();
        if (storageDir.exists() && storageDir.canWrite()) {

            final File capturedFolder = new File(storageDir.getAbsolutePath() + "/Life Reminder/.Reminder Images/");

            if (!capturedFolder.exists()) {
                capturedFolder.mkdirs();
            }
            if (capturedFolder.exists() && capturedFolder.canWrite()) {

                imageFile = File.createTempFile(imageFileName, ".jpg", capturedFolder);
                mCurrentPhotoPath = Uri.fromFile(imageFile.getAbsoluteFile());
            }
        } else {
            Toast.makeText(context, R.string.no_sdcard, Toast.LENGTH_LONG).show();
        }
        return imageFile;
    }

    public void log(String log) {
        Log.i("meh", log);
    }


    private void zoomImageFromThumb(final View thumbView, Drawable imageResId) {
        // If there's an animation in progress, cancel it
        // immediately and proceed with this one.
        if (mCurrentAnimator != null) {
            mCurrentAnimator.cancel();
        }
        // Load the high-resolution "zoomed-in" image.
        expandedImageView.setImageDrawable(imageResId);

        // Calculate the starting and ending bounds for the zoomed-in image.
        // This step involves lots of math. Yay, math.
        final Rect startBounds = new Rect();
        final Rect finalBounds = new Rect();
        final Point globalOffset = new Point();

        // The start bounds are the global visible rectangle of the thumbnail,
        // and the final bounds are the global visible rectangle of the container
        // view. Also set the container view's offset as the origin for the
        // bounds, since that's the origin for the positioning animation
        // properties (X, Y).
        thumbView.getGlobalVisibleRect(startBounds);
        findViewById(R.id.container)
                .getGlobalVisibleRect(finalBounds, globalOffset);
        startBounds.offset(-globalOffset.x, -globalOffset.y);
        finalBounds.offset(-globalOffset.x, -globalOffset.y);

        // Adjust the start bounds to be the same aspect ratio as the final
        // bounds using the "center crop" technique. This prevents undesirable
        // stretching during the animation. Also calculate the start scaling
        // factor (the end scaling factor is always 1.0).
        float startScale;
        if ((float) finalBounds.width() / finalBounds.height()
                > (float) startBounds.width() / startBounds.height()) {
            // Extend start bounds horizontally
            startScale = (float) startBounds.height() / finalBounds.height();
            float startWidth = startScale * finalBounds.width();
            float deltaWidth = (startWidth - startBounds.width()) / 2;
            startBounds.left -= deltaWidth;
            startBounds.right += deltaWidth;
        } else {
            // Extend start bounds vertically
            startScale = (float) startBounds.width() / finalBounds.width();
            float startHeight = startScale * finalBounds.height();
            float deltaHeight = (startHeight - startBounds.height()) / 2;
            startBounds.top -= deltaHeight;
            startBounds.bottom += deltaHeight;
        }

        // Hide the thumbnail and show the zoomed-in view. When the animation
        // begins, it will position the zoomed-in view in the place of the
        // thumbnail.
        thumbView.setAlpha(0f);
        assertVisibility = true;
        expandedImageView.setVisibility(View.VISIBLE);
        dockingMenu.setVisibility(View.VISIBLE);
        dockingMenu.animate().alpha(0.7f).rotationXBy(720).setDuration(1000);

        // Set the pivot point for SCALE_X and SCALE_Y transformations
        // to the top-left corner of the zoomed-in view (the default
        // is the center of the view).
        expandedImageView.setPivotX(0f);
        expandedImageView.setPivotY(0f);

        // Construct and run the parallel animation of the four translation and
        // scale properties (X, Y, SCALE_X, and SCALE_Y).
        AnimatorSet set = new AnimatorSet();
        set
                .play(ObjectAnimator.ofFloat(expandedImageView, View.X,
                        startBounds.left, finalBounds.left))
                .with(ObjectAnimator.ofFloat(expandedImageView, View.Y,
                        startBounds.top, finalBounds.top))
                .with(ObjectAnimator.ofFloat(expandedImageView, View.SCALE_X,
                        startScale, 1f)).with(ObjectAnimator.ofFloat(expandedImageView,
                View.SCALE_Y, startScale, 1f));
        set.setDuration(mShortAnimationDuration);
        set.setInterpolator(new DecelerateInterpolator());
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mCurrentAnimator = null;
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                mCurrentAnimator = null;
            }
        });
        set.start();
        mCurrentAnimator = set;

        // Upon clicking the zoomed-in image, it should zoom back down
        // to the original bounds and show the thumbnail instead of
        // the expanded image.
        final float startScaleFinal = startScale;
        expandedImageView.setOnClickListener(view -> {
            if (mCurrentAnimator != null) {
                mCurrentAnimator.cancel();
            }

            // Animate the four positioning/sizing properties in parallel,
            // back to their original values.
            AnimatorSet set1 = new AnimatorSet();
            set1.play(ObjectAnimator
                    .ofFloat(expandedImageView, View.X, startBounds.left))
                    .with(ObjectAnimator
                            .ofFloat(expandedImageView,
                                    View.Y, startBounds.top))
                    .with(ObjectAnimator
                            .ofFloat(expandedImageView,
                                    View.SCALE_X, startScaleFinal))
                    .with(ObjectAnimator
                            .ofFloat(expandedImageView,
                                    View.SCALE_Y, startScaleFinal));
            set1.setDuration(mShortAnimationDuration);
            set1.setInterpolator(new DecelerateInterpolator());
            set1.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    thumbView.setAlpha(1f);
                    expandedImageView.setVisibility(View.GONE);
                    mCurrentAnimator = null;
                    dockingMenu.setVisibility(View.GONE);
                    assertVisibility = false;
                }

                @Override
                public void onAnimationCancel(Animator animation) {
                    thumbView.setAlpha(1f);
                    expandedImageView.setVisibility(View.GONE);
                    mCurrentAnimator = null;
                    dockingMenu.setVisibility(View.GONE);
                    assertVisibility = false;
                }
            });
            set1.start();
            mCurrentAnimator = set1;
        });
    }

    public void launchGallery(View v) {

        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        try {
            imageFromPickedGallery = createImageFile();
        } catch (IOException e) {
            Toast.makeText(reminder_edit.this, "Error!", Toast.LENGTH_LONG).show();

        }
        if (imageFromPickedGallery != null) {
            photoPickerIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(imageFromPickedGallery));
            photoPickerIntent.setType("image/*");
            photoPickerIntent.putExtra("return-data", true);
            startActivityForResult(Intent.createChooser(photoPickerIntent, "Import photo from"), SELECT_PHOTO);
        }
    }

    private void decodeThumbnail() throws FileNotFoundException {

        int targetW = defaultImg.getWidth();
        int targetH = defaultImg.getHeight();

        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(getContentResolver().openInputStream(mCurrentPhotoPath), null, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        int scaleFactor = Math.min(photoW / targetW, photoH / targetH);

        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(mCurrentPhotoPath), null, bmOptions);
        defaultImg.setImageBitmap(scaleCenterCrop(bitmap, targetW, targetH));
    }

    private void decodeExpanded() throws FileNotFoundException {

        int targetW = expandedImageView.getWidth();
        int targetH = expandedImageView.getHeight();

        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;

        BitmapFactory.decodeStream(getContentResolver().openInputStream(mCurrentPhotoPath), null, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        int scaleFactor = Math.min(photoW / targetW, photoH / targetH);

        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(mCurrentPhotoPath), null, bmOptions);
        expandedImageView.setImageBitmap(bitmap);
    }


    public void switchImage(View v) {

        defaultImg.setImageResource(imageArray[imageSwitchPosition]);
        imageSwitchPosition = (imageSwitchPosition + 1) % imageArray.length;
        resetDefaults();
    }

    public Bitmap scaleCenterCrop(Bitmap source, int newHeight, int newWidth) {
        int sourceWidth = source.getWidth();
        int sourceHeight = source.getHeight();

// Compute the scaling factors to fit the new height and width, respectively.
// To cover the final image, the final scaling will be the bigger
// of these two.
        float xScale = (float) newWidth / sourceWidth;
        float yScale = (float) newHeight / sourceHeight;
        float scale = Math.max(xScale, yScale);

// Now get the size of the source bitmap when scaled
        float scaledWidth = scale * sourceWidth;
        float scaledHeight = scale * sourceHeight;

// Let's find out the upper left coordinates if the scaled bitmap
// should be centered in the new size give by the parameters
        float left = (newWidth - scaledWidth) / 2;
        float top = (newHeight - scaledHeight) / 2;

// The target rectangle for the new, scaled version of the source bitmap will now
// be
        RectF targetRect = new RectF(left, top, left + scaledWidth, top + scaledHeight);

// Finally, we create a new bitmap of the specified size and draw our new,
// scaled bitmap onto it.
        Bitmap dest = Bitmap.createBitmap(newWidth, newHeight, source.getConfig());
        Canvas canvas = new Canvas(dest);
        canvas.drawBitmap(source, null, targetRect, null);

        return dest;
    }

    private void copyImageFile(File sourceFile, File destFile) throws IOException {


        if (!sourceFile.exists()) {
            return;
        }
        FileChannel source = null;
        FileChannel destination = null;
        source = new FileInputStream(sourceFile).getChannel();
        destination = new FileOutputStream(destFile).getChannel();

        if (destination != null && source != null) {
            destination.transferFrom(source, 0, source.size());
        }
        if (source != null) {
            source.close();
        }
        if (destination != null) {
            destination.close();
        }
    }

    public void remove_Image(View v) {
        switchImage(v);
    }

    public boolean resetDefaults() {
        defaultDrawable = true;
        switchImageButton.setVisibility(View.VISIBLE);
        if (assertVisibility) {
            expandedImageView.setImageDrawable(animationDrawable);
            animationDrawable.start();
        }
        return true;
    }
}