package com.starcom.dater.client;

import java.util.ArrayList;

public class HtmlUtil
{
  public static StringBuilder buildTable(ArrayList<String> entries, int col)
  {
    StringBuilder sb = new StringBuilder();
    sb.append("<table>");
    int curCol = 0;
    String tab = "th";
    String tabClass = " class='mylittleborder'";
    boolean firstL = true;
    boolean firstE = true;
    for (String val : entries)
    {
      val = escapeHtml(val); // TODO: Dont escape all here, maybe in html
      if (curCol%col == 0) { sb.append("<tr>"); }
      sb.append("<"+tab+tabClass+">");
      doRotate(sb, val, firstL && (!firstE));
      sb.append("</"+tab+">");
      curCol ++;
      if (firstE) { firstE = false; }
      if (curCol%col == 0) { sb.append("</tr>"); tab = "td";  firstL = false; }
    }
    sb.append("</table>");
    return sb;
  }
  
  private static void doRotate(StringBuilder sb, String htmlLine, boolean rotate)
  {
    if (!rotate) { sb.append(htmlLine); }
    else
    {
      for (char c : htmlLine.toCharArray())
      {
        sb.append("<div class='myrotate'>").append(c).append("</div><br/>");
      }
    }
  }
  
  private static String escapeHtml(String html)
  {
    return html.replaceAll("&", "&amp;").replaceAll("<", "&lt;").replaceAll(">", "&gt;");
  }
}
