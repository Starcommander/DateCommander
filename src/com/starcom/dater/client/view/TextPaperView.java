package com.starcom.dater.client.view;

import java.util.logging.Logger;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.starcom.dater.client.HtmlUtil;
import com.starcom.dater.client.util.DaterUtils;
import com.starcom.dater.client.util.DaterUtils.ViewType;
import com.starcom.dater.client.view.form.TextRequest;
import com.starcom.dater.client.window.TextBoxWin;
import com.starcom.dater.shared.lang.Text;

public class TextPaperView {
	static Logger logger = Logger.getLogger(TextPaperView.class.getName());

	public static final String AREA_WIDTH = "30em";
	public static final String AREA_HEIGHT = "20em";
	private String htmlContainerId;
	private String userId;
	private String surveyId;
	TextArea txt;
	Button send;
	
	public TextPaperView(String htmlContainerId)
	{
		this.userId = DaterUtils.getCreateUserIdCookie();
		this.surveyId = DaterUtils.getParamSurveyId();
		this.htmlContainerId = htmlContainerId;
	}
	
	protected void showTextPaper(String surveyID)
	{
		init();
		if (surveyID != null)
		{
          TextRequest.request(surveyId, userId, ViewType.ToTextPaper, s -> onResponse(s));
		}
	}
	
	private void init()
	{
	    VerticalPanel vp = new VerticalPanel();
	    txt = new TextArea();
	    txt.setSize(AREA_WIDTH, AREA_HEIGHT);
	    txt.setReadOnly(true);
	    vp.add(txt);
	    
	    send = new Button();
	    send.setText(Text.getCur().getEdit());
	    send.addClickHandler((ev) -> onSendTextPaper());
	    vp.add(send);
	    
	    Button share = new Button();
	    share.setText("TODO Share Icon");
	    share.addClickHandler((ev) ->  generateShareWin());
	    vp.add(share);
	    RootPanel.get(htmlContainerId).add(vp);
	}
	
	private void generateShareWin()
	{
		TextBoxWin win = new TextBoxWin("TODO: SHARE_TITLE");
		win.setTextHtml("Just a text: " + DaterUtils.getUrl(surveyId, ViewType.ToTextPaper));
		win.setTextHtml(HtmlUtil.buildShareButton(DaterUtils.getUrl(surveyId, ViewType.ToTextPaper)).toString());
		win.showBox();
	}
	private void onSendTextPaper()
	{
		if (txt.isReadOnly())
		{
			txt.setReadOnly(false);
			send.setText(Text.getCur().getSend());
			return;
		}
		String txtS = txt.getText();
		logger.info("Sending text now:" + txtS);

	    TextRequest.requestWithText(surveyId, userId, ViewType.ToTextPaper, txtS, s -> onResponse(s));
	}
	
	private void onResponse(String resp)
	{
		int index = resp.indexOf(':');
		if (index < 1)
		{
			logger.severe("Missing surveyID in response: " + resp);
			return;
		}
		String reSurveyId = resp.substring(0,index);
		String reText = resp.substring(index+1);
		if (this.surveyId == null) { this.surveyId = reSurveyId; }
		else if (!this.surveyId.equals(reSurveyId))
		{
			logger.severe("Surviey ID is not equal! " + this.surveyId + "/" + reSurveyId);
			return;
		}
		txt.setText(reText);
		txt.setReadOnly(true);
		send.setText(Text.getCur().getEdit());
		//TODO: Share-Link create with surveyID
	}
}
