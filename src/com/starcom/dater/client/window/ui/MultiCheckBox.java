package com.starcom.dater.client.window.ui;

import java.util.ArrayList;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.starcom.dater.client.CliUtils;
import com.starcom.dater.client.HtmlUtil;

public class MultiCheckBox extends HorizontalPanel
{
  private ArrayList<String> iconList = new ArrayList<String>();
  private ArrayList<String> textList = new ArrayList<String>();
  private Button button = new Button();
  private Label label = new Label();
  private TextBox formBox = new TextBox();
  private int curIndex;
  private String textLabel;

  /** Creates a MultiCheckBox with checked/unchecked pre-configured. */
  public static MultiCheckBox createDefault(String txt)
  {
    MultiCheckBox box = new MultiCheckBox(CliUtils.CHECK_IMG_MAYBE, "---");
    box.addSelection(CliUtils.CHECK_IMG_YES, "Y");
    box.addSelection(CliUtils.CHECK_IMG_NO, "N");
    box.setTextLabel(txt);
    return box;
  }
  
  /** A checkbox with multible states and images, not just checked and unchecked. */
  public MultiCheckBox(String imgFile, String altTxt)
  {
    this.setVerticalAlignment(ALIGN_MIDDLE);
    addSelection(imgFile, altTxt);
    setHtmlImage(imgFile, altTxt, 0);
    button.addClickHandler((e) -> setValueIndex(curIndex+1));
    label.addStyleName("mylargefont");
    formBox.setVisible(false);
    add(button);
    add(label);
    add(formBox);
  }
  
  public void addSelection(String imgFile, String altTxt)
  {
    iconList.add(imgFile);
    textList.add(altTxt);
  }

  /** Sets the value-index of this MultiCheckBox  */
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

  /** Sets the value-index of this MultiCheckBox */
  public void setValueIndex(int index)
  {
    if (index < 0) { index = 0; }
    if (index >= iconList.size()) { index = 0; }
    setHtmlImage(iconList.get(index), textList.get(index), index);
  }

  /** Sets the value-index of this MultiCheckBox by altTxt */
  public void setValue(String value)
  {
    int index = textList.indexOf(value);
    setValueIndex(index);
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

}
