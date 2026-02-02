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
Check in one call if the app is being debugged
```bash
DebugCheck.isDebugging(
     function (bool) { 
          console.log('Debugger attached:', bool); 
          if (bool) {
                // Your policy: kill app, show message, whatever navigator.app.exitApp(); 
          } 
     }, 
     function (err) { 
          console.error('DebugCheck error:', err); 
     }
);
```

Get information
```bash
DebugCheck.getStats(
     function (stats) { 
          console.log(stats);
          //{ javaDebugger: true|false, webviewDebugger: true|false, adbEnabled: true|false, emulated: true|false, fridaPresent: true|false, hookFramework: true|false }    
     }, 
     function (err) { 
          console.error('DebugCheck error:', err); 
     }
);
```

Check if app is compromised
```bash
DebugCheck.isCompromised(
     function (bool) {
          console.log(true);
          //if true, app is compromised
     },
     function (err) { 
          console.error('DebugCheck error:', err); 
     }
);

```

Recursive check if app is compromised (in milliseconds)
```bash
DebugCheck.watch(ms);
```
then listen to custom event DebugCheck.COMPROMISED
