package com.starcom.dater.shared.service;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.starcom.dater.shared.WebXml;

/**
 * The client-side stub for the RPC service.
 */
@RemoteServiceRelativePath(WebXml.TEXT_SERVICE)
public interface TextService extends RemoteService {
  String sendTextToServer(String name) throws IllegalArgumentException;
}
