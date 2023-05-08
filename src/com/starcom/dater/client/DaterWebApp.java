package com.starcom.dater.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Cookies;
import com.starcom.dater.client.util.DaterUtils;
import com.starcom.dater.client.view.MainView;
import com.starcom.dater.client.window.TextBoxWin;
import com.starcom.dater.shared.Utils;
import com.starcom.dater.shared.lang.Text;
import com.starcom.dater.shared.FieldVerifier.CookieList;
import java.util.logging.Logger;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class DaterWebApp implements EntryPoint
{
  static Logger logger = Logger.getLogger(DaterWebApp.class.getName());
  final static String COOKIE_ALLOWED = CookieList.CookAllowed.toString();
  
  /**
   * This is the entry point method.
   */
  @Override
  public void onModuleLoad()
  {
    String curL = com.google.gwt.i18n.client.LocaleInfo.getCurrentLocale().getLocaleName();
    Text.selectLanguage(curL);
          logger.info("Now onModuleLoad!");
    
    final String surveyId = DaterUtils.requestSurveyId();
    boolean c_allowed = Boolean.parseBoolean(Cookies.getCookie(COOKIE_ALLOWED));
    if (c_allowed) { MainView.showSelectedViewType(surveyId); }
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
          Cookies.setCookie(COOKIE_ALLOWED, "true", Utils.getDateInYears());
          MainView.showOverviewNow();
        }
      });
      box.showBox();
    }
  }
  



  

}
