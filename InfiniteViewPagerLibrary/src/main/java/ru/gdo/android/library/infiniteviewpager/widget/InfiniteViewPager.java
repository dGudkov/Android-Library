package ru.gdo.android.library.infiniteviewpager.widget;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;

import ru.gdo.android.library.infiniteviewpager.adapter.InfinitePagerAdapter;

import static ru.gdo.android.library.infiniteviewpager.adapter.InfinitePagerAdapter.PAGE_MIDDLE;

/**
 * @author Danil Gudkov <d_n_l@mail.ru>
 * @copyrights Danil Gudkov, 2015
 * @since 27.07.15.
 */

public class InfiniteViewPager extends ViewPager {

    private int mSelectedPageIndex = 1;
    private InfinitePagerAdapter mInfinitePagerAdapter;

    public InfiniteViewPager(Context context) {
        this(context, null);
    }

    public InfiniteViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.ViewPager();
    }

    private void ViewPager() {
        this.setCurrentItem(PAGE_MIDDLE, false);

        this.addOnPageChangeListener(new OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                mSelectedPageIndex = position;
            }

            @Override
            public void onPageScrolled(int i, float v, int i1) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (state == ViewPager.SCROLL_STATE_IDLE) {
                    InfiniteViewPager.this.mInfinitePagerAdapter.scroll(InfiniteViewPager.this.mSelectedPageIndex);
                }
            }
        });
    }

    @Override
    public void setAdapter(PagerAdapter adapter) {
        super.setAdapter(adapter);
        this.mInfinitePagerAdapter = (InfinitePagerAdapter) adapter;
        this.mInfinitePagerAdapter.setViewPager(this);
//        this.mInfinitePagerAdapter.setContent();
        this.setCurrentItem(PAGE_MIDDLE, false);
    }
}
