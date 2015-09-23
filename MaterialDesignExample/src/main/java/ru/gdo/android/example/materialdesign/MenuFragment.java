package ru.gdo.android.example.materialdesign;

import java.util.ArrayList;

import ru.gdo.android.library.materialdesign.widget.BaseMenuFragment;

/**
 * @author Danil Gudkov <danil.gudkov@progforce.com>
 * @copyrights ProgForce, 2015
 * @since 23.09.15.
 */

public class MenuFragment extends BaseMenuFragment<MenuItem> {

    @Override
    public ArrayList<MenuItem> getMenItems() {
        ArrayList<MenuItem> objects = super.getMenItems();

        objects.add(new MenuItem("Home"));
        objects.add(new MenuItem("Friends"));
        objects.add(new MenuItem("Messages"));

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
