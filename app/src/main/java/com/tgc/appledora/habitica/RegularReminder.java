package com.tgc.appledora.habitica;


import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.fragment.app.ListFragment;
import androidx.viewpager.widget.ViewPager;

import com.maseno.franklinesable.lifereminder.R;

public class RegularReminder extends ListFragment {

    private ListView fav_ListView;
    public static boolean removeFav = false;
    private boolean contextualInAction = false;
    private ImageView contextA, contextB, contextC, contextD;
    private View myView;
    private String sortOrder = app_database.tb_Struct.KEY_TITLE + " ASC";
    private static final int ACTIVITY_CREATE = 69;
    private static final int ACTIVITY_EDIT = 32;
    private static boolean refreshArg = false;
    private Button view_reminderState;
    private app_database dbHandler;
    private TextView checkEmpty;
    private String searchQuery;
    private String searchArgs;
    private Context context;
    private Typeface roboto_Font, apple_Font;

    public RegularReminder() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = getActivity().getApplicationContext();
        dbHandler = new app_database(context);

        roboto_Font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Roboto Light (Open Type).ttf");
        apple_Font = Typeface.createFromAsset(getActivity().getAssets(), getString(R.string.appleFont));
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_fragment_b, container, false);
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        registerForContextMenu(getListView());
        TextView emptyView = (TextView) getListView().getEmptyView();
        refresh();

        ViewPager mViewPager = (ViewPager) getActivity().findViewById(R.id.mViewPager);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                refresh();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }


        });
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        if (!removeFav) {
            Intent intentCli = new Intent(context, reminder_edit.class);
            intentCli.putExtra(app_database.tb_Struct.KEY_ROW_ID, id);
            startActivityForResult(intentCli, 69);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fav_fragoptionmenu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        getActivity().getMenuInflater().inflate(R.menu.fav_contextual, menu);

    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        int itemId = item.getItemId();
        if (itemId == R.id.edit_ctx2) {

        } else if (itemId == R.id.share_ctx2) {

        } else if (itemId == R.id.fav_ctx2) {

            removeFav = true;
        }
        refresh();
        return true;
    }

    public void refresh() {
        asyncRefresh async = new asyncRefresh();
        async.execute();

    }

    private class asyncRefresh extends AsyncTask<Void, Cursor, Cursor> {
        @Override
        protected Cursor doInBackground(Void... params) {
            dbHandler.open();
            Cursor cursor = dbHandler.readDb(app_database.tb_Struct.KEY_REPEAT + " !=" + 0 + "", null, app_database.tb_Struct.KEY_TITLE + " ASC");
            getActivity().startManagingCursor(cursor);

            return cursor;
        }

        @Override
        protected void onPostExecute(Cursor cursor) {
            CursorAdapterRegularReminders adapter = new CursorAdapterRegularReminders(getActivity(), cursor, 0);
            setListAdapter(adapter);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        refresh();
    }

    @Override
    public void onPause() {
        super.onPause();
    }
}
