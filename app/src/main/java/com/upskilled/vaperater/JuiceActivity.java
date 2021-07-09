package com.upskilled.vaperater;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.preference.PreferenceManager;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.upskilled.vaperater.JuiceRaterDatabaseContract.JuiceInfoEntry;


public class JuiceActivity extends AppCompatActivity {
    public static final String JUICE_ID = "com.upskilled.vaperater.JUICE_POSITION";
    public static final int ID_NOT_SET = -1;
    private int mJuiceId;
    private boolean mIsNewJuice;
    private EditText mEditDescription;
    private EditText mEditName;
    private EditText mEditBrand;
    private EditText mEditRating;
    private Spinner mCategorySpinner;
    private TextView mRatingLabel;
    private TextView mCategoryLabel;
    private FloatingActionButton mFab;
    private JuiceRaterOpenHelper mMDbOpenHelper;
    private Cursor mJuiceCursor;
    private int mJuiceNamePos;
    private int mJuiceBrandPos;
    private int mJuiceCatPos;
    private int mJuiceDescPos;
    private int mJuiceRatePos;
    private boolean mIsSaving = false;

    @Override
    protected void onDestroy() {
        // Closes OpenHelper to not waste resources
        mMDbOpenHelper.close();
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Setting up views and adapters
        setContentView(R.layout.activity_juice);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mEditDescription = findViewById(R.id.muliText_juice_description);
        mEditName = findViewById(R.id.editText_name);
        mEditBrand = findViewById(R.id.editText_brand);
        mEditRating = findViewById(R.id.editText_rating);
        mCategorySpinner = findViewById(R.id.spinner_category_selection);
        mRatingLabel = findViewById(R.id.label_rating);
        mCategoryLabel = findViewById(R.id.label_category);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.category_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mCategorySpinner.setAdapter(adapter);

        mCategorySpinner.setAdapter(adapter);



        //Sets up Save button with click handler
        mFab = findViewById(R.id.fab);
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveJuiceToDatabase();
            }
        });

        // Create instance of OpenHelper class
        mMDbOpenHelper = new JuiceRaterOpenHelper(this);


        readDisplayStateValues();

        setFontSize();
        setBrightness();

        // Determine if a new juice is being created and call appropriate methods
        if(!mIsNewJuice) {
            loadJuiceData();
            disableEditability();
            mFab.hide();

        } else if(mIsNewJuice) {
            createNewJuice();
        }
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
    @Override
    protected void onResume() {
        super.onResume();
        setFontSize();
        setBrightness();
    }

    private void setFontSize() {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        Boolean switchPref = sharedPref.getBoolean(SettingsActivity.KEY_PREF_LARGE_FONT_SWITCH, false);

        if (switchPref) {
            mEditName.setTextSize(30);
            mEditBrand.setTextSize(25);
            mEditRating.setTextSize(25);
            mEditDescription.setTextSize(20);
            mRatingLabel.setTextSize(20);
            mCategoryLabel.setTextSize(20);
        } else {
            mEditName.setTextSize(25);
            mEditBrand.setTextSize(22);
            mEditRating.setTextSize(22);
            mEditDescription.setTextSize(18);
            mRatingLabel.setTextSize(18);
            mCategoryLabel.setTextSize(18);
        }
    }

    private void loadJuiceData() {



        // Open readable database, and define selection criteria
        SQLiteDatabase db = mMDbOpenHelper.getReadableDatabase();

        String selection = JuiceInfoEntry._ID + " = ?";
        String[] selectionArgs = {Integer.toString(mJuiceId)};

        String[] juiceColumns = {
                JuiceInfoEntry.COLUMN_NAME,
                JuiceInfoEntry.COLUMN_BRAND,
                JuiceInfoEntry.COLUMN_CATEGORY,
                JuiceInfoEntry.COLUMN_DESCRIPTION,
                JuiceInfoEntry.COLUMN_RATING
        };

        // Instantiate a Cursor by querying the database, move to first row, call display method and close Cursor
        mJuiceCursor = db.query(JuiceInfoEntry.TABLE_NAME, juiceColumns, selection, selectionArgs, null, null, null);
        mJuiceNamePos = mJuiceCursor.getColumnIndex(JuiceInfoEntry.COLUMN_NAME);
        mJuiceBrandPos = mJuiceCursor.getColumnIndex(JuiceInfoEntry.COLUMN_BRAND);
        mJuiceCatPos = mJuiceCursor.getColumnIndex(JuiceInfoEntry.COLUMN_CATEGORY);
        mJuiceDescPos = mJuiceCursor.getColumnIndex(JuiceInfoEntry.COLUMN_DESCRIPTION);
        mJuiceRatePos = mJuiceCursor.getColumnIndex(JuiceInfoEntry.COLUMN_RATING);
        mJuiceCursor.moveToNext();
        displayJuiceFromDb();
        mJuiceCursor.close();

    }

    private void displayJuiceFromDb() {

        // Set text for TextViews and Spinner selection to current juice at Cursor position
        mEditName.setText(mJuiceCursor.getString(mJuiceNamePos));
        mEditBrand.setText(mJuiceCursor.getString(mJuiceBrandPos));
        mCategorySpinner.setSelection(mJuiceCursor.getInt(mJuiceCatPos) - 1);
        mEditDescription.setText(mJuiceCursor.getString(mJuiceDescPos));
        mEditRating.setText(Float.toString(mJuiceCursor.getFloat(mJuiceRatePos)));

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        // Setting up menu item selection handling
        switch (item.getItemId()) {
            case R.id.edit_juice:
                // Call method to enable editing to TextViews and Spinner selection,
                // Disable button once clicked
                enableEditability();
                item.setEnabled(false);
                return true;
            case R.id.settings:
                // Open settings activity
                startActivity(new Intent(JuiceActivity.this, SettingsActivity.class));
                return true;
            case R.id.delete_juice:
                // Call delete method and return to Juice list
                deleteJuiceFromDatabase();
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void deleteJuiceFromDatabase() {

        // Delete the juice from the database which holds the current ID value
        String selection = JuiceInfoEntry._ID + " = ?";
        String[] selectionArgs = {Integer.toString(mJuiceId)};
        SQLiteDatabase db = mMDbOpenHelper.getWritableDatabase();
        db.delete(JuiceInfoEntry.TABLE_NAME, selection, selectionArgs);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        // If flagged as not saving, delete juice on back
        // This is used when a new juice is created and back is pressed instead of save
        if (!mIsSaving && mIsNewJuice)
            deleteJuiceFromDatabase();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    private void enableEditability() {

        // Set views to focusable, visible and enabled
        mEditName.setFocusable(true);
        mEditName.setFocusableInTouchMode(true);
        mEditName.setSelectAllOnFocus(true);
        mEditBrand.setFocusable(true);
        mEditBrand.setFocusableInTouchMode(true);
        mEditBrand.setSelectAllOnFocus(true);
        mCategorySpinner.setFocusable(true);
        mCategorySpinner.setFocusableInTouchMode(true);
        mCategorySpinner.setEnabled(true);
        mEditDescription.setFocusable(true);
        mEditDescription.setFocusableInTouchMode(true);
        mEditDescription.setSelectAllOnFocus(true);
        mEditRating.setFocusable(true);
        mEditRating.setFocusableInTouchMode(true);
        mEditRating.setSelectAllOnFocus(true);
        mFab.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate options menu layout resource
        getMenuInflater().inflate(R.menu.menu_juice, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        // If creating a new juice, disable edit button in menu
        if (mIsNewJuice) {
            menu.getItem(0).setEnabled(false);
        }
        return true;
    }

    private void disableEditability() {

        // Disable focusability for views
        mEditName.setFocusable(false);
        mEditBrand.setFocusable(false);
        mCategorySpinner.setFocusable(false);
        mCategorySpinner.setEnabled(false);
        mEditDescription.setFocusable(false);
        mEditRating.setFocusable(false);
    }

    private void saveJuiceToDatabase() {

        // Set selection parameters
        String selection = JuiceInfoEntry._ID + " = ?";
        String[] selectionArgs = {Integer.toString(mJuiceId)};

        //pass in new data from current TextViews and spinner to ContentValues instance
        ContentValues values = new ContentValues();
        values.put(JuiceInfoEntry.COLUMN_NAME, mEditName.getText().toString());
        values.put(JuiceInfoEntry.COLUMN_BRAND, mEditBrand.getText().toString());
        values.put(JuiceInfoEntry.COLUMN_CATEGORY, mCategorySpinner.getSelectedItemPosition() + 1);
        values.put(JuiceInfoEntry.COLUMN_DESCRIPTION, mEditDescription.getText().toString());
        values.put(JuiceInfoEntry.COLUMN_RATING, Float.parseFloat(mEditRating.getText().toString()));

        // Open writeable db and then update db
        SQLiteDatabase db = mMDbOpenHelper.getWritableDatabase();
        db.update(JuiceInfoEntry.TABLE_NAME, values, selection, selectionArgs);

        // Set boolean flag is saving to true, and then go back
        mIsSaving = true;
        onBackPressed();
    }

    private void createNewJuice() {

        //Set display text to default input values and enable editability
        mEditName.setText("Juice Name");
        mEditBrand.setText("Juice Brand");
        mEditRating.setText("0");
        mEditDescription.setText("Description");
        mCategorySpinner.setSelection(0);

        enableEditability();

        // Create empty ContentValues instance
        ContentValues values = new ContentValues();
        values.put(JuiceInfoEntry.COLUMN_NAME, "");
        values.put(JuiceInfoEntry.COLUMN_BRAND, "");
        values.put(JuiceInfoEntry.COLUMN_CATEGORY, "");
        values.put(JuiceInfoEntry.COLUMN_DESCRIPTION, "");
        values.put(JuiceInfoEntry.COLUMN_RATING, "");

        //Open readable DB, Insert new row and assign return value to ID
        SQLiteDatabase db = mMDbOpenHelper.getWritableDatabase();
        mJuiceId = (int) db.insert(JuiceInfoEntry.TABLE_NAME, null, values);

    }

    private void readDisplayStateValues() {

        // Get ID value from the intent, and set IsNew value to appropriate value
        Intent intent = getIntent();
        mJuiceId = intent.getIntExtra(JUICE_ID, ID_NOT_SET);

        if (mJuiceId != ID_NOT_SET) {
            mIsNewJuice = false;
        } else {
            mIsNewJuice = true;
        }
    }
}