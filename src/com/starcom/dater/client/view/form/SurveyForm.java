package com.starcom.dater.client.view.form;

import com.starcom.dater.shared.FieldVerifier;
import com.starcom.dater.shared.FieldVerifier.CookieList;
import com.starcom.dater.shared.FieldVerifier.FieldList;
import com.starcom.dater.shared.lang.Text;
import com.starcom.dater.shared.Utils;
import com.starcom.dater.shared.WebXml;
import com.starcom.dater.client.HtmlUtil;
import com.starcom.dater.client.util.DaterUtils;
import com.starcom.dater.client.window.CommitBox;
import com.starcom.dater.client.window.ui.MultiCheckBox;

import java.util.HashMap;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Form to transmit data to the server.
 */
public class SurveyForm
{
  final static String C_DATER_NAME = CookieList.DaterName.toString();
  final static String C_DATER_NAME_ID = CookieList.DaterNameId.toString();
  final static String F_SURVEY_NAME = FieldList.SURVEY_NAME.toString();
  final static String F_SURVEY_DESC = FieldList.SURVEY_DESCRIPTION.toString();
  final static String F_SURVEY_ID = FieldList.SURVEY_ID.toString();
  final static String F_USER_NAME = FieldList.USER_NAME.toString();
  final static String F_USER_ID = FieldList.USER_NAME_ID.toString();

  public static class CommitHandler implements ClickHandler
  {
    Label errorLabel;
    CommitBox commitBox;
    FormHeader formHeader;
    FormBodyList formBody;

    public CommitHandler(CommitBox commitBox, FormHeader formHeader, FormBodyList formBody, Label errorLabel)
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
        errorLabel.setText(Text.getCur().getName() + ": " + Text.getCur().getLeastFourChars());
        return;
      }
      Cookies.setCookie(C_DATER_NAME, text_name, Utils.getDateInYears());

      commitBox.onTransmit(text_name);

      formHeader.formPanel.addSubmitCompleteHandler(ev -> onSubmitComplete(ev));
      formHeader.formPanel.submit();
    }

    public void onSubmitComplete(SubmitCompleteEvent ev)
    {
      String result = ev.getResults();
      if (result == null) { result = Text.getCur().getNoResultFromServer(); }
      result = Utils.htmlToText(result);
      String resultResp = result;
      String resultUri = null;
      if (result.startsWith("http://") || result.startsWith("https://"))
      {
        resultResp = "\n" + Text.getCur().getSuccessfulCreated() + "\n\n" + Text.getCur().getForwardingToSurvey();
        if (result.contains("?")) resultUri = result + "&ViewType=ToSurvey"; //TODO: Find a better solution
        else resultUri = result + "?ViewType=ToSurvey";
      }
      commitBox.onTransmitFinish(resultUri, resultResp);
    }
  }

  public static class FormHeader
  {
    TextBox nameField = new TextBox();
    FormPanel formPanel = new FormPanel();
    VerticalPanel panel = new VerticalPanel();
    boolean showEdit;
    String daterName;
    String daterNameId;

    /** Creates the Textfields of the survey-form */
    public FormHeader(String containerName, boolean showEdit, String surveyId, HashMap<String, String> prop)
    {
      this.showEdit = showEdit;
      nameField.setName(F_USER_NAME);
      daterName = getCookie(C_DATER_NAME, null);
      daterNameId = getCookie(C_DATER_NAME_ID, null);
      String daterSurveyId = DaterUtils.getParamSurveyId();
      if (daterName != null)
      {
        nameField.setText(daterName);
      }
      if (daterNameId != null)
      { // Hidden field transfer.
        TextBox nameFieldId = new TextBox();
        nameFieldId.setName(F_USER_ID);
        nameFieldId.setText(daterNameId);
        nameFieldId.setVisible(false);
        panel.add(nameFieldId);
      }
      if (daterSurveyId != null)
      { // Hidden field.
        TextBox surveyFieldId = new TextBox();
        surveyFieldId.setName(F_SURVEY_ID);
        surveyFieldId.setText(daterSurveyId);
        surveyFieldId.setVisible(false);
        panel.add(surveyFieldId);
      }
      String href = GWT.getHostPageBaseURL() + WebXml.FORM_HANDLER + "?ViewType=ToSurvey";
      formPanel.setAction(href);
      formPanel.setEncoding(FormPanel.ENCODING_MULTIPART);
      formPanel.setMethod(FormPanel.METHOD_POST);
      formPanel.setWidget(panel);
      DecoratorPanel decoratorPanel = new DecoratorPanel();
      decoratorPanel.add(formPanel);
      RootPanel.get(containerName).add(decoratorPanel);
      
      panel.add(new Label(Text.getCur().getEnterName()));
      panel.add(nameField);
      String fTitleTxt = getFieldText(prop, F_SURVEY_NAME, Text.getCur().getTitle());
      String fDescTxt = getFieldText(prop, F_SURVEY_DESC, Text.getCur().getDescription());
      if (showEdit)
      {
        TextBox surveyField = new TextBox();
        surveyField.setName(F_SURVEY_NAME);
        surveyField.setText(fTitleTxt);
        TextArea surveyDescription = new TextArea();
        surveyDescription.setName(F_SURVEY_DESC);
        surveyDescription.setText(fDescTxt);
        surveyDescription.setTitle(Text.getCur().getUseMarkdown());
        panel.add(new Label(Text.getCur().getEnterTitleDescription()));
        panel.add(surveyField);
        panel.add(surveyDescription);
      }
      else
      {
        TextBox surveyIdField = new TextBox();
        surveyIdField.setVisible(false);
        surveyIdField.setText(surveyId);
        surveyIdField.setName(F_SURVEY_ID);
        HTML area = new HTML(HtmlUtil.markupToHtml(fDescTxt));
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
  
  public static class FormBodyList
  {
    /** Creates the list of Textfields or Checkboxes. */
    public FormBodyList(String editContainer, FormHeader header, HashMap<String, String> prop)
    {
      if (!header.showEdit)
      {
    	if (prop!=null)
    	{
          for (int i=0; i<Utils.MAX_CHOICES; i++)
          {
            String choiceTxt = getChoiceTxt(prop, i);
            if (choiceTxt == null) { break; }
            MultiCheckBox b = MultiCheckBox.createDefault(choiceTxt);
            b.setFormName(FieldList.CH.toString() + i);
            String selVal = prop.get(FieldList.U_CH.toString() + i);
            b.setValue(selVal);
            header.panel.add(b);
          }
    	}
      }
      else
      {
        int headerLen = header.panel.getWidgetCount();
        for (int i=0; i<Utils.MAX_CHOICES; i++)
        {
          String choiceTxt = getChoiceTxt(prop, i);
          if (prop == null && i>2) { break; } // New
          if (choiceTxt == null && i>2) { break; } // Edit
          addListField(header.panel, headerLen, choiceTxt);
        }
        HorizontalPanel hp = new HorizontalPanel();
        Button plus = new Button();
        Button minus = new Button();
        plus.setText("+");
        minus.setText("-");
        hp.add(plus);
        hp.add(minus);
        plus.addClickHandler((ev) -> onClick(header.panel,headerLen,true));
        minus.addClickHandler((ev) -> onClick(header.panel,headerLen,false));
        RootPanel.get(editContainer).add(hp);
      }
    }

    private void onClick(VerticalPanel panel, final int headerLen, final boolean plus)
    {
      int count = panel.getWidgetCount();
      if (plus)
      {
        if (count < (Utils.MAX_CHOICES + headerLen))
        {
          addListField(panel, headerLen, null);
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

    private void addListField(VerticalPanel panel, int headerLen, String choiceTxt)
    {
      if (choiceTxt == null) { choiceTxt = "Choice"; }
      TextBox b = new TextBox();
      b.setText(choiceTxt);
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
