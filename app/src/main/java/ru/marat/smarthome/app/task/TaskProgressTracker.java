package ru.marat.smarthome.app.task;

public interface TaskProgressTracker<Progress> {

  void onProgress(Progress message);

  void onComplete();
}
