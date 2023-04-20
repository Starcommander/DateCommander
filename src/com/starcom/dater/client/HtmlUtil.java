package com.starcom.dater.client;

import java.util.ArrayList;

import com.starcom.dater.client.util.DaterUtils;
import com.starcom.dater.shared.prims.Bool;

public class HtmlUtil
{
  public static StringBuilder buildShareButton(String link)
  {
    StringBuilder sb = new StringBuilder();
    sb.append("<a href='");
    sb.append("mailto:?to=&subject=Share%20Dater%20WebApp&body=");
    sb.append(link.replace(":", "%3A").replace("/", "%2F"));
    sb.append("'>Share: MAILTO</a><br/>");
    
    sb.append("<a href='");
    if (DaterUtils.getOsType() == DaterUtils.OsType.Mac) { sb.append("sms:;body="); }
    else { sb.append("sms:?body="); }
    sb.append(link);
    sb.append("'>Share: SMS</a>");
    return sb;
  }
  
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
      String nl = "<br/>";
      if (line.startsWith("* ")) { line = enterList(line, false, ul); nl = ""; }
      else if (line.startsWith("1. ")) { line = enterList(line, true, ol); nl = ""; }
      else if (ul.b) { line = "</ul>" + line; }
      else if (ol.b) { line = "</ol>" + line; }
      
      if (line.startsWith("http://") || line.startsWith("https://"))
      {
        line = "<a href='" + line + "'>Link</a>";
        nl = "";
      }
      sb.append(line + nl);
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
