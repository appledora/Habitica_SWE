package com.tgc.appledora.habitica.moodChart.viewmodel;


import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.tgc.appledora.habitica.moodChart.data.AppDatabase;


public class AddDailyMoodViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    private final AppDatabase mDb;
    private final int mPosition;
    private final int mYear;

    public AddDailyMoodViewModelFactory(AppDatabase database, int position, int year) {
        mDb = database;
        mPosition = position;
        mYear = year;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        //noinspection unchecked
        return (T) new AddDailyMoodViewModel(mDb, mPosition, mYear);
    }
}
