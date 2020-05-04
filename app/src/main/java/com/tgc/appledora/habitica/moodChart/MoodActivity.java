package com.tgc.appledora.habitica.moodChart;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.maseno.franklinesable.lifereminder.R;
import com.tgc.appledora.habitica.MainActivity;
import com.tgc.appledora.habitica.moodChart.fragments.GraphsFragment;
import com.tgc.appledora.habitica.moodChart.fragments.SettingsFragment;
import com.tgc.appledora.habitica.moodChart.fragments.YearFragment;


public class MoodActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static final String SELECTED_DAY_POSITION_KEY = "day_position_key";
    private static final String NAV_ID_KEY = "nav_id_key";
    private TextView mTitle;
    private int mNavId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mood);
        Toolbar toolbar = findViewById(R.id.toolbar);
        mTitle = toolbar.findViewById(R.id.toolbar_title);
        // mTitle.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "Norican-Regular.ttf"));
        setSupportActionBar(toolbar);
        setTitle("");

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        if (savedInstanceState != null && savedInstanceState.containsKey(NAV_ID_KEY)) {
            mNavId = savedInstanceState.getInt(NAV_ID_KEY, 0);
        }

        switch (mNavId) {
            case 0:
                YearFragment newFragment = new YearFragment();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, newFragment)
                        .commit();
                mTitle.setText(getText(R.string.action_pixels));
                break;
            case 1:
                GraphsFragment graphsFragment = new GraphsFragment();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, graphsFragment)
                        .commit();
                mTitle.setText(getText(R.string.action_chart));
                break;

            case 2:
                SettingsFragment settingsFragment = new SettingsFragment();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, settingsFragment)
                        .commit();
                mTitle.setText(getText(R.string.action_settings));
                break;

            case 3:
                Log.i("Mood", "Going Home");
                Intent intent = new Intent(MoodActivity.this, MainActivity.class);
                startActivity(intent);
                break;
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(NAV_ID_KEY, mNavId);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_year_in_pixels) {
            if (mNavId != 0) {
                YearFragment newFragment = new YearFragment();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, newFragment)
                        .commit();
                mTitle.setText(getText(R.string.action_pixels));
                mNavId = 0;
            }
        } else if (id == R.id.nav_graphs) {
            if (mNavId != 1) {
                GraphsFragment newFragment = new GraphsFragment();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, newFragment)
                        .commit();
                mTitle.setText(getText(R.string.action_chart));
                mNavId = 1;
            }
        } else if (id == R.id.nav_settings) {
            if (mNavId != 3) {
                SettingsFragment newFragment = new SettingsFragment();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, newFragment)
                        .commit();
                mTitle.setText(getText(R.string.action_settings));
                mNavId = 3;
            }
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
