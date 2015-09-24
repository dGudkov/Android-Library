package ru.gdo.android.example.webview;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import ru.gdo.android.library.materialdesign.exception.ToolBarActivityException;
import ru.gdo.android.library.materialdesign.interfaces.IToolBarInterface;
import ru.gdo.android.library.materialdesign.widget.BaseFragment;

/**
 * @author Danil Gudkov <danil.gudkov@progforce.com>
 * @copyrights ProgForce, 2015
 * @since 24.09.15.
 */

public class WebViewFragment extends BaseFragment {

    protected final String URL = "url";

    private ProgressBar mProgressBar;
    private WebView mWebView;
    private String mUrl;

    public WebViewFragment() {
        this.mLayoutId = R.layout.webview_layout;
    }

    public static WebViewFragment newInstance(Context context,
                                              Toolbar toolBar,
                                              IToolBarInterface onToolBarClickListener,
                                              int titleResourceId, String url) throws ToolBarActivityException {
        try {
            WebViewFragment fragment = (WebViewFragment) BaseFragment.newInstance(
                    WebViewFragment.class,
                    context,
                    toolBar,
                    onToolBarClickListener
            );
            fragment.setTitleTextId(titleResourceId);
            fragment.setUrl(url);
            return fragment;
        } catch (Exception e) {
            throw new ToolBarActivityException("Error fragment initialization", e);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        if (rootView != null) {

            this.mToolBar.findViewById(R.id.status_bar_webview_layout).setVisibility(View.VISIBLE);
            this.mToolBar.findViewById(R.id.status_bar_webview).setVisibility(View.VISIBLE);
            this.mToolBar.findViewById(R.id.status_bar_webview_layout).setOnClickListener(this);
            this.mToolBar.findViewById(R.id.status_bar_webview).setOnClickListener(this);

            this.mProgressBar = (ProgressBar) rootView.findViewById(R.id.progressbar);
            this.mProgressBar.setVisibility(View.VISIBLE);

            this.mWebView = (WebView) rootView.findViewById(R.id.webView);
            this.mWebView.getSettings().setJavaScriptEnabled(true);
            this.mWebView.setVisibility(View.GONE);

            this.mWebView.setWebViewClient(new WebViewClient() {

                @Override
                public void onPageStarted(WebView view, String url, Bitmap favicon) {
                    super.onPageStarted(view, url, favicon);
                    WebViewFragment.this.mProgressBar.setVisibility(View.VISIBLE);
                    WebViewFragment.this.mWebView.setVisibility(View.GONE);
                }

                @Override
                public void onPageFinished(WebView view, String url) {
                    super.onPageFinished(view, url);
                    WebViewFragment.this.mProgressBar.setVisibility(View.GONE);
                    WebViewFragment.this.mWebView.setVisibility(View.VISIBLE);
                }
            });

            if (savedInstanceState != null) {
                this.mUrl = savedInstanceState.getString(URL);
            }

            if (this.mUrl  != null) {
                this.mWebView.loadUrl(this.mUrl);
            }

            return rootView;
        } else {
            throw new ToolBarActivityException("Error fragment initialization");
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(URL, this.mUrl);
    }

    @Override
    protected boolean isToolBarComponent(View v) {
        int id = v.getId();
        return ((id == R.id.status_bar_webview_layout) || (id == R.id.status_bar_webview));
    }

    public void setUrl(String url) {
        this.mUrl = url;
    }

    public String getUrl() {
        return mUrl;
    }
}
