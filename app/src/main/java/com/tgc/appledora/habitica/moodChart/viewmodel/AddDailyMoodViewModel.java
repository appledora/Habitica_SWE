package com.tgc.appledora.habitica.moodChart.viewmodel;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.tgc.appledora.habitica.moodChart.data.AppDatabase;
import com.tgc.appledora.habitica.moodChart.data.Mood;

public class AddDailyMoodViewModel extends ViewModel {
    private LiveData<Mood> dailyMood;

    public AddDailyMoodViewModel(AppDatabase database, int position, int year) {
        dailyMood = database.moodsDao().loadMoodDetails(position, year);
    }

    public LiveData<Mood> getDailyMoodDetails() {
        return dailyMood;
    }
}
