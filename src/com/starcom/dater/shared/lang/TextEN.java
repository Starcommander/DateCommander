package com.starcom.dater.shared.lang;

public class TextEN extends Text
{
  @Override public String getError() { return "Error"; }
  @Override public String getClose() { return "Close"; }
  @Override public String getName() { return "Name"; }
  @Override public String getTitle() { return "Title"; }
  @Override public String getDescription() { return "Description"; }
  @Override public String getLeastFourChars() { return "Please enter at least four characters"; }
  @Override public String getEnterSurvey() { return "Enter Survey"; }
  @Override public String getEnterName() { return "Please enter your name:"; }
  @Override public String getEnterTitleDescription() { return "Please enter survey\ntitle and description:"; }
  @Override public String getEditSurveyChoice() { return "Edit Choice"; }
  @Override public String getEditSurveyForm() { return "Edit Form"; }
  @Override public String getGreetAdminHtml() { return "<b>Hello Admin!</b><br/><br/>You can execute this Survey<br/>directly, or edit the Survey.<br/><br/>"; }
  @Override public String getGreetUsrHtml() { return "<b>Hello user!</b><br/><br/>You can enter this Survey,<br/>or show the results.<br/><br/>"; }
  @Override public String getAcceptCookies() { return "Accept Cookies"; }
  @Override public String getSuccessfulCreated() { return "Successful created!"; }
  @Override public String getForwardingToSurvey() { return "Forwarding to Survey ..."; }
  @Override public String getSendToServer() { return "Send to server"; }
  @Override public String getSend() { return "Send"; }
  @Override public String getEdit() { return "Edit"; }
  @Override public String getReload() { return "Reload"; }
  @Override public String getServerReplies() { return "Server replies:"; }
  @Override public String getServerGetDataError() { return "Error getting data from server!"; }
  @Override public String getNoResultFromServer() { return "No result from server!"; }
  @Override public String getUseMarkdown() { return "Use markdown: The use of * or 1. or http:// is converted."; }
  @Override public String getNewSurvey() { return "Create new survey"; }
  @Override public String getNewTextPaper() { return "Create new text paper"; }
  @Override public String getFirstEditPaper() { return "Please edit paper first"; }
  @Override public String getCookieNeededHtml()
  {
    return "<b>Cookies are used to idendificate survey admins and users.</b><br/>"
     + "<b>You have to accept cookies to use this web-app!</b><br/><br/>"
     + "Only internal functional cookies are used.<br/>"
     + "If you do not accept, you can not use this service.";
  }

}
