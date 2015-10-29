package ru.gdo.android.example.materialdesign.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ru.gdo.android.example.materialdesign.R;
import ru.gdo.android.library.materialdesign.exception.ToolBarActivityException;
import ru.gdo.android.library.materialdesign.widget.BaseFragment;
import ru.gdo.android.library.materialdesign.widget.MDCheckBox;

/**
 * @author Danil Gudkov <danil.gudkov@progforce.com>
 * @copyrights ProgForce, 2015
 * @since 23.10.15.
 */

public class CheckBoxFragment extends BaseFragment implements MDCheckBox.OnCheckedChangeListener {

    public CheckBoxFragment() {
        this.mLayoutId = R.layout.fragment_checkbox;
        this.mTitleTextId = R.string.str_checkboxes_fragment_title;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);

        if (rootView != null) {

            this.mToolBar.findViewById(R.id.status_bar_calendar_layout).setVisibility(View.INVISIBLE);
            this.mToolBar.findViewById(R.id.status_bar_calendar).setVisibility(View.INVISIBLE);

            MDCheckBox mAnimCheckBox1 = (MDCheckBox) rootView.findViewById(R.id.checkbox_1);
            mAnimCheckBox1.setChecked(false, false);
            MDCheckBox mAnimCheckBox2 = (MDCheckBox) rootView.findViewById(R.id.checkbox_2);
            mAnimCheckBox2.setChecked(false, false);
            mAnimCheckBox1.setOnCheckedChangeListener(this);
            mAnimCheckBox2.setOnCheckedChangeListener(this);

            return rootView;
        } else {
            throw new ToolBarActivityException("Error fragment initialization");
        }

    }

    @Override
    public void onChange(boolean checked) {
        Log.d("MainActivity", "checked-->" + checked);
    }
}
