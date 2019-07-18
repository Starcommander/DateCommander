package com.starcom.dater.client;

import com.google.gwt.core.client.GWT;

public class Transmitter
{
  private static final GreetingServiceAsync greetingService = GWT.create(GreetingService.class);
  
  public static GreetingServiceAsync getTransmitter() { return greetingService; }
  
}
