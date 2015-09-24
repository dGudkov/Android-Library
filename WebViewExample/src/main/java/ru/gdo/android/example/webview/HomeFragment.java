package ru.gdo.android.example.webview;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ru.gdo.android.library.materialdesign.widget.BaseFragment;

/**
 * @author Danil Gudkov <danil.gudkov@progforce.com>
 * @copyrights ProgForce, 2015
 * @since 23.09.15.
 */

public class HomeFragment extends BaseFragment {

    public HomeFragment() {
        this.mLayoutId = R.layout.fragment_home;
        this.mTitleTextId = R.string.str_home_fragment_title;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);

        this.mToolBar.findViewById(R.id.status_bar_webview_layout).setVisibility(View.INVISIBLE);
        this.mToolBar.findViewById(R.id.status_bar_webview).setVisibility(View.INVISIBLE);

        return rootView;
    }

}
