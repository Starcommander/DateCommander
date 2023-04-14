package com.starcom.dater.client;

import java.util.ArrayList;
import java.util.HashMap;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.starcom.dater.client.CliUtils.ViewType;
import com.starcom.dater.client.window.CommitBox;
import com.starcom.dater.client.window.TextBoxWin;
import com.starcom.dater.shared.Utils;
import com.starcom.dater.shared.lang.Text;
import com.starcom.dater.shared.FieldVerifier.CookieList;
import com.starcom.dater.shared.FieldVerifier.FieldList;
import com.starcom.dater.shared.FieldVerifier.ReqType;

import java.util.logging.Logger;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class DaterWebApp implements EntryPoint
{
  static Logger logger = Logger.getLogger(DaterWebApp.class.getName());
  final static String C_COOK_ALLOWED = CookieList.CookAllowed.toString();
  final static String C_DATER_NAME_ID = CookieList.DaterNameId.toString();
  final static String F_SURVEY_NAME = FieldList.SURVEY_NAME.toString();
  final static String F_SURVEY_DESC = FieldList.SURVEY_DESCRIPTION.toString();
  /**
   * This is the entry point method.
   */
  public void onModuleLoad()
  {
    String curL = com.google.gwt.i18n.client.LocaleInfo.getCurrentLocale().getLocaleName();
    Text.selectLanguage(curL);
    
    final String surveyId = CliUtils.requestSurveyId();
    boolean c_allowed = Boolean.parseBoolean(Cookies.getCookie(C_COOK_ALLOWED));
    if (c_allowed) { showFormTable(surveyId, ReqType.GetSurvey.toString()); }
    else
    { // Ask for cookies.
      TextBoxWin box = new TextBoxWin("Cookies");
      box.setTextHtml(Text.getCur().getCookieNeededHtml());
      box.getCloseButton().setText(Text.getCur().getAcceptCookies());
      box.getCloseButton().addClickHandler(new ClickHandler()
      {
        @Override
        public void onClick(ClickEvent event)
        {
          Cookies.setCookie(C_COOK_ALLOWED, "true", Utils.getDateInYears());
          showFormTable(surveyId, ReqType.GetSurvey.toString());
        }
      });
      box.showBox();
    }
  }
  
  private void showFormTable(final String surveyId, final String requestType)
  {
    logger.fine("Execute showFormTable(s,s)");
    String userId = Cookies.getCookie(C_DATER_NAME_ID);
    if (userId == null)
    {
      userId = Utils.generateID();
      Cookies.setCookie(C_DATER_NAME_ID, userId, Utils.getDateInYears());
    }
    if (surveyId == null)
    {
      showFormNow(null, surveyId, false);
      return;
    }
    Transmitter.getTransmitter().sendTextToServer(
        requestType + ":" + userId + ":" + surveyId, new AsyncCallback<String>()
    {
      @Override
      public void onFailure(Throwable caught)
      {
        TextBoxWin box = new TextBoxWin("Error");
        String text = "<b>" + Text.getCur().getServerGetDataError() + "</b><br/>";
        box.setTextHtml(text + HtmlUtil.escapeHtml(caught.getMessage()));
        box.getCloseButton().setText(Text.getCur().getReload());
        box.getCloseButton().addClickHandler(new ClickHandler()
        {
          @Override
          public void onClick(ClickEvent event)
          {
            Window.Location.reload();
          }
        });
        box.showBox();
      }

      @Override
      public void onSuccess(String result)
      {
        logger.fine("Execute onSuccess(s)");
        if (requestType.equals(ReqType.GetSurveyTable.toString()))
        {
          logger.info("Show as table!");
          showTableNow(result, surveyId);
          return;
        }
        HashMap<String, String> prop = Utils.toHashMap(result);

        CliUtils.ViewType viewType = CliUtils.requestViewTypeJS();
        if (viewType == CliUtils.ViewType.EdChoice)
        {
          logger.info("Show EdChoice!");
          showFormNow(prop, surveyId, false);
        }
        else if (viewType == CliUtils.ViewType.EdForm)
        {
          logger.info("Show EdForm!");
          showFormNow(prop, surveyId, true);
        }
        else
        {
          logger.info("Show ToSurvey!");
          showFormTable(surveyId, ReqType.GetSurveyTable.toString());
        }
      }
    });
  }

  /** Show Result-Table.
   * Each entry is a line of input-String. <br>
   * Where a line with a single hypen is a separator.
   * The first section contains the header entries. */
  private void showTableNow(String input, String surveyId)
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
    editB.addClickHandler(createEditHandler(prop, surveyId));
    HTML html = new HTML();
    html.setHTML(HtmlUtil.buildTable(propTable, columns).toString());
    html.addStyleName("myhugeborder");
    RootPanel.get("formContainer").add(htmlHD);
    RootPanel.get("sendButtonContainer").add(html);
    RootPanel.get("editButtonContainer").add(editB);
  }
  
  private ClickHandler createEditHandler(final HashMap<String, String> prop, final String surveyId)
  {
    ClickHandler c = new ClickHandler()
    {
      @Override
      public void onClick(ClickEvent event)
      {
        boolean isAdm = CliUtils.requestAdmin(prop);
        if (isAdm)
        {
          showAdminGreeter(prop, surveyId);
        }
        else
        {
          showUserGreeter(prop, surveyId);
        }
      }
    };
    return c;
  }

  protected void showUserGreeter(HashMap<String, String> prop, String surveyId)
  {
    TextBoxWin win = new TextBoxWin("Survey-User");
    win.setTextHtml(buildGreetTextWithLinks(false));
    Button editB = new Button();
    if (CliUtils.requestNewUser(prop))
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

  private void showAdminGreeter(HashMap<String, String> prop, String surveyId)
  {
    TextBoxWin win = new TextBoxWin("Survey-Admin");
    if (CliUtils.requestNewUser(prop))
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
  
  private String buildGreetTextWithLinks(boolean admin)
  {
    String shareUrl = CliUtils.getUrl(CliUtils.requestSurveyId(), ViewType.ToSurvey);
    StringBuilder sb = new StringBuilder();
    if (admin) { sb.append(Text.getCur().getGreetAdminHtml()); }
    else { sb.append(Text.getCur().getGreetUsrHtml()); }
    sb.append(HtmlUtil.buildShareButton(shareUrl));
    sb.append("<br/><br/>");
    return sb.toString();
  }
  
  private void toFormClick(boolean editForm, final String surveyId)
  {
      if (editForm) { CliUtils.gotoUrl(surveyId, ViewType.EdForm); }
      else { CliUtils.gotoUrl(surveyId, ViewType.EdChoice); }
  }

  private void showFormNow(HashMap<String, String> prop, String surveyId, boolean forceEdit)
  {
    Label errorLabel = new Label();
    Button sendButton = new Button(Text.getCur().getSend());
    sendButton.addStyleName("sendButton");
    
    boolean showAsEdit = forceEdit || (surveyId==null);
    FormWebApp.FormHeader formHeader = new FormWebApp.FormHeader("formContainer", showAsEdit, surveyId, prop);
    FormWebApp.FormBody formBody = new FormWebApp.FormBody("editButtonContainer", formHeader, prop);
    RootPanel.get("sendButtonContainer").add(sendButton);
    RootPanel.get("errorLabelContainer").add(errorLabel);
    
    CommitBox commitBox = new CommitBox(sendButton);
    FormWebApp.CommitHandler handler = new FormWebApp.CommitHandler(commitBox, formHeader, formBody, errorLabel);
    sendButton.addClickHandler(handler);
  }
}
