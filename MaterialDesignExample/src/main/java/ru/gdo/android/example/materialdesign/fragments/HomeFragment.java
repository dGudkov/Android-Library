package ru.gdo.android.example.materialdesign.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import ru.gdo.android.example.materialdesign.R;
import ru.gdo.android.library.materialdesign.widget.BaseFragment;

/**
 * @author Danil Gudkov <danil.gudkov@progforce.com>
 * @copyrights ProgForce, 2015
 * @since 23.09.15.
 */

public class HomeFragment extends BaseFragment {

    private Button b1;
    private Button b2;

    public HomeFragment() {
        this.mLayoutId = R.layout.fragment_home;
        this.mTitleTextId = R.string.str_home_fragment_title;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);

//        b1 = (Button) rootView.findViewById(R.id.button1);
//        b2 = (Button) rootView.findViewById(R.id.button2);
//
//        ((CheckBox) rootView.findViewById(R.id.checkBox1)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                b1.setPressed(isChecked);
//                b2.setPressed(isChecked);
//            }
//        });
//
//        ((CheckBox) rootView.findViewById(R.id.checkBox2)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                b1.setEnabled(isChecked);
//                b2.setEnabled(isChecked);
//            }
//        });

        this.mToolBar.findViewById(R.id.status_bar_calendar_layout).setVisibility(View.INVISIBLE);
        this.mToolBar.findViewById(R.id.status_bar_calendar_layout).setOnClickListener(this);
        this.mToolBar.findViewById(R.id.status_bar_calendar).setVisibility(View.INVISIBLE);
        this.mToolBar.findViewById(R.id.status_bar_calendar).setOnClickListener(this);

        return rootView;

    }

    @Override
    protected boolean isToolBarComponent(View v) {
        int id = v.getId();
        return ((id == R.id.status_bar_calendar_layout) || (id == R.id.status_bar_calendar));
    }

}
