package ru.gdo.andriod.example.pagecurl;

import android.app.Activity;
import android.os.Bundle;

import ru.gdo.andriod.example.pagecurl.controller.ModelController;
import ru.gdo.andriod.example.pagecurl.view.BookLayout;

public class PageCurlActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BookLayout bk = new BookLayout(this);
        ModelController mc = new ModelController(this, 11);
        bk.setPageAdapter(mc);
        setContentView(bk);
    }

}
