package com.starcom.dater.client.window;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.starcom.dater.shared.lang.Text;

public class TextBoxWin
{
  private DialogBox dialogBox = new DialogBox();
  private HTML htmlTextLabel = new HTML();
  private Button closeButton = new Button(Text.getCur().getClose());
  private VerticalPanel dialogVPanel = new VerticalPanel();
  
  /** A simple html-text-window with close-button. */
  public TextBoxWin(String title)
  {
    dialogBox.setText(title);
    dialogBox.setAnimationEnabled(true);
    dialogVPanel.add(htmlTextLabel);
    dialogVPanel.setHorizontalAlignment(VerticalPanel.ALIGN_RIGHT);
    dialogVPanel.add(closeButton);
    dialogBox.setWidget(dialogVPanel);
    closeButton.addClickHandler((e) -> onClick(e));
  }
  
  private void onClick(ClickEvent event)
  {
    dialogBox.hide();
  }
  
  /** Sets the content text as html */
  public void setTextHtml(String htmlText)
  {
    htmlTextLabel.setHTML(htmlText);
  }
  
  public void showBox()
  {
    dialogBox.center();
    closeButton.setFocus(true);
  }
  
  public Button getCloseButton()
  {
	  return closeButton;
  }

  public void addExtraButton(Button xbutton)
  {
    dialogVPanel.add(xbutton);
    xbutton.addClickHandler((e) -> onClick(e));
  }
}
