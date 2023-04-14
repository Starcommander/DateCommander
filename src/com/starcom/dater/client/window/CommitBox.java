package com.starcom.dater.client.window;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.starcom.dater.shared.lang.Text;

public class CommitBox
{
  private DialogBox dialogBox = new DialogBox();
  private Label textToServerLabel = new Label();
  private HTML serverResponseLabel = new HTML();
  private Button closeButton = new Button("Close");
  private Button sendButton;
  private String resultUri;
  
  /** A simple box that appears and shows the result of transmission. */
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
    dialogVPanel.add(new HTML("<b>" + Text.getCur().getSendToServer() + "</b>"));
    dialogVPanel.add(textToServerLabel);
    dialogVPanel.add(new HTML("<br><b>" + Text.getCur().getServerReplies() + "</b>"));
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
  
  /** Lets the send-button disappear and sets the text for showing later. */
  public void onTransmit(String txt)
  {
      sendButton.setEnabled(false);
      textToServerLabel.setText(txt);
      serverResponseLabel.setText("");
  }
  
  /** Finish transmitting shows this box.
   * @param resultUri The uri for goto, or null to stay.
   * @param txt The response text to show. */
  public void onTransmitFinish(String resultUri, String txt)
  {
      dialogBox.setText("Remote Procedure Call");
      serverResponseLabel.removeStyleName("serverResponseLabelError");
      CommitBox.this.resultUri = resultUri;
      serverResponseLabel.setText(txt);
      dialogBox.center();
      closeButton.setFocus(true);
  }
}
