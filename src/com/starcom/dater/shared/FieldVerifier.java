package com.starcom.dater.shared;

/**
 * <p>
 * FieldVerifier validates that the name the user enters is valid.
 * </p>
 * <p>
 * This class is in the <code>shared</code> package because we use it in both
 * the client code and on the server. On the client, we verify that the name is
 * valid before sending an RPC request so the user doesn't have to wait for a
 * network round trip to get feedback. On the server, we verify that the name is
 * correct to ensure that the input is correct regardless of where the RPC
 * originates.
 * </p>
 * <p>
 * When creating a class that is used on both the client and the server, be sure
 * that all code is translatable and does not use native JavaScript. Code that
 * is not translatable (such as code that interacts with a database or the file
 * system) cannot be compiled into client-side JavaScript. Code that uses native
 * JavaScript (such as Widgets) cannot be run on the server.
 * </p>
 */
public class FieldVerifier
{
  public enum AnalyzerResult { ERROR, NEW_SURVEY, SURVEY_ENTRY, SURVEY_EDIT };
  public enum FieldList { USER_NAME, USER_NAME_ID, SURVEY_NAME, SURVEY_DESCRIPTION, SURVEY_ID, B_USR_ADM, B_USR_NEW, CH, U_CH };
  public enum CookieList { CookAllowed, DaterName, DaterNameId };
  public enum UrlParameter { SurveyId, ViewType };
  public enum ReqType { GetSurvey, GetSurveyTable };

  /**
   * Verifies that the specified name is valid for our service.
   * 
   * In this example, we only require that the name is at least four
   * characters. In your application, you can use more complex checks to ensure
   * that usernames, passwords, email addresses, URLs, and other fields have the
   * proper syntax.
   * 
   * @param name the name to validate
   * @return true if valid, false if invalid
   */
  public static boolean isValidName(String name)
  {
    if (name == null) {
      return false;
    }
    return name.length() > 3;
  }
  
  public static boolean isValidID(String id)
  {
    if (id == null) { return false; }
    if (id.length() < 10) { return false; }
    for (char c : id.toCharArray())
    {
      if (Character.isLetter(c)) { continue; }
      if (Character.isDigit(c)) { continue; }
      if (c == '-') { continue; }
      return false;
    }
    return true;
  }
}
