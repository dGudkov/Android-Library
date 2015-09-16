package ru.gdo.andriod.example.pagecurl;

import android.app.Activity;
import android.os.Bundle;

import ru.gdo.andriod.example.pagecurl.controller.ModelController;
import ru.gdo.andriod.example.pagecurl.model.CalendarDataWrapper;
import ru.gdo.andriod.example.pagecurl.model.CalendarModel;
import ru.gdo.andriod.example.pagecurl.model.IntegerDataWrapper;
import ru.gdo.andriod.example.pagecurl.model.IntegerModel;
import ru.gdo.andriod.example.pagecurl.view.BookLayout;

public class PageCurlActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BookLayout bk = new BookLayout(this);
        try {
//            ModelController mc = new ModelController(this, 11, IntegerDataWrapper.class, IntegerModel.class);
//            ModelController mc = new ModelController(this, 11, CalendarDataWrapper.class, CalendarModel.class);
//            IntegerDataWrapper dw = new IntegerDataWrapper(this, 11);
            CalendarDataWrapper dw = new CalendarDataWrapper(this, 11);
            bk.setPageAdapter(dw);
            setContentView(bk);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
