package com.jordansexton.react.crosswalk.webview;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;

import java.util.HashMap;
import java.util.Map;

public class CrosswalkWebViewManager extends ReactContextBaseJavaModule {

    public static final String JSNavigationScheme = "react-js-navigation";

    private static final String REACT_CLASS = "CrosswalkWebViewManager";

    public CrosswalkWebViewManager (ReactApplicationContext reactContext) {
        super(reactContext);
    }

    @Override
    public String getName () {
        return REACT_CLASS;
    }

    @Override
    public Map<String, Object> getConstants () {
        Map<String, Object> constants = new HashMap<>();
        constants.put("JSNavigationScheme", JSNavigationScheme);
        return constants;
    }
}
