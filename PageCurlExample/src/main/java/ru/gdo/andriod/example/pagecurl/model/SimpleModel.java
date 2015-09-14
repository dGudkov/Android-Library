package ru.gdo.andriod.example.pagecurl.model;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import ru.gdo.andriod.example.pagecurl.R;

/**
 * @author Danil Gudkov <danil.gudkov@progforce.com>
 * @copyrights ProgForce, 2015
 * @since 04.09.15.
 */

public class SimpleModel implements IModel {

    private int value;
    private int index;
    private TextView textView;

    public SimpleModel(int index, int value) {
        this.index = index;
        this.value = value;
    }

    @Override
    public View getView(int index, Context context) {
        if (this.textView == null) {
            this.textView = new TextView(context);
//			textView.setText(repeat(String.valueOf(pageIndex + position) + "_", 20));
//			strList.get(position)
            this.textView.setTextColor(Color.BLACK);
            this.textView.setBackgroundColor(Color.WHITE);
            this.textView.setBackgroundResource(R.drawable.ly);
            this.textView.setPadding(10, 10, 10, 10);
            this.textView.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.FILL_PARENT, FrameLayout.LayoutParams.FILL_PARENT));
        }
        this.textView.setText(repeat(String.valueOf(index + value) + "_", 20));
        return this.textView;
    }

    @Override
    public boolean executeRequest(Thread thread) {
        return !Thread.currentThread().isInterrupted();
    }

    public static String repeat(String str, int times) {
        return new String(new char[times]).replace("\0", str);
    }

    @Override
    public void setIndex(int index) {
        this.index = index;
    }

    @Override
    public void fillContent() {
        this.textView.setText(repeat(String.valueOf(index + value) + "_", 20));
    }

    @Override
    public void setValue(int value) {
        this.value = value;
    }

}
