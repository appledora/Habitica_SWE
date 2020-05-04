package com.tgc.appledora.habitica.sleepCycle.tabs.activealarms;


import com.tgc.appledora.habitica.sleepCycle.data.pojo.Alarm;

public interface AlarmsContract {
    interface AlarmsView {

        void showEditAlarmDialog(Alarm alarm);

        void setupAdapter();

    }

    interface AlarmsPresenter {
        void bindView(AlarmsContract.AlarmsView viewContract);

        void unbindView();

        void handleRealmChange();

        void showEditDialog(Alarm alarm);

        void deleteAlarmById(final String id);

    }
}
