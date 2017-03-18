package ru.marat.smarthome.app.task;

public class ProgressUpdateDataWrraper {

  private String message;
  private boolean updateProgressBar;

  public ProgressUpdateDataWrraper(String message, boolean updateProgressBar) {
    this.message = message;
    this.updateProgressBar = updateProgressBar;
  }

  public ProgressUpdateDataWrraper(String message) {
    this.message = message;
    this.updateProgressBar = false;
  }

  public String getMessage() {
    return message;
  }

  public boolean isUpdateProgressBar() {
    return updateProgressBar;
  }
}
