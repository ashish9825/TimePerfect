package com.example.ashishpanjwani.timeperfectagain;

import com.tramsun.libs.prefcompat.Pref;

public class Application extends android.app.Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Pref.init(this);
    }
}
