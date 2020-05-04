package com.tgc.appledora.habitica.sleepCycle.tabs.activealarms;

import androidx.fragment.app.Fragment;

import com.tgc.appledora.habitica.sleepCycle.app.RealmManager;


public class AlarmScopeListener extends Fragment {
    private AlarmsPresenter alarmsPresenter;

    public AlarmScopeListener() {
        setRetainInstance(true);
        RealmManager.incrementCount();
        alarmsPresenter = new AlarmsPresenter();
    }

    @Override
    public void onDestroy() {
        RealmManager.decrementCount();
        super.onDestroy();
    }

    public AlarmsPresenter getPresenter() {
        return alarmsPresenter;
    }
}
