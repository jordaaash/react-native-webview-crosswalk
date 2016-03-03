'use strict';

import React, { requireNativeComponent, PropTypes, View } from 'react-native';
var { NativeModules: { UIManager, CrosswalkWebViewManager: { JSNavigationScheme } } } = React;

var WEBVIEW_REF = 'crosswalkWebView';

var CrosswalkWebView = React.createClass({
    statics: {
        JSNavigationScheme
    },
    propTypes: {
        onNavigationStateChange: PropTypes.func,
        url:                     PropTypes.string,
        ...View.propTypes
    },
    render () {
        return (
            <NativeCrosswalkWebView
                { ...this.props }
                onNavigationStateChange={ this.onNavigationStateChange }
                ref={ WEBVIEW_REF }/>
        );
    },
    getWebViewHandle () {
        return React.findNodeHandle(this.refs[WEBVIEW_REF]);
    },
    onNavigationStateChange (event) {
        var { onNavigationStateChange } = this.props;
        if (onNavigationStateChange) {
            onNavigationStateChange(event.nativeEvent);
        }
    },
    goBack () {
        UIManager.dispatchViewManagerCommand(
            this.getWebViewHandle(),
            UIManager.NativeCrosswalkWebView.Commands.goBack,
            null
        );
    },
    goForward () {
        UIManager.dispatchViewManagerCommand(
            this.getWebViewHandle(),
            UIManager.NativeCrosswalkWebView.Commands.goForward,
            null
        );
    },
    reload () {
        UIManager.dispatchViewManagerCommand(
            this.getWebViewHandle(),
            UIManager.NativeCrosswalkWebView.Commands.reload,
            null
        );
    }
});

var NativeCrosswalkWebView = requireNativeComponent('CrosswalkWebView', CrosswalkWebView);

module.exports = CrosswalkWebView;
