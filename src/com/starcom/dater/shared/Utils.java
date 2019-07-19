package com.starcom.dater.shared;

import java.util.Date;
import java.util.HashMap;
import java.util.Map.Entry;

import com.google.gwt.user.client.ui.HTML;

public class Utils
{
  public static String generateID()
  {
    // TODO: Drop some letters into the id, for more security.
    long dateNow = new Date().getTime();
    long random = (long)(Math.random() * 1000.0 * 1000.0 * 1000.0);
    return dateNow + "-" + random;
  }

  public static String getUriParameter(String key, String uri)
  {
    for (String curParam : uri.split("\\&|\\?"))
    {
      if (curParam.startsWith(key + "="))
      {
        return curParam.substring(curParam.indexOf("=") + 1);
      }
    }
    return null;
  }
  
  public static HashMap<String,String> toHashMap(String s)
  {
    HashMap<String,String> h = new HashMap<String,String>();
    for (String l : s.split("\n"))
    {
      if (!l.contains("=")) { continue; }
      int index = l.indexOf('=');
      String key = l.substring(0, index);
      String val = l.substring(index + 1);
      val = val.replace(';', '\n');
      h.put(key, val);
    }
    return h;
  }
  
  /** Will loose ';' and switch to ',' for easy convertion. */
  public static String toString(HashMap<String,String> h)
  {
    StringBuilder sb = new StringBuilder();
    for (Entry<String, String> e : h.entrySet())
    {
      sb.append(e.getKey()).append("=");
      String val = e.getValue().replace(';', ',').replace('\n', ';');
      sb.append(val).append('\n');
    }
    return sb.toString();
  }
  
  public static Date getDateInYears()
  {
    long dateNow = new Date().getTime();
    long dateInYears = dateNow + (1000L * 60L * 60L * 24L * 265L * 5L);
    return new Date(dateInYears);
  }

  public static String htmlToText(String result)
  {
    HTML html = new HTML();
    html.setHTML(result);
    return html.getText();
  }

  public static String cutText(String txt, int maxLength)
  {
    if (txt.length() <= maxLength) { return txt; }
    return txt.substring(0, maxLength);
  }
}
