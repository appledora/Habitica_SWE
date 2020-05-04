package com.tgc.appledora.habitica;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.maseno.franklinesable.lifereminder.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class CursorAdapterCompletedReminders extends CursorAdapter {

    private final LayoutInflater cursorInflater;
    public static int colorView;

    static class ViewHolder {

        private TextView scheduledDate, currentTime, title;
        private Typeface apple_Font;
        private ImageView checkboxImage;

    }

    public CursorAdapterCompletedReminders(Context context, Cursor c, int flags) {
        super(context, c, flags);
        cursorInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return cursorInflater.inflate(R.layout.custom_defined_layout2, parent, false);
    }

    @Override
    public void bindView(final View view, final Context context, final Cursor cursor) {

        final ViewHolder viewHolder = new ViewHolder();
        viewHolder.scheduledDate = (TextView) view.findViewById(R.id.text3);
        viewHolder.currentTime = (TextView) view.findViewById(R.id.text5);
        viewHolder.title = (TextView) view.findViewById(R.id.text4);
        viewHolder.checkboxImage = (ImageView) view.findViewById(R.id.completedClick);

        viewHolder.apple_Font = Typeface.createFromAsset(context.getAssets(), "fonts/Helvetica Neue Light (Open Type).ttf");

        String title = cursor.getString(cursor.getColumnIndexOrThrow(app_database.tb_Struct.KEY_TITLE));
        String currentTime = cursor.getString(cursor.getColumnIndexOrThrow(app_database.tb_Struct.KEY_CURRENT_TIME));
        String date = cursor.getString(cursor.getColumnIndexOrThrow(app_database.tb_Struct.KEY_SCHEDULED_DATE_IN_WORDS));

        colorView = cursor.getInt(cursor.getColumnIndexOrThrow(app_database.tb_Struct.KEY_COLOR));

        viewHolder.scheduledDate.setTypeface(viewHolder.apple_Font);
        viewHolder.currentTime.setTypeface(viewHolder.apple_Font);
        viewHolder.title.setTypeface(viewHolder.apple_Font);

        viewHolder.title.setText(title);
        viewHolder.currentTime.setText(currentTime);
        viewHolder.scheduledDate.setText(date);

        viewHolder.checkboxImage.setOnClickListener(v -> {

            ImageView imageView = (ImageView) v;
            imageView.setImageResource(R.drawable.btn_check_on_disabled_focused_holo_dark);
            View parentView = (View) v.getParent();
            final ListView listView = (ListView) parentView.getParent();
            final int pos = listView.getPositionForView(parentView);
            final app_database dbHandler = new app_database(context);
            final Calendar mCalendar = Calendar.getInstance();

            new Thread(() -> {
                SystemClock.sleep(1500);
                dbHandler.open();
                new Handler(Looper.getMainLooper()).post(() -> {

                    final int currentDate = mCalendar.get(Calendar.DAY_OF_MONTH);
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
                    final SimpleDateFormat dateTimeFormat = new SimpleDateFormat("MM/dd/yy HH:mm:ss", Locale.US);

                    view.animate().translationXBy(1000).setDuration(1500).alpha(0f).setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            dbHandler.updateTimeOnlyDb(listView.getItemIdAtPosition(pos), dateTimeFormat.format(mCalendar.getTime()));
                            cursor.requery();
                            notifyDataSetChanged();
                        }

                    });

                });
            }).start();
        });
    }


}
