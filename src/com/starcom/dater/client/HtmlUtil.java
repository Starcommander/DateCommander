package com.starcom.dater.client;

import java.util.ArrayList;

import com.starcom.dater.shared.prims.Bool;

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
      val = escapeHtml(val); // Dont allow side attack scripts.
      if (val.endsWith(".png"))
      {
        val = "<img src='" + val + "' alt='" + val.substring(0,val.length()-4) + "' />";
      }
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
  
  public static String markupToHtml(String markup)
  {
    markup = escapeHtml(markup);
    StringBuilder sb = new StringBuilder();
    Bool ul = new Bool(false); // unordered list
    Bool ol = new Bool(false); // ordered list
    for (String line : markup.split("\n"))
    {
      if (line.startsWith("* ")) { line = enterList(line, false, ul); }
      else if (line.startsWith("1. ")) { line = enterList(line, true, ol); }
      else if (ul.b) { line = "</ul>" + line; }
      else if (ol.b) { line = "</ol>" + line; }
      
      if (line.startsWith("http://") || line.startsWith("https://"))
      {
        line = "<a href='" + line + "'>Link</a>";
      }
      sb.append(line);
    }
    return sb.toString();
  }
  
  private static String enterList(String line, boolean ordered, Bool wasEntered)
  {
    if (ordered)
    {
      line = line.substring(3);
      if (wasEntered.b) { line = "<li>" + line + "</li>"; }
      else { wasEntered.b = true; line = "<ol><li>" + line + "</li>"; }
    }
    else
    {
      line = line.substring(2);
      if (wasEntered.b) { line = "<li>" + line + "</li>"; }
      else { wasEntered.b = true; line = "<ul><li>" + line + "</li>"; }
    }
    return line;
  }

  public static String escapeHtml(String html)
  {
    return html.replaceAll("&", "&amp;").replaceAll("<", "&lt;").replaceAll(">", "&gt;");
  }
}
