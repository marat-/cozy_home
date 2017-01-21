package ru.marat.smarthome.device.edit;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import ru.marat.smarthome.R;
import ru.marat.smarthome.model.DeviceType;

/**
 * Custom ArrayAdapter for spinner in DeviceEditActivity
 */
public class DeviceTypeArrayAdapter extends ArrayAdapter<DeviceType> {

    private Context context;
    private List<DeviceType> deviceTypeList;

    public DeviceTypeArrayAdapter(Context context, int viewResourceId, List<DeviceType> deviceTypeList) {
        super(context, viewResourceId, deviceTypeList);
        this.context = context;
        this.deviceTypeList = deviceTypeList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return this.getCustomView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, View convertView,
                                ViewGroup parent) {
        return this.getCustomView(position, convertView, parent);
    }

    /**
     * Custom view for spinner
     *
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
    public View getCustomView(int position, View convertView, ViewGroup parent) {
        DeviceType deviceType = deviceTypeList.get(position);
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        View view = inflater.inflate(R.layout.device_edit_spinner_row, parent, false);

        TextView deviceTypeName = (TextView) view.findViewById(R.id.device_type_name);
        deviceTypeName.setText(deviceType.getName());

        TextView deviceTypeActive = (TextView) view.findViewById(R.id.device_type_active);
        deviceTypeActive.setText(deviceType.isActive() ? "Active" : "Disactive");

        ImageView deviceImage = (ImageView) view.findViewById(R.id.device_type_icon);
        String deviceImageValue = deviceType.getImage();
        if (deviceImageValue != null) {
            int imageResID = this.context.getResources().getIdentifier(deviceImageValue, "drawable", this.context.getPackageName());
            deviceImage.setImageResource(imageResID);
        }
        return view;
    }
}
