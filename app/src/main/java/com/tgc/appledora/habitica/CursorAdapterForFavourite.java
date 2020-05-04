package com.tgc.appledora.habitica;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.maseno.franklinesable.lifereminder.R;

public class CursorAdapterForFavourite extends CursorAdapter {

    private final LayoutInflater cursorInflater;
    private Handler mHandler = new Handler();
    public static int colorView;


    static class ViewHolder {

        private TextView scheduledDate, currentState, title, body;
        private Typeface apple_Font;

    }

    public CursorAdapterForFavourite(Activity context, Cursor c, int flags) {
        super(context, c, flags);
        cursorInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        return cursorInflater.inflate(R.layout.favourite_layout_lists, parent, false);

    }

    @Override
    public void bindView(final View view, final Context context, final Cursor cursor) {

        final ViewHolder viewHolder = new ViewHolder();

        viewHolder.scheduledDate = (TextView) view.findViewById(R.id.text3);
        viewHolder.currentState = (TextView) view.findViewById(R.id.text5);
        viewHolder.title = (TextView) view.findViewById(R.id.text4);
        viewHolder.body = (TextView) view.findViewById(R.id.text6);

        viewHolder.apple_Font = Typeface.createFromAsset(context.getAssets(), "fonts/Helvetica Neue Light (Open Type).ttf");

        String title = cursor.getString(cursor.getColumnIndexOrThrow(app_database.tb_Struct.KEY_TITLE));
        String body = cursor.getString(cursor.getColumnIndexOrThrow(app_database.tb_Struct.KEY_BODY));
        String date = cursor.getString(cursor.getColumnIndexOrThrow(app_database.tb_Struct.KEY_SCHEDULED_DATE_IN_WORDS));

        colorView = cursor.getInt(cursor.getColumnIndexOrThrow(app_database.tb_Struct.KEY_COLOR));

        viewHolder.body.setTextColor(context.getResources().getColor(colorView));

        viewHolder.scheduledDate.setTypeface(viewHolder.apple_Font);
        viewHolder.currentState.setTypeface(viewHolder.apple_Font);
        viewHolder.title.setTypeface(viewHolder.apple_Font);
        viewHolder.body.setTypeface(viewHolder.apple_Font);

        viewHolder.title.setText(title);
        viewHolder.body.setText(body);
        viewHolder.scheduledDate.setText(date);

        String reminderState = cursor.getString(cursor.getColumnIndexOrThrow(app_database.tb_Struct.KEY_EXPIRED));

        if (reminderState.equalsIgnoreCase("false")) {
            viewHolder.currentState.setText(R.string.pending);
            viewHolder.currentState.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.pending_icon, 0);

        } else {
            viewHolder.currentState.setText(R.string.complete);
            viewHolder.currentState.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.completed_icon, 0);
        }

    }
}


