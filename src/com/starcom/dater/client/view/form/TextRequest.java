package com.starcom.dater.client.view.form;

import java.util.logging.Logger;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.starcom.dater.client.HtmlUtil;
import com.starcom.dater.client.util.DaterUtils.ViewType;
import com.starcom.dater.client.window.TextBoxWin;
import com.starcom.dater.shared.lang.Text;
import com.starcom.dater.shared.service.TextService;
import com.starcom.dater.shared.service.TextServiceAsync;

public class TextRequest
{
  static Logger logger = Logger.getLogger(TextRequest.class.getName());
  private static final TextServiceAsync textService = GWT.create(TextService.class);
  
  /** Shows a form to fill in.
   * @param requestType The first part of the transmitted response. */
  public static void request(final String surveyId, String userId, final ViewType viewType, TextResponse response)
  {
    textService.sendTextToServer(
        viewType + ":" + userId + ":" + surveyId, new AsyncCallback<String>()
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
        response.run(result);
      }
    });
  }
}
