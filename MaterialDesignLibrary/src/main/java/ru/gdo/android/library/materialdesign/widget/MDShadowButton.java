package ru.gdo.android.library.materialdesign.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

/**
 * @author Danil Gudkov <danil.gudkov@progforce.com>
 * @copyrights ProgForce, 2015
 * @since 30.09.15.
 */

public class MDShadowButton extends MDShadow {

    public MDShadowButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public View getChildView(Context context, AttributeSet attrs) {
        return new MDRippleButton(context, attrs);
    }

}