package com.upskilled.vaperater;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.upskilled.vaperater.JuiceRaterDatabaseContract.JuiceInfoEntry;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;

import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class JuiceListActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    public static final String JUICE_POSITION = "com.upskilled.vaperater.JUICE_POSITION";
    private JuiceRaterOpenHelper mDbOpenHelper;
    private JuicesRecyclerAdapter mJuicesRecyclerAdapter;
    private String mSortOrder = JuiceInfoEntry.COLUMN_RATING + " DESC";


    private int set_sortByLabelSize;
    private Spinner mSpinnerFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        // Setting up layouts and views, defining click handler for fab
        setContentView(R.layout.activity_juice_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(JuiceListActivity.this, JuiceActivity.class));
            }
        });



        // Instantiate new instance of OpenHelper class
        mDbOpenHelper = new JuiceRaterOpenHelper(this);

        initializeDisplayContent();

        setFontSizeRecyclerView();
        setBrightness();

    }

    private void setBrightness() {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        Boolean switchPref = sharedPref.getBoolean(SettingsActivity.KEY_PREF_MAX_BRIGHTNESS_SWITCH, false);

        WindowManager.LayoutParams settings = getWindow().getAttributes();

        if (switchPref == true) {
            settings.screenBrightness = 1;
        } else {
            settings.screenBrightness = -1;
        }
        getWindow().setAttributes(settings);
    }


    private void initializeDisplayContent() {

        // Setting up recycler view with layout manager
        final RecyclerView recyclerJuices = (RecyclerView) findViewById(R.id.list_juices);
        final LinearLayoutManager juicesLayoutManager = new LinearLayoutManager(this);
        recyclerJuices.setLayoutManager(juicesLayoutManager);

        // Setting up recycler view with adapter
        mJuicesRecyclerAdapter = new JuicesRecyclerAdapter(this, null);
        recyclerJuices.setAdapter(mJuicesRecyclerAdapter);

        // Setting up spinner from resource file, setting adapter and listener for spinner
        mSpinnerFilter = (Spinner) findViewById(R.id.spinner_filter);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.filter_options,
                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinnerFilter.setAdapter(adapter);
        mSpinnerFilter.setOnItemSelectedListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setFontSizeRecyclerView();
        loadJuices(mSortOrder);
        setBrightness();

    }



    private void setFontSizeRecyclerView() {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        Boolean switchPref = sharedPref.getBoolean(SettingsActivity.KEY_PREF_LARGE_FONT_SWITCH, false);

        mJuicesRecyclerAdapter.setTextSizes(switchPref);
        if (switchPref) {
            set_sortByLabelSize = 20;
        } else {
            set_sortByLabelSize = 15;
        }

        TextView sortByLabel = findViewById(R.id.text_sort_by_label);
        sortByLabel.setText("Sort By: ");
        sortByLabel.setTextSize(set_sortByLabelSize);
    }

    private void loadJuices(String sortOrder) {

        // Open readable database
        SQLiteDatabase db = mDbOpenHelper.getReadableDatabase();

        // Define columns to be returned
        final String[] juiceColumns = {
                JuiceInfoEntry._ID,
                JuiceInfoEntry.COLUMN_NAME,
                JuiceInfoEntry.COLUMN_BRAND,
                JuiceInfoEntry.COLUMN_CATEGORY,
                JuiceInfoEntry.COLUMN_RATING};

        // Instantiate cursor to be ordered by our passed in sortOrder variable
        final Cursor juiceCursor = db.query(JuiceInfoEntry.TABLE_NAME, juiceColumns,
                null, null, null, null, sortOrder);
        mJuicesRecyclerAdapter.changeCursor(juiceCursor);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    protected void onDestroy() {

        // Close db connection
        mDbOpenHelper.close();
        super.onDestroy();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            // Open settings activity class
            startActivity(new Intent(JuiceListActivity.this, SettingsActivity.class));
            return true;
        }

        if (id == R.id.action_random_joke) {
            // Open Juice DB class
            startActivity(new Intent(JuiceListActivity.this, RandomJokeActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {
            case (0):
                //Load juices sorted by rating
                loadJuices(JuiceInfoEntry.COLUMN_RATING + " DESC");
                parent.setSelection(position);
                break;
            case (1):
                //Load juices sorted by category, rating
                loadJuices(JuiceInfoEntry.COLUMN_CATEGORY + ", " + JuiceInfoEntry.COLUMN_RATING + " DESC");
                parent.setSelection(position);
                break;
            case (2):
                //Load juices sorted by brand, rating
                loadJuices(JuiceInfoEntry.COLUMN_BRAND + ", " + JuiceInfoEntry.COLUMN_RATING + " DESC");
                parent.setSelection(position);
                break;
            default:
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}