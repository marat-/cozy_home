package ru.marat.smarthome.scenario.edit;

import android.content.Context;
import android.view.DragEvent;
import android.view.View;
import android.view.View.OnDragListener;
import android.widget.ListView;
import java.util.List;
import ru.marat.smarthome.R;
import ru.marat.smarthome.model.Cmd;
import ru.marat.smarthome.scenario.edit.ScnrEditActivity.PassObject;

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

        PassObject passObj = (PassObject) event.getLocalState();
        View view = passObj.view;
        Cmd passedItem = passObj.item;
        ListView listView = (ListView) passObj.listView;
        CmdInScnrArrayAdapter srcAdapter = ((CmdInScnrArrayAdapter) listView.getAdapter());

        List<Cmd> srcList = srcAdapter.getList();

        int removeLocation = srcList.indexOf(passedItem);
        int insertLocation = srcList.indexOf(cmd);

        if (removeLocation != insertLocation) {
          if (removeItemToList(srcList, passedItem)) {
            srcList.add(insertLocation, passedItem);
          }

          srcAdapter.notifyDataSetChanged();
          listView.smoothScrollToPosition(srcAdapter.getCount() - 1);
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
