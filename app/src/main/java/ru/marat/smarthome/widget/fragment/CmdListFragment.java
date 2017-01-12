package ru.marat.smarthome.widget.fragment;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.query.From;
import com.activeandroid.query.Select;
import com.getbase.floatingactionbutton.FloatingActionsMenu;

import roboguice.fragment.RoboFragment;
import roboguice.inject.InjectView;
import ru.marat.smarthome.R;
import ru.marat.smarthome.adapters.CmdListCursorAdapter;
import ru.marat.smarthome.model.Cmd;
import ru.marat.smarthome.model.CmdType;
import ru.marat.smarthome.model.Device;

public class CmdListFragment extends RoboFragment {

    @InjectView(R.id.main_fab_menu)
    private FloatingActionsMenu mainFABMenu;

    @InjectView(R.id.commandsGridView)
    private GridView commandsGridView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cmd_list, null);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final com.getbase.floatingactionbutton.FloatingActionButton mainFABMenuEnable = (com.getbase.floatingactionbutton.FloatingActionButton) view.findViewById(R.id.main_fab_menu_enable);
        mainFABMenu.setEnabled(false);
        mainFABMenu.setOnFloatingActionsMenuUpdateListener(new FloatingActionsMenu.OnFloatingActionsMenuUpdateListener() {
            @Override
            public void onMenuExpanded() {
            }

            @Override
            public void onMenuCollapsed() {
            }
        });

        From query = new Select("cmd._id, cmd.name AS cmd_name, device.name AS device_name, cmd.value, device.image")
                .from(Cmd.class).as("cmd")
                .innerJoin(CmdType.class).as("cmd_type")
                .on("cmd.type_id=cmd_type._id")
                .innerJoin(Device.class).as("device")
                .on("cmd.device_id=device._id");

        String[] from = {"cmd_name"};
        int[] to = {R.id.cmd_name};
        Cursor cursor = ActiveAndroid.getDatabase().rawQuery(query.toSql(), query.getArguments());

        CmdListCursorAdapter cmdListCursorAdapter = new CmdListCursorAdapter(
                this.getActivity(),
                R.layout.cmd_grid_row,
                cursor,
                from,
                to,
                CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);

        commandsGridView.setAdapter(cmdListCursorAdapter);
    }
}