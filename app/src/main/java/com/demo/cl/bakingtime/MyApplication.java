package com.demo.cl.bakingtime;

import android.app.Application;

import timber.log.Timber;

/**
 * Created by CL on 9/14/17.
 */

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        if(BuildConfig.DEBUG) {
            // DebugTree has all usual logging functionality
            Timber.plant(new Timber.DebugTree());
        }
    }
}
