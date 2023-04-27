package com.starcom.dater.client.view;

import java.util.HashMap;
import java.util.logging.Logger;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.starcom.dater.client.HtmlUtil;
import com.starcom.dater.client.Transmitter;
import com.starcom.dater.client.view.form.SurveyForm;
import com.starcom.dater.client.window.CommitBox;
import com.starcom.dater.client.window.TextBoxWin;
import com.starcom.dater.shared.Utils;
import com.starcom.dater.shared.FieldVerifier.CookieList;
import com.starcom.dater.shared.FieldVerifier.ReqType;
import com.starcom.dater.shared.lang.Text;

public class FormView
{
	  static Logger logger = Logger.getLogger(FormView.class.getName());

	  final static String COOKIE_NAME_ID = CookieList.DaterNameId.toString();
	  
	  /** Shows a form to fill in.
	   * @param requestType The first part of the transmitted response. */
	  public static void showFormTable(final String surveyId, final String requestType)
	  {
	    logger.fine("Execute showFormTable(s,s)");
	    String userId = Cookies.getCookie(COOKIE_NAME_ID);
	    if (userId == null)
	    {
	      userId = Utils.generateID();
	      Cookies.setCookie(COOKIE_NAME_ID, userId, Utils.getDateInYears());
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
	          TableView.showTableNow(result, surveyId); //TODO: Hier geht es?
	          return;
	        }
	        HashMap<String, String> prop = Utils.toHashMap(result);
	logger.warning("Got props: " + result);
	        MainView.showSelectedViewType(prop, null); //TODO: Hier geht es nicht?
	      }
	    });
	  }
	  

	  /** Show the form for editing the survey itshelf, or the selected choices */
	  public static void showFormNow(HashMap<String, String> prop, String surveyId, boolean forceEdit)
	  {
	    Label errorLabel = new Label();
	    Button sendButton = new Button(Text.getCur().getSend());
	    sendButton.addStyleName("sendButton");
	    
	    boolean showAsEdit = forceEdit || (surveyId==null);
	    SurveyForm.FormHeader formHeader = new SurveyForm.FormHeader("formContainer", showAsEdit, surveyId, prop);
	    SurveyForm.FormBody formBody = new SurveyForm.FormBody("editButtonContainer", formHeader, prop);
	    RootPanel.get("sendButtonContainer").add(sendButton);
	    RootPanel.get("errorLabelContainer").add(errorLabel);
	    
	    CommitBox commitBox = new CommitBox(sendButton);
	    SurveyForm.CommitHandler handler = new SurveyForm.CommitHandler(commitBox, formHeader, formBody, errorLabel);
	    sendButton.addClickHandler(handler);
	  }
}
