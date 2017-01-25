package com.jordansexton.react.crosswalk.webview;

import android.app.Activity;
import android.content.Intent;

import com.facebook.react.bridge.BaseActivityEventListener;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.common.MapBuilder;
import com.facebook.react.common.annotations.VisibleForTesting;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.ViewGroupManager;
import com.facebook.react.uimanager.annotations.ReactProp;
import org.xwalk.core.XWalkNavigationHistory;
import org.xwalk.core.XWalkView;

import org.json.JSONObject;
import org.json.JSONException;

import com.facebook.react.views.webview.events.TopMessageEvent;

import javax.annotation.Nullable;
import java.util.Map;

public class CrosswalkWebViewGroupManager extends ViewGroupManager<CrosswalkWebView> {

    public static final int GO_BACK = 1;

    public static final int GO_FORWARD = 2;

    public static final int RELOAD = 3;

    public static final int POST_MESSAGE = 4;

    @VisibleForTesting
    public static final String REACT_CLASS = "CrosswalkWebView";

    private ReactApplicationContext reactContext;

    private static final String BLANK_URL = "about:blank";

    public CrosswalkWebViewGroupManager (ReactApplicationContext _reactContext) {
        reactContext = _reactContext;
    }

    @Override
    public String getName () {
        return REACT_CLASS;
    }

    @Override
    public CrosswalkWebView createViewInstance (ThemedReactContext context) {
        Activity _activity = reactContext.getCurrentActivity();
        CrosswalkWebView crosswalkWebView = new CrosswalkWebView(context, _activity);
        context.addLifecycleEventListener(crosswalkWebView);
        reactContext.addActivityEventListener(new XWalkActivityEventListener(crosswalkWebView));
        return crosswalkWebView;
    }

    @Override
    public void onDropViewInstance(CrosswalkWebView view) {
        super.onDropViewInstance(view);
        ((ThemedReactContext) view.getContext()).removeLifecycleEventListener((CrosswalkWebView) view);
        view.onDestroy();
    }

    @ReactProp(name = "source")
    public void setSource(final CrosswalkWebView view, @Nullable ReadableMap source) {
      Activity _activity = reactContext.getCurrentActivity();
      if (_activity != null) {
          if (source != null) {
              if (source.hasKey("html")) {
                  final String html = source.getString("html");
                  _activity.runOnUiThread(new Runnable() {
                      @Override
                      public void run () {
                          view.load(null, html);
                      }
                  });
                  return;
              }
              if (source.hasKey("uri")) {
                  final String url = source.getString("uri");
                  _activity.runOnUiThread(new Runnable() {
                      @Override
                      public void run () {
                          view.load(url, null);
                      }
                  });
                  return;
              }
          }
      }
      setUrl(view, BLANK_URL);
    }


    @ReactProp(name = "injectedJavaScript")
    public void setInjectedJavaScript (XWalkView view, @Nullable String injectedJavaScript) {
        ((CrosswalkWebView) view).setInjectedJavaScript(injectedJavaScript);
    }

    @ReactProp(name = "messagingEnabled")
    public void setMessagingEnabled(XWalkView view, boolean enabled) {
        ((CrosswalkWebView) view).setMessagingEnabled(enabled);
    }

    @ReactProp(name = "url")
    public void setUrl (final CrosswalkWebView view, @Nullable final String url) {
        Activity _activity = reactContext.getCurrentActivity();
        if (_activity != null) {
            _activity.runOnUiThread(new Runnable() {
                @Override
                public void run () {
                    view.load(url, null);
                }
            });
        }
    }

    @ReactProp(name = "localhost")
    public void setLocalhost (CrosswalkWebView view, Boolean localhost) {
        view.setLocalhost(localhost);
    }

    @Override
    public
    @Nullable
    Map<String, Integer> getCommandsMap () {
        return MapBuilder.of(
            "goBack", GO_BACK,
            "goForward", GO_FORWARD,
            "reload", RELOAD,
            "postMessage", POST_MESSAGE
        );
    }

    @Override
    public void receiveCommand (CrosswalkWebView view, int commandId, @Nullable ReadableArray args) {
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
            case POST_MESSAGE:
                try {
                    JSONObject eventInitDict = new JSONObject();
                    eventInitDict.put("data", args.getString(0));
                    view.evaluateJavascript("document.dispatchEvent(new MessageEvent('message', " + eventInitDict.toString() + "))", null);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
                break;
        }
    }

    @Override
    public Map getExportedCustomDirectEventTypeConstants () {
        return MapBuilder.of(
            NavigationStateChangeEvent.EVENT_NAME,
            MapBuilder.of("registrationName", "onCrosswalkWebViewNavigationStateChange"),
            ErrorEvent.EVENT_NAME,
            MapBuilder.of("registrationName", "onCrosswalkWebViewError"),
            ProgressEvent.EVENT_NAME,
            MapBuilder.of("registrationName", "onCrosswalkWebViewProgress"),
            TopMessageEvent.EVENT_NAME,
            MapBuilder.of("registrationName", "onMessage")
        );
    }

    protected class XWalkActivityEventListener extends BaseActivityEventListener {
        private CrosswalkWebView crosswalkWebView;

        public XWalkActivityEventListener(CrosswalkWebView _crosswalkWebView) {
            crosswalkWebView = _crosswalkWebView;
        }

        @Override
        public void onActivityResult(Activity activity, int requestCode, int resultCode, Intent data) {
            crosswalkWebView.onActivityResult(requestCode, resultCode, data);
        }

        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data) {
            crosswalkWebView.onActivityResult(requestCode, resultCode, data);
        }
    }
}
