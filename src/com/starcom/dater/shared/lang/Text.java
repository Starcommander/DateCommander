package com.starcom.dater.shared.lang;

public abstract class Text
{
  static Text curLang = new TextEN();

  public abstract String getError();
  public abstract String getEnterSurvey();
  public abstract String getEditSurvey();
  public abstract String getGreetAdminHtml();
  public abstract String getCookieNeeded();
  
  public static void selectLanguage(String lang)
  { // Default is en.
    if (lang.equalsIgnoreCase("de")) { curLang = new TextDE(); }
  }
  public static Text getCur() { return curLang; }
}
