package ru.marat.smarthome.adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import ru.marat.smarthome.R;

public class DeviceListCursorAdapter extends CursorAdapter {

    private Context context;
    private int layout;
    private final LayoutInflater inflater;

    public DeviceListCursorAdapter(Context context, int layout, Cursor c, int flags) {
        super(context, c, flags);
        this.layout = layout;
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return inflater.inflate(layout, null);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView deviceName = (TextView) view.findViewById(R.id.device_name);
        ImageView deviceImage = (ImageView) view.findViewById(R.id.device_image);

        int deviceNameIndex = cursor.getColumnIndexOrThrow("name");
        int deviceImageIndex = cursor.getColumnIndexOrThrow("image");

        deviceName.setText(cursor.getString(deviceNameIndex));
        int imageResID = this.context.getResources().getIdentifier(cursor.getString(deviceImageIndex), "drawable", this.context.getPackageName());
        deviceImage.setImageResource(imageResID);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return false;
            }
        });
    }
}
