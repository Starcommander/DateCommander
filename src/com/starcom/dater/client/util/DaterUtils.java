package com.starcom.dater.client.util;

import java.util.HashMap;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.Window;
import com.starcom.dater.shared.FieldVerifier.CookieList;
import com.starcom.dater.shared.FieldVerifier.FieldList;
import com.starcom.dater.shared.FieldVerifier.UrlParameter;
import com.starcom.dater.shared.Utils;
import com.starcom.dater.shared.WebXml;

public class DaterUtils
{
  final static String COOKIE_NAME_ID = CookieList.DaterNameId.toString();
  public static final String CHECK_IMG_MAYBE = "img/maybe.png";
  public static final String CHECK_IMG_YES = "img/yes.png";
  public static final String CHECK_IMG_NO = "img/no.png";
  public enum ViewType { EdForm, EdChoice, ToSurvey, ToTextPaper, Overview };
  public enum OsType {Win, Unix, Linux, Mac, Sun, Unknown}
  
  /** This is the Value of UriParameter ViewType, for request on JS. */
  public static ViewType requestViewTypeJS()
  {
    String viewTypeS = Window.Location.getParameter(UrlParameter.ViewType.toString());
    if (viewTypeS == null) { return ViewType.Overview; }
    ViewType viewType = ViewType.valueOf(viewTypeS);
    if (viewType == null) { return ViewType.Overview; }
    return viewType;
  }
  
  /** @return The uri-parameter-value, or null if missing. */
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
  
  /** Creates and sets the User-Id-Cookie, if not present.<br>
   * Otherwise just read from cookies.
   * @return The existing or new userID */
  public static String getCreateUserIdCookie()
  {
    String userId = Cookies.getCookie(COOKIE_NAME_ID);
    if (userId == null)
    {
      userId = Utils.generateID();
      Cookies.setCookie(COOKIE_NAME_ID, userId, Utils.getDateInYears());
    }
    return userId;
  }
  
  /** Builds the expected url with parameters, where surveyId may be null */
  public static String getUrl(String surveyId, ViewType viewType)
  {
    StringBuilder url = new StringBuilder(GWT.getModuleBaseURL()); // ???
    if (!url.toString().endsWith(WebXml.DATER_BASE))
    {
      throw new IllegalArgumentException("Base url wrong: " + url);
    }
    int end = url.length() - WebXml.DATER_BASE.length();
    url = new StringBuilder(url.substring(0, end));
    url.append("?").append(UrlParameter.ViewType.toString()).append("=").append(viewType.toString());
    if (surveyId != null)
    {
      url.append("&").append(UrlParameter.SurveyId.toString()).append("=").append(surveyId);
    }
    return url.toString();
  }
  
  public static void gotoUrl(String surveyId, ViewType viewType)
  {
    Window.Location.replace(getUrl(surveyId, viewType));
  }
  
  public static OsType getOsType()
  {
    String osName = Window.Navigator.getAppVersion();
    if (osName.indexOf("Win") != -1)
    {
      return OsType.Win;
    }
    else if (osName.indexOf("Mac") != -1)
    {
      return OsType.Mac;
    }
    else if (osName.indexOf("X11") != -1)
    {
      return OsType.Unix;
    }
    else if (osName.indexOf("Linux") != -1)
    {
      return OsType.Linux;
    }
    else if (osName.indexOf("SunOS") != -1)
    {
      return OsType.Sun;
    }
    return OsType.Unknown;
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
