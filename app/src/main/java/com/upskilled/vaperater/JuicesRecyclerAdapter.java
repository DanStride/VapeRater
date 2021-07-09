package com.upskilled.vaperater;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.upskilled.vaperater.JuiceRaterDatabaseContract.JuiceInfoEntry;

public class JuicesRecyclerAdapter extends RecyclerView.Adapter<JuicesRecyclerAdapter.ViewHolder> {

    private int set_itemNameSize;
    private int set_itemBrandSize;
    private int set_itemCategorySize;
    private int set_itemRatingSize;


    private final Context mContext;
    private Cursor mCursor;
    private final LayoutInflater mLayoutInflater;
    private int mJuiceNamePos;
    private int mJuiceBrandPos;
    private int mJuiceCatPos;
    private int mJuiceDescPos;
    private int mJuiceRatePos;
    private int mIdPos;

    public void setTextSizes(boolean isLargeFont) {
        if (isLargeFont) {
            set_itemNameSize = 30;
            set_itemBrandSize = 20;
            set_itemCategorySize = 18;
            set_itemRatingSize = 18;
        }
        else {
            set_itemNameSize = 24;
            set_itemBrandSize = 14;
            set_itemCategorySize = 12;
            set_itemRatingSize = 12;
        }

        notifyDataSetChanged();
    }

    public JuicesRecyclerAdapter(Context context, Cursor cursor) {

        // Creates new instance, assign fields, inflate layout resource and populate column positions
        mContext = context;
        mCursor = cursor;
        mLayoutInflater = LayoutInflater.from(mContext);
        populateColumnPositions();
    }

    private void populateColumnPositions() {
        if(mCursor == null)
            return;

        //Get column indexes from mCursor
       mJuiceNamePos = mCursor.getColumnIndex(JuiceInfoEntry.COLUMN_NAME);
       mJuiceBrandPos = mCursor.getColumnIndex(JuiceInfoEntry.COLUMN_BRAND);
       mJuiceCatPos = mCursor.getColumnIndex(JuiceInfoEntry.COLUMN_CATEGORY);
       mJuiceDescPos = mCursor.getColumnIndex(JuiceInfoEntry.COLUMN_DESCRIPTION);
       mJuiceRatePos = mCursor.getColumnIndex(JuiceInfoEntry.COLUMN_RATING);
       mIdPos = mCursor.getColumnIndex(JuiceInfoEntry._ID);
    }

    public void changeCursor(Cursor cursor) {

        // Takes in new cursor to populate column positions and notify data change
        if(mCursor != null)
            mCursor.close();
        mCursor = cursor;
        populateColumnPositions();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        // Inflate view using layout resource
        View itemView = mLayoutInflater.inflate(R.layout.item_juice_list, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        // Move cursor to position of current item
        mCursor.moveToPosition(position);

        // Assign data from Cursor into ViewHolder objects
        holder.mTextJuiceName.setText(mCursor.getString(mJuiceNamePos));
        holder.mTextJuiceName.setTextSize(set_itemNameSize);
        holder.mTextJuiceBrand.setText(mCursor.getString(mJuiceBrandPos));
        holder.mTextJuiceBrand.setTextSize(set_itemBrandSize);
        holder.mTextJuiceRating.setText(Float.toString(mCursor.getFloat(mJuiceRatePos)));
        holder.mTextJuiceRating.setTextSize(set_itemRatingSize);
        holder.mId = mCursor.getInt(mIdPos);

        // Using a switch for category as in the database it is an integer
        // and the ViewHolder needs the related string
        switch (mCursor.getInt(mJuiceCatPos)) {
            case 1:
                holder.mTextJuiceCategory.setText("Fruit/Sweets");
                break;
            case 2:
                holder.mTextJuiceCategory.setText("Desserts");
                break;
            case 3:
                holder.mTextJuiceCategory.setText("Other");
                break;
            default:
        }

        holder.mTextJuiceCategory.setTextSize(set_itemCategorySize);
    }

    @Override
    public int getItemCount() {

        // If cursor is null, return 0
        return mCursor == null ? 0 : mCursor.getCount();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        // Handles creation of ViewHolder instances
        public final TextView mTextJuiceName;
        public final TextView mTextJuiceBrand;
        public final TextView mTextJuiceCategory;
        public final TextView mTextJuiceRating;
        public int mId;

        public ViewHolder(@NonNull View itemView) {

            // Assigns variables to layout views, defines the click handler for clicking on
            // an item.
            super(itemView);
            mTextJuiceName = (TextView) itemView.findViewById(R.id.text_juice);
            mTextJuiceBrand = (TextView) itemView.findViewById(R.id.text_brand);
            mTextJuiceCategory = (TextView) itemView.findViewById(R.id.text_category);
            mTextJuiceRating = (TextView) itemView.findViewById(R.id.text_rating);



            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext, JuiceActivity.class);
                    intent.putExtra(JuiceListActivity.JUICE_POSITION, mId);
                    mContext.startActivity(intent);
                }
            });
        }
    }
}
