package ru.gdo.android.library.infiniteviewpager.interfaces;

import android.view.LayoutInflater;
import android.view.View;

import ru.gdo.android.library.infiniteviewpager.adapter.InfinitePagerAdapter;

/**
 * @author Danil Gudkov <d_n_l@mail.ru>
 * @copyrights Danil Gudkov, 2015
 * @since 27.07.15.
 */

public interface IInfiniteViewPagerModel {

    View getLayout(LayoutInflater inflater,  InfinitePagerAdapter adapter);  // this.layoutId inside model

    void setContent();

    void shift(int i);

    void shift(IInfiniteViewPagerModel model, int shift);

    void assign(IInfiniteViewPagerModel model);

    void init(int index);
}
