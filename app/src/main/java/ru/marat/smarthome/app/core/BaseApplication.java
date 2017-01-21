package ru.marat.smarthome.app.core;

import com.activeandroid.ActiveAndroid;

/**
 * Base application class
 */
public class BaseApplication extends com.activeandroid.app.Application {
    @Override
    public void onCreate() {
        super.onCreate();
        ActiveAndroid.initialize(this);
    }
}
