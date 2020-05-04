package com.tgc.appledora.habitica;

import android.Manifest;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.media.MediaScannerConnection;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.ShareActionProvider;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.view.MenuItemCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.maseno.franklinesable.lifereminder.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class reminder_view extends AppCompatActivity {

    private Calendar mcalendar = Calendar.getInstance();
    private TextView title_View, body_View, scheduled_View, created_View;
    private app_database dbHandler;
    private Long mRowId = null;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder_view);
        context = getApplicationContext();
        applyDefaultSettings();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setSubtitle(R.string.view_info);
        setSupportActionBar(toolbar);

        Typeface apple_Font = Typeface.createFromAsset(getAssets(), getString(R.string.appleFont));

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(view -> {

            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                Snackbar.make(view, R.string.sd_permission_denied, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            } else {
                screenCaptureService(view);
            }

        });
        setupActionBar();

        title_View = (TextView) findViewById(R.id.title_View);
        body_View = (TextView) findViewById(R.id.body_View);
        scheduled_View = (TextView) findViewById(R.id.scheduledDateView);
        created_View = (TextView) findViewById(R.id.createDateView);

        title_View.setTypeface(apple_Font);
        body_View.setTypeface(apple_Font);
        scheduled_View.setTypeface(apple_Font);
        created_View.setTypeface(apple_Font);

        dbHandler = new app_database(this);
        mRowId = savedInstanceState != null ? savedInstanceState.getLong(app_database.tb_Struct.KEY_ROW_ID) : null;

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_reminder_view, menu);

        MenuItem shareItem = menu.findItem(R.id.action_share);
        final ShareActionProvider mShareActionProvider = (ShareActionProvider)
                MenuItemCompat.getActionProvider(shareItem);
        mShareActionProvider.setShareIntent(getDefaultIntent());


        return super.onCreateOptionsMenu(menu);
    }

    private Intent getDefaultIntent() {

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, title_View.getText().toString() + ", entailing " + body_View.getText().toString()
                + " ,is to take place on, " + scheduled_View.getText().toString());
        return intent;
    }

    private void setRowIdFromIntent() {

        if (mRowId == null) {
            Bundle extras = getIntent().getExtras();
            mRowId = extras != null ? extras.getLong(app_database.tb_Struct.KEY_ROW_ID) : null;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setRowIdFromIntent();
        populateFields();
    }

    public void populateFields() {

        if (mRowId != null) {

            populateAsyncTask asyncTask = new populateAsyncTask();
            asyncTask.execute();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        populateFields();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong(app_database.tb_Struct.KEY_ROW_ID, mRowId);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();


        switch (itemId) {

            case R.id.actionEdit:

                Intent intentCli = new Intent(context, reminder_edit.class);
                intentCli.putExtra(app_database.tb_Struct.KEY_ROW_ID, mRowId);
                startActivity(intentCli);

                break;
            case R.id.action_share:
                break;
            case R.id.actionDelete:

                new Thread(() -> {
                    dbHandler.open();
                    dbHandler.deleteDb(app_database.tb_Struct.KEY_ROW_ID + " = " + mRowId);
                    new reminder_Manager(context).setReminderAlarm(mRowId, null, false);
                    finish();
                    new Handler(getMainLooper()).post(() -> {
                        dbHandler.close();
                        finish();
                    });
                }).start();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    public void screenCaptureService(final View view) {
        Date now = new Date();
        // android.text.format.DateFormat.format("ccc-MMM-dd hh/mm/a_yyyy", now);
        String picName = getString(R.string.app_name) + now + ".png";
        picName = picName.replace(" ", "_");
        picName = picName.replace(":", "_");

        File sdCardDir = Environment.getExternalStorageDirectory();
        if (sdCardDir.exists() && sdCardDir.canWrite()) {

            File screenShotFolder = new File(sdCardDir.getAbsolutePath() + "/Life Reminder/.Screenshots/");

            if (!screenShotFolder.exists()) {
                screenShotFolder.mkdirs();
            }
            if (screenShotFolder.exists() && screenShotFolder.canWrite()) {

                final File imageFile = new File(screenShotFolder.getAbsolutePath() + "/" + picName);
                View currentView = getWindow().getDecorView().getRootView();
                currentView.setDrawingCacheEnabled(true);
                final Bitmap bitmap = Bitmap.createBitmap(currentView.getDrawingCache());
                currentView.setDrawingCacheEnabled(false);
                new Thread(() -> {
                    try {
                        imageFile.createNewFile();

                    } catch (IOException e) {
                        Snackbar.make(view, "Error!", Snackbar.LENGTH_LONG).setAction("Action", null).setAction("Action", null).show();
                    }
                    new Handler(getMainLooper()).post(() -> {

                        if (imageFile.exists() && imageFile.canWrite()) {

                            final FileOutputStream[] imageFileStream = {null};

                            new Thread(() -> {
                                try {
                                    imageFileStream[0] = new FileOutputStream(imageFile);
                                    int imageQuality = 100;
                                    bitmap.compress(Bitmap.CompressFormat.PNG, imageQuality, imageFileStream[0]);
                                } catch (IOException e) {
                                    //ignore error
                                } finally {

                                    new Handler(getMainLooper()).post(() -> {

                                        try {
                                            if (imageFileStream[0] != null) {
                                                imageFileStream[0].flush();
                                                imageFileStream[0].close();

                                                MediaScannerConnection.scanFile(context, new String[]{imageFile.toString()}, null, (path, uri) -> {

                                                    Intent sendImage = new Intent(Intent.ACTION_SEND);
                                                    sendImage.setDataAndType(uri, "image/*");
                                                    sendImage.putExtra(Intent.EXTRA_STREAM, uri);
                                                    Intent chooser = Intent.createChooser(sendImage, getString(R.string.share_capture));
                                                    startActivity(chooser);
                                                });
                                            }
                                        } catch (IOException e) {
                                            //ignore error
                                        }
                                    });
                                }
                            }).start();

                        } else {
                            Snackbar.make(view, R.string.error, Snackbar.LENGTH_LONG).setAction("Action", null).setAction("Action", null).show();
                        }
                    });
                }).start();

            }
        } else {
            Snackbar.make(view, R.string.no_sdcard, Snackbar.LENGTH_LONG).setAction("Action", null).show();
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

    private class populateAsyncTask extends AsyncTask<Void, Cursor, Cursor> {

        public populateAsyncTask() {
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Cursor doInBackground(Void... params) {
            dbHandler.open();

            Cursor rem_Cursor = dbHandler.readDb(app_database.tb_Struct.KEY_ROW_ID + "=" + mRowId, null, null);
            startManagingCursor(rem_Cursor);
            return rem_Cursor;

        }

        @Override
        protected void onPostExecute(Cursor cursor) {

            title_View.setText(cursor.getString(cursor.getColumnIndexOrThrow(app_database.tb_Struct.KEY_TITLE)));
            body_View.setText(cursor.getString(cursor.getColumnIndexOrThrow(app_database.tb_Struct.KEY_BODY)));
            title_View.setTextColor(getResources().getColor(cursor.getInt(cursor.getColumnIndexOrThrow(app_database.tb_Struct.KEY_COLOR))));

            body_View.setTextColor(title_View.getTextColors());
            scheduled_View.setTextColor(title_View.getTextColors());
            created_View.setTextColor(title_View.getTextColors());

            Date date = null;
            SimpleDateFormat dateFormat = new SimpleDateFormat(reminder_edit.DATE_TIME_FORMAT, Locale.US);
            String dateString = cursor.getString(cursor.getColumnIndexOrThrow(app_database.tb_Struct.KEY_SCHEDULED_DATE));
            try {
                date = dateFormat.parse(dateString);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if (date != null) {
                mcalendar.setTime(date);
            }
            date = null;

            SimpleDateFormat dateFmt = new SimpleDateFormat("cccc, MMMM dd, yyyy", Locale.US);
            SimpleDateFormat timeFmt = new SimpleDateFormat("hh:mm a", Locale.US);

            String dateCreatedText = "Date Scheduled: " + dateFmt.format(mcalendar.getTime()) + " at " + timeFmt.format(mcalendar.getTime());
            scheduled_View.setText(dateCreatedText);

            String dateString2 = cursor.getString(cursor.getColumnIndexOrThrow(app_database.tb_Struct.KEY_CREATED_DATE));
            try {
                date = dateFormat.parse(dateString2);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if (date != null) {
                mcalendar.setTime(date);
            }

            String dateScheduledText = "Date Created: " + dateFmt.format(mcalendar.getTime()) + " at " + timeFmt.format(mcalendar.getTime());
            created_View.setText(dateScheduledText);
        }
    }

    private void setTheme() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        if (!sharedPreferences.getAll().isEmpty()) {
            int appTheme = Integer.parseInt(sharedPreferences.getString(preference_activity.KEY_THEME, ""));
            if (appTheme == 1)
                setTheme(R.style.DarkTheme);
        }
    }
}
