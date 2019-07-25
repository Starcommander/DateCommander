package com.starcom.dater.client.window;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.starcom.dater.shared.lang.Text;

public class TextBoxWin implements ClickHandler
{
  private DialogBox dialogBox = new DialogBox();
  private HTML htmlTextLabel = new HTML();
  private Button closeButton = new Button(Text.getCur().getClose());
  VerticalPanel dialogVPanel = new VerticalPanel();
  
  public TextBoxWin(String title)
  {
    dialogBox.setText(title);
    dialogBox.setAnimationEnabled(true);
    dialogVPanel.add(htmlTextLabel);
    dialogVPanel.setHorizontalAlignment(VerticalPanel.ALIGN_RIGHT);
    dialogVPanel.add(closeButton);
    dialogBox.setWidget(dialogVPanel);
    closeButton.addClickHandler(this);
  }
  
  @Override
  public void onClick(ClickEvent event)
  {
    dialogBox.hide();
  }
  
  public void setTextHtml(String htmlText)
  {
    htmlTextLabel.setHTML(htmlText);
  }
  
  public void showBox()
  {
    dialogBox.center();
    closeButton.setFocus(true);
  }
  
  public void setButtonText(String txt)
  {
    closeButton.setText(txt);
  }
  
  public void onClose(ClickHandler action)
  {
    closeButton.addClickHandler(action);
  }

  public void addExtraButton(Button xbutton)
  {
    dialogVPanel.add(xbutton);
    xbutton.addClickHandler(this);
  }
}
