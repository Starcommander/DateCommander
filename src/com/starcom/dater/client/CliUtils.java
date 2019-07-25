package com.starcom.dater.client;

import java.util.HashMap;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.starcom.dater.shared.FieldVerifier.FieldList;
import com.starcom.dater.shared.FieldVerifier.UrlParameter;
import com.starcom.dater.shared.WebXml;

public class CliUtils
{
  public static final String CHECK_IMG_MAYBE = "img/maybe.png";
  public static final String CHECK_IMG_YES = "img/yes.png";
  public static final String CHECK_IMG_NO = "img/no.png";
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
  
  /** Get BaseUrl: <b>protocol://hostname:port/</b> */
  public static String getBaseUrl()
  {
    String prot = Window.Location.getProtocol();
    String host = Window.Location.getHost();
    return prot + "//" + host + "/";
  }
  
  public static void gotoUrl(String surveyId, ViewType viewType)
  {
    StringBuilder url = new StringBuilder(GWT.getModuleBaseURL());
    if (!url.toString().endsWith(WebXml.DATER_BASE))
    {
      throw new IllegalArgumentException("Base url wrong: " + url);
    }
    int end = url.length() - WebXml.DATER_BASE.length();
    url = new StringBuilder(url.substring(0, end));
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
