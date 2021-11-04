package com.starcom.dater.server;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.starcom.dater.shared.FieldVerifier.AnalyzerResult;
import com.starcom.dater.shared.FieldVerifier.FieldList;
import com.starcom.dater.shared.FieldVerifier.UrlParameter;
import com.starcom.dater.shared.Constants;
import com.starcom.dater.shared.Utils;

/**
 * Servlet implementation class for Servlet: UploadFileServlet
 */
public class FormUploadResponse extends HttpServlet implements Servlet
{
  private static final long serialVersionUID = 8305367618713715640L;

  protected void doPost(HttpServletRequest request,
    HttpServletResponse response) throws ServletException, IOException
  {
    response.setContentType("text/plain");
    response.setCharacterEncoding(Constants.UTF_8);
    HashMap<String,String> fields = new HashMap<String,String>();
    FileItem uploadItem = getFileItemAndFields(request, fields);
    String requestorID = fields.get(FieldList.USER_NAME_ID.toString());
    fields.remove(FieldList.USER_NAME_ID.toString());
    AnalyzerResult result = ServUtils.getAnalyzerResult(fields, requestorID);
    if (result == AnalyzerResult.ERROR)
    {
      response.getWriter().write("AnalyseResult error!");
    }
    else if (uploadItem != null)
    {
      byte[] data = uploadItem.get();
      response.getWriter().write("File received! Bytes:" + data.length);
    }
    else if (result == AnalyzerResult.NEW_SURVEY)
    {
      String surveyId = Utils.generateID();
      fields.put(FieldList.SURVEY_ID.toString(), surveyId);
      File workDir = ServUtils.getWorkingDirNew(surveyId);
      File tarFile = new File(workDir, ServUtils.MAIN_FILE);
      File tarFileAdm = new File(workDir, ServUtils.ADMIN_FILE);
      boolean sOk = ServUtils.writeTextFile(tarFile, Utils.toString(fields));
      boolean aOk = ServUtils.writeTextFile(tarFileAdm, requestorID);
      if (sOk && aOk)
      {
        String param = "?" + UrlParameter.SurveyId.toString() + "=" + surveyId;
        response.getWriter().write(ServUtils.getServerHost(request) + param);
      }
      else
      {
        response.getWriter().write("Error generating Survey!");
      }
    }
    else if (result == AnalyzerResult.SURVEY_EDIT)
    {
      String surveyId = fields.get(FieldList.SURVEY_ID.toString());
      fields.put(FieldList.SURVEY_ID.toString(), surveyId);
      File workDir = ServUtils.getWorkingDirExisting(surveyId, false);
      File tarFile = new File(workDir, ServUtils.MAIN_FILE);
      boolean sOk = ServUtils.writeTextFile(tarFile, Utils.toString(fields));
      if (sOk)
      {
        String param = "?" + UrlParameter.SurveyId.toString() + "=" + surveyId;
        response.getWriter().write(ServUtils.getServerHost(request) + param);
      }
      else
      {
        response.getWriter().write("Error generating Survey!");
      }
    }
    else if (result == AnalyzerResult.SURVEY_ENTRY)
    {
      String surveyId = fields.get(FieldList.SURVEY_ID.toString());
      File usersDir = ServUtils.getWorkingDirExisting(surveyId, true);
      File tarFile = new File(usersDir, requestorID);
      boolean sOk = ServUtils.writeTextFile(tarFile, Utils.toString(fields));
      if (sOk)
      {
        String param = "?" + UrlParameter.SurveyId.toString() + "=" + surveyId;
        response.getWriter().write(ServUtils.getServerHost(request) + param);
      }
      else
      {
        response.getWriter().write("Error writing Survey entry!");
      }
    }
    else
    {
      response.getWriter().write("NO-RESULT-DATA");
    }

  }
  
  private FileItem getFileItemAndFields(HttpServletRequest request, HashMap<String,String> fields)
  {
    FileItemFactory factory = new DiskFileItemFactory();
    ServletFileUpload upload = new ServletFileUpload(factory);
    try
    {
      List<FileItem> items = upload.parseRequest(request);
      Iterator<FileItem> it = items.iterator();
      FileItem resultItem = null;
      while (it.hasNext())
      {
        FileItem item = it.next();
        if (!item.isFormField()
            && "uploadFormElement".equals(item.getFieldName())) {
          resultItem = item;
        }
        String name = item.getFieldName();
        String value = new String(item.get(),Charset.forName("UTF-8"));
        System.out.println("Name: " + name);
        System.out.println("Value: " + value);
        fields.put(name, value);
      }
      return resultItem;
    }
    catch (FileUploadException e)
    {
      e.printStackTrace();
    }
    return null;
  }
  
} 
