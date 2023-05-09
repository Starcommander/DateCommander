package com.starcom.dater.server.service;

import com.starcom.dater.shared.service.TextService;
import com.starcom.dater.client.util.DaterUtils.ViewType;
import com.starcom.dater.server.ServUtils;
import com.starcom.dater.shared.FieldVerifier;
import com.starcom.dater.shared.FieldVerifier.FieldList;
import com.starcom.dater.shared.Utils;

import java.io.File;
import java.util.HashMap;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * The server-side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class TextServiceImpl extends RemoteServiceServlet implements TextService
{
  /** Server side implementation when sending text data to the server.<br>
   * @param input Must have this form: ReqType:UserID:SurveyID
   * @return The result for the client. */
  public String sendTextToServer(String input) throws IllegalArgumentException
  {
    if (input.startsWith(ViewType.EdForm.toString() + ":") || input.startsWith(ViewType.EdChoice.toString() + ":"))
    {
      String[] fields = input.split(":");
      if ( fields.length != 3 ) throw new IllegalArgumentException("Fields len not ok!");
      if (!FieldVerifier.isValidID(fields[1])) throw new IllegalArgumentException("UserID not ok!");
      if (!FieldVerifier.isValidID(fields[2])) throw new IllegalArgumentException("SurveyID not ok!");
      String ret = sendSurvey(fields[1], fields[2]).toString();
      return ret;
    }
    else if (input.startsWith(ViewType.ToSurvey.toString() + ":"))
    {
      String[] fields = input.split(":");
      if ( fields.length != 3 ) throw new IllegalArgumentException("Fields len not ok!");
      if (!FieldVerifier.isValidID(fields[1])) throw new IllegalArgumentException("UserID not ok!");
      if (!FieldVerifier.isValidID(fields[2])) throw new IllegalArgumentException("SurveyID not ok!");
      String ret = sendSurveyTable(fields[1], fields[2]).toString();
      return ret;
    }
    else if (input.startsWith(ViewType.ToTextPaper.toString() + ":"))
    { // Fields: view user survId txt
      String[] fields = input.split(":");
      String surveyId;
      if ( fields.length < 3 ) throw new IllegalArgumentException("Fields len not ok!");
      if (!FieldVerifier.isValidID(fields[1])) throw new IllegalArgumentException("UserID not ok!");
      if (fields[2].equals("null")) { surveyId = createNewIdDir(); }
      else { surveyId = fields[2]; }
      if (!FieldVerifier.isValidID(surveyId)) throw new IllegalArgumentException("SurveyID not ok!");
      String text = null;
      if (fields.length == 4)
      {
        text = input.substring(fields[0].length() + 1 + fields[1].length() + 1 + fields[2].length() + 1 );
      }
      String ret = sendTextPaper(fields[1], surveyId, text).toString();
      return surveyId + ":" + ret; //TODO: Better StringBuilder?
    }
    else
    {
      throw new IllegalArgumentException("Unknown Service received!");
    }
  }

  /** Creates the content for sending the table to the client.<br>
   * The userID and surveyID must be checked before. */
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

  /** Creates the content for sending the table to the client.<br>
   * The userID and surveyID must be checked before. */
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
  
  /** Creates the content for sending the txt-response to the client.<br>
   * The userID and surveyID must be checked before. */
  private StringBuilder sendTextPaper(String userId, String surveyID, String newText) throws IllegalArgumentException
  {
    File userDir = ServUtils.getWorkingDirExisting(surveyID, false);
    File userFile = new File(userDir, "Content.txt");
    StringBuilder sb = new StringBuilder();
    if (newText == null)
    {
    	sb.append(ServUtils.readTextFile(userFile));
    }
    else
    {
    	ServUtils.writeTextFile(userFile, newText);
    	sb.append(newText);
    }
    return sb;
  }
  
  private String createNewIdDir()
  {
      //TODO: DuplicateCode in FormUploadResponse.java: NEW_SURVEY
      String surveyId = Utils.generateID();
      ServUtils.getWorkingDirNew(surveyId);
      return surveyId;
  }

  /** Appends the values isUsrAdm and isUsrNew.<br>
   * The userID and surveyID must be checked before. */
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
