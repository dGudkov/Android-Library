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
 * @since 01.10.15.
 */

public class ButtonFragment extends BaseFragment {

    public ButtonFragment() {
        this.mLayoutId = R.layout.fragment_button;
        this.mTitleTextId = R.string.str_button_fragment_title;
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
