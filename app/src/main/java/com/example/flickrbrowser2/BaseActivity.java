package com.example.flickrbrowser2;

import android.util.Log;

import androidx.appcompat.widget.Toolbar;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class BaseActivity extends AppCompatActivity {
    private static final String TAG = "BaseActivity";
    static final String FLICKR_QUERY = "FLICKR_QUERY";
     static final String PHOTO_TRANSFER = "PHOTO_TRANSFER";

    void activateToolBar(boolean enableHome) {
        Log.d(TAG, "activateActionBar: starts");
        ActionBar actionbar = getSupportActionBar();
        if (actionbar == null) {
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            if (toolbar != null) {
                setSupportActionBar(toolbar);
                actionbar = getSupportActionBar();
            }
        }
        if (actionbar != null) {
            actionbar.setDisplayHomeAsUpEnabled(enableHome);
        }
    }
}
