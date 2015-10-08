package ru.gdo.android.example.webview;

import android.content.Intent;
import android.net.Uri;
import android.view.View;

import ru.gdo.android.library.materialdesign.widget.BaseFragment;
import ru.gdo.android.library.materialdesign.widget.ToolBarFragmentActivity;

public class WebViewActivity extends ToolBarFragmentActivity {

    public static final int HOME_FRAGMENT = 0;
    public static final int GOOGLE_FRAGMENT = 1;
    public static final int YANDEX_FRAGMENT = 2;
    public static final int BING_FRAGMENT = 3;
    public static final int YAHOO_FRAGMENT = 4;
    public static final int HOTBOT_FRAGMENT = 5;
    public static final int WEB_VIEW_FRAGMENT = 6;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_web_view;
    }

    @Override
    protected int getToolbarId() {
        return R.id.toolbar;
    }

    @Override
    protected int getMenuLayoutId() {
        return R.id.status_bar_menu_layout;
    }

    @Override
    protected int getMenuId() {
        return R.id.status_bar_menu;
    }

    @Override
    protected int getTitleId() {
        return R.id.header_title;
    }

    @Override
    protected int getNavigationDrawerId() {
        return R.id.fragment_navigation_drawer;
    }

    @Override
    protected int getDrawerLayoutId() {
        return R.id.drawer_layout;
    }

    @Override
    protected int getContainerId() {
        return R.id.container_body;
    }

    @Override
    protected boolean isMainFragment() {
        return (mContent instanceof HomeFragment);
    }

    @Override
    protected int getHomeFragmentId() {
        return HOME_FRAGMENT;
    }

    @Override
    protected BaseFragment getFragment(int position) {
        BaseFragment fragment = null;
        switch (position) {
            case HOME_FRAGMENT:
                fragment = BaseFragment.newInstance(HomeFragment.class, this, this.mToolBar, this);
                break;
            case GOOGLE_FRAGMENT:
                fragment = WebViewFragment.newInstance(
                        this,
                        this.mToolBar,
                        this,
                        R.string.str_google_fragment_title,
                        "https://www.google.com/?#newwindow=1&q=android");
                break;
            case YANDEX_FRAGMENT:
                fragment = WebViewFragment.newInstance(
                        this,
                        this.mToolBar,
                        this,
                        R.string.str_yandex_fragment_title,
                        "http://yandex.ru/search/?text=android");
                break;
            case BING_FRAGMENT:
                fragment = WebViewFragment.newInstance(
                        this,
                        this.mToolBar,
                        this,
                        R.string.str_bing_fragment_title,
                        "http://www.bing.com/search?q=android"
                );
                break;
            case YAHOO_FRAGMENT:
                fragment = WebViewFragment.newInstance(
                        this,
                        this.mToolBar,
                        this,
                        R.string.str_yahoo_fragment_title,
                        "https://search.yahoo.com/search;?p=android"
                );
                break;
            case HOTBOT_FRAGMENT:
                fragment = WebViewFragment.newInstance(
                        this,
                        this.mToolBar,
                        this,
                        R.string.str_hotbot_fragment_title,
                        "http://www.hotbot.com/search/web?q=android"
                );
                break;
            case WEB_VIEW_FRAGMENT:
                fragment = WebViewFragment.newInstance(
                        this,
                        this.mToolBar,
                        this,
                        R.string.str_webview_fragment_title,
                        null
                );
                break;
        }
        return fragment;
    }

    protected void displayView(int fragmentId, String url) {
        BaseFragment fragment = getFragment(fragmentId);
        if (fragment instanceof WebViewFragment) {
            ((WebViewFragment)fragment).setUrl(url);
        }
        this.switchView(fragment);
    }

    @Override
    public void onToolBarClick(View v) {
        if ((v.getId() == R.id.status_bar_webview_layout) || (v.getId() == R.id.status_bar_webview)) {
            if (mContent instanceof  WebViewFragment) {
                WebViewFragment webViewFragment = (WebViewFragment) mContent;
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(webViewFragment.getUrl()));
                startActivity(intent);
            }
        } else {
            super.onToolBarClick(v);
        }
    }

}
