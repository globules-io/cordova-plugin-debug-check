package io.globules;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;
import org.json.JSONArray;
import android.os.Debug;
import android.webkit.WebView;

public class DebugCheck extends CordovaPlugin {

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) {
        if ("isDebugging".equals(action)) {
            boolean javaDebugger = Debug.isDebuggerConnected();
            boolean webviewDebugger = WebView.isDebuggingEnabled();
            boolean isDebugging = javaDebugger || webviewDebugger;
            callbackContext.success(isDebugging ? 1 : 0);
            return true;
        }
        return false;
    }
}
