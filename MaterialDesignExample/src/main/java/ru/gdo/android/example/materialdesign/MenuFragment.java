package ru.gdo.android.example.materialdesign;

import java.util.ArrayList;

import ru.gdo.android.library.materialdesign.widget.BaseMenuFragment;

/**
 * @author Danil Gudkov <danil.gudkov@progforce.com>
 * @copyrights ProgForce, 2015
 * @since 23.09.15.
 */

public class MenuFragment extends BaseMenuFragment<MenuTextItem> {

    @Override
    public ArrayList<MenuTextItem> getMenItems() {
        ArrayList<MenuTextItem> objects = super.getMenItems();

        objects.add(new MenuTextItem("Home"));
        objects.add(new MenuTextItem("Friends"));
        objects.add(new MenuTextItem("Messages"));

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
