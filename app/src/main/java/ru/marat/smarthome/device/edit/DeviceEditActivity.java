package ru.marat.smarthome.device.edit;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.activeandroid.query.Select;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.marat.smarthome.R;
import ru.marat.smarthome.app.core.BaseActivity;
import ru.marat.smarthome.app.validator.TextValidator;
import ru.marat.smarthome.model.Device;
import ru.marat.smarthome.model.DeviceType;

public class DeviceEditActivity extends BaseActivity {
    @BindView(R.id.device_edit_name)
    EditText deviceEditName;

    @BindView(R.id.device_edit_active)
    CheckBox deviceEditActive;

    @BindView(R.id.device_edit_type)
    Spinner deviceEditType;

    private String deviceId;

    protected List<DeviceType> deviceTypeList;

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

        deviceTypeList = new Select().from(DeviceType.class).orderBy("active DESC").execute();
        ArrayAdapter deviceTypeAdapter = new DeviceTypeArrayAdapter(DeviceEditActivity.this, R.layout.device_edit_spinner_row,
                deviceTypeList);
        deviceEditType.setAdapter(deviceTypeAdapter);
        if (deviceId != null) {
            Device device = new Select()
                    .from(Device.class).as("device")
                    .leftJoin(DeviceType.class).as("device_type")
                    .on("device.type_id = device_type._id")
                    .where("device._id = ?", new String[]{deviceId})
                    .orderBy("device.active DESC")
                    .executeSingle();
            deviceEditName.setText(device.getName());
            deviceEditActive.setChecked(device.isActive());
            for (int i = 0; i < deviceEditType.getCount(); i++) {
                if (deviceTypeList.get(i).getId() == Long.valueOf(device.getTypeId())) {
                    deviceEditType.setSelection(i);
                }
            }
        }

        validateFields();
    }

    protected void validateFields() {
        deviceEditName.addTextChangedListener(new TextValidator(deviceEditName) {
            @Override
            public void validate(TextView textView, String text) {
                validataDeviceEditName(textView, text);
            }
        });
    }

    private boolean validataDeviceEditName(TextView textView, String text) {
        if (text != null && text.isEmpty()) {
            deviceEditName.setError(getString(R.string.device_edit_name_empty_error));
            return false;
        }
        return true;
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
                if (validataDeviceEditName(deviceEditName, deviceEditName.getText().toString())) {
                    Device device;
                    if (deviceId != null) {
                        device = new Select().from(Device.class).where("_id = ?", new String[]{deviceId}).executeSingle();
                    } else {
                        device = new Device();
                    }
                    device.setName(deviceEditName.getText().toString());
                    device.setActive(deviceEditActive.isChecked());
                    device.setTypeId(deviceTypeList.get(deviceEditType.getSelectedItemPosition()).getId());
                    device.save();
                    DeviceEditActivity.this.finish();
                }
                return true;
            case R.id.device_edit_delete_action:
                DeviceEditActivity.this.finish();
        }

        return super.onOptionsItemSelected(item);
    }

}
