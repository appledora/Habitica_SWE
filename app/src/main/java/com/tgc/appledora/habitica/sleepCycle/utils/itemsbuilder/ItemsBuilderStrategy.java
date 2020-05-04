package com.tgc.appledora.habitica.sleepCycle.utils.itemsbuilder;


import com.tgc.appledora.habitica.sleepCycle.data.pojo.Item;

import org.joda.time.DateTime;

import java.util.List;


public interface ItemsBuilderStrategy {
    List<Item> getArrayOfItems(DateTime currentDate, DateTime executionDate);

    DateTime getNextAlarmDate(DateTime executionDate);

    boolean isPossibleToCreateNextItem(DateTime currentDate, DateTime dateToGoToSleep);
}
