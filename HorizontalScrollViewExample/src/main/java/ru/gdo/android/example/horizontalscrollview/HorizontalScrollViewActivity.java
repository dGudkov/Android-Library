package ru.gdo.android.example.horizontalscrollview;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import ru.gdo.android.example.horizontalscrollview.data.ViewData_Alphabet;
import ru.gdo.android.example.horizontalscrollview.data.ViewData_Calendar;
import ru.gdo.android.example.horizontalscrollview.data.ViewData_Images;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import ru.gdo.android.library.horizontalscrollview.adapter.HorizontalListViewAdapter;
import ru.gdo.android.library.horizontalscrollview.interfaces.IOnDataChangeListener;
import ru.gdo.android.library.horizontalscrollview.interfaces.IScrollViewData;
import ru.gdo.android.library.horizontalscrollview.widget.HorizontalScrollView;

import static ru.gdo.android.library.horizontalscrollview.widget.HorizontalScrollView.InitialState.ALIGN_CENTER;
import static ru.gdo.android.library.horizontalscrollview.widget.HorizontalScrollView.InitialState.ALIGN_LEFT;
import static ru.gdo.android.library.horizontalscrollview.widget.HorizontalScrollView.InitialState.ALIGN_RIGHT;

/**
 * @author Danil Gudkov <d_n_l@mail.ru>
 * @copyrights Danil Gudkov, 2015
 * @since 23.07.15.
 */

public class HorizontalScrollViewActivity extends Activity
        implements IOnDataChangeListener<IScrollViewData>, View.OnClickListener {
    HorizontalScrollView mHorizontalScrollView;
    HorizontalListViewAdapter<IScrollViewData> mAdapter;
    private RelativeLayout mExampleView;

    ArrayList<IScrollViewData> list_Alphabet = new ArrayList<IScrollViewData>() {
        {
            add(new ViewData_Alphabet("A", R.layout.item_alphabet));
            add(new ViewData_Alphabet("B", R.layout.item_alphabet));
            add(new ViewData_Alphabet("C", R.layout.item_alphabet));
            add(new ViewData_Alphabet("D", R.layout.item_alphabet));
            add(new ViewData_Alphabet("E", R.layout.item_alphabet));
            add(new ViewData_Alphabet("F", R.layout.item_alphabet));
            add(new ViewData_Alphabet("G", R.layout.item_alphabet));
            add(new ViewData_Alphabet("H", R.layout.item_alphabet));
            add(new ViewData_Alphabet("I", R.layout.item_alphabet));
            add(new ViewData_Alphabet("J", R.layout.item_alphabet));
            add(new ViewData_Alphabet("K", R.layout.item_alphabet));
            add(new ViewData_Alphabet("L", R.layout.item_alphabet));
            add(new ViewData_Alphabet("M", R.layout.item_alphabet));
            add(new ViewData_Alphabet("N", R.layout.item_alphabet));
            add(new ViewData_Alphabet("O", R.layout.item_alphabet));
            add(new ViewData_Alphabet("P", R.layout.item_alphabet));
            add(new ViewData_Alphabet("Q", R.layout.item_alphabet));
            add(new ViewData_Alphabet("R", R.layout.item_alphabet));
            add(new ViewData_Alphabet("S", R.layout.item_alphabet));
            add(new ViewData_Alphabet("T", R.layout.item_alphabet));
            add(new ViewData_Alphabet("U", R.layout.item_alphabet));
            add(new ViewData_Alphabet("V", R.layout.item_alphabet));
            add(new ViewData_Alphabet("W", R.layout.item_alphabet));
            add(new ViewData_Alphabet("X", R.layout.item_alphabet));
            add(new ViewData_Alphabet("Y", R.layout.item_alphabet));
            add(new ViewData_Alphabet("Z", R.layout.item_alphabet));
        }
    };

    ArrayList<IScrollViewData> list_Images = new ArrayList<IScrollViewData>() {
        {
            add(new ViewData_Images(R.drawable.image_1, R.layout.item_image));
            add(new ViewData_Images(R.drawable.image_2, R.layout.item_image));
            add(new ViewData_Images(R.drawable.image_3, R.layout.item_image));
            add(new ViewData_Images(R.drawable.image_4, R.layout.item_image));
            add(new ViewData_Images(R.drawable.image_5, R.layout.item_image));
            add(new ViewData_Images(R.drawable.image_6, R.layout.item_image));
            add(new ViewData_Images(R.drawable.image_7, R.layout.item_image));
            add(new ViewData_Images(R.drawable.image_8, R.layout.item_image));
            add(new ViewData_Images(R.drawable.image_9, R.layout.item_image));
            add(new ViewData_Images(R.drawable.image_10, R.layout.item_image));
            add(new ViewData_Images(R.drawable.image_11, R.layout.item_image));
            add(new ViewData_Images(R.drawable.image_12, R.layout.item_image));
            add(new ViewData_Images(R.drawable.image_13, R.layout.item_image));
            add(new ViewData_Images(R.drawable.image_14, R.layout.item_image));
            add(new ViewData_Images(R.drawable.image_15, R.layout.item_image));
            add(new ViewData_Images(R.drawable.image_16, R.layout.item_image));
            add(new ViewData_Images(R.drawable.image_17, R.layout.item_image));
            add(new ViewData_Images(R.drawable.image_18, R.layout.item_image));
            add(new ViewData_Images(R.drawable.image_19, R.layout.item_image));
            add(new ViewData_Images(R.drawable.image_20, R.layout.item_image));
            add(new ViewData_Images(R.drawable.image_21, R.layout.item_image));
            add(new ViewData_Images(R.drawable.image_22, R.layout.item_image));
            add(new ViewData_Images(R.drawable.image_23, R.layout.item_image));
            add(new ViewData_Images(R.drawable.image_24, R.layout.item_image));
            add(new ViewData_Images(R.drawable.image_25, R.layout.item_image));
            add(new ViewData_Images(R.drawable.image_26, R.layout.item_image));
            add(new ViewData_Images(R.drawable.image_27, R.layout.item_image));
            add(new ViewData_Images(R.drawable.image_28, R.layout.item_image));
        }
    };

    ArrayList<IScrollViewData> list_Collages = new ArrayList<IScrollViewData>() {
        {
            add(new ViewData_Images(R.drawable.image_1, R.layout.item_image));
            add(new ViewData_Alphabet("A", R.layout.item_alphabet));
            add(new ViewData_Images(R.drawable.image_2, R.layout.item_image));
            add(new ViewData_Alphabet("B", R.layout.item_alphabet));
            add(new ViewData_Images(R.drawable.image_3, R.layout.item_image));
            add(new ViewData_Alphabet("C", R.layout.item_alphabet));
            add(new ViewData_Images(R.drawable.image_4, R.layout.item_image));
            add(new ViewData_Alphabet("D", R.layout.item_alphabet));
            add(new ViewData_Images(R.drawable.image_5, R.layout.item_image));
            add(new ViewData_Alphabet("E", R.layout.item_alphabet));
            add(new ViewData_Images(R.drawable.image_6, R.layout.item_image));
            add(new ViewData_Alphabet("F", R.layout.item_alphabet));
            add(new ViewData_Images(R.drawable.image_7, R.layout.item_image));
            add(new ViewData_Alphabet("G", R.layout.item_alphabet));
            add(new ViewData_Images(R.drawable.image_8, R.layout.item_image));
            add(new ViewData_Alphabet("H", R.layout.item_alphabet));
            add(new ViewData_Images(R.drawable.image_9, R.layout.item_image));
            add(new ViewData_Alphabet("I", R.layout.item_alphabet));
            add(new ViewData_Images(R.drawable.image_10, R.layout.item_image));
            add(new ViewData_Alphabet("J", R.layout.item_alphabet));
            add(new ViewData_Images(R.drawable.image_11, R.layout.item_image));
            add(new ViewData_Alphabet("K", R.layout.item_alphabet));
            add(new ViewData_Images(R.drawable.image_12, R.layout.item_image));
            add(new ViewData_Alphabet("L", R.layout.item_alphabet));
            add(new ViewData_Images(R.drawable.image_13, R.layout.item_image));
            add(new ViewData_Alphabet("M", R.layout.item_alphabet));
            add(new ViewData_Images(R.drawable.image_14, R.layout.item_image));
            add(new ViewData_Alphabet("N", R.layout.item_alphabet));
            add(new ViewData_Images(R.drawable.image_15, R.layout.item_image));
        }
    };
    ArrayList<IScrollViewData> list_Calendar = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.horizontalscrollviewactivity);

        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.MONTH, -1);

        Calendar calLast = Calendar.getInstance();
        calLast.setTime(cal.getTime());
        calLast.add(Calendar.MONTH, 2);

        while (cal.getTimeInMillis() <= calLast.getTimeInMillis()) {
            list_Calendar.add(new ViewData_Calendar(cal, R.layout.item_calendar));
            cal.add(Calendar.DAY_OF_MONTH, 1);
        }

        this.mExampleView = (RelativeLayout) findViewById(R.id.exampleView);
        this.mHorizontalScrollView = (HorizontalScrollView) findViewById(R.id.scrollView);

        findViewById(R.id.btnFirst).setOnClickListener(this);
        findViewById(R.id.btnCenter).setOnClickListener(this);
        findViewById(R.id.btnLast).setOnClickListener(this);
        findViewById(R.id.btnNext).setOnClickListener(this);
        findViewById(R.id.btnPrev).setOnClickListener(this);

        findViewById(R.id.btnAlphabet).setOnClickListener(this);
        findViewById(R.id.btnCalendar).setOnClickListener(this);
        findViewById(R.id.btnImages).setOnClickListener(this);
        findViewById(R.id.btnCollage).setOnClickListener(this);

        this.fillScroll(this.list_Alphabet, R.color.clr_alphabet_selected_color, R.color.clr_alphabet_background_text_color);
    }

    @Override
    public void onClick(View v) {
        int viewId = v.getId();
        if (viewId == R.id.btnAlphabet) {
            this.fillScroll(this.list_Alphabet,
                    R.color.clr_alphabet_selected_color,
                    R.color.clr_alphabet_background_text_color);
        } else if (viewId == R.id.btnCalendar) {
            this.fillScroll(this.list_Calendar,
                    R.color.clr_calendar_selected_color,
                    R.color.clr_calendar_background_text_color);
        } else if (viewId == R.id.btnImages) {
            this.fillScroll(this.list_Images,
                    R.color.clr_images_selected_color,
                    R.color.clr_images_background_color);
        } else if (viewId == R.id.btnCollage) {
            this.fillScroll(this.list_Collages,
                    R.color.clr_collage_selected_color,
                    R.color.clr_collage_background_color);
        } else if (viewId == R.id.btnFirst) {
            this.mAdapter.setCenter(ALIGN_LEFT);
        } else if (viewId == R.id.btnCenter) {
            this.mAdapter.setCenter(ALIGN_CENTER);
        } else if (viewId == R.id.btnLast) {
            this.mAdapter.setCenter(ALIGN_RIGHT);
        } else if (viewId == R.id.btnPrev) {
            this.mAdapter.goPrev();
        } else if (viewId == R.id.btnNext) {
            this.mAdapter.goNext();
        }
    }

    private void fillScroll(ArrayList<IScrollViewData> list, int activeColor, int passiveColor) {
        this.mAdapter = new HorizontalListViewAdapter<>(this, list, this.mHorizontalScrollView,
                activeColor, passiveColor);
        this.mAdapter.setDataChangeListener(this);
        this.mHorizontalScrollView.setAdapter(this.mAdapter);
    }

    @Override
    public void onDataChange(IScrollViewData object) {
        this.mExampleView.removeAllViews();
        View view = object.cloneView();
        if (view != null) {
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
            this.mExampleView.addView(view, layoutParams);
        }
    }

}
