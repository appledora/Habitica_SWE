package com.tgc.appledora.habitica;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.maseno.franklinesable.lifereminder.R;
import com.tgc.appledora.habitica.moodChart.MoodActivity;
import com.tgc.appledora.habitica.sleepCycle.app.base.SleepCycleActivity;
import com.tgc.appledora.habitica.streakCounter.ui.task_list.TaskListActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Context context;
    private app_database dbHandler;
    private TabLayout tabLayout;
    private boolean exit = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_nav);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ViewPager mViewPager = (ViewPager) findViewById(R.id.mViewPager);
        setUpViewPager(mViewPager);

        tabLayout = (TabLayout) findViewById(R.id.mTabs);
        tabLayout.setupWithViewPager(mViewPager);

        applyDefaultSettings();

        context = this.getApplicationContext();
        dbHandler = new app_database(context);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        setTabIcons();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(v -> startActivity(new Intent(context, reminder_edit.class)));

        //Animation fabAnim= AnimationUtils.loadAnimation(context, R.anim.scale);


    }


    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.menu_reminder_lists, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        switch (id) {

            case R.id.action_settings:
                startActivity(new Intent(context, preference_activity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {

            if (exit) {
                super.onBackPressed();
            } else {
                exit = true;
                Toast.makeText(context, "click again to exit", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.nav_favourite) {
            startActivity(new Intent(context, favourite_reminders.class));
        } else if (id == R.id.nav_manage) {

            Intent intentPref = new Intent(context, preference_activity.class);
            startActivity(intentPref);

        } else if (id == R.id.nav_share) {

            ApplicationInfo app = context.getApplicationInfo();
            String filePath = app.sourceDir;

            Intent intent = new Intent(Intent.ACTION_SEND);


            intent.setType("*/*");

            intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File(filePath)));
            startActivity(Intent.createChooser(intent, "Share Life Reminder Apk Via"));

        } else if (id == R.id.nav_sleep) {
            Log.i("Sleep cycle", " CLicked button to sleep");
            Intent intent = new Intent(this, SleepCycleActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_streak) {
            Log.i("Streak activity", "Clicked button streak");
            Intent intent = new Intent(this, TaskListActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_mood) {
            Log.i("Streak activity", "Clicked button mood");
            Intent intent = new Intent(this, MoodActivity.class);
            startActivity(intent);

        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void applyDefaultSettings() {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        SettingsFragment fragment = new SettingsFragment();
        fragmentTransaction.add(fragment, "settings_fragment");
        fragmentTransaction.commit();
    }

    private void setUpViewPager(ViewPager mViewPager) {

        ViewPagerAdapter pagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        pagerAdapter.addFragment(new FragmentPrimary(), "");
        pagerAdapter.addFragment(new RegularReminder(), "");
        mViewPager.setAdapter(pagerAdapter);
    }

    private class ViewPagerAdapter extends FragmentPagerAdapter {
        List<Fragment> mFragmentList = new ArrayList<>();
        List<String> mFragmentNames = new ArrayList<>();

        public ViewPagerAdapter(androidx.fragment.app.FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentNames.get(position);
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentNames.add(title);

        }
    }

    public void setTabIcons() {

        tabLayout.getTabAt(0).setIcon(R.drawable.survey);
        tabLayout.getTabAt(1).setIcon(R.drawable.calendar);
    }

    private void setTheme() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        if (!sharedPreferences.getAll().isEmpty()) {
            int appTheme = Integer.parseInt(sharedPreferences.getString(preference_activity.KEY_THEME, ""));
            if (appTheme == 1)
                setTheme(R.style.DarkTheme);
        }
    }
}


