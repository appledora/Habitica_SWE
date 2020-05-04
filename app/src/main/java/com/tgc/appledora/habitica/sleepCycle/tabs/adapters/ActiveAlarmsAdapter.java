package com.tgc.appledora.habitica.sleepCycle.tabs.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.maseno.franklinesable.lifereminder.R;
import com.tgc.appledora.habitica.sleepCycle.data.pojo.Alarm;
import com.tgc.appledora.habitica.sleepCycle.schedule.AlarmController;
import com.tgc.appledora.habitica.sleepCycle.tabs.activealarms.AlarmsContract;
import com.tgc.appledora.habitica.sleepCycle.tabs.activealarms.AlarmsPresenter;
import com.tgc.appledora.habitica.sleepCycle.utils.AlarmContentUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.RealmRecyclerViewAdapter;
import io.realm.RealmResults;

public class ActiveAlarmsAdapter extends RealmRecyclerViewAdapter<Alarm,
        ActiveAlarmsAdapter.AlarmViewHolder> {

    public ActiveAlarmsAdapter(RealmResults<Alarm> alarms) {
        super(alarms, true);
    }

    @NonNull
    @Override
    public AlarmViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AlarmViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_alarm, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull AlarmViewHolder alarmViewHolder, final int position) {
        final Alarm alarm = getItem(position);
        if (alarm != null) {
            alarmViewHolder.bind(alarm);
        }
    }

    public class AlarmViewHolder extends RecyclerView.ViewHolder {

        private final AlarmsContract.AlarmsPresenter alarmsPresenter;
        private AlarmController alarmController;
        private Context context;

        @BindView(R.id.cl_item_alarm_root)
        ConstraintLayout clRoot;

        @BindView(R.id.iv_item_alarm_icon)
        ImageView ivIcon;

        @BindView(R.id.tv_item_alarm_title)
        TextView tvTitle;

        @BindView(R.id.tv_item_alarm_subtitle)
        TextView tvSubtitle;

        public AlarmViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            this.context = itemView.getContext();
            this.alarmsPresenter = AlarmsPresenter.getService();
            this.alarmController = new AlarmController(context);
        }

        public void bind(final Alarm alarm) {
            final String id = alarm.getId();
            ivIcon.setImageResource(R.drawable.ic_list_alarm_access_full_shape);
            String alarmExecutionDate = AlarmContentUtils.getTitle(alarm.getExecutionDate());
            tvTitle.setText(alarmExecutionDate);
            tvSubtitle.setText(alarm.getSummary());

            clRoot.setOnLongClickListener(v -> {
                alarmsPresenter.deleteAlarmById(id);
                alarmController.cancelAlarm(alarm);
                return false;
            });

            clRoot.setOnClickListener(v -> alarmsPresenter.showEditDialog(alarm));
        }
    }
}
