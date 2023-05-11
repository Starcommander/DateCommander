package com.starcom.dater.client.view;

import java.util.ArrayList;
import java.util.HashMap;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RootPanel;
import com.starcom.dater.client.HtmlUtil;
import com.starcom.dater.client.util.DaterUtils;
import com.starcom.dater.client.util.DaterUtils.ViewType;
import com.starcom.dater.client.window.TextBoxWin;
import com.starcom.dater.shared.Utils;
import com.starcom.dater.shared.FieldVerifier.FieldList;
import com.starcom.dater.shared.lang.Text;

public class TableView
{

  final static String F_SURVEY_NAME = FieldList.SURVEY_NAME.toString();
  final static String F_SURVEY_DESC = FieldList.SURVEY_DESCRIPTION.toString();

  /** Show Result-Table.
   * Each entry is a line of input-String. <br>
   * Where a line with a single hypen is a separator.
   * The first section contains the header entries. */
  public static void showTableNow(String input, String surveyId)
  {
    int headIndex = input.indexOf("\n-\n");
    if (headIndex == -1) { headIndex = input.length(); }
    HashMap<String, String> prop = Utils.toHashMap(input.substring(0, headIndex));
    ArrayList<String> propTable = new ArrayList<String>();

    String fTitleTxt = HtmlUtil.escapeHtml(prop.get(F_SURVEY_NAME));
    String fDescTxt = HtmlUtil.markupToHtml(prop.get(F_SURVEY_DESC));
    HTML htmlHD = new HTML();
    htmlHD.setHTML("<h2>" + fTitleTxt + "</h2><br/>" + fDescTxt);
    
    propTable.add(Text.getCur().getName());
    for (int i=0; i<Utils.MAX_CHOICES; i++)
    { // Fill Header
      String v = prop.get(FieldList.CH.toString() + i);
      if (v == null) { break; }
      propTable.add(v);
    }
    int columns = propTable.size();
    boolean isHeader = true;
    for (String userEntries : input.split("\n-\n"))
    {
      if (isHeader) { isHeader = false; continue; }
      String[] userLine = new String[columns];
      for (String userEntry : userEntries.split("\n"))
      {
        int index = userEntry.indexOf('=');
        if (index == -1) { continue; }
        String key = userEntry.substring(0, index);
        String val = userEntry.substring(index + 1);
        if (key.equals(FieldList.USER_NAME.toString()))
        {
          userLine[0] = val;
        }
        else if (key.startsWith(FieldList.CH.toString()))
        {
          String numS = key.substring(2);
          if (!numS.matches("[0-9][0-9]*")) { continue; }
          int num = Integer.parseInt(numS) + 1;
          if (num > (userLine.length - 1)) { continue; }
          userLine[num] = val;
        }
      }
      for (String userField : userLine)
      {
        if (userField == null) { propTable.add("---"); }
        else { propTable.add(userField); }
      }
    }
    Button editB = new Button();
    editB.setHTML("<img src='img/cogwheel.png' alt='MENU' />");
    editB.addClickHandler(e -> onClick(prop, surveyId));
    HTML html = new HTML();
    html.setHTML(HtmlUtil.buildTable(propTable, columns).toString());
    html.addStyleName("myhugeborder");
    RootPanel.get("formContainer").add(htmlHD);
    RootPanel.get("sendButtonContainer").add(html);
    RootPanel.get("editButtonContainer").add(editB);
  }
  
  private static void onClick(final HashMap<String, String> prop, final String surveyId)
  {
    boolean isAdm = DaterUtils.requestAdmin(prop);
    if (isAdm)
    {
      showAdminGreeter(prop, surveyId);
    }
    else
    {
      showUserTextbox(prop, surveyId);
    }
  }

  /** Shows the context-menu for a non-admin user */
  private static void showUserTextbox(HashMap<String, String> prop, String surveyId)
  {
    TextBoxWin win = new TextBoxWin("Survey-User");
    win.setTextHtml(buildGreetTextWithLinks(false));
    Button editB = new Button();
    if (DaterUtils.requestNewUser(prop))
    {
      editB.setText(Text.getCur().getEnterSurvey());
    }
    else
    {
      editB.setText(Text.getCur().getEditSurveyChoice());
    }
    editB.addClickHandler((e) -> toFormClick(false, surveyId));
    win.addExtraButton(editB);
    win.showBox();
  }

  /** Shows the context-menu for the admin user */
  private static void showAdminGreeter(HashMap<String, String> prop, String surveyId)
  {
    TextBoxWin win = new TextBoxWin("Survey-Admin");
    if (DaterUtils.requestNewUser(prop))
    {
      win.getCloseButton().setText(Text.getCur().getEnterSurvey());
    }
    else
    {
      win.getCloseButton().setText(Text.getCur().getEditSurveyChoice());
    }
    win.getCloseButton().addClickHandler((e) -> toFormClick(false, surveyId));
    win.setTextHtml(buildGreetTextWithLinks(true));
    Button editB = new Button();
    editB.setText(Text.getCur().getEditSurveyForm());
    editB.addClickHandler((e) -> toFormClick(true, surveyId));
    win.addExtraButton(editB);
    Button closeB = new Button();
    closeB.setText(Text.getCur().getClose());
    win.addExtraButton(closeB);
    win.showBox();
  }
  

  private static String buildGreetTextWithLinks(boolean admin)
  {
    String shareUrl = DaterUtils.getUrl(DaterUtils.getParamSurveyId(), ViewType.ToSurvey);
    StringBuilder sb = new StringBuilder();
    if (admin) { sb.append(Text.getCur().getGreetAdminHtml()); }
    else { sb.append(Text.getCur().getGreetUsrHtml()); }
    sb.append(HtmlUtil.buildShareButton(shareUrl));
    sb.append("<br/><br/>");
    return sb.toString();
  }
  
  private static void toFormClick(boolean editForm, final String surveyId)
  {
      if (editForm) { DaterUtils.gotoUrl(surveyId, ViewType.EdForm); }
      else { DaterUtils.gotoUrl(surveyId, ViewType.EdChoice); }
  }
}
