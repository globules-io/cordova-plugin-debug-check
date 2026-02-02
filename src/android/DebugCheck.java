package io.globules;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;
import org.json.JSONArray;
import org.json.JSONObject;

import android.os.Debug;
import android.os.Build;
import android.webkit.WebView;
import android.provider.Settings;
import android.content.Context;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStreamReader;

public class DebugCheck extends CordovaPlugin {

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) {
        Context ctx = this.cordova.getActivity().getApplicationContext();

        if ("isDebugging".equals(action)) {

            boolean javaDebugger    = Debug.isDebuggerConnected();
            boolean webviewDebugger = WebView.isDebuggingEnabled();
            boolean adbEnabled      = isAdbEnabled(ctx);
            boolean emulator        = isEmulator();
            boolean fridaPresent    = isFridaPresent();
            boolean hookFramework   = isHookFrameworkPresent();

            boolean detected = javaDebugger
                    || webviewDebugger
                    || adbEnabled
                    || emulator
                    || fridaPresent
                    || hookFramework;

            callbackContext.success(detected ? 1 : 0);
            return true;
        }

        if ("getStats".equals(action)) {
            try {
                JSONObject stats = new JSONObject();

                stats.put("javaDebugger",    Debug.isDebuggerConnected());
                stats.put("webviewDebugger", WebView.isDebuggingEnabled());
                stats.put("adbEnabled",      isAdbEnabled(ctx));
                stats.put("emulated",        isEmulator());
                stats.put("fridaPresent",    isFridaPresent());
                stats.put("hookFramework",   isHookFrameworkPresent());

                callbackContext.success(stats);
            } catch (Exception e) {
                callbackContext.error("Failed to build stats object");
            }
            return true;
        }

        return false;
    }

    // ---------- ADB detection ----------
    private boolean isAdbEnabled(Context context) {
        try {
            int adb = Settings.Global.getInt(
                    context.getContentResolver(),
                    Settings.Global.ADB_ENABLED,
                    0
            );
            if (adb == 1) return true;
        } catch (Exception ignored) {}

        String prop = getSystemProperty("persist.sys.usb.config");
        return prop != null && prop.contains("adb");
    }

    private String getSystemProperty(String name) {
        try {
            Process p = Runtime.getRuntime().exec("getprop " + name);
            BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
            return br.readLine();
        } catch (Exception e) {
            return null;
        }
    }

    // ---------- Emulator detection ----------
    private boolean isEmulator() {
        String fp = Build.FINGERPRINT;
        String model = Build.MODEL;
        String brand = Build.BRAND;
        String device = Build.DEVICE;
        String product = Build.PRODUCT;
        String manufacturer = Build.MANUFACTURER;

        if (fp != null && (fp.startsWith("generic")
                || fp.toLowerCase().contains("vbox")
                || fp.toLowerCase().contains("test-keys"))) return true;

        if (model != null && (model.contains("google_sdk")
                || model.contains("Emulator")
                || model.contains("Android SDK built for x86"))) return true;

        if (manufacturer != null && manufacturer.contains("Genymotion")) return true;

        if (brand != null && brand.startsWith("generic")
                && device != null && device.startsWith("generic")) return true;

        if (product != null && product.equals("google_sdk")) return true;

        return false;
    }

    // ---------- Frida detection ----------
    private boolean isFridaPresent() {
        String[] paths = new String[] {
                "/data/local/tmp/frida-server",
                "/data/local/frida-server",
                "/data/local/tmp/re.frida.server",
                "/system/lib/libfrida.so",
                "/system/lib64/libfrida.so",
                "/system/bin/frida-server",
                "/system/xbin/frida-server"
        };

        for (String path : paths) {
            if (new File(path).exists()) return true;
        }

        try {
            BufferedReader br = new BufferedReader(new FileReader("/proc/self/maps"));
            String line;
            while ((line = br.readLine()) != null) {
                String lower = line.toLowerCase();
                if (lower.contains("frida") || lower.contains("gadget")) {
                    br.close();
                    return true;
                }
            }
            br.close();
        } catch (Exception ignored) {}

        return false;
    }

    // ---------- Xposed / LSPosed / Zygisk detection ----------
    private boolean isHookFrameworkPresent() {
        String[] hookClasses = new String[] {
                "de.robv.android.xposed.XposedBridge",
                "de.robv.android.xposed.XC_MethodHook",
                "org.lsposed.lspd.nativebridge.NativeAPI",
                "org.lsposed.lspd.core.Main",
                "com.topjohnwu.magisk.core.MagiskEnv"
        };

        for (String cls : hookClasses) {
            try {
                Class.forName(cls, false, this.getClass().getClassLoader());
                return true;
            } catch (ClassNotFoundException ignored) {}
        }

        String[] paths = new String[] {
                "/system/framework/XposedBridge.jar",
                "/system/lib/libxposed_art.so",
                "/system/lib64/libxposed_art.so",
                "/data/adb/modules/lsposed",
                "/data/adb/modules/zygisk",
                "/data/adb/lspd",
                "/data/adb/lsposed"
        };

        for (String path : paths) {
            if (new File(path).exists()) return true;
        }

        try {
            BufferedReader br = new BufferedReader(new FileReader("/proc/self/maps"));
            String line;
            while ((line = br.readLine()) != null) {
                String lower = line.toLowerCase();
                if (lower.contains("xposed")
                        || lower.contains("lsposed")
                        || lower.contains("zygisk")) {
                    br.close();
                    return true;
                }
            }
            br.close();
        } catch (Exception ignored) {}

        return false;
    }
}
