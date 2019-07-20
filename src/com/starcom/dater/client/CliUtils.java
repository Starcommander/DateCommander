package com.starcom.dater.client;

import java.util.HashMap;

import com.google.gwt.user.client.Window;
import com.starcom.dater.shared.FieldVerifier.FieldList;
import com.starcom.dater.shared.FieldVerifier.UrlParameter;

public class CliUtils
{
  public enum ViewType { EdForm, EdChoice, ToSurvey };
  
  /** This is a UriParameter set from JS, for request on JS. */
  public static ViewType requestViewTypeJS()
  {
    String viewTypeS = Window.Location.getParameter(UrlParameter.ViewType.toString());
    if (viewTypeS == null) { return ViewType.ToSurvey; }
    ViewType viewType = ViewType.valueOf(viewTypeS);
    if (viewType == null) { return ViewType.ToSurvey; }
    return viewType;
  }
  
  /** @return The value, or null if missing. */
  public static String requestSurveyId()
  {
    String surveyId = Window.Location.getParameter(UrlParameter.SurveyId.toString());
    return surveyId;
  }
  
  public static void gotoUrl(String surveyId, ViewType viewType)
  {
    StringBuilder url = new StringBuilder(Window.Location.getProtocol());
    url.append("//").append(Window.Location.getHost());
    url.append("?").append(UrlParameter.SurveyId.toString()).append("=").append(surveyId);
    url.append("&").append(UrlParameter.ViewType.toString()).append("=").append(viewType.toString());
    Window.Location.replace(url.toString());
  }
  
  public static boolean requestAdmin(HashMap<String, String> prop)
  {
    return requestBool(prop, FieldList.B_USR_ADM);
  }
  
  public static boolean requestNewUser(HashMap<String, String> prop)
  {
    return requestBool(prop, FieldList.B_USR_NEW);
  }
  
  private static boolean requestBool(HashMap<String, String> prop, FieldList field)
  {
    String viewTypeS = prop.get(field.toString());
    if (viewTypeS == null) { return false; }
    return viewTypeS.equals("true");
  }
}
