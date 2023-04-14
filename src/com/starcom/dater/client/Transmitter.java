package com.starcom.dater.client;

import com.google.gwt.core.client.GWT;
import com.starcom.dater.shared.service.TextService;
import com.starcom.dater.shared.service.TextServiceAsync;

public class Transmitter
{
  private static final TextServiceAsync textService = GWT.create(TextService.class);
  
  public static TextServiceAsync getTransmitter() { return textService; }
  
}
