package ru.gdo.android.example.infiniteviewpager.data;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import ru.gdo.android.example.infiniteviewpager.R;
import ru.gdo.android.library.infiniteviewpager.adapter.InfinitePagerAdapter;
import ru.gdo.android.library.infiniteviewpager.interfaces.IInfiniteViewPagerModel;

/**
 * @author Danil Gudkov <danil.gudkov@progforce.com>
 * @copyrights ProgForce, 2015
 * @since 27.07.15.
 */

public class Data_Integer implements IInfiniteViewPagerModel {

    private int mValue = 1000;
    private View mView;
    private TextView mTVNext, mTVCurrent, mTVPrev;
    private String mNextText, mCurrentText, mPrevText;

    @Override
    public View getLayout(LayoutInflater inflater, final InfinitePagerAdapter adapter) {
        if (this.mView == null) {
            this.mView = inflater.inflate(R.layout.data_integer, null);
        }
        this.mTVPrev = (TextView) this.mView.findViewById(R.id.prev);
        this.mTVCurrent = (TextView) this.mView.findViewById(R.id.current);
        this.mTVNext = (TextView) this.mView.findViewById(R.id.next);

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
        if (this.mTVPrev != null) {
            this.mTVPrev.setText(this.mPrevText);
        }
        if (this.mTVCurrent != null) {
            this.mTVCurrent.setText(this.mCurrentText);
        }
        if (this.mTVNext != null) {
            this.mTVNext.setText(this.mNextText);
        }
    }

    private void setTextDate() {
        this.mPrevText = String.valueOf(this.mValue - 1);
        this.mCurrentText = String.valueOf(this.mValue);
        this.mNextText = String.valueOf(this.mValue + 1);
    }

    @Override
    public void shift(int dayShift) {
        this.mValue += dayShift;
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
    }

    @Override
    public void assign(IInfiniteViewPagerModel model) {
        Data_Integer dataModel = (Data_Integer) model;
        this.mValue = dataModel.mValue;
    }

    @Override
    public void init(int index) {
        this.mValue = 1001 + index - 1;
        this.setTextDate();
        this.setContent();
    }

}
