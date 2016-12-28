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
  private static final String TAG = "PinterestPlugin";
  private static final String INIT = "initPinterest";
  private static final String AVAILABLE = "canPinWithSDK";
  private static final String PIN = "pin";
  private static final int PINTEREST_RESULT = 10101;
  private CallbackContext currentCallback;

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
    /*cordova.getThreadPool().execute(new Runnable() {
      public void run() {
        final PinIt pinIt = new PinIt();
        pinIt.setUrl(sourceUrl);
        pinIt.setImageUrl(imageUrl);
        pinIt.setDescription(description);
        pinIt.setListener(_listener);
        pinIt.doPinIt(getApplicationContext());
      }
    });*/
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
      this.currentCallback = callback;
      cordova.startActivityForResult(this, new Intent(Intent.ACTION_VIEW, Uri.parse("pinterest://www.pinterest.com" + urlRoute)), PINTEREST_RESULT);
    } catch (Exception e) {
      activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.pinterest.com" + urlRoute)));
      callback.success();
    }
  }

  private void executeCheckAvailable(CallbackContext callback) {
    Log.v(TAG, "Checking pinterest availability");
    callback.error(AVAILABLE + " is not Supported on Android");
  }


  @Override
  public void onActivityResult(int requestCode, int resultCode, android.content.Intent data) {
    if (requestCode == PINTEREST_RESULT) {
      switch (resultCode){
        case Activity.RESULT_OK:
          currentCallback.success();
          break;
        case Activity.RESULT_CANCELED:
          //currentCallback.error(""); // empty string signals cancellation
          // Pinterest always signals back canceled, even when it shares successfully
          currentCallback.success();
          break;
        default:
          currentCallback.error("Pinterest sharing error (code " + resultCode + ")");
          break;
      }
    }
  }


}
