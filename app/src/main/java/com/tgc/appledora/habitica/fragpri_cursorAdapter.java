package com.tgc.appledora.habitica;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.maseno.franklinesable.lifereminder.R;

import java.io.FileNotFoundException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * This class is responsible for maintaining the list reminders and has enhanced performance
 */
public class fragpri_cursorAdapter extends CursorAdapter {

    private Activity context;
    private int colorView;
    private String defaultTimeDisplay;
    private Calendar mCalendar = Calendar.getInstance();
    private Uri mCurrentPhotoPath;
    private Date startDate, endDate;
    private Animation timeAnim;
    private ViewHolder viewHolder;
    private Bitmap listImage;
    private static int drawables[] = {R.drawable.flag_normal, R.drawable.flag_medium, R.drawable.flag_high};
    public final Typeface apple_Font;
    private Boolean animDir = true, processImage = true;
    int track = 0, tt = 0;

    public static class ViewHolder {

        public final TextView titleView, dateView, timeView;
        public ImageView thumb1View;

        public ViewHolder(View view) {

            titleView = (TextView) view.findViewById(R.id.text1);
            dateView = (TextView) view.findViewById(R.id.text2);
            timeView = (TextView) view.findViewById(R.id.timeField);
            // thumb1View = (ImageView) view.findViewById(R.id.thumb_button_1);
        }
    }

    public fragpri_cursorAdapter(Activity context, Cursor c, int flags) {
        super(context, c, flags);
        this.context = context;
        apple_Font = Typeface.createFromAsset(context.getAssets(), context.getString(R.string.appleFont));
    }


    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        View view = LayoutInflater.from(context).inflate(R.layout.custom_layout3, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        view.setTag(viewHolder);
        return view;
    }

    @Override
    public void bindView(final View view, final Context context, final Cursor cursor) {

        viewHolder = (ViewHolder) view.getTag();
        Log.i("fragA", "View =>" + view.toString() + "\n");
        String title = cursor.getString(cursor.getColumnIndexOrThrow(app_database.tb_Struct.KEY_TITLE));
        String date = cursor.getString(cursor.getColumnIndexOrThrow(app_database.tb_Struct.KEY_SCHEDULED_DATE));
        String imagePath = cursor.getString(cursor.getColumnIndexOrThrow(app_database.tb_Struct.KEY_IMAGE_URI));
        int flag = cursor.getInt(cursor.getColumnIndexOrThrow(app_database.tb_Struct.KEY_PRIORITY));
        colorView = cursor.getInt(cursor.getColumnIndexOrThrow(app_database.tb_Struct.KEY_COLOR));
        Log.i("fragA", "ColorView =>" + colorView + "\n");

        SimpleDateFormat timeFmt = new SimpleDateFormat("MM/dd/yy HH:mm:ss", Locale.US);

        try {
            endDate = timeFmt.parse(date);
            mCalendar.setTime(endDate);
            startDate = new Date();

        } catch (ParseException e) {/*ignore error silently*/}

        String[] dateOnly = date.split(context.getString(R.string.empty));
        date = dateOnly[0];

        SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a", Locale.US);

        defaultTimeDisplay = timeFormat.format(mCalendar.getTime());
        viewHolder.timeView.setText(defaultTimeDisplay);
        viewHolder.titleView.setTextColor(context.getResources().getColor(colorView));
        viewHolder.dateView.setTextColor(context.getResources().getColor(colorView));
        viewHolder.timeView.setTypeface(apple_Font);
        viewHolder.titleView.setTypeface(apple_Font);
        viewHolder.dateView.setTypeface(apple_Font);

        viewHolder.dateView.setCompoundDrawablesWithIntrinsicBounds(0, 0, drawables[flag], 0);
        viewHolder.titleView.setText(title);
        viewHolder.dateView.setText(date);

        mCurrentPhotoPath = Uri.parse(imagePath);

        if (!mCurrentPhotoPath.toString().equals("null")) {
            loadImageInTheBackground loadImg = new loadImageInTheBackground();
            loadImg.execute();

        }
        viewHolder.thumb1View = (ImageView) view.findViewById(R.id.thumb_button_1);

      /*  viewHolder.thumb1View.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final TextView timeView = (TextView) view.findViewById(R.id.timeField);
                v.animate().rotationYBy(1800).setDuration(1000).setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);

                        timeObserver(startDate, endDate, timeView);

                    }
                });
            }
        });*/
    }

    private void timeObserver(Date startDate, Date endDate, final TextView timeRem) {

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

        // Log.i("meh", +elapsedDays + " days, " + elapsedHours + " hours, " + elapsedMinutes + " minutes, " + elapsedSeconds + " seconds\n\n");

        timeAnim = AnimationUtils.loadAnimation(context, R.anim.time_view_anim);
        timeRem.startAnimation(timeAnim);

        timeAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

                if (!animDir) {
                    animation.setFillAfter(true);
                    animDir = true;
                    timeRem.setAlpha(0.5f);
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

                if (animDir) {
                    timeRem.setTextColor(context.getResources().getColor(R.color.colorAccent));
                    timeRem.setText("Time Remaining: " + elapsedDays + "days, " + elapsedHours + "hrs, " + elapsedMinutes + "min, " + elapsedSeconds + "sec");

                    new Thread(() -> {

                        SystemClock.sleep(3000);

                        new Handler(Looper.getMainLooper()).post(() -> {

                            timeRem.setTextColor(context.getResources().getColor(colorView));
                            animDir = false;
                            timeRem.startAnimation(timeAnim);


                        });
                    }).start();
                } else {
                    timeRem.setTextColor(context.getResources().getColor(android.R.color.black));
                    timeRem.setText(defaultTimeDisplay);

                }

            }
        });
    }

    private class loadImageInTheBackground extends AsyncTask<Bitmap, Bitmap, Bitmap> {

        @Override
        protected Bitmap doInBackground(Bitmap... params) {

            try {
                listImage = decodeThumbnail();
            } catch (FileNotFoundException e) {
                listImage = null;//if sdcard has been removed or the images are not available
            }

            return listImage;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);

            if (bitmap != null) {
                viewHolder.thumb1View.setImageBitmap(bitmap);
            } else {
                if (processImage) {
                    Toast.makeText(context, "Some Images may not display correctly, because they are missing", Toast.LENGTH_SHORT).show();
                    processImage = false;
                }
                viewHolder.thumb1View.setImageResource(R.drawable.side_nav_bar);
            }
        }

        private Bitmap decodeThumbnail() throws FileNotFoundException {

            int targetW = 48;
            int targetH = 48;

            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            bmOptions.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(context.getContentResolver().openInputStream(mCurrentPhotoPath), null, bmOptions);
            int photoW = bmOptions.outWidth;
            int photoH = bmOptions.outHeight;

            int scaleFactor = Math.min(photoW / targetW, photoH / targetH);

            bmOptions.inJustDecodeBounds = false;
            bmOptions.inSampleSize = scaleFactor;
            bmOptions.inPurgeable = true;

            Bitmap bitmap = BitmapFactory.decodeStream(context.getContentResolver().openInputStream(mCurrentPhotoPath), null, bmOptions);
            listImage = scaleCenterCrop(bitmap, targetW, targetH);
            return listImage;
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

    }

}
