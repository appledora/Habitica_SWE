package com.tgc.appledora.habitica.sleepCycle.tabs.ui.dialog;


import com.tgc.appledora.habitica.sleepCycle.data.pojo.Alarm;
import com.tgc.appledora.habitica.sleepCycle.data.pojo.Item;

public interface ItemDialogContract {

    void bind(Item item);

    void bind(Alarm alarm);

    void setRingtone(String itemRingtone);

    String getRingtone();
}
