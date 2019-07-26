package com.starcom.dater.client.window.ui;

import java.util.ArrayList;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.starcom.dater.client.CliUtils;
import com.starcom.dater.client.HtmlUtil;

public class MultiCheckBox extends HorizontalPanel implements ClickHandler
{
  /** Contains ImgFile and altTxt. */
  private ArrayList<String> iconList = new ArrayList<String>();
  private Button button = new Button();
  private Label label = new Label();
  private TextBox formBox = new TextBox();
  private int curIndex;
  private String textLabel;

  public static MultiCheckBox createDefault(String txt)
  {
    MultiCheckBox box = new MultiCheckBox(CliUtils.CHECK_IMG_MAYBE, "---");
    box.addSelection(CliUtils.CHECK_IMG_YES, "Y");
    box.addSelection(CliUtils.CHECK_IMG_NO, "N");
    box.setTextLabel(txt);
    return box;
  }
  
  public MultiCheckBox(String imgFile, String altTxt)
  {
    this.setVerticalAlignment(ALIGN_MIDDLE);
    addSelection(imgFile, altTxt);
    setHtmlImage(imgFile, altTxt, 0);
    button.addClickHandler(this);
    label.addStyleName("mylargefont");
    formBox.setVisible(false);
    add(button);
    add(label);
    add(formBox);
  }
  
  public void addSelection(String imgFile, String altTxt)
  {
    iconList.add(imgFile);
    iconList.add(altTxt);
  }

  private void setHtmlImage(String imgFile, String altTxt, int index)
  {
    imgFile = HtmlUtil.escapeHtml(imgFile);
    altTxt = HtmlUtil.escapeHtml(altTxt);
    formBox.setValue(imgFile);
    this.curIndex = index;
    String name = textLabel;
    String h = "<img src='" + imgFile + "' alt='" + altTxt + "' />";
    if (name != null) { label.setText(name); }
    button.setHTML(h);
  }

  public void setValueIndex(int index)
  {
    if (index < 0) { index = 0; }
    int index2 = index * 2; // ArrayIndex
    if (iconList.size() <= index2) { index2 = 0; index = 0; }
    setHtmlImage(iconList.get(index2), iconList.get(index2 + 1), index);
  }

  public void setValue(String value)
  {
    int index = iconList.indexOf(value);
    setValueIndex(index/2);
  }

  /** Sets the name for submit. */
  public void setFormName(String name)
  {
    this.formBox.setName(name);
  }

  public String getFormName()
  {
    return this.formBox.getName();
  }
  
  public void setTextLabel(String textLabel)
  {
    this.textLabel = textLabel;
  }

  public String getTextLabel()
  {
    return textLabel;
  }

  @Override
  public void onClick(ClickEvent event)
  {
    setValueIndex(curIndex+1);
  }

}
