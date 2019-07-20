package com.starcom.dater.server;

import com.starcom.dater.client.GreetingService;
import com.starcom.dater.shared.FieldVerifier;
import com.starcom.dater.shared.FieldVerifier.FieldList;
import com.starcom.dater.shared.FieldVerifier.ReqType;
import com.starcom.dater.shared.Utils;

import java.io.File;
import java.util.HashMap;

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
      String ret = sendSurvey(fields[1], fields[2]).toString();
      return ret;
    }
    else if (input.startsWith(ReqType.GetSurveyTable.toString() + ":"))
    {
      String[] fields = input.split(":");
      if ( fields.length != 3 ) throw new IllegalArgumentException("Fields len not ok!");
      if (!FieldVerifier.isValidID(fields[1])) throw new IllegalArgumentException("UserID not ok!");
      if (!FieldVerifier.isValidID(fields[2])) throw new IllegalArgumentException("SurveyID not ok!");
      String ret = sendSurveyTable(fields[1], fields[2]).toString();
      return ret;
    }
    else
    {
      throw new IllegalArgumentException(
          "Unknown Service received!");
    }
  }

  private StringBuilder sendSurveyTable(String userId, String surveyID)
  {
    File userDir = ServUtils.getWorkingDirExisting(surveyID, true);
    StringBuilder sb = sendSurvey(userId, surveyID);
    for (String f : userDir.list())
    {
      StringBuilder usb = ServUtils.readTextFile(new File(userDir,f));
      if (usb == null)
      {
        throw new IllegalArgumentException("Error on reading Survey user: " + f);
      }
      sb.append("\n-\n").append(usb);
    }
    return sb;
  }
  
  /** The userID and surveyID must be checked before. */
  private StringBuilder sendSurvey(String userId, String surveyID) throws IllegalArgumentException
  {
    File userDir = ServUtils.getWorkingDirExisting(surveyID, true);
    StringBuilder sb = obtainMain(userId, surveyID);
    File userFile = new File(userDir,userId);
    if (!userFile.exists()) { return sb; }
    StringBuilder usbS = ServUtils.readTextFile(userFile);
    HashMap<String, String> usbMap = Utils.toHashMap(usbS.toString());
    for (int i=0; i<Utils.MAX_CHOICES; i++)
    {
      String val = usbMap.get(FieldList.CH.toString() + i);
      if (val == null) { break; }
      sb.append("\n").append(FieldList.U_CH.toString() + i).append("=").append(val);
    }
    return sb;
  }

  /** The userID and surveyID must be checked before. */
  private StringBuilder obtainMain(String userId, String surveyID) throws IllegalArgumentException
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
    boolean isAdm = ServUtils.requestIsAdm(userId, surveyID);
    boolean isNew = ServUtils.requestNewUsr(userId, surveyID);
    sb.append("\n").append(FieldList.B_USR_ADM.toString()).append("=").append("" + isAdm);
    sb.append("\n").append(FieldList.B_USR_NEW.toString()).append("=").append("" + isNew);
    return sb;
  }

}
