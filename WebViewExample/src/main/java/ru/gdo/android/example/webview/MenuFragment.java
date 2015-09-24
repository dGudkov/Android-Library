package ru.gdo.android.example.webview;

import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import ru.gdo.android.library.materialdesign.widget.BaseMenuFragment;

/**
 * @author Danil Gudkov <danil.gudkov@progforce.com>
 * @copyrights ProgForce, 2015
 * @since 23.09.15.
 */

public class MenuFragment extends BaseMenuFragment<MenuTextItem> {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);

        TextView link = (TextView) view.findViewById(R.id.feedback);

        link.setTag(link.getText().toString());
        link.setPaintFlags(link.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = (String) v.getTag();
                closeMenu();
                ((WebViewActivity) MenuFragment.this.getActivity()).displayView(
                        WebViewActivity.WEB_VIEW_FRAGMENT,
                        url
                );
            }
        });

        return view;
    }

    @Override
    public ArrayList<MenuTextItem> getMenItems() {
        ArrayList<MenuTextItem> objects = super.getMenItems();

        objects.add(new MenuTextItem("Home"));
        objects.add(new MenuTextItem("Google"));
        objects.add(new MenuTextItem("Yandex"));
        objects.add(new MenuTextItem("Bing"));
        objects.add(new MenuTextItem("Yahoo"));
        objects.add(new MenuTextItem("Hotbot"));

        return objects;
    }

    @Override
    public int getMenuLayoutId() {
        return R.layout.menu_layout;
    }

    @Override
    public int getListViewId() {
        return R.id.drawerList;
    }
}
