package ru.marat.smarthome.app.task;

/**
 * Enumerator for task statuses
 */
public enum TaskStatus {
  READY(0),
  INPROGRESS(1),
  DONE(-1),
  ERROR(5);

  private int code;

  private TaskStatus(int code) {
    this.code = code;
  }

  public int getCode() {
    return code;
  }

  public static TaskStatus get(int code) {
    for (TaskStatus s : values()) {
      if (s.code == code) {
        return s;
      }
    }
    return null;
  }
}
