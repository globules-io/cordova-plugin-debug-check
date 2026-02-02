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
};
module.exports = DebugCheck;
