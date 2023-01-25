package com.starcom.dater.client;

import com.google.gwt.core.client.GWT;
import com.starcom.dater.client.service.GreetingService;
import com.starcom.dater.client.service.GreetingServiceAsync;

public class Transmitter
{
  private static final GreetingServiceAsync greetingService = GWT.create(GreetingService.class);
  
  public static GreetingServiceAsync getTransmitter() { return greetingService; }
  
}
