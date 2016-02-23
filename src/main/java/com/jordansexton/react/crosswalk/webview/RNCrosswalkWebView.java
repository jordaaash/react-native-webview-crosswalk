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

class RNCrosswalkWebView extends XWalkView {
    protected class ResourceClient extends XWalkResourceClient {
        ResourceClient (XWalkView view) {
            super(view);
        }

        @Override
        public void onLoadFinished (XWalkView view, String url) {
            XWalkNavigationHistory navigationHistory = view.getNavigationHistory();
            mEventDispatcher.dispatchEvent(
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
            mEventDispatcher.dispatchEvent(
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
            if (uri.getHost().equals("localhost")) {
                return false;
            }
            else {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, uri);
                mActivity.startActivity(browserIntent);
                return true;
            }
        }
    }

    private final Activity mActivity;
    private final EventDispatcher mEventDispatcher;
    private final ResourceClient mResourceClient;

    public RNCrosswalkWebView (ReactContext reactContext, Activity activity) {
        super(reactContext, activity);

        mActivity = activity;
        mEventDispatcher = reactContext.getNativeModule(UIManagerModule.class).getEventDispatcher();
        mResourceClient = new ResourceClient(this);

        this.setResourceClient(mResourceClient);
    }
}
