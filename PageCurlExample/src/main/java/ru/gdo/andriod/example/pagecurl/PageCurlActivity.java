package ru.gdo.andriod.example.pagecurl;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;

import java.util.ArrayList;
import java.util.List;

import ru.gdo.andriod.example.pagecurl.view.BookAdapter;
import ru.gdo.andriod.example.pagecurl.view.BookLayout;

public class PageCurlActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BookLayout bk = new BookLayout(this);
        List<String> str = new ArrayList<String>();
        str.add("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
        str.add("bbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb");
        str.add("ccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccc");
        BookAdapter ba = new BookAdapter(this);
        ba.addItem(str);
        bk.setPageAdapter(ba);
        setContentView(bk);
//        setContentView(R.layout.main_layout);
    }

}
