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
 * @since 23.09.15.
 */

public class MessagesFragment extends BaseFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_messages, container, false);

        this.mToolBar.findViewById(R.id.status_bar_calendar_layout).setVisibility(View.VISIBLE);
        this.mToolBar.findViewById(R.id.status_bar_calendar).setVisibility(View.VISIBLE);

        return rootView;
    }

    @Override
    public void setTitleText() {
        this.mTitleText = this.mContext.getResources().getString(R.string.str_messages_fragment_title);
    }

}
