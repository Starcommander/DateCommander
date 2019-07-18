package com.starcom.dater.shared.lang;

public class TextDE extends Text
{
  @Override public String getError() { return "Fehler"; }
  @Override public String getEnterSurvey() { return "Zur Umfrage"; }
  @Override public String getEditSurvey() { return "Umfrage bearbeiten"; }
  @Override public String getGreetAdminHtml() { return "<b>Hallo Admin!</b><br/><br/>Du kannst direkt die Umfrage<br/>starten, oder diese bearbeiten.<br/><br/>"; }
  @Override public String getCookieNeeded()
  {
    return "<b>Cookies werden benötigt, um Benutzer<br/>und Admin der Umfrage zu identifizieren.</b><br/>"
     + "<b>Sie müssen Cookies aktzeptieren, um diese Anwendung zu nutzen!</b><br/><br/>"
     + "Wenn sie Cookies nicht aktzeptieren, können sie hier nicht fortfahren.";
  }
}
