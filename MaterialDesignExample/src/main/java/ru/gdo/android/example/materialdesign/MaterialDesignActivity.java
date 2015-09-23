package ru.gdo.android.example.materialdesign;

import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class MaterialDesignActivity extends AppCompatActivity
    implements MenuFragment.FragmentDrawerListener {

    private MenuFragment menuFragment;
//    private BaseFragment mContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_material_design);

        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);


//        mToolbar.findViewById(R.id.status_bar_menu_layout).setOnClickListener(this);
//        mToolbar.findViewById(R.id.status_bar_menu).setOnClickListener(this);
//        mToolbar.setContentInsetsAbsolute(0, 0);

        menuFragment = (MenuFragment)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        menuFragment.setUp(R.id.fragment_navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout),
                mToolbar);
        menuFragment.setDrawerListener(this);

    }

    @Override
    public void onDrawerItemSelected(View view, int position) {
        displayView(position);
    }

    public void displayView(int position) {
//        BaseFragment fragment = null;
        boolean showCalendar = false;
        boolean showWebView = false;

//        switch (position) {
//            case JEWISH_HISTORY_TODAY_FRAGMENT:
//                this.application.getCalendar().init();
//            case JEWISH_HISTORY_CURRENT_FRAGMENT:
//                fragment = JewishHistoryFragment.newInstance(this, this.application);
//                showCalendar = true;
//                break;
//            case NOTIFICATION_FRAGMENT:
//                fragment = NotificationFragment.newInstance(this, this.application);
//                break;
//            case WEB_VIEW_MORE_APPS_FRAGMENT:
//                fragment = WebViewFragment.newInstance(
//                        "http://m.chabad.org/apps?sc=a555",
//                        getResources().getString(R.string.str_actionbar_webview_more_apps_title)
//                );
//                showWebView = true;
//                break;
//            case WEB_VIEW_DONATE_FRAGMENT:
//                fragment = WebViewFragment.newInstance(
//                        "https://m-chabadorg.clhosting.org/tools/donate/donate_cdo/aid/161548/sc/History_Android",
//                        getResources().getString(R.string.str_actionbar_webview_donate_title)
//                );
//                showWebView = true;
//                break;
//            case ABOUT_FRAGMENT:
//                fragment = AboutFragment.newInstance(
//                        getResources().getString(R.string.str_actionbar_about_title)
//                );
//                break;
//            case GCM_FRAGMENT:
//                fragment = GCMSenderFragment.newInstance(
//                        this.application,
//                        getResources().getString(R.string.str_actionbar_gsmsender_title)
//                );
//                break;
//            case WEB_VIEW_FEEDBACK_FRAGMENT:
//                fragment = WebViewFragment.newInstance(
//                        "http://m.chabad.org/tools/feedback_cdo/subject/Jewish+History+Android+Feedback",
//                        getResources().getString(R.string.str_actionbar_webview_feedback_title)
//                );
//                showWebView = true;
//                break;
//            case WEB_VIEW_LINK_FRAGMENT:
//                fragment = WebViewFragment.newInstance(url, header);
//                showWebView = true;
//                break;
//        }

//        if (fragment != null) {
//            this.mTitleText = fragment.getHeaderTitle();
//            this.mTitle.setText(Html.fromHtml(this.mTitleText));
//            this.mCalendarLayout.setVisibility((showCalendar || showWebView) ? View.VISIBLE : View.INVISIBLE);
//            this.mCalendar.setVisibility(showCalendar ? View.VISIBLE : View.INVISIBLE);
//            this.mWebView.setVisibility(showWebView ? View.VISIBLE : View.INVISIBLE);
//
//            getSupportFragmentManager()
//                    .beginTransaction()
//                    .replace(R.id.container_body, fragment, "mContent")
//                    .commit();
//            this.mContent = fragment;
//        }
    }
}
