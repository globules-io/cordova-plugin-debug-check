# cordova-plugin-debug-check
Check if an app is being debugged via ADB/Chrome

## Installation
```bash
cordova plugin add @globules-io/cordova-plugin-debug-check
cordova plugin rm @globules-io/cordova-plugin-debug-check
```

## Supported Platforms
Android

## JS API
```bash
DebugCheck.isDebugging(
     function (isDebugging) { 
          console.log('Debugger attached:', isDebugging); 
          if (isDebugging) {
                // Your policy: kill app, show message, whatever navigator.app.exitApp(); 
          } 
     }, 
     function (err) { 
          console.error('DebugCheck error:', err); 
     }
);
```
