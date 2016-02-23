package com.jordansexton.react.crosswalk.webview;

import android.app.Activity;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.common.MapBuilder;
import com.facebook.react.common.annotations.VisibleForTesting;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.ViewGroupManager;
import com.facebook.react.uimanager.annotations.ReactProp;
import org.xwalk.core.XWalkNavigationHistory;
import org.xwalk.core.XWalkView;

import javax.annotation.Nullable;
import java.util.Map;

public class RNCrosswalkWebViewManager extends ViewGroupManager<RNCrosswalkWebView> {
    public static final int GO_BACK = 1;
    public static final int GO_FORWARD = 2;
    public static final int RELOAD = 3;

    @VisibleForTesting
    public static final String REACT_CLASS = "RNCrosswalkWebViewAndroid";

    Activity mActivity;

    public RNCrosswalkWebViewManager (Activity activity) {
        mActivity = activity;
    }

    @Override
    public String getName () {
        return REACT_CLASS;
    }

    @Override
    public RNCrosswalkWebView createViewInstance (ThemedReactContext context) {
        return new RNCrosswalkWebView(context, mActivity);
    }

    @ReactProp(name = "url")
    public void setUrl (final RNCrosswalkWebView view, @Nullable final String url) {
        mActivity.runOnUiThread(new Runnable () {
            @Override
            public void run () {
                view.load(url, null);
            }
        });
    }

    @Override
    public
    @Nullable
    Map<String, Integer> getCommandsMap () {
        return MapBuilder.of(
                "goBack", GO_BACK,
                "goForward", GO_FORWARD,
                "reload", RELOAD
        );
    }

    @Override
    public void receiveCommand (RNCrosswalkWebView view, int commandId, @Nullable ReadableArray args) {
        switch (commandId) {
            case GO_BACK:
                view.getNavigationHistory().navigate(XWalkNavigationHistory.Direction.BACKWARD, 1);
                break;
            case GO_FORWARD:
                view.getNavigationHistory().navigate(XWalkNavigationHistory.Direction.FORWARD, 1);
                break;
            case RELOAD:
                view.reload(XWalkView.RELOAD_NORMAL);
                break;
        }
    }

    @Override
    public Map getExportedCustomDirectEventTypeConstants () {
        return MapBuilder.of(
                NavigationStateChangeEvent.EVENT_NAME, MapBuilder.of("registrationName", "onNavigationStateChange")
        );
    }
}
