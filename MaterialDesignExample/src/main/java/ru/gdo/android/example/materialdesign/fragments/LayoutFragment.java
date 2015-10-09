package ru.gdo.android.example.materialdesign.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ru.gdo.android.example.materialdesign.R;
import ru.gdo.android.library.materialdesign.widget.BaseFragment;

/**
 * @author Danil Gudkov <danil.gudkov@progforce.com>
 * @copyrights ProgForce, 2015
 * @since 05.10.15.
 */

public class LayoutFragment extends BaseFragment {

    public LayoutFragment() {
        this.mLayoutId = R.layout.fragment_layout;
        this.mTitleTextId = R.string.str_layout_fragment_title;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);

        this.mToolBar.findViewById(R.id.status_bar_calendar_layout).setVisibility(View.INVISIBLE);
        this.mToolBar.findViewById(R.id.status_bar_calendar).setVisibility(View.INVISIBLE);

        return rootView;

    }

}
