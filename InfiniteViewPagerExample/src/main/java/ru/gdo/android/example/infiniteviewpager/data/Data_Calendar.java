package ru.gdo.android.example.infiniteviewpager.data;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import ru.gdo.android.example.infiniteviewpager.R;
import ru.gdo.android.library.infiniteviewpager.adapter.InfinitePagerAdapter;
import ru.gdo.android.library.infiniteviewpager.interfaces.IInfiniteViewPagerModel;

/**
 * @author Danil Gudkov <danil.gudkov@progforce.com>
 * @copyrights ProgForce, 2015
 * @since 27.07.15.
 */

public class Data_Calendar implements IInfiniteViewPagerModel {

    private Calendar mDate = Calendar.getInstance();
    private View mView;
    private TextView mTVDate, mTVMonth, mTVWeekDay;
    private String mDateText, mMonthText, mWeekDayText;

    @Override
    public View getLayout(LayoutInflater inflater, final InfinitePagerAdapter adapter) {
        if (this.mView == null) {
            this.mView = inflater.inflate(R.layout.data_calendar, null);
        }
        this.mTVDate = (TextView) this.mView.findViewById(R.id.textdate);
        this.mTVMonth = (TextView) this.mView.findViewById(R.id.textmonth);
        this.mTVWeekDay = (TextView) this.mView.findViewById(R.id.textweekday);

        this.mView.findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.initModels();
            }
        });

        setContent();
        return this.mView;
    }

    @Override
    public void setContent() {
        if (this.mTVDate != null) {
            this.mTVDate.setText(this.mDateText);
        }
        if (this.mTVMonth != null) {
            this.mTVMonth.setText(this.mMonthText);
        }
        if (this.mTVWeekDay != null) {
            this.mTVWeekDay.setText(this.mWeekDayText);
        }
    }

    private void setTextDate() {
        this.mDateText = String.valueOf(this.mDate.get(Calendar.DAY_OF_MONTH));
        this.mMonthText = this.mDate.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.US);
        this.mWeekDayText = this.mDate.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.US);
    }

    @Override
    public void shift(int dayShift) {
        this.mDate.add(Calendar.DAY_OF_MONTH, dayShift);
        this.setTextDate();

    }

    @Override
    public void shift(IInfiniteViewPagerModel model, int shift) {
        if (model != null) {
            this.assign(model);
            this.setTextDate();
        } else {
            this.shift(shift);
        }
//        setContent();
    }

    @Override
    public void assign(IInfiniteViewPagerModel model) {
        Data_Calendar dataModel = (Data_Calendar) model;
        this.mDate.setTime(dataModel.mDate.getTime());
    }

    @Override
    public void init(int index) {
        this.mDate.setTime(new Date());
        this.mDate.add(Calendar.DAY_OF_MONTH, index - 1);
        this.setTextDate();
        this.setContent();
    }

}
