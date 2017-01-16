package ru.marat.smarthome.adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import ru.marat.smarthome.IrSenderConnect;
import ru.marat.smarthome.R;

public class ScnrListCursorAdapter extends CursorAdapter {

    private Context context;
    private int layout;
    private final LayoutInflater inflater;
    public static final String irSenderIp = "192.168.1.120";

    public ScnrListCursorAdapter(Context context, int layout, Cursor c, int flags) {
        super(context, c, flags);
        this.layout=layout;
        this.context = context;
        this.inflater=LayoutInflater.from(context);
    }

    @Override
    public View newView (Context context, Cursor cursor, ViewGroup parent) {
        return inflater.inflate(layout, null);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView scnrName=(TextView)view.findViewById(R.id.scnr_name);
        TextView scnrDescription=(TextView)view.findViewById(R.id.scnr_description);

        int scnrNameIndex=cursor.getColumnIndexOrThrow("scnr_name");
        int scnrDescriptionIndex=cursor.getColumnIndexOrThrow("scnr_description");

        scnrName.setText(cursor.getString(scnrNameIndex));
        scnrDescription.setText(cursor.getString(scnrDescriptionIndex));

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new IrSenderConnect(ScnrListCursorAdapter.this.context).execute(String.format("http://%s/?%s", irSenderIp, ""));
            }
        });
    }
}
