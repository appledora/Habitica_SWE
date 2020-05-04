package com.tgc.appledora.habitica.sleepCycle.data;

import android.util.Log;

import com.tgc.appledora.habitica.sleepCycle.app.RealmManager;
import com.tgc.appledora.habitica.sleepCycle.data.pojo.Alarm;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmQuery;


public class AlarmDAO {

    public AlarmDAO() {
        RealmManager.incrementCount();
    }

    public void saveIfNotDuplicate(final Alarm alarm) {
        Realm realm = RealmManager.getRealm();
        realm.executeTransactionAsync(realm1 -> {
            if (isNotDuplicate(alarm, realm1)) {
                realm1.insertOrUpdate(alarm);
            }
        });
    }

    private boolean isNotDuplicate(Alarm alarm, Realm realm) {
        Alarm duplicateAlarm = realm.where(Alarm.class)
                .equalTo("executionDate", alarm.getExecutionDate()).findFirst();
        return duplicateAlarm == null;
    }

    public void saveEvenIfDuplicate(final Alarm alarm) {
        Realm realm = RealmManager.getRealm();
        realm.executeTransactionAsync(realm1 -> realm1.insertOrUpdate(alarm));
    }

    public void removeFromRealmById(final String id) {
        Log.d(getClass().getName(), "Removing from realm...");
        Realm realm = RealmManager.getRealm();

        realm.executeTransactionAsync(realm1 -> {
            Alarm alarm = realm1.where(Alarm.class).equalTo("id", id).findFirst();
            if (alarm != null) {
                alarm.deleteFromRealm();
            }
        });
    }

    public List<Alarm> getListOfAlarms() {
        Realm realm = RealmManager.getRealm();
        RealmQuery<Alarm> query = realm.where(Alarm.class);
        return query.findAll();
    }

    public void cleanUp() {
        RealmManager.decrementCount();
    }

}
