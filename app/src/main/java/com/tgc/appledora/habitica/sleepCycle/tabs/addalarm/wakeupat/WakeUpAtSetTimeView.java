package com.tgc.appledora.habitica.sleepCycle.tabs.addalarm.wakeupat;

import android.annotation.TargetApi;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TimePicker;

import androidx.annotation.Nullable;

import com.maseno.franklinesable.lifereminder.R;

import org.joda.time.DateTime;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WakeUpAtSetTimeView extends LinearLayout
        implements WakeUpAtContract.WakeUpAtView.DialogContract {

    @BindView(R.id.dateTimeSelect)
    protected TimePicker timePicker;

    private DateTime dateTime;

    public WakeUpAtSetTimeView(Context context) {
        super(context);
    }

    public WakeUpAtSetTimeView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public WakeUpAtSetTimeView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(21)
    public WakeUpAtSetTimeView(Context context, AttributeSet attrs,
                               int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.bind(this);

        timePicker.setIs24HourView(true);
        timePicker.setOnTimeChangedListener((timePicker, hourOfDay, minute) -> setDateTime(hourOfDay, minute));
    }

    private void setDateTime(int hourOfDay, int minute) {
        DateTime currentDate = DateTime.now();
        DateTime pickedDate = currentDate.withHourOfDay(hourOfDay)
                .withMinuteOfHour(minute);

        this.dateTime = currentDate.getHourOfDay() > hourOfDay
                ? pickedDate.plusDays(1)
                : pickedDate;
    }

    @Override
    public DateTime getDateTime() {
        if (dateTime == null) {
            dateTime = DateTime.now();
        }
        return dateTime;
    }
}
