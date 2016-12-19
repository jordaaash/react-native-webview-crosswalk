'use strict';

import React, { PropTypes } from 'react';
import ReactNative, { requireNativeComponent, View } from 'react-native';

var {
    addons: { PureRenderMixin },
    NativeModules: { UIManager, CrosswalkWebViewManager: { JSNavigationScheme } }
} = ReactNative;

var resolveAssetSource = require('react-native/Libraries/Image/resolveAssetSource');

var WEBVIEW_REF = 'crosswalkWebView';

var CrosswalkWebView = React.createClass({
    mixins:    [PureRenderMixin],
    statics:   { JSNavigationScheme },
    propTypes: {
        injectedJavaScript:      PropTypes.string,
        localhost:               PropTypes.bool.isRequired,
        onError:                 PropTypes.func,
        onNavigationStateChange: PropTypes.func,
        source:                  PropTypes.oneOfType([
            PropTypes.shape({
                uri: PropTypes.string,  // URI to load in WebView
            }),
            PropTypes.shape({
                html: PropTypes.string, // static HTML to load in WebView
            }),
            PropTypes.number,           // used internally by React packager
        ]),
        url:                     PropTypes.string,
        ...View.propTypes
    },
    getDefaultProps () {
        return {
            localhost: false
        };
    },
    render () {
        var source = this.props.source || {};
        if (this.props.url) {
            source.uri = this.props.url;
        }
        var nativeProps = Object.assign({}, this.props, {
            onCrosswalkWebViewNavigationStateChange: this.onNavigationStateChange,
            onCrosswalkWebViewError: this.onError
        });
        return (
            <NativeCrosswalkWebView
                { ...nativeProps }
                ref={ WEBVIEW_REF }
                source={ resolveAssetSource(source) }
            />
        );
    },
    getWebViewHandle () {
        return ReactNative.findNodeHandle(this.refs[WEBVIEW_REF]);
    },
    onNavigationStateChange (event) {
        var { onNavigationStateChange } = this.props;
        if (onNavigationStateChange) {
            onNavigationStateChange(event.nativeEvent);
        }
    },
    onError (event) {
        var { onError } = this.props;
        if (onError) {
            onError(event.nativeEvent);
        }
    },
    goBack () {
        UIManager.dispatchViewManagerCommand(
            this.getWebViewHandle(),
            UIManager.CrosswalkWebView.Commands.goBack,
            null
        );
    },
    goForward () {
        UIManager.dispatchViewManagerCommand(
            this.getWebViewHandle(),
            UIManager.CrosswalkWebView.Commands.goForward,
            null
        );
    },
    reload () {
        UIManager.dispatchViewManagerCommand(
            this.getWebViewHandle(),
            UIManager.CrosswalkWebView.Commands.reload,
            null
        );
    }
});

var NativeCrosswalkWebView = requireNativeComponent('CrosswalkWebView', CrosswalkWebView);

export default CrosswalkWebView;
