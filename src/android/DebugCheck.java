package io.globules;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;
import org.json.JSONArray;
import org.json.JSONException;
import android.os.Debug;

public class DebugCheck extends CordovaPlugin {
     @Override
     public boolean execute(String action, JSONArray args, CallbackContext callbackContext) {
          if ("isDebugging".equals(action)) {
               boolean isDebugging = Debug.isDebuggerConnected();
               callbackContext.success(isDebugging ? 1 : 0);
               return true;
          }
          return false;
     }
}