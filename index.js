'use strict';

import React, { requireNativeComponent, PropTypes, View } from 'react-native';
var { NativeModules: { UIManager } } = React;

var WEBVIEW_REF = 'crosswalkWebView';

var CrosswalkWebView = React.createClass({
    propTypes: {
        onNavigationStateChange: PropTypes.func,
        url:                     PropTypes.string,
        ...View.propTypes
    },
    render () {
        return (
            <RNCrosswalkWebView
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
            UIManager.RNCrosswalkWebView.Commands.goBack,
            null
        );
    },
    goForward () {
        UIManager.dispatchViewManagerCommand(
            this.getWebViewHandle(),
            UIManager.RNCrosswalkWebView.Commands.goForward,
            null
        );
    },
    reload () {
        UIManager.dispatchViewManagerCommand(
            this.getWebViewHandle(),
            UIManager.RNCrosswalkWebView.Commands.reload,
            null
        );
    }
});

var RNCrosswalkWebView = requireNativeComponent('RNCrosswalkWebView', CrosswalkWebView);

module.exports = CrosswalkWebView;
