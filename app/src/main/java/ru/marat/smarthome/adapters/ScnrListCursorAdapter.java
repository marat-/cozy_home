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

public class ScnrListCursorAdapter extends SimpleCursorAdapter{

    private Context mContext;
    private int layout;
    private Cursor cr;
    private final LayoutInflater inflater;
    public static final String irSenderIp = "192.168.1.120";

    public ScnrListCursorAdapter(Context context, int layout, Cursor c, String[] from, int[] to, int flags) {
        super(context,layout, c, from, to, flags);
        this.layout=layout;
        this.mContext = context;
        this.inflater=LayoutInflater.from(context);
        this.cr=c;
    }

    @Override
    public View newView (Context context, Cursor cursor, ViewGroup parent) {
        return inflater.inflate(layout, null);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        super.bindView(view, context, cursor);
        TextView scnrName=(TextView)view.findViewById(R.id.scnr_name);
        TextView scnrDescription=(TextView)view.findViewById(R.id.scnr_description);

        int scnrNameIndex=cursor.getColumnIndexOrThrow("scnr_name");
        int scnrDescriptionIndex=cursor.getColumnIndexOrThrow("scnr_description");

        scnrName.setText(cursor.getString(scnrNameIndex));
        scnrDescription.setText(cursor.getString(scnrDescriptionIndex));

//        view.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                new IrSenderConnect(mContext).execute(String.format("http://%s/?%s", irSenderIp, irCommand));
//            }
//        });
    }
}
