package com.starcom.dater.server;

import com.starcom.dater.client.GreetingService;
import com.starcom.dater.shared.FieldVerifier;
import com.starcom.dater.shared.FieldVerifier.FieldList;
import com.starcom.dater.shared.FieldVerifier.ReqType;
import com.starcom.dater.shared.Utils;

import java.io.File;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * The server-side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class GreetingServiceImpl extends RemoteServiceServlet implements GreetingService
{
  
  public String greetServer(String input) throws IllegalArgumentException
  {
    if (input.startsWith(ReqType.GetSurvey.toString() + ":"))
    {
      String[] fields = input.split(":");
      if ( fields.length != 3 ) throw new IllegalArgumentException("Fields len not ok!");
      if (!FieldVerifier.isValidID(fields[1])) throw new IllegalArgumentException("UserID not ok!");
      if (!FieldVerifier.isValidID(fields[2])) throw new IllegalArgumentException("SurveyID not ok!");
      String ret = sendSurvey(fields[1], fields[2]);
      return ret;
    }
    else if (input.startsWith(ReqType.GetSurveyTable.toString() + ":"))
    {
      String[] fields = input.split(":");
      if ( fields.length != 3 ) throw new IllegalArgumentException("Fields len not ok!");
      if (!FieldVerifier.isValidID(fields[1])) throw new IllegalArgumentException("UserID not ok!");
      if (!FieldVerifier.isValidID(fields[2])) throw new IllegalArgumentException("SurveyID not ok!");
      String ret = sendSurveyTable(fields[1], fields[2]);
      return ret;
    }
    else
    {
      throw new IllegalArgumentException(
          "Unknown Service received!");
    }
//    // Verify that the input is valid.
//    if (!FieldVerifier.isValidName(input))
//    {
//      // If the input is not valid, throw an IllegalArgumentException back to
//      // the client.
//      throw new IllegalArgumentException(
//          "Name must be at least 4 characters long");
//    }
//
//    String serverInfo = getServletContext().getServerInfo();
//    String userAgent = getThreadLocalRequest().getHeader("User-Agent");
//
//    // Escape data from the client to avoid cross-site script vulnerabilities.
//    input = escapeHtml(input);
//    userAgent = escapeHtml(userAgent);
//
//    return "Hello, " + input + "!<br><br>I am running " + serverInfo
//        + ".<br><br>It looks like you are using:<br>" + userAgent;
  }

  private String sendSurveyTable(String userId, String surveyID)
  {
    File userDir = ServUtils.getWorkingDirExisting(surveyID, true);
    File srcFile = new File(userDir.getParent(), ServUtils.MAIN_FILE);
    if (!srcFile.exists())
    {
      throw new IllegalArgumentException("Unknown Survey ID: " + Utils.cutText(surveyID, 30));
    }
    StringBuilder sb = ServUtils.readTextFile(srcFile);
    if (sb == null)
    {
      throw new IllegalArgumentException("Error on reading Survey!");
    }
    for (String f : userDir.list())
    {
      StringBuilder usb = ServUtils.readTextFile(new File(userDir,f));
      if (usb == null)
      {
        throw new IllegalArgumentException("Error on reading Survey user: " + f);
      }
      sb.append("\n-\n").append(usb);
    }
    return sb.toString();
  }

  /** The userID and surveyID must be checked before. */
  private String sendSurvey(String userId, String surveyID) throws IllegalArgumentException
  {
    File workDir = ServUtils.getWorkingDirExisting(surveyID, false);
    File srcFile = new File(workDir, ServUtils.MAIN_FILE);
    if (!srcFile.exists())
    {
      throw new IllegalArgumentException("Unknown Survey ID: " + Utils.cutText(surveyID, 30));
    }
    StringBuilder sb = ServUtils.readTextFile(srcFile);
    if (sb == null)
    {
      throw new IllegalArgumentException("Error on reading Survey!");
    }
    String viewType = ServUtils.requestViewType(userId, surveyID);
    sb.append("\n").append(FieldList.VIEW_TYPE.toString()).append("=").append(viewType);
    return sb.toString();
  }

}
