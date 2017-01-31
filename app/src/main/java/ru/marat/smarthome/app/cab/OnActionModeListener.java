package ru.marat.smarthome.app.cab;

/**
 * Listener for handling create and destroy CAB
 */
public interface OnActionModeListener<T> {

  void onDestroyActionMode();

  void onCreateActionMode(T selectedItemId);
}
