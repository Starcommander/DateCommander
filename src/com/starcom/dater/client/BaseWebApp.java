package com.starcom.dater.client;

import com.starcom.dater.shared.FieldVerifier;
import com.starcom.dater.shared.FieldVerifier.CookieList;
import com.starcom.dater.shared.FieldVerifier.FieldList;
import com.starcom.dater.shared.FieldVerifier.UrlParameter;
import com.starcom.dater.shared.Utils;
import com.starcom.dater.client.window.CommitBox;
import java.util.HashMap;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteHandler;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class BaseWebApp
{
  public final static int MAX_CHOICES = 100;
  final static String C_DATER_NAME = CookieList.DaterName.toString();
  final static String C_DATER_NAME_ID = CookieList.DaterNameId.toString();
  final static String F_SURVEY_NAME = FieldList.SURVEY_NAME.toString();
  final static String F_SURVEY_DESC = FieldList.SURVEY_DESCRIPTION.toString();
  final static String F_SURVEY_ID = FieldList.SURVEY_ID.toString();

  static class CommitHandler implements ClickHandler
  {
    Label errorLabel;
    CommitBox commitBox;
    FormHeader formHeader;
    FormBody formBody;

    public CommitHandler(CommitBox commitBox, FormHeader formHeader, FormBody formBody, Label errorLabel)
    {
      this.commitBox = commitBox;
      this.formHeader = formHeader;
      this.formBody = formBody;
      this.errorLabel = errorLabel;
    }
    
    public void onClick(ClickEvent event)
    {
      errorLabel.setText("");
      String text_name = formHeader.nameField.getText();
      if (!FieldVerifier.isValidName(text_name))
      {
        errorLabel.setText("Please enter at least four characters");
        return;
      }
      Cookies.setCookie(C_DATER_NAME, text_name, Utils.getDateInYears());

      commitBox.sendButton.setEnabled(false);
      commitBox.textToServerLabel.setText(text_name);
      commitBox.serverResponseLabel.setText("");
      
      formHeader.formPanel.addSubmitCompleteHandler(createSubmitComplete());
      formHeader.formPanel.submit();
      
    }

    private SubmitCompleteHandler createSubmitComplete()
    {
      SubmitCompleteHandler h = new SubmitCompleteHandler()
      {
        @Override
        public void onSubmitComplete(SubmitCompleteEvent ev)
        {
          String result = ev.getResults();
          if (result == null) { result = "No result from server!"; }
          result = Utils.htmlToText(result);
          commitBox.dialogBox.setText("Remote Procedure Call");
          commitBox.serverResponseLabel.removeStyleName("serverResponseLabelError");
          String resultResp = result;
          if (result.startsWith("http://") || result.startsWith("https://"))
          {
            resultResp = "<b>Successful created!</b><br><br>Forwarding to Survey ...";
            commitBox.resultUri = result;
            String surId = UrlParameter.SurveyId.toString();
            surId = Utils.getUriParameter(surId, result);
          }
          commitBox.serverResponseLabel.setHTML(resultResp);
          commitBox.dialogBox.center();
          commitBox.closeButton.setFocus(true);
        }
      };
      return h;
    }
  }
  
  static class FormHeader
  {
    TextBox nameField = new TextBox();
    TextBox nameFieldId = new TextBox();
    FormPanel formPanel = new FormPanel();
    VerticalPanel panel;
    
    boolean showEdit;
    String daterName;
    String daterNameId;
    
    public FormHeader(String containerName, boolean showEdit, String surveyId, HashMap<String, String> prop)
    {
      this.showEdit = showEdit;
      nameField.setName(FieldList.USER_NAME.toString());
      nameFieldId.setName(FieldList.USER_NAME_ID.toString());
      daterName = getCookie(C_DATER_NAME, null);
      daterNameId = getCookie(C_DATER_NAME_ID, null);
      if (daterName != null)
      {
        nameField.setText(daterName);
      }
      if (daterNameId != null)
      {
        nameFieldId.setText(daterNameId);
      }
      nameFieldId.setVisible(false);
      panel = new VerticalPanel();
      formPanel.setAction("/surveyFormHandler");  
      formPanel.setEncoding(FormPanel.ENCODING_MULTIPART);  
      formPanel.setMethod(FormPanel.METHOD_POST); 
      formPanel.setWidget(panel);
      DecoratorPanel decoratorPanel = new DecoratorPanel();
      decoratorPanel.add(formPanel);
      RootPanel.get(containerName).add(decoratorPanel);
      
      panel.add(new Label("Please enter your name:"));
      panel.add(nameField);
      panel.add(nameFieldId);
      String fTitleTxt = getFieldText(prop, F_SURVEY_NAME, "Title");
      String fDescTxt = getFieldText(prop, F_SURVEY_DESC, "Description");
      if (showEdit)
      {
        TextBox surveyField = new TextBox();
        surveyField.setName(F_SURVEY_NAME);
        surveyField.setText(fTitleTxt);
        TextArea surveyDescription = new TextArea();
        surveyDescription.setName(F_SURVEY_DESC);
        surveyDescription.setText(fDescTxt);
        panel.add(new Label("Please enter survey\ntitle and description:"));
        panel.add(surveyField);
        panel.add(surveyDescription);
      }
      else
      {
        TextBox surveyIdField = new TextBox();
        surveyIdField.setVisible(false);
        surveyIdField.setText(surveyId);
        surveyIdField.setName(F_SURVEY_ID);
        HTML area = new HTML(fDescTxt);
        area.setText(fDescTxt);
        panel.add(new Label(fTitleTxt));
        panel.add(area);
        panel.add(surveyIdField);
      }
      nameField.setFocus(true);
      nameField.selectAll();
    }
  
    private String getFieldText(HashMap<String, String> prop, String key, String def)
    {
      if (prop == null) { return def; }
      return prop.get(key);
    }
  }
  
  static class FormBody
  {
    public FormBody(String editContainer, FormHeader header, HashMap<String, String> prop)
    {
      if (!header.showEdit)
      {
        for (int i=0; i<MAX_CHOICES; i++)
        {
          String choiceTxt = getChoiceTxt(prop, i);
          if (choiceTxt == null) { break; }
          CheckBox b = new CheckBox(choiceTxt);
          b.setName(FieldList.CH.toString() + i);
          header.panel.add(b);
        }
      }
      else
      {
        int headerLen = header.panel.getWidgetCount();
        for (int i=0; i<3; i++)
        {
          addListField(header.panel, headerLen);
        }
        HorizontalPanel hp = new HorizontalPanel();
        Button plus = new Button();
        Button minus = new Button();
        plus.setText("+");
        minus.setText("-");
        hp.add(plus);
        hp.add(minus);
        plus.addClickHandler(createListHandler(header.panel,headerLen,true));
        minus.addClickHandler(createListHandler(header.panel,headerLen,false));
        RootPanel.get(editContainer).add(hp);
      }
    }

    private ClickHandler createListHandler(final VerticalPanel panel, final int headerLen, final boolean plus)
    {
      ClickHandler c = new ClickHandler()
      {
        @Override
        public void onClick(ClickEvent event)
        {
          int count = panel.getWidgetCount();
          if (plus)
          {
            if (count < (MAX_CHOICES + headerLen))
            {
              addListField(panel, headerLen);
            }
          }
          else
          {
            if (count > (2 + headerLen))
            {
              panel.remove(count - 1);
            }
          }
        }
      };
      return c;
    }
    
    private void addListField(VerticalPanel panel, int headerLen)
    {
      TextBox b = new TextBox();
      b.setText("Choice");
      int count = panel.getWidgetCount();
      b.setName(FieldList.CH.toString() + (count - headerLen));
      panel.add(b);
    }

    private String getChoiceTxt(HashMap<String, String> prop, int i)
    {
      if (prop == null)
      {
        return "Choice " + (i+1);
      }
      return prop.get(FieldList.CH.toString() + i);
    }
  }
  
  private static String getCookie(String key, String defValue)
  {
    String value = Cookies.getCookie(key);
    if (value == null)
    {
      return defValue;
    }
    return value;
  }
}
