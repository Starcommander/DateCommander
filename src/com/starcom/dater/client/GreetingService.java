package com.starcom.dater.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.starcom.dater.shared.WebXml;

/**
 * The client-side stub for the RPC service.
 */
@RemoteServiceRelativePath(WebXml.GREET_SERVICE)
public interface GreetingService extends RemoteService {
  String greetServer(String name) throws IllegalArgumentException;
}
