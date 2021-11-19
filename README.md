# can-i-use-on-webview

I love and rely on [caniuse](https://caniuse.com/) to know which features are supported in browsers, but for WebViews the information is not always accurate, specially when it's used to load local assets instead of remote assets from a https url.

So this repository will contain a set of test apps that use web APIs and are run on WebView apps to verify if the API works or not.
Also will document the required configuration that the WebView might need to run those APIs (permissions, usage descriptions, delegates, chrome client, etc.)

The repository can also be used to report issues to WebKit and Chromium teams with those web APIs.

| JavaScript API | iOS | Android |
| -------------- | --- | ------- |
| clipboard      |  ✅  |   ❌    |
| geolocation    |  ✅  |   ✅    |
| getUserMedia   |  ✅  |   ✅    |
| permissions    |  ❌  |   ❌    |
| share          |  ✅  |   ❌    |
