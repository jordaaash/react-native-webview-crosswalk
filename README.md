# react-native-webview-crosswalk
Crosswalk's WebView for React Native on Android.

[![npm version](http://img.shields.io/npm/v/react-native-webview-crosswalk.svg?style=flat-square)](https://npmjs.org/package/react-native-webview-crosswalk "View this project on npm")
[![npm downloads](http://img.shields.io/npm/dm/react-native-webview-crosswalk.svg?style=flat-square)](https://npmjs.org/package/react-native-webview-crosswalk "View this project on npm")
[![npm licence](http://img.shields.io/npm/l/react-native-webview-crosswalk.svg?style=flat-square)](https://npmjs.org/package/react-native-webview-crosswalk "View this project on npm")

### Installation

```bash
npm install react-native-webview-crosswalk --save
```

### Include module in your Android project

* In `android/setting.gradle`

```gradle
...
include ':CrosswalkWebView', ':app'
project(':CrosswalkWebView').projectDir = new File(rootProject.projectDir, '../node_modules/react-native-webview-crosswalk')
```

* In `android/app/build.gradle`

```gradle
...
dependencies {
  ...
  compile project(':CrosswalkWebView')
}
```

* Register package in MainActivity.java

```java
import com.jordansexton.react.crosswalk.webview.CrosswalkWebViewPackage;  // <--- import

public class MainActivity extends ReactActivity {
  ......

  @Override
  protected List<ReactPackage> getPackages() {
    return Arrays.<ReactPackage>asList(
        new MainReactPackage(),
        new CrosswalkWebViewPackage() // <------ add this line to your MainActivity class
    );
  }

  ......

}
```

## License
MIT
