package org.androidtransfuse.util;

import java.util.Locale;

/**
 * Created by dan on 5/9/15.
 */
public class StringUtil {
  public static String upperFirst(String name) {
      return name.substring(0, 1).toUpperCase(Locale.ENGLISH) + name.substring(1);
  }
}
