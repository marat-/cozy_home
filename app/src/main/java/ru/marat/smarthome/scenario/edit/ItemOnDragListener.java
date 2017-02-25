package ru.marat.smarthome.scenario.edit;

import android.content.Context;
import android.view.DragEvent;
import android.view.View;
import android.view.View.OnDragListener;
import android.widget.ListView;
import java.util.List;
import ru.marat.smarthome.R;
import ru.marat.smarthome.app.core.PassObject;
import ru.marat.smarthome.model.ScnrCmd;

class ItemOnDragListener implements OnDragListener {

  private ScnrCmd scnrCmd;
  private Context context;
  private int resumeColor;
  private int dragColor;

  ItemOnDragListener(ScnrCmd scnrCmd, Context context) {

    this.context = context;
    this.scnrCmd = scnrCmd;

    resumeColor = context.getResources().getColor(R.color.white);
    dragColor = context.getResources().getColor(R.color.blue_pressed);
  }

  @Override
  public boolean onDrag(View v, DragEvent event) {
    switch (event.getAction()) {
      case DragEvent.ACTION_DRAG_ENTERED:
        v.setBackgroundColor(dragColor);
        break;
      case DragEvent.ACTION_DRAG_EXITED:
        v.setBackgroundColor(resumeColor);
        break;
      case DragEvent.ACTION_DROP:

        PassObject<ScnrCmd> passObj = (PassObject) event.getLocalState();
        ScnrCmd passedScnrCmd = passObj.getItem();
        ListView listView = passObj.getListView();
        CmdInScnrArrayAdapter adapter = ((CmdInScnrArrayAdapter) listView.getAdapter());

        List<ScnrCmd> cmdList = adapter.getList();

        int removeLocation = cmdList.indexOf(passedScnrCmd);
        int insertLocation = cmdList.indexOf(scnrCmd);

        if (removeLocation != insertLocation) {
          if (cmdList.remove(passedScnrCmd)) {
            cmdList.add(insertLocation, passedScnrCmd);
          }

          adapter.notifyDataSetChanged();
          listView.smoothScrollToPosition(adapter.getCount() - 1);
        }

        v.setBackgroundColor(resumeColor);

        break;
      case DragEvent.ACTION_DRAG_ENDED:
        v.setBackgroundColor(resumeColor);
      default:
        break;
    }

    return true;
  }
}
