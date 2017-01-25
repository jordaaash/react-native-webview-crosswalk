'use strict';

import React, { PropTypes } from 'react';
import ReactNative, { requireNativeComponent, View, ActivityIndicator, StyleSheet } from 'react-native';

var {
    addons: { PureRenderMixin },
    NativeModules: { UIManager, CrosswalkWebViewManager: { JSNavigationScheme } }
} = ReactNative;

var resolveAssetSource = require('react-native/Libraries/Image/resolveAssetSource');

var WEBVIEW_REF = 'crosswalkWebView';

const WebViewState = {
    IDLE: 'IDLE',
    LOADING: 'LOADING',
    ERROR: 'ERROR',
};

var defaultRenderLoading = () => (
  <View style={styles.loadingView}>
    <ActivityIndicator
      style={styles.loadingProgressBar}
    />
  </View>
);

var CrosswalkWebView = React.createClass({
    mixins:    [PureRenderMixin],
    statics:   { JSNavigationScheme },
    propTypes: {
        injectedJavaScript:      PropTypes.string,
        localhost:               PropTypes.bool.isRequired,
        onError:                 PropTypes.func,
        onNavigationStateChange: PropTypes.func,
        onProgress:              PropTypes.func,
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
        renderError: PropTypes.func,
        renderLoading: PropTypes.func,
        onLoad: PropTypes.func,
        onLoadEnd: PropTypes.func,
        startInLoadingState:     PropTypes.bool, // force WebView to show loadingView on first load
        ...View.propTypes
    },
    getInitialState () {
      return {
          viewState: WebViewState.IDLE,
          lastErrorEvent: null,
          startInLoadingState: true,
      };
    },
    getDefaultProps () {
      return {
          localhost: false
      };
    },
    componentWillMount() {
      if (this.props.startInLoadingState) {
        this.setState({viewState: WebViewState.LOADING});
      }
    },
    render () {
        var otherView = null;

        if (this.state.viewState === WebViewState.LOADING) {
          otherView = (this.props.renderLoading || defaultRenderLoading)();
        } else if (this.state.viewState === WebViewState.ERROR) {
          var errorEvent = this.state.lastErrorEvent;
          otherView = this.props.renderError && this.props.renderError(
            errorEvent.url,
            errorEvent.errorNumber,
            errorEvent.errorMessage);
        } else if (this.state.viewState !== WebViewState.IDLE) {
          console.error('WebView invalid state encountered: ' + this.state.loading);
        }

        var webViewStyles = [styles.container, this.props.style];
        if (this.state.viewState === WebViewState.LOADING ||
          this.state.viewState === WebViewState.ERROR) {
          // if we're in either LOADING or ERROR states, don't show the webView
          webViewStyles.push(styles.hidden);
        }

        var source = this.props.source || {};
        if (this.props.url) {
            source.uri = this.props.url;
        }
        var nativeProps = Object.assign({}, this.props, {
            onCrosswalkWebViewNavigationStateChange: this.onNavigationStateChange,
            onCrosswalkWebViewError: this.onError,
            onCrosswalkWebViewProgress: this.onProgress
        });
        var webView =
            <NativeCrosswalkWebView
                { ...nativeProps }
                ref={ WEBVIEW_REF }
                source={ resolveAssetSource(source) }
                style={webViewStyles}
            />;

        return (
          <View style={styles.container}>
            {webView}
            {otherView}
          </View>
        );
    },
    getWebViewHandle () {
        return ReactNative.findNodeHandle(this.refs[WEBVIEW_REF]);
    },
    onNavigationStateChange (event) {
        if (event.nativeEvent && !event.nativeEvent.loading) {
          if (this.state.viewState !== WebViewState.ERROR) {
            this._onLoadingFinish(event);
          }
        }

        var { onNavigationStateChange } = this.props;
        if (onNavigationStateChange) {
            onNavigationStateChange(event.nativeEvent);
        }
    },
    onError (event) {
      this._onLoadingFinish(event);

      var {onError, onLoadEnd} = this.props;
      onError && onError(event.nativeEvent);
      onLoadEnd && onLoadEnd(event);
      console.warn('Encountered an error loading page', event.nativeEvent);

      this.setState({
        lastErrorEvent: event.nativeEvent,
        viewState: WebViewState.ERROR
      });
    },
    _onLoadingFinish (event) {
      var {onLoad, onLoadEnd} = this.props;
      onLoad && onLoad(event);
      onLoadEnd && onLoadEnd(event);
      this.setState({
        viewState: WebViewState.IDLE,
      });
    },
    onProgress (event) {
        var { onProgress } = this.props;
        if (onProgress) {
            onProgress(event.nativeEvent.progress / 100);
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

var styles = StyleSheet.create({
  container: {
    flex: 1,
  },
  hidden: {
    height: 0,
    flex: 0, // disable 'flex:1' when hiding a View
  },
  loadingView: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
  },
  loadingProgressBar: {
    height: 20,
  },
});

var NativeCrosswalkWebView = requireNativeComponent('CrosswalkWebView', CrosswalkWebView);

export default CrosswalkWebView;
