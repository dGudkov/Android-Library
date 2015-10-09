package ru.gdo.android.example.materialdesign.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import ru.gdo.android.example.materialdesign.R;
import ru.gdo.android.library.materialdesign.exception.ToolBarActivityException;
import ru.gdo.android.library.materialdesign.interfaces.OnToggleListener;
import ru.gdo.android.library.materialdesign.widget.BaseFragment;
import ru.gdo.android.library.materialdesign.widget.MDShadowToggle;
import ru.gdo.android.library.materialdesign.widget.MDToggle;

/**
 * @author Danil Gudkov <danil.gudkov@progforce.com>
 * @copyrights ProgForce, 2015
 * @since 08.10.15.
 */

public class SwitchFragment extends BaseFragment implements OnToggleListener {

    private TextView tvSelected;
    private CompoundButton.OnCheckedChangeListener checkListener;
    private CheckBox cbChecked;
    private MDShadowToggle sw;

    public SwitchFragment() {
        this.mLayoutId = R.layout.fragment_switch;
        this.mTitleTextId = R.string.str_switch_fragment_title;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);

        if (rootView != null) {

            this.mToolBar.findViewById(R.id.status_bar_calendar_layout).setVisibility(View.INVISIBLE);
            this.mToolBar.findViewById(R.id.status_bar_calendar).setVisibility(View.INVISIBLE);

            sw = (MDShadowToggle) rootView.findViewById(R.id.sliding_toggle_switch);

            cbChecked = (CheckBox) rootView.findViewById(R.id.checkChecked);
            cbChecked.setChecked(sw.isChecked());
            checkListener = new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    sw.setChecked(isChecked, 2000);
                }
            };
            cbChecked.setOnCheckedChangeListener(checkListener);

            CheckBox cbEnabled = (CheckBox) rootView.findViewById(R.id.checkBoxEnabled);
            cbEnabled.setChecked(sw.isEnabled());
            cbEnabled.setOnCheckedChangeListener(
                    new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            sw.setEnabled(isChecked);
                        }
                    }
            );

            tvSelected = (TextView) rootView.findViewById(R.id.textView1);
            tvSelected.setText(sw.getText(MDToggle.SWITCH_ON));

            sw.setOnToggleListener(this);
            sw.setTextSize(25);

            return rootView;
        } else {
            throw new ToolBarActivityException("Error fragment initialization");
        }

    }

    @Override
    public void onToggle(int result) {

//        try {
//            cbChecked.setOnCheckedChangeListener(null);
//            cbChecked.setChecked(result == MDToggle.SWITCH_ON);
//        } finally {
//            cbChecked.setOnCheckedChangeListener(checkListener);
//        }
//        tvSelected.setText(sw.getText());
    }

}
