var exec = require('cordova/exec');
var DebugCheck = {
     isDebugging: function (success, error) {
          exec(
               function (result) {
                    if (typeof success === 'function') {
                         success(!!result);
                    }
               },
               function (err) {
                    if (typeof error === 'function') {
                         error(err);
                    }
               },
               'DebugCheck',
               'isDebugging',
               [],
          );
     },
     getStats: function (success, error) {
          exec(
               function (result) {
                    if (typeof success === 'function') {
                         success(result);
                    }
               },
               function (err) {
                    if (typeof error === 'function') {
                         error(err);
                    }
               },
               'DebugCheck',
               'getStats',
               [],
          );
     },
     isCompromised: function (success, error) {
          DebugCheck.getStats(function (stats) {
               var compromised = stats.javaDebugger || stats.webviewDebugger || stats.adbEnabled || stats.emulated || stats.fridaPresent || stats.hookFramework;
               if (typeof success === 'function') {
                    success(compromised);
               }
          }, error);
     },
};
module.exports = DebugCheck;
