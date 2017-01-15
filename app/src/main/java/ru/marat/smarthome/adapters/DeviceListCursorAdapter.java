package ru.marat.smarthome.adapters;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import ru.marat.smarthome.R;

public class DeviceListCursorAdapter extends SimpleCursorAdapter {

    private Context mContext;
    private int layout;
    private final LayoutInflater inflater;

    public DeviceListCursorAdapter(Context context, int layout, Cursor c, String[] from, int[] to, int flags) {
        super(context, layout, c, from, to, flags);
        this.layout = layout;
        this.mContext = context;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return inflater.inflate(layout, null);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        super.bindView(view, context, cursor);
        TextView deviceName = (TextView) view.findViewById(R.id.device_name);
        ImageView deviceImage = (ImageView) view.findViewById(R.id.device_image);

        int deviceNameIndex = cursor.getColumnIndexOrThrow("name");
        int deviceImageIndex = cursor.getColumnIndexOrThrow("image");

        deviceName.setText(cursor.getString(deviceNameIndex));
        int imageResID = mContext.getResources().getIdentifier(cursor.getString(deviceImageIndex), "drawable", mContext.getPackageName());
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
