package com.starcom.dater.client.window;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

public class CommitBox
{
  public DialogBox dialogBox = new DialogBox();
  public Label textToServerLabel = new Label();
  public HTML serverResponseLabel = new HTML();
  public Button closeButton = new Button("Close");
  public Button sendButton;
  public String resultUri;
  
  public CommitBox(final Button sendButton)
  {
    // Create the popup dialog box
    this.sendButton = sendButton;
    dialogBox.setText("Remote Procedure Call");
    dialogBox.setAnimationEnabled(true);
    // We can set the id of a widget by accessing its Element
    closeButton.getElement().setId("closeButton");
    VerticalPanel dialogVPanel = new VerticalPanel();
    dialogVPanel.addStyleName("dialogVPanel");
    dialogVPanel.add(new HTML("<b>Sending name to the server:</b>"));
    dialogVPanel.add(textToServerLabel);
    dialogVPanel.add(new HTML("<br><b>Server replies:</b>"));
    dialogVPanel.add(serverResponseLabel);
    dialogVPanel.setHorizontalAlignment(VerticalPanel.ALIGN_RIGHT);
    dialogVPanel.add(closeButton);
    dialogBox.setWidget(dialogVPanel);

    closeButton.addClickHandler(new ClickHandler()
    {
      public void onClick(ClickEvent event)
      {
        dialogBox.hide();
        if (resultUri != null)
        {
          Window.Location.assign(resultUri);
        }
        else
        {
          sendButton.setEnabled(true);
          sendButton.setFocus(true);
        }
      }
    });
  }
}
