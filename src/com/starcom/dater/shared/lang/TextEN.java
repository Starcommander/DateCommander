package com.starcom.dater.shared.lang;

public class TextEN extends Text
{
  @Override public String getError() { return "Error"; }
  @Override public String getClose() { return "Close"; }
  @Override public String getEnterSurvey() { return "Enter Survey"; }
  @Override public String getEditSurveyChoice() { return "Edit Choice"; }
  @Override public String getEditSurveyForm() { return "Edit Form"; }
  @Override public String getGreetAdminHtml() { return "<b>Hello Admin!</b><br/><br/>You can execute this Survey<br/>directly, or edit the Survey.<br/><br/>"; }
  @Override public String getGreetUsrHtml() { return "<b>Hello user!</b><br/><br/>You can enter this Survey,<br/>or show the results.<br/><br/>"; }
  @Override public String getAcceptCookies() { return "Accept Cookies"; }
  @Override public String getCookieNeeded()
  {
    return "<b>Cookies are used to idendificate survey admins and users.</b><br/>"
     + "<b>You have to accept cookies to use this web-app!</b><br/><br/>"
     + "If you do not accept, please leave this service.";
  }

}
