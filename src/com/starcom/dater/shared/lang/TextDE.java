package com.starcom.dater.shared.lang;

public class TextDE extends Text
{
  @Override public String getError() { return "Fehler"; }
  @Override public String getClose() { return "Schlie\u00dfen"; }
  @Override public String getName() { return "Name"; }
  @Override public String getTitle() { return "Titel"; }
  @Override public String getDescription() { return "Beschreibung"; }
  @Override public String getLeastFourChars() { return "Bitte mindestens 4 Buchstaben eingeben!"; }
  @Override public String getEnterSurvey() { return "Zur Umfrage"; }
  @Override public String getEnterName() { return "Bitte Namen eingeben:"; }
  @Override public String getEnterTitleDescription() { return "Bitte Eingabe von\nTitel und Beschreibung:"; }
  @Override public String getEditSurveyChoice() { return "Auswahl bearbeiten"; }
  @Override public String getEditSurveyForm() { return "Formular bearbeiten"; }
  @Override public String getGreetAdminHtml() { return "<b>Hallo Admin!</b><br/><br/>Du kannst direkt die Umfrage<br/>starten, oder diese bearbeiten.<br/><br/>"; }
  @Override public String getGreetUsrHtml() { return "<b>Hallo Benutzer!</b><br/><br/>Du kannst dich hier eintragen,<br/>oder direkt die Umfrage anzeigen.<br/><br/>"; }
  @Override public String getAcceptCookies() { return "Akzeptiere Cookies"; }
  @Override public String getSuccessfulCreated() { return "Erfolgreich erzeugt!"; }
  @Override public String getForwardingToSurvey() { return "Weiterleitung zur Umfrage ..."; }
  @Override public String getSendToServer() { return "Sende an den Server"; }
  @Override public String getSend() { return "Senden"; }
  @Override public String getEdit() { return "Bearbeiten"; }
  @Override public String getReload() { return "Erneut laden"; }
  @Override public String getServerReplies() { return "Server antwortet:"; }
  @Override public String getServerGetDataError() { return "Fehler beim Abrufen von Daten vom Server!"; }
  @Override public String getNoResultFromServer() { return "Keine Antwort vom Server!"; }
  @Override public String getUseMarkdown() { return "Verwende Markdown: Verwendunv von * oder 1. oder http:// wird konvertiert."; }
  @Override public String getNewSurvey() { return "Neue Umfrage erstellen"; }
  @Override public String getNewTextPaper() { return "Neues Textblatt erstellen"; }
  @Override public String getCookieNeededHtml()
  {
    return "<b>Cookies werden ben\u00f6tigt, um Benutzer<br/>und Admin der Umfrage zu identifizieren.</b><br/>"
     + "<b>Sie m\u00fcssen Cookies aktzeptieren,<br/>um diese Anwendung zu nutzen!</b><br/><br/>"
     + "Es werden ausschliesslich funktionale interne Cookies verwendet.<br/>"
     + "Wenn sie Cookies nicht aktzeptieren, k\u00f6nnen sie hier nicht fortfahren.";
  }
}
