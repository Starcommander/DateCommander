package com.starcom.dater.client.view;

import java.util.HashMap;
import java.util.logging.Logger;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.starcom.dater.client.util.DaterUtils;
import com.starcom.dater.client.util.DaterUtils.ViewType;
import com.starcom.dater.shared.FieldVerifier.ReqType;
import com.starcom.dater.shared.lang.Text;

public class MainView {
	  static Logger logger = Logger.getLogger(MainView.class.getName());

	  /** Show the View that is selected as uri-parameter.
	   * @param prop Transmitted properties, or null?
	   * @param surveyId Id when present, null otherwise. */
	  public static void showSelectedViewType(HashMap<String, String> prop, String surveyId)
	  {
	        DaterUtils.ViewType viewType = DaterUtils.requestViewTypeJS();
	        if (viewType == DaterUtils.ViewType.EdChoice)
	        {
	          logger.info("Show EdChoice!");
	          FormView.showFormNow(prop, surveyId, false);
	        }
	        else if (viewType == DaterUtils.ViewType.EdForm)
	        {
	          logger.info("Show EdForm!");
	          FormView.showFormNow(prop, surveyId, true);
	        }
	        else if (viewType == DaterUtils.ViewType.ToSurvey)
	        {
	          logger.info("Show ToSurvey!");
	          FormView.showFormTable(surveyId, ReqType.GetSurveyTable.toString());
	        }
	        else if (viewType == DaterUtils.ViewType.ToTextPaper)
	        {
	          logger.info("Show ToTextPaper!");
	          showTextPaperView("formContainer", null);
	        }
	        else
	        {
	          logger.info("Show Overview!");
	          showOverviewNow();
	        }
	  }
	  

	private static void showTextPaperView(String editContainer, HashMap<String, String> prop)
	{
	    VerticalPanel vp = new VerticalPanel();
	    TextArea txt = new TextArea();
	    vp.add(txt);
	    
	    Button send = new Button();
	    send.setText(Text.getCur().getSend());
	    send.addClickHandler((ev) -> System.out.println("Send text now."));
	    vp.add(send);
	    RootPanel.get(editContainer).add(vp);
	}

  public static void showOverviewNow()
  {
    Button daterButton = new Button(Text.getCur().getNewSurvey());
    Button textButton = new Button(Text.getCur().getNewTextPaper());
    daterButton.addStyleName("sendButton");
    textButton.addStyleName("sendButton");
    RootPanel.get("formContainer").add(daterButton);
    RootPanel.get("formContainer").add(textButton);
    textButton.addClickHandler(e -> DaterUtils.gotoUrl(null, ViewType.ToTextPaper));
    daterButton.addClickHandler(e -> DaterUtils.gotoUrl(null, ViewType.ToSurvey));
  }

}
