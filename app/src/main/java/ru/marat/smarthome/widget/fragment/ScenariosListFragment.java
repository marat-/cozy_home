package ru.marat.smarthome.widget.fragment;

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

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.marat.smarthome.R;
import ru.marat.smarthome.adapters.ScnrListCursorAdapter;
import ru.marat.smarthome.model.Scnr;

public class ScenariosListFragment extends Fragment {

    @BindView(R.id.scnr_list_grid_view)
    GridView scnrListGridView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_scenario_list, null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        From query = new Select("scnr._id, scnr.name AS scnr_name, scnr.description AS scnr_description")
                .from(Scnr.class).as("scnr");
//                .innerJoin(ScnrCmd.class).as("scnr_cmd")
//                .on("scnr._id=scnr_cmd.scnr_id")
//                .innerJoin(Cmd.class).as("cmd")
//                .on("scnr_cmd.cmd_id=cmd._id")
//                .innerJoin(CmdType.class).as("cmd_type")
//                .on("cmd.type_id=cmd_type._id")
//                .innerJoin(Device.class).as("device")
//                .on("cmd.device_id=device._id");

        Cursor cursor = ActiveAndroid.getDatabase().rawQuery(query.toSql(), query.getArguments());

        CursorAdapter scnrListCursorAdapter = new ScnrListCursorAdapter(
                this.getActivity(),
                R.layout.scnr_grid_row,
                cursor,
                CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);

        scnrListGridView.setAdapter(scnrListCursorAdapter);
    }
}