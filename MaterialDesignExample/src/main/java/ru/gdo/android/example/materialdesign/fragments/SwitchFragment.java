package ru.gdo.android.example.materialdesign.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import org.w3c.dom.Text;

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

public class SwitchFragment extends BaseFragment {

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

            final TextView tv = (TextView) rootView.findViewById(R.id.switchstate);

            final MDShadowToggle mdShadowToggle = (MDShadowToggle) rootView.findViewById(R.id.defswitch);

            mdShadowToggle.setOnToggleListener(new OnToggleListener() {
                @Override
                public void onToggle(int result) {
                    tv.setText(mdShadowToggle.getText());
                }
            });

            mdShadowToggle.setChecked(true);

            return rootView;
        } else {
            throw new ToolBarActivityException("Error fragment initialization");
        }

    }

}
