package ru.gdo.andriod.example.pagecurl.view;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import android.widget.TextView;

import ru.gdo.andriod.example.pagecurl.R;
import ru.gdo.andriod.example.pagecurl.interfaces.IAdapter;

import java.util.ArrayList;
import java.util.List;

public class BookAdapter implements IAdapter {

	private List<String> strList = new ArrayList<>();
	private View[] viewList;

	private Context mContext;
	private int pageIndex;

	public BookAdapter(Context context) {
		super();
		this.mContext = context;
	}

	public void addItem(List<String> list){
		strList.addAll(list);
		viewList = new View[list.size()];
		pageIndex = 0;
	}

	@Override
	public int getCount() {
		return strList.size();
	}

	@Override
	public int getIndex() {
		return 0;
	}

//	@Override
//	public String getItem(int position) {
//		return strList.get(position);
//	}
//
//	@Override
//	public long getItemId(int position) {
//		return position;
//	}

	@Override
	public View getView(int position) {
		if (viewList[position] == null) {
			TextView textView = new TextView(mContext);
//			textView.setText(repeat(String.valueOf(pageIndex + position) + "_", 20));
//			strList.get(position)
			textView.setTextColor(Color.BLACK);
			textView.setBackgroundColor(Color.WHITE);
			textView.setBackgroundResource(R.drawable.ly);
			textView.setPadding(10, 10, 10, 10);
			textView.setLayoutParams(new FrameLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
			viewList[position] = textView;
		}
		((TextView)viewList[position]).setText(repeat(String.valueOf(pageIndex + position) + "_", 20));
		return viewList[position];
	}

	@Override
	public void updateView(int shift) {
		if (shift != 0) {
			pageIndex += shift;
		}
	}

	@Override
	public void refreshHistoricallyData(int shift) {

	}

	public static String repeat(String str, int times) {
		return new String(new char[times]).replace("\0", str);
	}
}
