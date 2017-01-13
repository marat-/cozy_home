package ru.marat.smarthome;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import roboguice.inject.ContentView;
import roboguice.inject.InjectResource;
import roboguice.inject.InjectView;
import ru.marat.smarthome.core.BaseRoboActivity;
import ru.marat.smarthome.entity.device.DeviceManagerActivity;
import ru.marat.smarthome.widget.fragment.WidgetsFragment;

@ContentView(R.layout.activity_main)
public class MainActivity extends BaseRoboActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final Logger logger = LoggerFactory.getLogger(MainActivity.class);

    @InjectView(R.id.drawer_layout)
    private DrawerLayout mDrawerLayout;

    @InjectView(R.id.toolbar)
    private Toolbar toolbar;

    @InjectView(R.id.navigation_view)
    private NavigationView navigationView;

    @InjectResource(R.drawable.ic_menu)
    private Drawable ic_menu;

    private FragmentManager mFragmentManager;
    private FragmentTransaction mFragmentTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.setDrawerListener(toggle);
        toggle.syncState();

        mFragmentManager = getSupportFragmentManager();
        mFragmentTransaction = mFragmentManager.beginTransaction();
        mFragmentTransaction.replace(R.id.containerView,new WidgetsFragment()).commit();

        navigationView.setNavigationItemSelectedListener(this);

        /*fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(findViewById(R.id.coordinator), "I'm a Snackbar", Snackbar.LENGTH_LONG).setAction("Action", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(MainActivity.this, "Snackbar Action", Toast.LENGTH_LONG).show();
                    }
                }).show();
            }
        });*/
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
            case R.id.action_settings:
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {
        mDrawerLayout.closeDrawers();
        Intent intent = new Intent(this, DeviceManagerActivity.class);
        startActivity(intent);


//        if (menuItem.getItemId() == R.id.navigation_item_device_manager) {
//            FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
//            fragmentTransaction.replace(R.id.containerView, new ScenariosListFragment()).commit();
//
//        }
//
//        if (menuItem.getItemId() == R.id.navigation_item_settings) {
//            FragmentTransaction xfragmentTransaction = mFragmentManager.beginTransaction();
//            xfragmentTransaction.replace(R.id.containerView,new CmdListFragment()).commit();
//        }

        return false;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
