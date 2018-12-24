package com.example.muhammadusama.weather;

/**
 * Created by Muhammad Usama on 5/10/2018.
 */

public class Weather {

    private String mDefaultTranslation;
    private String mMiwokTranslation;
    private static final int HAS_IMAGE_PROVIDED = -1;
    private int mImageResourceId = HAS_IMAGE_PROVIDED;
    private int mAudioResourceId;


    public Weather (String defaultTranslation, String miwokTranslation, int audioResourceId){

        mDefaultTranslation = defaultTranslation;
        mMiwokTranslation = miwokTranslation;
        mAudioResourceId = audioResourceId;
    }
    public Weather (String defaultTranslation,String miwokTranslation, int imageResourceId, int audioResourceId) {

        mDefaultTranslation = defaultTranslation;
        mMiwokTranslation = miwokTranslation;
        mImageResourceId = imageResourceId;
        mAudioResourceId = audioResourceId;
    }

    public String getmDefaultTranslation() {
        return mDefaultTranslation;
    }

    public String getmMiwokTranslation() {
        return mMiwokTranslation;
    }

    public int getmImageResourceId() {
        return mImageResourceId;
    }

    public int getmAudioResourceId() {
        return mAudioResourceId;
    }

    public boolean hasImage(){

        return mImageResourceId!=HAS_IMAGE_PROVIDED;
    }
}
