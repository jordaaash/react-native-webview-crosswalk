package com.jordansexton.react.crosswalk.webview;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.uimanager.events.Event;
import com.facebook.react.uimanager.events.RCTEventEmitter;

class ErrorEvent extends Event<ErrorEvent> {

    public static final String EVENT_NAME = "error";

    private final int errorNumber;

    private final String errorMessage;

    private final String url;

    protected ErrorEvent (int viewTag, long timestampMs, int _errorNumber, String _errorMessage, String _url) {
        super(viewTag);

        errorNumber = _errorNumber;
        errorMessage = _errorMessage;
        url = _url;
    }

    @Override
    public String getEventName () {
        return EVENT_NAME;
    }

    @Override
    public void dispatch (RCTEventEmitter rctEventEmitter) {
        rctEventEmitter.receiveEvent(getViewTag(), getEventName(), serializeEventData());
    }

    private WritableMap serializeEventData () {
        WritableMap eventData = Arguments.createMap();
        eventData.putInt("errorNumber", errorNumber);
        eventData.putString("errorMessage", errorMessage);
        eventData.putString("url", url);

        return eventData;
    }
}
