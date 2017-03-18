package ru.marat.smarthome.app.utils;

import android.graphics.Color;
import java.util.Random;

/**
 * Utilities
 */
public class ColorUtils {

  public static String generateRandomColor() {
    int[] colors = {Color.RED, Color.BLUE, Color.GREEN, Color.YELLOW, Color.CYAN, Color.MAGENTA,
        Color.GRAY};
    Random random = new Random();
    int intColor = random.nextInt(colors.length);
    return String.format("#66%06X", (0xFFFFFF & colors[intColor]));
  }
}
