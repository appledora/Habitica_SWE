package com.tgc.appledora.habitica.sleepCycle.tabs.activealarms;

import com.tgc.appledora.habitica.sleepCycle.data.AlarmDAO;
import com.tgc.appledora.habitica.sleepCycle.data.pojo.Alarm;

public class AlarmsPresenter implements AlarmsContract.AlarmsPresenter {

    private AlarmsContract.AlarmsView view;
    private AlarmDAO alarmDAO;

    public static AlarmsPresenter getService() {
        return AlarmsFragment.getAlarmsPresenter();
    }

    @Override
    public void bindView(AlarmsContract.AlarmsView view) {
        this.view = view;
        alarmDAO = new AlarmDAO();
    }

    @Override
    public void unbindView() {
        this.view = null;
        alarmDAO.cleanUp();
    }

    @Override
    public void handleRealmChange() {
        if (view != null) {
            view.setupAdapter();
        }
    }

    @Override
    public void showEditDialog(Alarm alarm) {
        if (hasView()) {
            view.showEditAlarmDialog(alarm);
        }
    }

    @Override
    public void deleteAlarmById(final String id) {
        alarmDAO.removeFromRealmById(id);
    }

    private boolean hasView() {
        return view != null;
    }
}
