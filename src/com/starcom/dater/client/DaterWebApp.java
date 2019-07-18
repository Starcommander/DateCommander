package com.starcom.dater.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

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
import com.starcom.dater.client.window.CommitBox;
import com.starcom.dater.client.window.TextBoxWin;
import com.starcom.dater.shared.Utils;
import com.starcom.dater.shared.lang.Text;
import com.starcom.dater.shared.FieldVerifier.CookieList;
import com.starcom.dater.shared.FieldVerifier.FieldList;
import com.starcom.dater.shared.FieldVerifier.ReqType;
import com.starcom.dater.shared.FieldVerifier.UrlParameter;
import com.starcom.dater.shared.FieldVerifier.ViewType;

import java.util.logging.Logger;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class DaterWebApp implements EntryPoint
{
  private static Logger logger = Logger.getLogger(DaterWebApp.class.getName());
  final static String C_COOK_ALLOWED = CookieList.CookAllowed.toString();
  final static String C_DATER_NAME_ID = CookieList.DaterNameId.toString();
  /**
   * This is the entry point method.
   */
  public void onModuleLoad()
  {
    String curL = com.google.gwt.i18n.client.LocaleInfo.getCurrentLocale().getLocaleName();
    Text.selectLanguage(curL);
    
    final String surveyId = Window.Location.getParameter(UrlParameter.SurveyId.toString());
    boolean c_allowed = Boolean.parseBoolean(Cookies.getCookie(C_COOK_ALLOWED));
    if (c_allowed) { showFormTable(surveyId, ReqType.GetSurvey.toString()); }
    else
    { // Ask for cookies.
      TextBoxWin box = new TextBoxWin("Cookies");
      String text = Text.getCur().getCookieNeeded();
      box.setText(text);
      box.setButtonText(Text.getCur().getAcceptCookies());
      box.onClose(new ClickHandler()
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
    logger.fine("Execute showForm(");
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
    Transmitter.getTransmitter().greetServer(
        requestType + ":" + userId + ":" + surveyId, new AsyncCallback<String>()
    {
      @Override
      public void onFailure(Throwable caught)
      {
        TextBoxWin box = new TextBoxWin("Error");
        String text = "<b>Error getting data from server!</b><br/>" + caught.getMessage();
        box.setText(text);
        box.setButtonText("Reload");
        box.onClose(new ClickHandler()
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
logger.warning("FullResult:\n" + result);
        if (requestType.equals(ReqType.GetSurveyTable.toString()))
        {
          showTableNow(result, surveyId);
          return;
        }
        HashMap<String, String> prop = Utils.toHashMap(result);
        String viewTypeS = prop.get(FieldList.VIEW_TYPE.toString());
logger.warning("Result View: " + viewTypeS);
        if (viewTypeS == null) { viewTypeS = ViewType.NewUser.toString(); }
        ViewType viewType = ViewType.valueOf(viewTypeS);
logger.warning("Result ViewV: " + viewType.toString());
        if (viewType == ViewType.Admin)
        { // Greetings to admin
logger.warning("Show as admin!");
          showAdminGreeter(prop, surveyId);
        }
        else if (viewType == ViewType.NewUser)
        {
          showFormNow(prop, surveyId, false);
logger.warning("Show as newUser!");
        }
        else
        {
logger.warning("Show result!");
          showFormTable(surveyId, ReqType.GetSurveyTable.toString());
        }
      }
    });
  }

  private void showTableNow(String input, String surveyId)
  {
    int headIndex = input.indexOf("\n-\n");
    HashMap<String, String> prop = Utils.toHashMap(input.substring(0, headIndex));
    ArrayList<String> propTable = new ArrayList<String>();
    propTable.add("Name");
    for (int i=0; i<BaseWebApp.MAX_CHOICES; i++)
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
    
//    // Replace this fakeTable
//    ArrayList<String> fakeTable = new ArrayList<String>();
//    fakeTable.add("Name"); fakeTable.add("Wohnung"); fakeTable.add("Haus");
//    fakeTable.add("Paul"); fakeTable.add("Ja");      fakeTable.add("Nein");
//    fakeTable.add("Hans"); fakeTable.add("Nein");    fakeTable.add("Ja");

    HTML html = new HTML();
    html.setHTML(HtmlUtil.buildTable(propTable, columns).toString());
    html.addStyleName("mytable");
    RootPanel.get("formContainer").add(html);
  }
  
  private void showAdminGreeter(HashMap<String, String> prop, String surveyId)
  {
    TextBoxWin win = new TextBoxWin("Survey-Admin");
    win.setButtonText(Text.getCur().getEnterSurvey());
    win.onClose(createClickHandler(false, prop, surveyId));
    win.setText(Text.getCur().getGreetAdminHtml());
    Button editB = new Button();
    editB.setText(Text.getCur().getEditSurvey());
    editB.addClickHandler(createClickHandler(true, prop, surveyId));
    win.addExtraButton(editB);
    win.showBox();
  }
  
  private ClickHandler createClickHandler(final boolean adm,
      final HashMap<String, String> prop, final String surveyId)
  {
    ClickHandler h = new ClickHandler()
    {
      @Override
      public void onClick(ClickEvent event)
      {
        showFormNow(prop, surveyId, adm);
      }
    };
    return h;
  }

  private void showFormNow(HashMap<String, String> prop, String surveyId, boolean forceEdit)
  {
    Label errorLabel = new Label();
    Button sendButton = new Button("Send");
    sendButton.addStyleName("sendButton");
    
    boolean showAsEdit = forceEdit || (surveyId==null);
    BaseWebApp.FormHeader formHeader = new BaseWebApp.FormHeader("formContainer", showAsEdit, surveyId, prop);
    BaseWebApp.FormBody formBody = new BaseWebApp.FormBody("editButtonContainer", formHeader, prop);
    RootPanel.get("sendButtonContainer").add(sendButton);
    RootPanel.get("errorLabelContainer").add(errorLabel);
    
    CommitBox commitBox = new CommitBox(sendButton);
    BaseWebApp.CommitHandler handler = new BaseWebApp.CommitHandler(commitBox, formHeader, formBody, errorLabel);
    sendButton.addClickHandler(handler);
  }
  
//  private static boolean checkSurveyAdmin(String surveyId)
//  {
//    String admViewList = Cookies.getCookie(C_ADM_VIEW_LIST);
//    logger.info("SurveyId: " + surveyId);
//    logger.info("AdmViewList: " + admViewList);
//    if (admViewList == null) { return false; }
//    for (String curId : admViewList.split(","))
//    {
//      if (curId.equals(surveyId)) { return true; }
//    }
//    return false;
//  }
}
