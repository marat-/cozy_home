package ru.marat.smarthome.scenario.edit;

import android.content.Context;
import android.view.DragEvent;
import android.view.View;
import android.view.View.OnDragListener;
import android.widget.ListView;
import java.util.List;
import ru.marat.smarthome.R;
import ru.marat.smarthome.model.Cmd;

class ItemOnDragListener implements OnDragListener {

  private Cmd cmd;
  private Context context;
  private int resumeColor;
  private int dragColor;

  ItemOnDragListener(Cmd cmd, Context context) {

    this.context = context;
    ListView scnrEditCmdListView;
    this.cmd = cmd;

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

        PassObject<Cmd> passObj = (PassObject) event.getLocalState();
        View view = passObj.view;
        Cmd passedCmd = passObj.item;
        ListView listView = passObj.listView;
        CmdInScnrArrayAdapter adapter = ((CmdInScnrArrayAdapter) listView.getAdapter());

        List<Cmd> cmdList = adapter.getList();

        int removeLocation = cmdList.indexOf(passedCmd);
        int insertLocation = cmdList.indexOf(cmd);

        if (removeLocation != insertLocation) {
          if (removeItemToList(cmdList, passedCmd)) {
            cmdList.add(insertLocation, passedCmd);
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

  private boolean removeItemToList(List<Cmd> l, Cmd it) {
    boolean result = l.remove(it);
    return result;
  }
}
