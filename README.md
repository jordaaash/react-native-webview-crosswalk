# react-native-webview-crosswalk
Crosswalk's WebView for React Native on Android.

[![npm version](http://img.shields.io/npm/v/react-native-webview-crosswalk.svg?style=flat-square)](https://npmjs.org/package/react-native-webview-crosswalk "View this project on npm")
[![npm downloads](http://img.shields.io/npm/dm/react-native-webview-crosswalk.svg?style=flat-square)](https://npmjs.org/package/react-native-webview-crosswalk "View this project on npm")
[![npm licence](http://img.shields.io/npm/l/react-native-webview-crosswalk.svg?style=flat-square)](https://npmjs.org/package/react-native-webview-crosswalk "View this project on npm")

### Dependencies

* [0.4.0](https://github.com/jordansexton/react-native-webview-crosswalk/releases/tag/v0.4.0)+: `react-native >=0.32.0`, `react >= 15.3.0`

* [0.3.0](https://github.com/jordansexton/react-native-webview-crosswalk/releases/tag/v0.3.0)+: `react-native >=0.29.0`, `react >= 15.2.0`

* [0.2.0](https://github.com/jordansexton/react-native-webview-crosswalk/releases/tag/v0.2.0)+: `react-native >=0.25.0`, `react >= 0.14.5`

* [0.1.0](https://github.com/jordansexton/react-native-webview-crosswalk/releases/tag/v0.1.0): `react-native >= 0.19.0`

### Installation

* From the root of your React Native project

```shell
npm install react-native-webview-crosswalk --save
mkdir android/app/libs
cp node_modules/react-native-webview-crosswalk/libs/xwalk_core_library-22.52.561.4.aar android/app/libs/
```

### Include module in your Android project

* In `android/setting.gradle`

```gradle
...
include ':CrosswalkWebView', ':app'
project(':CrosswalkWebView').projectDir = new File(rootProject.projectDir, '../node_modules/react-native-webview-crosswalk')
```

### Include libs in your Android project

* In `android/build.gradle`

```gradle
...
allprojects {
    repositories {
        mavenLocal()
        jcenter()

        flatDir {          // <--- add this line
            dirs 'libs'    // <--- add this line
        }                  // <--- add this line
    }
}
```

* In `android/app/build.gradle`

```gradle
...
dependencies {
  ...
  compile (name: "xwalk_core_library-22.52.561.4", ext: "aar")     // <--- add this line
  compile project(':CrosswalkWebView')                             // <--- add this line
}
```
* Register package :

If [0.1.0](https://github.com/jordansexton/react-native-webview-crosswalk/releases/tag/v0.1.0) or [0.2.0](https://github.com/jordansexton/react-native-webview-crosswalk/releases/tag/v0.2.0)+ used add code into MainActivity.java

```java
import com.jordansexton.react.crosswalk.webview.CrosswalkWebViewPackage;    // <--- add this line

public class MainActivity extends ReactActivity {
  ......

  @Override
  protected List<ReactPackage> getPackages() {
    return Arrays.<ReactPackage>asList(
        new MainReactPackage(),
        new CrosswalkWebViewPackage(this)    // <--- add this line
    );
  }

  ......

}
```

If [0.3.0](https://github.com/jordansexton/react-native-webview-crosswalk/releases/tag/v0.3.0)+ used add code into MainApplication.java

```java
import com.jordansexton.react.crosswalk.webview.CrosswalkWebViewPackage;    // <--- add this line

public class MainApplication extends Application implements ReactApplication {
  ......

  @Override
  protected List<ReactPackage> getPackages() {
    return Arrays.<ReactPackage>asList(
        new MainReactPackage(),
        new CrosswalkWebViewPackage()    // <--- add this line
    );
  }

  ......

}
```

## License
MIT
