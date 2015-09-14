package ru.gdo.andriod.example.pagecurl.interfaces;

import android.view.View;

public interface IAdapter {
	int getCount();
	int getIndex();
	View getView(int position);
	void refreshHistoricallyData(int shift);
}
