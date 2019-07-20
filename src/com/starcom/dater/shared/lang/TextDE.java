package com.starcom.dater.shared.lang;

public class TextDE extends Text
{
  @Override public String getError() { return "Fehler"; }
  @Override public String getClose() { return "Schlie\u00dfen"; }
  @Override public String getEnterSurvey() { return "Zur Umfrage"; }
  @Override public String getEditSurveyChoice() { return "Auswahl bearbeiten"; }
  @Override public String getEditSurveyForm() { return "Formular bearbeiten"; }
  @Override public String getGreetAdminHtml() { return "<b>Hallo Admin!</b><br/><br/>Du kannst direkt die Umfrage<br/>starten, oder diese bearbeiten.<br/><br/>"; }
  @Override public String getGreetUsrHtml() { return "<b>Hallo Benutzer!</b><br/><br/>Du kannst dich hier eintragen,<br/>oder direkt die Umfrage anzeigen.<br/><br/>"; }
  @Override public String getAcceptCookies() { return "Akzeptiere Cookies"; }
  @Override public String getCookieNeeded()
  {
    return "<b>Cookies werden ben\u00f6tigt, um Benutzer<br/>und Admin der Umfrage zu identifizieren.</b><br/>"
     + "<b>Sie m\u00fcssen Cookies aktzeptieren, um diese Anwendung zu nutzen!</b><br/><br/>"
     + "Wenn sie Cookies nicht aktzeptieren, k\u00f6nnen sie hier nicht fortfahren.";
  }
}
