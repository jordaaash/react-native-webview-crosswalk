package com.jordansexton.react.crosswalk.webview;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.uimanager.events.Event;
import com.facebook.react.uimanager.events.RCTEventEmitter;

class ProgressEvent extends Event<ProgressEvent> {

    public static final String EVENT_NAME = "progress";

    private final int progress;

    protected ProgressEvent (int viewTag, long timestampMs, int progress) {
        super(viewTag);

        this.progress = progress;
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
        eventData.putInt("progress", progress);

        return eventData;
    }
}
