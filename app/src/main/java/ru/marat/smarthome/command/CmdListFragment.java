package ru.marat.smarthome.command;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.query.From;
import com.activeandroid.query.Select;
import com.getbase.floatingactionbutton.FloatingActionsMenu;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.marat.smarthome.R;
import ru.marat.smarthome.model.Cmd;
import ru.marat.smarthome.model.CmdType;
import ru.marat.smarthome.model.Device;
import ru.marat.smarthome.model.DeviceType;

public class CmdListFragment extends Fragment {

    @BindView(R.id.cmd_list_fab_menu)
    FloatingActionsMenu mainFABMenu;

    @BindView(R.id.commands_grid_view)
    GridView commandsGridView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cmd_list, null);
        ButterKnife.bind(this, view);

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

        From query = new Select("cmd._id, cmd.name AS cmd_name, device.name AS device_name, cmd.value, device_type.image")
                .from(Cmd.class).as("cmd")
                .innerJoin(CmdType.class).as("cmd_type")
                .on("cmd.type_id=cmd_type._id")
                .innerJoin(Device.class).as("device")
                .on("cmd.device_id=device._id")
                .innerJoin(DeviceType.class).as("device_type")
                .on("device.type_id = device_type._id");

        Cursor cursor = ActiveAndroid.getDatabase().rawQuery(query.toSql(), query.getArguments());

        CursorAdapter cmdListCursorAdapter = new CmdListCursorAdapter(
                this.getActivity(),
                R.layout.cmd_grid_row,
                cursor,
                CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);

        commandsGridView.setAdapter(cmdListCursorAdapter);
    }
}