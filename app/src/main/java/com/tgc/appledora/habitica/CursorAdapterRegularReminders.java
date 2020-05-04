package com.tgc.appledora.habitica;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.maseno.franklinesable.lifereminder.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * This class is responsible for maintaining the list reminders
 */
public class CursorAdapterRegularReminders extends CursorAdapter {

    private final LayoutInflater cursorInflater;
    private Handler mHandler = new Handler();
    public static int colorView;
    private Calendar mCalendar = Calendar.getInstance();
    private boolean proceeedWithAction = false;
    private Date startDate, endDate;
    String dateCreated;
    private Context context;

    static class ViewHolder {

        private TextView dateDue, desc, det, repeatStatus;
        private Typeface apple_Font;
        private ImageView thumbIcon, infoIcon;

    }

    public CursorAdapterRegularReminders(Activity context, Cursor c, int flags) {
        super(context, c, flags);
        cursorInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        return cursorInflater.inflate(R.layout.custom_regular_rem_fragment, parent, false);

    }

    @Override
    public void bindView(final View view, final Context context, final Cursor cursor) {

        this.context = context;
        final ViewHolder viewHolder = new ViewHolder();

        viewHolder.dateDue = (TextView) view.findViewById(R.id.dueDate);
        viewHolder.desc = (TextView) view.findViewById(R.id.description);
        viewHolder.det = (TextView) view.findViewById(R.id.detail);
        viewHolder.repeatStatus = (TextView) view.findViewById(R.id.text5);
        viewHolder.thumbIcon = (ImageView) view.findViewById(R.id.thumb_button_1);
        viewHolder.infoIcon = (ImageView) view.findViewById(R.id.remInfoIcon);

        viewHolder.apple_Font = Typeface.createFromAsset(context.getAssets(), "fonts/Helvetica Neue Light (Open Type).ttf");

        /*Animation repeatAnim= AnimationUtils.loadAnimation(context, R.anim.rotate);
        viewHolder.repeatStatus.startAnimation(repeatAnim);*/

        String title = cursor.getString(cursor.getColumnIndexOrThrow(app_database.tb_Struct.KEY_TITLE));
        String body = cursor.getString(cursor.getColumnIndexOrThrow(app_database.tb_Struct.KEY_BODY));
        String date = cursor.getString(cursor.getColumnIndexOrThrow(app_database.tb_Struct.KEY_SCHEDULED_DATE_IN_WORDS));
        colorView = cursor.getInt(cursor.getColumnIndexOrThrow(app_database.tb_Struct.KEY_COLOR));
        int repeatHow = cursor.getInt(cursor.getColumnIndexOrThrow(app_database.tb_Struct.KEY_REPEAT));

        final String selectedRepeat[] = {"Never", "Daily", "Weekly", "Biweekly", "Monthly", "Yearly"};

        body = body.replace(" ", "");
        if (TextUtils.isEmpty(body)) {
            viewHolder.infoIcon.setVisibility(View.GONE);
            viewHolder.det.setVisibility(View.GONE);
        } else {
            viewHolder.infoIcon.setColorFilter(context.getResources().getColor(colorView));
            viewHolder.det.setTypeface(viewHolder.apple_Font);
            viewHolder.det.setText(body);
        }


        viewHolder.det.setTextColor(context.getResources().getColor(colorView));

        viewHolder.dateDue.setTypeface(viewHolder.apple_Font);
        viewHolder.desc.setTypeface(viewHolder.apple_Font);
        viewHolder.repeatStatus.setTypeface(viewHolder.apple_Font);

        viewHolder.desc.setText(title);
        viewHolder.dateDue.setText(String.format("Due date: %s", date));
        viewHolder.repeatStatus.setText(String.format("Repeating every %s", selectedRepeat[repeatHow]));

        viewHolder.infoIcon.setOnClickListener(v -> {

            View parentView = (View) v.getParent();
            final ListView listView = (ListView) parentView.getParent();
            final int pos = listView.getPositionForView(parentView);
            final app_database dbHandler = new app_database(context);


            new Thread(() -> {
                dbHandler.open();
                final Cursor cursor2 = dbHandler.readDb(app_database.tb_Struct.KEY_ROW_ID + "=" + listView.getItemIdAtPosition(pos), null, null);

                new Handler(context.getMainLooper()).post(() -> {
                    Date date1 = null;
                    Calendar calendar = Calendar.getInstance();
                    SimpleDateFormat dateFormat = new SimpleDateFormat(reminder_edit.DATE_TIME_FORMAT, Locale.US);
                    SimpleDateFormat dateFmt = new SimpleDateFormat("cccc, MMMM dd, yyyy", Locale.US);
                    SimpleDateFormat timeFmt = new SimpleDateFormat("hh:mm a", Locale.US);

                    try {
                        date1 = dateFormat.parse(cursor2.getString(cursor2.getColumnIndexOrThrow(app_database.tb_Struct.KEY_CREATED_DATE)));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    if (date1 != null) {
                        calendar.setTime(date1);
                    }
                    dateCreated = dateFmt.format(calendar.getTime()) + " at " + timeFmt.format(calendar.getTime());

                    AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.MaterialDialogStyle);
                    builder.setCancelable(true).setTitle("Date Created").setPositiveButton("okay", null).setMessage("This reminder was created on\n" + dateCreated)
                            .setIcon(R.drawable.time_52px).create().show();
                });
            }).start();

        });

        viewHolder.thumbIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                v.animate().rotationXBy(1800).setDuration(1000).setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);


                    }
                });
            }
        });

        viewHolder.repeatStatus.setOnClickListener(v -> {

            View parentView = (View) v.getParent();
            final ListView listView = (ListView) parentView.getParent();
            final int pos = listView.getPositionForView(parentView);
            final app_database dbHandler = new app_database(context);


            new Thread(() -> {
                dbHandler.open();
                final Cursor cursor3 = dbHandler.readDb(app_database.tb_Struct.KEY_ROW_ID + "=" + listView.getItemIdAtPosition(pos), null, null);

                new Handler(context.getMainLooper()).post(() -> {

                    String date4 = cursor3.getString(cursor3.getColumnIndexOrThrow(app_database.tb_Struct.KEY_SCHEDULED_DATE));
                    SimpleDateFormat timeFmt = new SimpleDateFormat("MM/dd/yy HH:mm:ss", Locale.US);
                    try {
                        endDate = timeFmt.parse(date4);
                        mCalendar.setTime(endDate);
                        startDate = new Date();
                        timeObserver(startDate, endDate);

                    } catch (ParseException e) {/*ignore error silently*/}

                });
            }).start();

        });

    }

    private void timeObserver(Date startDate, Date endDate) {

        long difference = endDate.getTime() - startDate.getTime();
        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;

        final long elapsedDays = difference / daysInMilli;
        difference = difference % daysInMilli;

        final long elapsedHours = difference / hoursInMilli;
        difference = difference % hoursInMilli;

        final long elapsedMinutes = difference / minutesInMilli;
        difference = difference % minutesInMilli;

        final long elapsedSeconds = difference / secondsInMilli;

        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.MaterialDialogStyle);
        builder.setCancelable(true).setTitle("Due Reminder Time").setPositiveButton("okay", null).setMessage("Time Remaining: " + elapsedDays + "days, " + elapsedHours + "hrs, " + elapsedMinutes + "min, " + elapsedSeconds + "sec")
                .setIcon(R.drawable.time_span_64px).create().show();
    }
}


