package com.tgc.appledora.habitica.sleepCycle.utils.itemsbuilder;

import com.tgc.appledora.habitica.sleepCycle.data.pojo.Item;

import org.joda.time.DateTime;

import java.util.List;

public class ItemsBuilder extends ItemsBuilderAbstraction {

    @Override
    public List<Item> getItems(DateTime currentDate, DateTime executionDate) {
        return getBuildingStrategy().getArrayOfItems(currentDate, executionDate);
    }

    @Override
    public DateTime getNextAlarmDate(DateTime executionDate) {
        return getBuildingStrategy().getNextAlarmDate(executionDate);
    }

    @Override
    public boolean isPossibleToCreateNextItem(DateTime currentDate, DateTime executionDate) {
        return getBuildingStrategy().isPossibleToCreateNextItem(currentDate, executionDate);
    }
}
