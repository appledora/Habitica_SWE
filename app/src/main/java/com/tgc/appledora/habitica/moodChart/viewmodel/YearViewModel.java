package com.tgc.appledora.habitica.moodChart.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.tgc.appledora.habitica.moodChart.data.AppDatabase;
import com.tgc.appledora.habitica.moodChart.data.Mood;
import com.tgc.appledora.habitica.moodChart.utils.Preferences;

import java.util.List;


public class YearViewModel extends AndroidViewModel {

    private final LiveData<List<Mood>> allYearMoods;

    public YearViewModel(@NonNull Application application) {
        super(application);
        AppDatabase appDatabase = AppDatabase.getInstance(this.getApplication());
        allYearMoods = appDatabase.moodsDao().loadAllMoodsOfThisYear(Preferences.getSelectedYear(getApplication()));
    }

    public LiveData<List<Mood>> getAllYearMoods() {
        return allYearMoods;
    }
}
