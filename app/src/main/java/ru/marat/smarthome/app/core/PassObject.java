package ru.marat.smarthome.app.core;

import android.view.View;
import android.widget.ListView;

/**
 * Objects passed in Drag and Drop operation
 */
public class PassObject<T> {

  private View view;
  private T item;
  private ListView listView;

  public PassObject(View v, T item, ListView listView) {
    this.view = v;
    this.item = item;
    this.listView = listView;
  }

  public T getItem() {
    return item;
  }

  public void setItem(T item) {
    this.item = item;
  }

  public ListView getListView() {
    return listView;
  }

  public void setListView(ListView listView) {
    this.listView = listView;
  }

  public View getView() {
    return view;
  }

  public void setView(View view) {
    this.view = view;
  }
}
