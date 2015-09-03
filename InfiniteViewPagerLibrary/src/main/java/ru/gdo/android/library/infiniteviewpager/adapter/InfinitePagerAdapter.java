package ru.gdo.android.library.infiniteviewpager.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ru.gdo.android.library.infiniteviewpager.interfaces.IInfiniteViewPagerModel;
import ru.gdo.android.library.infiniteviewpager.widget.InfiniteViewPager;

/**
 * @author Danil Gudkov <d_n_l@mail.ru>
 * @copyrights Danil Gudkov, 2015
 * @since 27.07.15.
 */

public class InfinitePagerAdapter<T extends IInfiniteViewPagerModel> extends PagerAdapter {

    public static final int PAGE_LEFT = 0;
    public static final int PAGE_MIDDLE = 1;
    public static final int PAGE_RIGHT = 2;

    public enum AssignOnShift {
        BY_INDEX,
        BY_CONTEXT
    }

    private LayoutInflater mInflater;
    private T[] mPageModel;
    private AssignOnShift mAssignOnShift = AssignOnShift.BY_INDEX;
    private InfiniteViewPager mViewPager;

    public InfinitePagerAdapter(LayoutInflater mInflater, T[] pageModel) {
        this.mInflater = mInflater;
        this.mPageModel = pageModel;
        this.initModels();
    }

    public void initModels() {
        for (int i=0; i < this.mPageModel.length; i++) {
            this.mPageModel[i].init(i);
        }
    }

    @Override
    public int getCount() {
        return 3;           // we only need three pages
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        T currentPage = this.mPageModel[position];
        View layout = currentPage.getLayout(mInflater, this);  // this.layoutId
        layout.setTag(currentPage);
        container.addView(layout);
        return layout;
    }

    @Override
    public boolean isViewFromObject(View view, Object obj) {
        return view == obj;
    }

    public void scroll(int pageIndex) {
        int shift = 0;
        if (pageIndex == PAGE_LEFT) {
            shift = -1;
        } else if (pageIndex == PAGE_RIGHT) {
            shift = 1;
        }
        shiftModels(shift);
    }

    private void shiftModels(int shift) {

        if (shift != 0) {

            final IInfiniteViewPagerModel leftPage = mPageModel[PAGE_LEFT];
            final IInfiniteViewPagerModel middlePage = mPageModel[PAGE_MIDDLE];
            final IInfiniteViewPagerModel rightPage = mPageModel[PAGE_RIGHT];

            if (this.mAssignOnShift == AssignOnShift.BY_INDEX) {
                leftPage.shift(shift);
                middlePage.shift(shift);
                this.mViewPager.setCurrentItem(PAGE_MIDDLE, false);
                rightPage.shift(shift);
            } else {
                if (shift > 0) {
                    leftPage.shift(middlePage, shift);
                    middlePage.shift(rightPage, shift);
                    this.mViewPager.setCurrentItem(PAGE_MIDDLE, false);
                    rightPage.shift(null, shift);
                } else {
                    rightPage.shift(middlePage, shift);
                    middlePage.shift(leftPage, shift);
                    this.mViewPager.setCurrentItem(PAGE_MIDDLE, false);
                    leftPage.shift(null, shift);
                }            }
        }
    }

    public void goNext() {
        shiftModels(1);
    }

    public void goPrev() {
        shiftModels(-1);
    }


    public void setContent() {
        for (final IInfiniteViewPagerModel model : mPageModel) {
            model.setContent();
        }
    }

    public void setViewPager(InfiniteViewPager viewPager) {
        this.mViewPager = viewPager;
    }

    public void setAssignOnShift(AssignOnShift assignOnShift) {
        this.mAssignOnShift = assignOnShift;
    }

}
