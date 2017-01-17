package ru.marat.smarthome.core;

import com.activeandroid.ActiveAndroid;

/**
 * Created by Марат on 04.01.2016.
 */
public class BaseApplication extends com.activeandroid.app.Application {
    @Override
    public void onCreate() {
        super.onCreate();
        ActiveAndroid.initialize(this);
    }
}
