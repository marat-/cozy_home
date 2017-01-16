package ru.marat.smarthome.core;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.activeandroid.ActiveAndroid;

/**
 * Created by Марат on 04.01.2016.
 */
public class BaseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActiveAndroid.initialize(this);
    }
}
