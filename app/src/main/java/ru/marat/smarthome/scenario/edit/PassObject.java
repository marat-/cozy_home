package ru.marat.smarthome.scenario.edit;

import android.view.View;
import android.widget.ListView;

/**
 * Objects passed in Drag and Drop operation
 */
public class PassObject<T> {

  View view;
  T item;
  ListView listView;

  PassObject(View v, T item, ListView listView) {
    this.view = v;
    this.item = item;
    this.listView = listView;
  }
}
