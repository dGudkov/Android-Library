package ru.gdo.android.example.pagecurl;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import ru.gdo.android.example.pagecurl.model.CalendarDataAdapter;
import ru.gdo.android.example.pagecurl.model.IntegerDataAdapter;
import ru.gdo.android.library.pagecurl.widget.BookLayout;

public class PageCurlActivity extends FragmentActivity implements View.OnClickListener {

    private static final int UNINITIALIZED = -1;
    private static final int INTEGER_ADAPTER = 0;
    private static final int CALENDAR_ADAPTER = 1;

    private int mCurrentAdapter = UNINITIALIZED;
    private CalendarDataAdapter calendarDataAdapter;
    private IntegerDataAdapter integerDataAdapter;
    private BookLayout bookLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pagecurl_activity);

        this.bookLayout = (BookLayout) findViewById(R.id.booklayout);
        try {
            this.calendarDataAdapter = new CalendarDataAdapter(this, 11);
            this.integerDataAdapter = new IntegerDataAdapter(this, 11);
            this.bookLayout.setPageAdapter(calendarDataAdapter);

            findViewById(R.id.integerbutton).setOnClickListener(this);
            findViewById(R.id.calendarbutton).setOnClickListener(this);

            setContentAdapter(INTEGER_ADAPTER);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void setContentAdapter(int currentAdapter) {
        switch (currentAdapter) {
            case INTEGER_ADAPTER:
                if (this.mCurrentAdapter != currentAdapter) {
                    this.bookLayout.setPageAdapter(integerDataAdapter);
                    this.bookLayout.invalidate();
                    this.mCurrentAdapter = currentAdapter;
                }
                break;
            case CALENDAR_ADAPTER:
                if (this.mCurrentAdapter != currentAdapter) {
                    this.bookLayout.setPageAdapter(calendarDataAdapter);
                    this.bookLayout.invalidate();
                    this.mCurrentAdapter = currentAdapter;
                }
                break;
        }
    }

    public void onClick(View view) {
        if (view.getId() == R.id.integerbutton) {
            setContentAdapter(INTEGER_ADAPTER);
        } else if (view.getId() == R.id.calendarbutton) {
            setContentAdapter(CALENDAR_ADAPTER);
        }
    }

}

