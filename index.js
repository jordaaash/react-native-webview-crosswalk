'use strict';

import React, { PropTypes } from 'react';
import ReactNative, { requireNativeComponent, View, StyleSheet } from 'react-native';

var { 
	addons: { PureRenderMixin }, 
	NativeModules: { UIManager, CrosswalkWebViewManager: { JSNavigationScheme } } 
} = ReactNative;

var resolveAssetSource = require('../../node_modules/react-native/Libraries/Image/resolveAssetSource');

var WEBVIEW_REF = 'crosswalkWebView';

var CrosswalkWebView = React.createClass({
    mixins:    [PureRenderMixin],
    statics:   { JSNavigationScheme },
    propTypes: {
        localhost:               PropTypes.bool.isRequired,
        onNavigationStateChange: PropTypes.func,
        url:                     PropTypes.string,
        source:                  PropTypes.oneOfType([
          PropTypes.shape({
            /*
             * The URI to load in the WebView. Can be a local or remote file.
             */
            uri: PropTypes.string,
          }),
          PropTypes.shape({
            /*
             * A static HTML page to display in the WebView.
             */
            html: PropTypes.string,
          }),
          /*
           * Used internally by packager.
           */
          PropTypes.number,
        ]),
        ...View.propTypes
    },
    getDefaultProps () {
        return {
            localhost: false
        };
    },
    render () {
        var webViewStyles = [styles.container, this.props.style];
        var source = this.props.source || {};
        if (this.props.url) {
          source.uri = this.props.url;
        }

        return (
            <NativeCrosswalkWebView
                ref={ WEBVIEW_REF }
                key="webViewKey"
                style={ webViewStyles }
                source={ resolveAssetSource(source) }
                testID={ this.props.testID }
                localhost={ this.props.localhost }
                onNavigationStateChange={ this.onNavigationStateChange } />
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

var styles = StyleSheet.create({
  container: {
    flex: 1,
  },
});

export default CrosswalkWebView;
