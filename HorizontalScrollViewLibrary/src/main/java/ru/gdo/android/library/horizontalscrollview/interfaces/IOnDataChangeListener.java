package ru.gdo.android.library.horizontalscrollview.interfaces;

/**
 * @author Danil Gudkov <d_n_l@mail.ru>
 * @copyrights Danil Gudkov, 2015
 * @since 23.07.15.
 */

public interface IOnDataChangeListener<T extends IScrollViewData> {
    void onDataChange(T object);
}
