package com.starcom.dater.shared.lang;

/** For a new Language you have to edit:
 *  <br/><li>Text.selectLanguage(s) --> Add your language.
 *  <br/><li>DaterWebApp.gwt.xml --> Add your language as extend-property.
 *  <br/><li>TextXX.java --> Create a new Language file, implements Text. */
public abstract class Text
{
  static Text curLang = new TextEN();

  public abstract String getError();
  public abstract String getClose();
  public abstract String getName();
  public abstract String getTitle();
  public abstract String getDescription();
  public abstract String getLeastFourChars();
  public abstract String getEnterSurvey();
  public abstract String getEnterName();
  public abstract String getEnterTitleDescription();
  public abstract String getEditSurveyChoice();
  public abstract String getEditSurveyForm();
  public abstract String getGreetAdminHtml();
  public abstract String getGreetUsrHtml();
  public abstract String getAcceptCookies();
  public abstract String getSuccessfulCreated();
  public abstract String getForwardingToSurvey();
  public abstract String getSendToServer();
  public abstract String getSend();
  public abstract String getEdit();
  public abstract String getReload();
  public abstract String getNoResultFromServer();
  public abstract String getServerReplies();
  public abstract String getServerGetDataError();
  public abstract String getCookieNeededHtml();
  public abstract String getUseMarkdown();
  public abstract String getNewSurvey();
  public abstract String getNewTextPaper();
  public abstract String getFirstEditPaper();
  public static void selectLanguage(String lang)
  { // Default is en.
    if (lang.equalsIgnoreCase("de")) { curLang = new TextDE(); }
  }
  public static Text getCur() { return curLang; }
}
