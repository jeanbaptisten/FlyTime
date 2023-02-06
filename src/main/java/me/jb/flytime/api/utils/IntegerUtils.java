package me.jb.flytime.api.utils;

public class IntegerUtils {

  public static boolean isNumeric(String string) {
    if (string.trim().equals("")) return false;

    try {
      Integer.parseInt(string.trim());
    } catch (NumberFormatException e) {
      return false;
    }
    return true;
  }

  public static String parseTime(Integer totalSecs) {
    int h = totalSecs / 3600;
    int m = (totalSecs % 3600) / 60;
    int s = totalSecs % 60;
    String sh = (h > 0 ? h + " " + "h" : "");
    String sm =
        (m < 10 && m > 0 && h > 0 ? "0" : "")
            + (m > 0 ? (h > 0 && s == 0 ? String.valueOf(m) : String.valueOf(m) + " " + "mn") : "");
    String ss =
        (s == 0 && (h > 0 || m > 0)
            ? ""
            : (s < 10 && (h > 0 || m > 0) ? "0" : "") + String.valueOf(s) + " " + "s");
    return sh + (h > 0 ? " " : "") + sm + (m > 0 ? " " : "") + ss;
  }
}
