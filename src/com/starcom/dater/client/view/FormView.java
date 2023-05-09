package com.starcom.dater.client.view;

import java.util.HashMap;
import java.util.logging.Logger;

import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.starcom.dater.client.util.DaterUtils;
import com.starcom.dater.client.util.DaterUtils.ViewType;
import com.starcom.dater.client.view.form.SurveyForm;
import com.starcom.dater.client.view.form.TextRequest;
import com.starcom.dater.client.window.CommitBox;
import com.starcom.dater.shared.Utils;
import com.starcom.dater.shared.FieldVerifier.CookieList;
import com.starcom.dater.shared.lang.Text;

public class FormView
{
	  static Logger logger = Logger.getLogger(FormView.class.getName());

	  
	  /** Shows a form to fill in.
	   * @param viewType The first part of the transmitted response. */
	  public static void showFormTable(final String surveyId, final ViewType viewType)
	  {
	    String userId = DaterUtils.getCreateUserIdCookie();
	    logger.fine("Execute showFormTable(s,s)");
	    if (surveyId == null)
	    {
	      showFormNow(null, surveyId, viewType);
	      return;
	    }
	    TextRequest.request(surveyId, userId, viewType, s -> onResponse(s, viewType, surveyId));
	  }

      private static void onResponse(String result, ViewType viewType, String surveyId)
      {
        logger.fine("Execute onSuccess(s)");
        if (viewType.equals(ViewType.ToSurvey))
        {
          logger.info("Show as table!");
          TableView.showTableNow(result, surveyId);
          return;
        }
        else if (viewType == ViewType.EdForm)
        {
          HashMap<String, String> prop = Utils.toHashMap(result);
          showFormNow(prop, surveyId, viewType);
        }
        else if (viewType == ViewType.EdChoice)
        {
          HashMap<String, String> prop = Utils.toHashMap(result);
          showFormNow(prop, surveyId, viewType);
        }
      }

          /** Show the form for editing the survey itshelf, or the selected choices
           * @param viewType EdForm to ensure form-edition-page.
           * @param prop The transmitted properties, or null for initial-edit. */
	  public static void showFormNow(HashMap<String, String> prop, String surveyId, ViewType viewType)
	  {
	    Label errorLabel = new Label();
	    Button sendButton = new Button(Text.getCur().getSend());
	    sendButton.addStyleName("sendButton");
	    
	    boolean showAsEdit = viewType==ViewType.EdForm || (surveyId==null);
	    SurveyForm.FormHeader formHeader = new SurveyForm.FormHeader("formContainer", showAsEdit, surveyId, prop);
	    SurveyForm.FormBodyList formBodyList = new SurveyForm.FormBodyList("editButtonContainer", formHeader, prop);
	    RootPanel.get("sendButtonContainer").add(sendButton);
	    RootPanel.get("errorLabelContainer").add(errorLabel);
	    
	    CommitBox commitBox = new CommitBox(sendButton);
	    SurveyForm.CommitHandler handler = new SurveyForm.CommitHandler(commitBox, formHeader, formBodyList, errorLabel);
	    sendButton.addClickHandler(handler);
	  }
}
