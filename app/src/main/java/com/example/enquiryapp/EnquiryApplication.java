package com.example.enquiryapp;

import android.app.Application;

import com.example.enquiryapp.db.DatabaseManager;
import com.example.enquiryapp.util.DataHelper;
import com.google.android.material.color.DynamicColors;

public class EnquiryApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        DynamicColors.applyToActivitiesIfAvailable(EnquiryApplication.this);
        DataHelper.initialize(this);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        DatabaseManager.getInstance().disconnect();
    }
}
