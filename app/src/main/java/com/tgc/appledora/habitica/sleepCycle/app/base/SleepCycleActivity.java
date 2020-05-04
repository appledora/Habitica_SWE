package com.tgc.appledora.habitica.sleepCycle.app.base;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.maseno.franklinesable.lifereminder.R;
import com.tgc.appledora.habitica.MainActivity;
import com.tgc.appledora.habitica.sleepCycle.app.RealmManager;
import com.tgc.appledora.habitica.sleepCycle.events.SetHourButtonClickedEvent;
import com.tgc.appledora.habitica.sleepCycle.tabs.activealarms.AlarmsFragment;
import com.tgc.appledora.habitica.sleepCycle.tabs.addalarm.sleepnow.SleepNowFragment;
import com.tgc.appledora.habitica.sleepCycle.tabs.addalarm.wakeupat.WakeUpAtFragment;
import com.tgc.appledora.habitica.sleepCycle.tabs.ui.WakeUpAtSetHourButton;
import com.tgc.appledora.habitica.sleepCycle.utils.Const;
import com.tgc.appledora.habitica.sleepCycle.utils.ThemeUtils;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;

public class SleepCycleActivity extends AppCompatActivity
        implements
        MainContract.MainView,
        BottomNavigationView.OnNavigationItemSelectedListener,
        Toolbar.OnMenuItemClickListener {

    private FragmentManager fragmentManager;
    private MainPresenter mainPresenter;
    private WakeUpAtSetHourButton wakeUpAtSetHourButton;
    private Bundle savedInstanceState;
    private WakeUpAtFragment wakeUpAtFragment;
    private int lastBottomViewClickedId = -1;

    @BindView(R.id.toolbar)
    Toolbar appToolbar;

    @BindView(R.id.bottom_navigation_bar)
    BottomNavigationView bottomNavigationBar;

    @BindView(R.id.wakeUpAtFloatingActionButtonExtended)
    Button wakeUpAtButton;

    @OnClick(R.id.wakeUpAtFloatingActionButtonExtended)
    public void onWakeUpAtFloatingActionButtonExtendedClicked() {
        EventBus.getDefault().post(new SetHourButtonClickedEvent());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.savedInstanceState = savedInstanceState;
        setAppTheme();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_sleep);
        ButterKnife.bind(this);
        Realm.init(this);
        RealmManager.initializeRealmConfig();
        mainPresenter = new MainPresenter(this);
        wakeUpAtFragment = new WakeUpAtFragment();
        wakeUpAtSetHourButton = new WakeUpAtSetHourButton(wakeUpAtButton);
        fragmentManager = getSupportFragmentManager();
        mainPresenter.setUpUi(savedInstanceState);
    }

    private void setAppTheme() {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        String key = getString(R.string.key_change_theme);
        int themeId = ThemeUtils.getCurrentTheme(pref.getString(key, Const.DEFAULTS.THEME_ID));
        getDelegate().setLocalNightMode(themeId);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (!EventBus.getDefault().isRegistered(wakeUpAtFragment)) {
            EventBus.getDefault().register(wakeUpAtFragment);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(getString(R.string.key_last_execution_date), bottomNavigationBar
                .getSelectedItemId());
        super.onSaveInstanceState(outState);
    }

    @Override
    public void setUpBottomNavigationBar() {
        bottomNavigationBar.setOnNavigationItemSelectedListener(this);
    }

    @Override
    public void openLatestFragment() {
        final int latestFragmentId = savedInstanceState
                .getInt(getString(R.string.key_last_execution_date));
        bottomNavigationBar.setSelectedItemId(latestFragmentId);
    }

    @Override
    public void openDefaultFragment() {
        final int defaultFragmentId = R.id.action_sleepnow;
        bottomNavigationBar.setSelectedItemId(defaultFragmentId);
    }

    @Override
    public void setUpToolbar() {
        setSupportActionBar(appToolbar);
        setupActionbarTitle();
        appToolbar.setOnMenuItemClickListener(this);
    }

    private void setupActionbarTitle() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false);
        } else {
            Log.e(getClass().getName(), "setupActionbarTitle(): actionBar == null");
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int menuItemId = menuItem.getItemId();
        if (lastBottomViewClickedId != menuItemId) {
            mainPresenter.handleBottomNavigationTabClick(menuItemId);
        }
        lastBottomViewClickedId = menuItemId;
        return true;
    }

    @Override
    public void showWakeUpAtActionButton() {
        wakeUpAtSetHourButton.show();
    }

    @Override
    public void hideWakeUpAtActionButton() {
        wakeUpAtSetHourButton.hide();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.three_dot_menu, menu);
        return true;
    }

    @Override
    public void openSleepNowFragment() {
        replaceFragment(new SleepNowFragment());
    }

    @Override
    public void openWakeUpAtFragment() {
        replaceFragment(new WakeUpAtFragment());
    }

    @Override
    public void openAlarmsFragment() {
        replaceFragment(new AlarmsFragment());
    }

    public void replaceFragment(Fragment newFragment) {
        FragmentTransaction ft = this.fragmentManager.beginTransaction();
        ft.setCustomAnimations(R.anim.grow_fade_in_center, R.anim.fast_fade_out)
                .replace(R.id.main_activity_container, newFragment)
                .commit();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        int itemId = item.getItemId();
        mainPresenter.handleMenuItemClick(itemId);
        return true;
    }

    @Override
    public void openMainActivity() {
        startActivity(new Intent(getApplicationContext(), MainActivity.class));

    }

    @Override
    public void onBackPressed() {
        mainPresenter.handleBackPress();
    }

    @Override
    public void showToastWithDoubleBackMessage() {
        Toast.makeText(getApplicationContext(),
                getString(R.string.toast_double_back_to_exit), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void countDownInMilliseconds(int milliseconds) {
        new Handler().postDelayed(() -> mainPresenter.onCountedDown(), milliseconds);
    }

    @Override
    public void moveAppToBack() {
        moveTaskToBack(true);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (EventBus.getDefault().isRegistered(wakeUpAtFragment)) {
            EventBus.getDefault().unregister(wakeUpAtFragment);
        }
    }

    @Override
    public void onDestroy() {
        removeWakeUpAtPreferences();
        super.onDestroy();
    }

    @SuppressLint("ApplySharedPref")
    private void removeWakeUpAtPreferences() {
        SharedPreferences wakeUpAtPreferences
                = getSharedPreferences(getString(R.string.wakeupat_preferences_name), MODE_PRIVATE);
        wakeUpAtPreferences.edit().clear().commit();
    }
}
