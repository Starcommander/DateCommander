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
    for (String val : entries)
    {
      val = escapeHtml(val); // TODO: Dont escape here, maybe in html
      if (curCol%col == 0) { sb.append("<tr>"); }
      sb.append("<"+tab+">").append(val).append("</"+tab+">");
      curCol ++;
      if (curCol%col == 0) { sb.append("</tr>"); tab = "td"; }
    }
    sb.append("</table>");
    return sb;
  }
  
  private static String escapeHtml(String html)
  {
    return html.replaceAll("&", "&amp;").replaceAll("<", "&lt;").replaceAll(">", "&gt;");
  }
}
