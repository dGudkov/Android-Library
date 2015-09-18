package ru.gdo.android.library.horizontalscrollview.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;

import java.util.ArrayList;

import ru.gdo.android.library.horizontalscrollview.interfaces.IOnDataChangeListener;
import ru.gdo.android.library.horizontalscrollview.interfaces.IScrollViewData;
import ru.gdo.android.library.horizontalscrollview.widget.HorizontalScrollView;

/**
 * @author Danil Gudkov <d_n_l@mail.ru>
 * @copyrights Danil Gudkov, 2015
 * @since 23.07.15.
 */

public class HorizontalListViewAdapter<T extends IScrollViewData>
		extends ArrayAdapter<T>
		implements View.OnClickListener {

	private Context mContext;
	private ArrayList<T> mList;
	private HorizontalScrollView mHorizontalScrollView;
	private int mCurrIndex = 0;
	private IOnDataChangeListener<T> onDataChangeListener;
	private final int mPassiveColor;

	public HorizontalListViewAdapter(Context context, ArrayList<T> list,
									 HorizontalScrollView hScrollview,
									 int activeColor, int passiveColor) {
		super(context, -1, list);
		this.mContext = context;
		this.mList = list;
		this.mHorizontalScrollView = hScrollview;
		this.mPassiveColor = context.getResources().getColor(passiveColor);
		this.mHorizontalScrollView.setBorderColors(activeColor, passiveColor);
	}

	@Override
	public int getCount() {
		return mList.size();
	}

	@Override
	public T getItem(int position) {
		return mList.get(position);
	}

	@Override
	public View getView(final int position, View view, ViewGroup parent) {
		IScrollViewData data;

		if (view == null) {
			data = getItem(position);
			view = data.initViews(this.mContext);

			view.setBackgroundColor(this.mPassiveColor);
			android.widget.LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.MATCH_PARENT,
					LinearLayout.LayoutParams.MATCH_PARENT);
			layoutParams.setMargins(5, 5, 5, 5);
			view.setLayoutParams(layoutParams);
			view.setOnClickListener(this);
		}
		return view;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void onClick(View view) {
		T data = (T) view.getTag();
		this.setCenter(mList.indexOf(data));
	}

	public void setCenter(int index) {
		if (index >= 0) {
			mCurrIndex = index;
			if (mHorizontalScrollView.setCenter(index)) {
				if (this.onDataChangeListener != null) {
					this.onDataChangeListener.onDataChange(mList.get(mCurrIndex));
				}
			}
		}
	}

	public int setCenter(HorizontalScrollView.InitialState state) {
		int index;
		switch (state) {
			case ALIGN_CENTER:
				index = this.getCount() / 2;
				break;
			case ALIGN_RIGHT:
				index = this.getCount() - 1;
				break;
			default:
				index = 0;
		}
		this.setCenter(index);
		return index;
	}

	public void goNext() {
		if (mCurrIndex < mList.size() - 1) {
			mCurrIndex++;
			if (mHorizontalScrollView.setCenter(mCurrIndex)) {
				if (this.onDataChangeListener != null) {
					this.onDataChangeListener.onDataChange(mList.get(mCurrIndex));
				}
			}
		}
	}

	public void goPrev() {
		if (mCurrIndex > 0) {
			mCurrIndex--;
			mHorizontalScrollView.setCenter(mCurrIndex);
			if (this.onDataChangeListener != null) {
				this.onDataChangeListener.onDataChange(mList.get(mCurrIndex));
			}
		}
	}

	public void setDataChangeListener(IOnDataChangeListener<T> onDataChangeListener) {
		this.onDataChangeListener = onDataChangeListener;
	}

}
