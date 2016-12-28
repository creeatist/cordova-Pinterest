package com.realizedmobile.pinterestplugin;

import org.json.JSONException;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaArgs;
import org.apache.cordova.CordovaPlugin;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

public class PinterestPlugin extends CordovaPlugin {
  public static final String TAG = "PinterestPlugin";
  public static final String INIT = "initPinterest";
  public static final String AVAILABLE = "canPinWithSDK";
  public static final String PIN = "pin";

  /**
   * Gets the application context from cordova's main activity.
   * @return the application context
   */
  private Context getApplicationContext() {
    return this.webView.getContext();
  }
  
  @Override
  public boolean execute(String action, CordovaArgs args, final CallbackContext callbackContext) throws JSONException {
    boolean result = false;
    Log.v(TAG, "execute: action=" + action);
    if (INIT.equals(action)) {
      executeInit(callbackContext, args.getString(0));
      result = true;
    }
    else if (PIN.equals(action)) {
      executePin(callbackContext, args.getString(0), args.getString(1), args.getString(2));
      result = true;
    } else if (AVAILABLE.equals(action)) {
      executeCheckAvailable(callbackContext);
      result = true;
    }
    return result;
  }

  private void executeInit(CallbackContext callback, final String appId) {
    Log.v(TAG, "Init: appId=" + appId);
    callback.success();
  }
  private void executePin(CallbackContext callback, final String sourceUrl, final String imageUrl, final String description) {
    Log.v(TAG, "PinIt: source=" + sourceUrl + ", imageUrl: " +
            imageUrl + ", description: " + description);
    Activity activity = cordova.getActivity();
    String urlRoute = "/pin/create/button/?";
    if (sourceUrl != null) {
      urlRoute += "url=" + Uri.encode(sourceUrl) + "&";
    }
    if (imageUrl != null) {
      urlRoute += "media=" + Uri.encode(imageUrl) + "&";
    }
    if (description != null) {
      urlRoute += "description=" + Uri.encode(description);
    }
    try {
      activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("pinterest://www.pinterest.com" + urlRoute)));
    } catch (Exception e) {
      activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.pinterest.com" + urlRoute)));
    }
    callback.success();
  }

  private void executeCheckAvailable(CallbackContext callback) {
    Log.v(TAG, "Checking pinterest availability");
    callback.error(AVAILABLE + " is not Supported on Android");
  }


}
