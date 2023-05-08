package com.starcom.dater.client.util;

public class JsWindow {
	/** Returns the pixel-density as float.<br>
	 * For example 1.5 is hi density screen, and 0.75 is low. */
	public final native float getDevicePixelRatio() /*-{
      return window.devicePixelRatio;
    }-*/;
}
