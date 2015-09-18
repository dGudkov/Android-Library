package ru.gdo.android.example.webview;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.ViewAnimator;

import ru.gdo.android.example.webview.R;

public class WebViewActivity extends FragmentActivity {

    private static final String CURRENT_VIEW = "view";
    private static final String CURRENT_LINK = "link";

    private static final int UNINITIALIZED = -1;
    private static final int BUTTON_VIEW = 0;
    private static final int WEB_VIEW = 1;

    private ViewAnimator mViewAnimator;
    private int mCurrentView = UNINITIALIZED;
    private ProgressBar mProgressBar;
    private WebView mWebView;
    private String mLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        LayoutInflater layoutInflater = LayoutInflater.from(this);

        this.mViewAnimator = (ViewAnimator) findViewById(R.id.animator);
        this.mViewAnimator.addView(getButtonView(layoutInflater));
        this.mViewAnimator.addView(getWebView(layoutInflater));

        int viewId = BUTTON_VIEW;

        if (savedInstanceState != null) {
            viewId = savedInstanceState.getInt(CURRENT_VIEW);
            this.mLink = savedInstanceState.getString(CURRENT_LINK);
        }

        this.setCurrentView(viewId);

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(CURRENT_VIEW, this.mCurrentView);
        outState.putString(CURRENT_LINK, this.mLink);
        super.onSaveInstanceState(outState);
    }

    private View getButtonView(LayoutInflater layoutInflater) {
        View view = layoutInflater.inflate(R.layout.buttonview_layout, null, false);

        view.findViewById(R.id.googlebutton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WebViewActivity.this.mLink = "https://www.google.ru/?#newwindow=1&q=android";
                setCurrentView(WEB_VIEW);
            }
        });

        view.findViewById(R.id.yandexbutton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WebViewActivity.this.mLink = "http://yandex.ru/search/?text=adnroid";
                setCurrentView(WEB_VIEW);
            }
        });

        view.findViewById(R.id.bingbutton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WebViewActivity.this.mLink = "http://www.bing.com/search?q=android";
                setCurrentView(WEB_VIEW);
            }
        });

        view.findViewById(R.id.yahoobutton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WebViewActivity.this.mLink = "https://search.yahoo.com/search;?p=android";
                setCurrentView(WEB_VIEW);
            }
        });

        view.findViewById(R.id.hotbotbutton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WebViewActivity.this.mLink = "http://www.hotbot.com/search/web?q=android";
                setCurrentView(WEB_VIEW);
            }
        });
        return view;
    }

    private View getWebView(LayoutInflater layoutInflater) {
        View view = layoutInflater.inflate(R.layout.webview_layout, null, false);

        this.mProgressBar = (ProgressBar) view.findViewById(R.id.progressbar);

        this.mWebView = (WebView) view.findViewById(R.id.webView);
        this.mWebView.getSettings().setJavaScriptEnabled(true);
        this.mWebView.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                WebViewActivity.this.mProgressBar.setVisibility(View.VISIBLE);
                WebViewActivity.this.mWebView.setVisibility(View.GONE);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                WebViewActivity.this.mProgressBar.setVisibility(View.GONE);
                WebViewActivity.this.mWebView.setVisibility(View.VISIBLE);
            }
        });
        return view;
    }

    private void setCurrentView(int currentView) {
        if (this.mCurrentView != currentView) {
            switch (currentView) {
                case BUTTON_VIEW:
                    this.mViewAnimator.setDisplayedChild(BUTTON_VIEW);
                    this.mCurrentView = currentView;
                    break;
                case WEB_VIEW:
                    this.mViewAnimator.setDisplayedChild(WEB_VIEW);
                    if (this.mLink != null) {
                        this.mWebView.loadUrl(this.mLink);
                    }
                    this.mCurrentView = currentView;
                    break;
            }
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) { // если нажата кнопка "Назад"
            if (this.mCurrentView == WEB_VIEW) {
                setCurrentView(BUTTON_VIEW);
                return false;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

}
