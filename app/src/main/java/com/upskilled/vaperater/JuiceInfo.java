package com.upskilled.vaperater;

import android.os.Parcel;
import android.os.Parcelable;

// Definitions for a juice with implementation to make it parcelable
public class JuiceInfo implements Parcelable, Comparable {
    private final int mID;
    private final String mName;
    private final String mBrand;
    private final int mCategory;
    private final String mDescription;
    private final float mRating;


    public int getJuiceID() {
        return mID;
    }

    public String getJuiceName() {
        return mName;
    }

    public String getJuiceBrand () {
        return mBrand;
    }

    public int getJuiceCategory () {
        return mCategory - 1;
    }

    public String getJuiceDescription () {
        return mDescription;
    }

    public String getJuiceRating () {
        return Float.toString(mRating);
    }

    public JuiceInfo(int id, String name, String brand, int category, String description, float rating) {
        mID = id;
        mName = name;
        mBrand = brand;
        mCategory = category;
        mDescription = description;
        mRating = rating;
    }

    private JuiceInfo(Parcel source) {
        mID = source.readInt();
        mName = source.readString();
        mBrand = source.readString();
        mCategory = source.readInt();
        mDescription = source.readString();
        mRating = source.readFloat();
    }

    public static final Creator<JuiceInfo> CREATOR = new Creator<JuiceInfo>() {
        @Override
        public JuiceInfo createFromParcel(Parcel source) {
            return new JuiceInfo(source);
        }

        @Override
        public JuiceInfo[] newArray(int size) {
            return new JuiceInfo[size];
        }
    };


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(mID);
        parcel.writeString(mName);
        parcel.writeString(mBrand);
        parcel.writeInt(mCategory);
        parcel.writeString(mDescription);
        parcel.writeFloat(mRating);
    }

    @Override
    public int compareTo(Object o) {
        return 0;
    }
}
