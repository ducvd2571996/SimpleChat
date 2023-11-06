package com.example.simplechat;

import android.app.Application;

public class App extends Application {

   public static App instant;

    @Override
    public void onCreate() {
        super.onCreate();
        instant = this;
    }
}
