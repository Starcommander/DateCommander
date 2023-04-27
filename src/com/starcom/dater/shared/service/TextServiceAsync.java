package com.starcom.dater.shared.service;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The async counterpart of <code>TextService</code>.
 */
public interface TextServiceAsync {
  void sendTextToServer(String input, AsyncCallback<String> callback)
      throws IllegalArgumentException;
}
