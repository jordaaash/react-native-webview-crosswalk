package com.jordansexton.react.crosswalk.webview;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.SystemClock;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.uimanager.UIManagerModule;
import com.facebook.react.uimanager.events.EventDispatcher;
import org.xwalk.core.XWalkNavigationHistory;
import org.xwalk.core.XWalkResourceClient;
import org.xwalk.core.XWalkView;

class CrosswalkWebView extends XWalkView {

    private final Activity activity;

    private final EventDispatcher eventDispatcher;

    private final ResourceClient resourceClient;

    public CrosswalkWebView (ReactContext reactContext, Activity _activity) {
        super(reactContext, _activity);

        activity = _activity;
        eventDispatcher = reactContext.getNativeModule(UIManagerModule.class).getEventDispatcher();
        resourceClient = new ResourceClient(this);

        this.setResourceClient(resourceClient);
    }

    public Boolean getLocalhost () {
        return resourceClient.getLocalhost();
    }

    public void setLocalhost (Boolean localhost) {
        resourceClient.setLocalhost(localhost);
    }

    protected class ResourceClient extends XWalkResourceClient {

        private Boolean localhost = false;

        ResourceClient (XWalkView view) {
            super(view);
        }

        public Boolean getLocalhost () {
            return localhost;
        }

        public void setLocalhost (Boolean _localhost) {
            localhost = _localhost;
        }

        @Override
        public void onLoadFinished (XWalkView view, String url) {
            XWalkNavigationHistory navigationHistory = view.getNavigationHistory();
            eventDispatcher.dispatchEvent(
                new NavigationStateChangeEvent(
                    getId(),
                    SystemClock.uptimeMillis(),
                    view.getTitle(),
                    false,
                    url,
                    navigationHistory.canGoBack(),
                    navigationHistory.canGoForward()
                )
            );

        }

        @Override
        public void onLoadStarted (XWalkView view, String url) {
            XWalkNavigationHistory navigationHistory = view.getNavigationHistory();
            eventDispatcher.dispatchEvent(
                new NavigationStateChangeEvent(
                    getId(),
                    SystemClock.uptimeMillis(),
                    view.getTitle(),
                    true,
                    url,
                    navigationHistory.canGoBack(),
                    navigationHistory.canGoForward()
                )
            );
        }

        @Override
        public boolean shouldOverrideUrlLoading (XWalkView view, String url) {
            Uri uri = Uri.parse(url);
            if (uri.getScheme().equals(CrosswalkWebViewManager.JSNavigationScheme)) {
                onLoadFinished(view, url);
                return true;
            }
            else if (getLocalhost()) {
                if (uri.getHost().equals("localhost")) {
                    return false;
                }
                else {
                    overrideUri(uri);
                    return true;
                }
            }
            else if (uri.getScheme().equals("http") || uri.getScheme().equals("https") || uri.getScheme().equals("file")) {
                return false;
            }
            else {
                overrideUri(uri);
                return true;
            }
        }

        private void overrideUri (Uri uri) {
            Intent action = new Intent(Intent.ACTION_VIEW, uri);
            activity.startActivity(action);
        }
    }
}
