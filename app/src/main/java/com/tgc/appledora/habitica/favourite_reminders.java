package com.tgc.appledora.habitica;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.maseno.franklinesable.lifereminder.R;

public class favourite_reminders extends AppCompatActivity {

    private app_database dbHandler;
    private Context context;
    private ListView fav_ListView;
    public static boolean deleteFav = false;
    private boolean contextualInAction = false;
    private ImageView contextA, contextB, contextC, contextD;
    private View myView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_favourite_reminders);
        applyDefaultSettings();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        context = getApplicationContext();
        Typeface apple_Font = Typeface.createFromAsset(getAssets(), getString(R.string.appleFont));
        fav_ListView = (ListView) findViewById(R.id.fvourite_listView);
        TextView emptyView = (TextView) findViewById(R.id.emptyView);

        contextA = (ImageView) findViewById(R.id.imageA);
        contextB = (ImageView) findViewById(R.id.imageB);
        contextC = (ImageView) findViewById(R.id.imageC);
        contextD = (ImageView) findViewById(R.id.imageD);

        emptyView.setTypeface(apple_Font);
        fav_ListView.setEmptyView(emptyView);


        setUpActionBar();
        dbHandler = new app_database(context);
        refresh();

        fav_ListView.setOnItemClickListener((parent, view, position, id) -> {

            if (!contextualInAction) {

                if (contextA.getAlpha() == 1f) {
                    toggleContextVisibility(false);
                } else {
                    if (!deleteFav) {
                        Intent intentCli = new Intent(context, reminder_edit.class);
                        intentCli.putExtra(app_database.tb_Struct.KEY_ROW_ID, id);
                        startActivityForResult(intentCli, 69);
                    }
                    toggleContextVisibility(false);
                    refresh();

                }

            } else {
                contextualInAction = false;
                toggleContextVisibility(false);
            }

        });

        fav_ListView.setOnCreateContextMenuListener((menu, v, menuInfo) -> {
            myView = (View) v.getParent();
            TextView view1 = (TextView) v.findViewById(R.id.text6);
            TextView view2 = (TextView) v.findViewById(R.id.text4);
            view1.setTextColor(Color.BLUE);
            view2.setTextColor(Color.BLUE);

            RotateAnimation AnimationA = new RotateAnimation(0, -1080, Animation.ABSOLUTE, 20.5f, Animation.ABSOLUTE, 100f);
            AnimationA.setDuration(1000);
            AnimationA.setFillAfter(true);
            AnimationA.setRepeatCount(1);
            AnimationA.setRepeatMode(Animation.REVERSE);
            contextA.startAnimation(AnimationA);

            RotateAnimation AnimationB = new RotateAnimation(0, -1170, Animation.ABSOLUTE, -15.0f, Animation.ABSOLUTE, 45.0f);
            AnimationB.setFillAfter(true);
            AnimationB.setDuration(1000);
            AnimationB.setRepeatCount(1);
            AnimationB.setRepeatMode(Animation.REVERSE);
            contextB.startAnimation(AnimationB);

            RotateAnimation AnimationC = new RotateAnimation(0, -1260, Animation.ABSOLUTE, 40.0f, Animation.ABSOLUTE, 0.0f);
            AnimationC.setFillAfter(true);
            AnimationC.setDuration(1000);
            AnimationC.setRepeatCount(1);
            AnimationC.setRepeatMode(Animation.REVERSE);
            contextC.startAnimation(AnimationC);

            RotateAnimation AnimationD = new RotateAnimation(0, -1350, Animation.ABSOLUTE, 110.0f, Animation.ABSOLUTE, 25f);
            AnimationD.setFillAfter(true);
            AnimationD.setDuration(1000);
            AnimationD.setRepeatCount(1);
            AnimationD.setRepeatMode(Animation.REVERSE);

            contextD.startAnimation(AnimationD);

            AnimationA.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    toggleContextVisibility(true);
                }

                @Override
                public void onAnimationEnd(Animation animation) {

                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                    toggleContextVisibility(true);
                }
            });
        });
    }


    public void setUpActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    public void refresh() {
        asyncRefresh async = new asyncRefresh();
        async.execute();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.favourite_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.action_trash) {
            if (deleteFav) {

                deleteFav = false;
                item.setIcon(R.drawable.trash_off);
            } else {

                deleteFav = true;
                item.setIcon(R.drawable.trash_on);
            }
        }
        refresh();
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        super.onPause();
        deleteFav = false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        refresh();
    }

    public void applyDefaultSettings() {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        SettingsFragment fragment = new SettingsFragment();
        fragment.setRetainInstance(true);//retain the fragment when it has been created
        fragmentTransaction.add(fragment, "settings_fragment");
        fragmentTransaction.commit();
    }

    private class asyncRefresh extends AsyncTask<Void, Cursor, Cursor> {
        @Override
        protected Cursor doInBackground(Void... params) {
            dbHandler.open();
            Cursor cursor = dbHandler.readDb(app_database.tb_Struct.KEY_FAVOURITE + " =\"" + true + "\"", null, app_database.tb_Struct.KEY_TITLE + " ASC");
            startManagingCursor(cursor);

            return cursor;
        }

        @Override
        protected void onPostExecute(Cursor cursor) {
            CursorAdapterForFavourite adapter = new CursorAdapterForFavourite(favourite_reminders.this, cursor, 0);
            fav_ListView.setAdapter(adapter);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        refresh();
    }

    public void toggleContextVisibility(boolean vis) {

        if (vis) {

            contextA.setAlpha(1f);
            contextB.setAlpha(1f);
            contextC.setAlpha(1f);
            contextD.setAlpha(1f);
            contextA.setVisibility(View.VISIBLE);
            contextB.setVisibility(View.VISIBLE);
            contextC.setVisibility(View.VISIBLE);
            contextD.setVisibility(View.VISIBLE);
            contextualInAction = true;
        } else {
            contextA.setAlpha(0f);
            contextB.setAlpha(0f);
            contextC.setAlpha(0f);
            contextD.setAlpha(0f);
            contextA.setVisibility(View.INVISIBLE);
            contextB.setVisibility(View.INVISIBLE);
            contextC.setVisibility(View.INVISIBLE);
            contextD.setVisibility(View.INVISIBLE);
            contextualInAction = false;
        }
    }

    public void contextClickMe(View v) {

        final ListView listView = (ListView) myView.getParent();
        //final TextView dateView = (TextView) myView.findViewById(R.id.text2);jj
        final int pos = listView.getPositionForView(myView);

        int itemId = v.getId();

        switch (itemId) {

            case R.id.imageA:
                break;
            case R.id.imageB:
                break;
            case R.id.imageC:
                break;
            case R.id.imageD:
                break;
        }
    }
}
