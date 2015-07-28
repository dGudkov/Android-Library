package ru.gdo.android.example.infiniteviewpager;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import ru.gdo.android.example.infiniteviewpager.data.Data_Calendar;
import ru.gdo.android.example.infiniteviewpager.data.Data_Integer;
import ru.gdo.android.library.infiniteviewpager.adapter.InfinitePagerAdapter;
import ru.gdo.android.library.infiniteviewpager.interfaces.IInfiniteViewPagerModel;
import ru.gdo.android.library.infiniteviewpager.widget.InfiniteViewPager;

import static ru.gdo.android.library.infiniteviewpager.adapter.InfinitePagerAdapter.AssignOnShift.BY_CONTEXT;

 /**
 * @author Danil Gudkov <d_n_l@mail.ru>
 * @copyrights Danil Gudkov, 2015
 * @since 27.07.15.
 */

public class InfiniteViewPagerActivity extends Activity implements View.OnClickListener {

    private InfiniteViewPager mViewPager;
    private Data_Integer[] integerPageModel;
    private Data_Calendar[] calendarPageModel;
    private InfinitePagerAdapter<IInfiniteViewPagerModel> mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.infiniteviewpageractivity);

        this.calendarPageModel = new Data_Calendar[3];
        for (int i=0; i < this.calendarPageModel.length; i++) {
            this.calendarPageModel[i] = new Data_Calendar();
        }

        this.integerPageModel = new Data_Integer[3];
        for (int i=0; i < this.integerPageModel.length; i++) {
            this.integerPageModel[i] = new Data_Integer();
        }

        this.findViewById(R.id.btnCalendar).setOnClickListener(this);
        this.findViewById(R.id.btnInteger).setOnClickListener(this);
        this.findViewById(R.id.btnPrev).setOnClickListener(this);
        this.findViewById(R.id.btnFirst).setOnClickListener(this);
        this.findViewById(R.id.btnNext).setOnClickListener(this);

        this.mViewPager = (InfiniteViewPager) findViewById(R.id.viewpager);
        this.fillScroll(this.calendarPageModel);
    }

    @Override
    public void onClick(View v) {
        int viewId = v.getId();
        if (viewId == R.id.btnCalendar) {
            this.fillScroll(this.calendarPageModel);
        } else if (viewId == R.id.btnInteger) {
            this.fillScroll(this.integerPageModel );
        } else if (viewId == R.id.btnPrev) {
            this.mAdapter.goPrev();
        } else if (viewId == R.id.btnFirst) {
            this.mAdapter.initModels();
        } else if (viewId == R.id.btnNext) {
            this.mAdapter.goNext();
        }

    }

    private void fillScroll(IInfiniteViewPagerModel model[]) {
        this.mAdapter = new InfinitePagerAdapter<>(getLayoutInflater(), model);
        this.mAdapter.setAssignOnShift(BY_CONTEXT);
        this.mViewPager.setAdapter(this.mAdapter);
    }
}
