package com.starcom.dater.server;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.net.MalformedURLException;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import com.starcom.dater.client.DaterWebApp;
import com.starcom.dater.shared.FieldVerifier;
import com.starcom.dater.shared.FieldVerifier.AnalyzerResult;
import com.starcom.dater.shared.FieldVerifier.FieldList;
import com.starcom.dater.shared.WebXml;

public class ServUtils
{
  public static final String MAIN_FILE = "Main.txt";
  public static final String ADMIN_FILE = "Adm.txt";
  public static final String USER_SUB_DIR = "user";
  
//  public static Properties toProp(HashMap<String,String> fields)
//  {
//    Properties prop = new Properties();
//    for (Entry<String, String> e : fields.entrySet())
//    {
//      prop.setProperty(e.getKey(), e.getValue());
//    }
//    return prop;
//  }
  
  
  public static AnalyzerResult getAnalyzerResult(HashMap<String,String> prop, String requestorID)
  {
    if (!FieldVerifier.isValidID(requestorID)) { return AnalyzerResult.ERROR; }
    String surveyId = prop.get(FieldList.SURVEY_ID.toString());
    String name = prop.get(FieldList.USER_NAME.toString());
    String surveyName = prop.get(FieldList.SURVEY_NAME.toString());
    if (!FieldVerifier.isValidName(name)) { return AnalyzerResult.ERROR; }
    if (surveyId == null)
    {
      System.out.println("AnalyseResult: NEW_SURVEY");
      return AnalyzerResult.NEW_SURVEY;
    }
    if (FieldVerifier.isValidName(surveyName) && checkIsAdmin(requestorID, surveyId))
    {
      System.out.println("AnalyseResult: SURVEY_EDIT");
      return AnalyzerResult.SURVEY_EDIT;
    }
    else
    {
      System.out.println("AnalyseResult: SURVEY_ENTRY");
      return AnalyzerResult.SURVEY_ENTRY;
    }
  }
  
  private static boolean checkIsAdmin(String requestorID, String surveyId)
  {

    File workDir = new File(getWorkingDirRoot(), surveyId);
    File admFile = new File(workDir,ADMIN_FILE);
    return readTextFile(admFile).toString().trim().equals(requestorID);
  }
  
  public static boolean requestIsAdm(String userId, String surveyID)
  {
    File workDir = new File(getWorkingDirRoot(), surveyID);
    File admFile = new File(workDir,ADMIN_FILE);
    if (!workDir.exists()) { throw new IllegalStateException("Assume existing Survey!"); }
    if (!admFile.exists()) { throw new IllegalStateException("Assume existing Survey adm!"); }
    boolean isAdmin = readTextFile(admFile).toString().trim().equals(userId);
    return isAdmin;
  }

  public static boolean requestNewUsr(String userId, String surveyID)
  {
    File workDir = new File(getWorkingDirRoot(), surveyID);
    File userDir = new File(workDir, USER_SUB_DIR);
    if (!workDir.exists()) { throw new IllegalStateException("Assume existing Survey!"); }
    if (!userDir.exists()) { throw new IllegalStateException("Assume existing Survey usr!"); }
    return !new File(userDir, userId).exists();
  }
  
  public static StringBuilder readTextFile(File file)
  {
    StringBuilder sb = new StringBuilder();
    char[] cbuf = new char[256];
    try (FileReader fr = new FileReader(file))
    {
      while (true)
      {
        int len = fr.read(cbuf, 0, cbuf.length);
        if (len == -1) { break; }
        sb.append(cbuf, 0, len);
      }
    }
    catch (Exception e)
    {
      e.printStackTrace();
      return null;
    }
    return sb;
  }

  public static boolean writeTextFile(File file, String txt)
  {
    try(FileWriter fw = new FileWriter(file))
    {
      fw.write(txt);
      return true;
    }
    catch (Exception e)
    {
      e.printStackTrace();
      return false;
    }
  }
  /** Create and get directory, also checks subDir-Validity. <br/>
   * @param subDir A SubDir, or null for root.
   * @return An existing Working-Directory. **/
  public static File getWorkingDirNew(String subDir)
  {
    if (!FieldVerifier.isValidID(subDir))
    {
      throw new IllegalStateException("Forbidden working dir!");
    }
    File rootF = getWorkingDirRoot();
    File subDirF = new File(rootF, subDir);
    File subDirUsers = new File(subDirF, USER_SUB_DIR);
    if (subDirF.exists())
    {
      throw new IllegalStateException("Existing working dir!");
    }
    else
    {
      subDirF.mkdir();
      subDirUsers.mkdir();
      if (subDirUsers.isDirectory())
      { // All ok and existing.
        return subDirF;
      }
    }
    throw new IllegalStateException("Cannot create working dir!");
  }
  
  public static File getWorkingDirExisting(String subDir, boolean getUsersDir)
  {
    if (!FieldVerifier.isValidID(subDir))
    {
      throw new IllegalStateException("Forbidden working dir!");
    }
    File rootF = getWorkingDirRoot();
    File subDirF = new File(rootF, subDir);
    File subDirUsers = new File(subDirF, USER_SUB_DIR);
    if (!subDirUsers.exists())
    {
      throw new IllegalStateException("User dir not existing!");
    }
    else if (getUsersDir)
    {
      return subDirUsers;
    }
    return subDirF;
  }
  
  /** Get root working dir, ensures existing. */
  public static File getWorkingDirRoot()
  {
    String dot = ".";
    String dir = System.getProperty("user.home");
    if (!new File(dir).canWrite()) { dir = "/var/lib/"; dot = ""; }
    File target = new File(dir, dot + "daterserver");
    if (!target.exists()) { target.mkdir(); }
    if (!target.isDirectory())
    {
      throw new IllegalStateException("Missing working dir root!");
    }
    return target;
  }

  public static String getServerHost(HttpServletRequest request) throws MalformedURLException
  {
    String htmlFile = DaterWebApp.class.getSimpleName() + ".html";
    String reqUrl = request.getRequestURL().toString();
    if (reqUrl.endsWith(WebXml.FORM_HANDLER))
    {
      int end = reqUrl.length() - WebXml.FORM_HANDLER.length();
      reqUrl = reqUrl.substring(0, end);
    }
    else
    {
      throw new IllegalArgumentException("Request service not known: " + reqUrl);
    }
    return reqUrl + "/" + htmlFile;
  }
}
