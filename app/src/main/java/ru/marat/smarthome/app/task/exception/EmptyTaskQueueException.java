package ru.marat.smarthome.app.task.exception;

/**
 * Exception for the case if tasks queue is empty
 */
public class EmptyTaskQueueException extends Exception {

  public EmptyTaskQueueException() {
    super();
  }

  public EmptyTaskQueueException(String message) {
    super(message);
  }
}
