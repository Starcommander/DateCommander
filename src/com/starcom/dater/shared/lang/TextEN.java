package com.starcom.dater.shared.lang;

public class TextEN extends Text
{
  @Override public String getError() { return "Error"; }
  @Override public String getEnterSurvey() { return "Enter Survey"; }
  @Override public String getEditSurvey() { return "Edit Survey"; }
  @Override public String getGreetAdminHtml() { return "<b>Hello Admin!</b><br/><br/>You can execute this Survey<br/>directly, or edit the Survey.<br/><br/>"; }
  @Override public String getCookieNeeded()
  {
    return "<b>Cookies are used to idendificate survey admins and users.</b><br/>"
     + "<b>You have to accept cookies to use this web-app!</b><br/><br/>"
     + "If you do not accept, please leave this service.";
  }

}
