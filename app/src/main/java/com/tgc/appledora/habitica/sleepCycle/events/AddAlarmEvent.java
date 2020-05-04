package com.tgc.appledora.habitica.sleepCycle.events;


import com.tgc.appledora.habitica.sleepCycle.data.pojo.Item;

public class AddAlarmEvent {

    private Item item;

    public AddAlarmEvent(Item item) {
        this.item = item;
    }

    public Item getItem() {
        return item;
    }
}
