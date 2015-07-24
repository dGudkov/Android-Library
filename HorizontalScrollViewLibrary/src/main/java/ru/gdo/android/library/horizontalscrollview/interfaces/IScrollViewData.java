package ru.gdo.android.library.horizontalscrollview.interfaces;

import android.content.Context;
import android.view.View;

/**
 * @author Danil Gudkov <d_n_l@mail.ru>
 * @copyrights Danil Gudkov, 2015
 * @since 23.07.15.
 */

public interface IScrollViewData {
    View initViews(Context context);
    View cloneView();
}
