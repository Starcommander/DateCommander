package com.starcom.dater.shared.lang;

/** For a new Language you have to edit:
 *  <br/><li>Text.selectLanguage(s) --> Add your language.
 *  <br/><li>DaterWebApp.gwt.xml --> Add your language as extend-property.
 *  <br/><li>TextXX.java --> Create a new Language file, implements Text. */
public abstract class Text
{
  static Text curLang = new TextEN();

  public abstract String getError();
  public abstract String getEnterSurvey();
  public abstract String getEditSurvey();
  public abstract String getGreetAdminHtml();
  public abstract String getCookieNeeded();
  public abstract String getAcceptCookies();
  public static void selectLanguage(String lang)
  { // Default is en.
    if (lang.equalsIgnoreCase("de")) { curLang = new TextDE(); }
  }
  public static Text getCur() { return curLang; }
}
