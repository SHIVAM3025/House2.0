package com.dtu.house;

import android.app.Application;

public class CustomFontApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        TypeFaceUtil.overrideFont(getApplicationContext() , "SERIF" , "fonts/google.ttf");

    }
}
