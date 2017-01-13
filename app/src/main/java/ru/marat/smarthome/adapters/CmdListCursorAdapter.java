package ru.marat.smarthome.adapters;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import ru.marat.smarthome.IrSenderConnect;
import ru.marat.smarthome.R;

public class CmdListCursorAdapter extends SimpleCursorAdapter{

    private Context mContext;
    private int layout;
    private final LayoutInflater inflater;
    public static final String irSenderIp = "192.168.1.120";

    public CmdListCursorAdapter(Context context, int layout, Cursor c, String[] from, int[] to, int flags) {
        super(context,layout, c, from, to, flags);
        this.layout=layout;
        this.mContext = context;
        this.inflater=LayoutInflater.from(context);
    }

    @Override
    public View newView (Context context, Cursor cursor, ViewGroup parent) {
        return inflater.inflate(layout, null);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        super.bindView(view, context, cursor);
        TextView cmdName=(TextView)view.findViewById(R.id.cmd_name);
        TextView cmdDescription=(TextView)view.findViewById(R.id.cmd_description);
        ImageView deviceImage=(ImageView)view.findViewById(R.id.device_image);

        int cmdDeviceNameIndex=cursor.getColumnIndexOrThrow("device_name");
        int deviceImageIndex=cursor.getColumnIndexOrThrow("image");
        int commandIndex=cursor.getColumnIndexOrThrow("value");
        int commandDescription=cursor.getColumnIndexOrThrow("cmd_name");

        cmdName.setText(cursor.getString(cmdDeviceNameIndex));
        cmdDescription.setText(cursor.getString(commandDescription));
        int imageResID = mContext.getResources().getIdentifier(cursor.getString(deviceImageIndex) , "drawable", mContext.getPackageName());
        deviceImage.setImageResource(imageResID);

        final String irCommand = cursor.getString(commandIndex);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new IrSenderConnect(mContext).execute(String.format("http://%s/?%s", irSenderIp, irCommand));
            }
        });
    }
}
