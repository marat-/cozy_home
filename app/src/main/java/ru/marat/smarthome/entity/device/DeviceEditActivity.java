package ru.marat.smarthome.entity.device;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

import com.activeandroid.query.Select;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.marat.smarthome.R;
import ru.marat.smarthome.adapters.DeviceTypeArrayAdapter;
import ru.marat.smarthome.core.BaseActivity;
import ru.marat.smarthome.model.Device;
import ru.marat.smarthome.model.DeviceType;

public class DeviceEditActivity extends BaseActivity {

    @BindView(R.id.device_edit_name)
    EditText deviceEditName;

    @BindView(R.id.device_edit_active)
    CheckBox deviceEditActive;

    @BindView(R.id.device_edit_icon)
    Spinner deviceEditIcon;

    private String deviceId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_edit);
        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        deviceId = intent.getStringExtra("item_id");

        if (deviceId != null) {
            Device device = new Select()
                    .from(Device.class)
                    .leftJoin(DeviceType.class).as("device_type")
                    .on("device.type_id = device_type._id")
                    .where("device._id = ?", new String[]{deviceId})
                    .orderBy("device.active DESC")
                    .executeSingle();
            deviceEditName.setText(device.getName());
            deviceEditActive.setChecked(device.isActive());
        }

        fillDeviceTypeSpinner();
    }

    protected void fillDeviceTypeSpinner() {
        List<DeviceType> deviceTypeList = new Select().from(DeviceType.class).orderBy("active DESC").execute();
        deviceEditIcon.setAdapter(new DeviceTypeArrayAdapter(DeviceEditActivity.this, R.layout.device_edit_spinner_row,
                deviceTypeList));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.device_edit_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id) {
            case R.id.device_edit_save_action:
                Device device;
                if (deviceId != null) {
                    device = new Select().from(Device.class).where("_id = ?", new String[]{deviceId}).executeSingle();
                } else {
                    device = new Device();
                }
                device.setName(deviceEditName.getText().toString());
                device.setActive(deviceEditActive.isChecked());
                device.save();
                DeviceEditActivity.this.finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
