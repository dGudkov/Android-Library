package ru.gdo.android.library.materialdesign.widget;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import ru.gdo.android.library.materialdesign.interfaces.IMenuItem;
import ru.gdo.android.library.materialdesign.R;

/**
 * @author Danil Gudkov <danil.gudkov@progforce.com>
 * @copyrights ProgForce, 2015
 * @since 05.08.15.
 */

public abstract class BaseMenuFragment<I extends IMenuItem> extends Fragment {

    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private View containerView;
    private FragmentDrawerListener drawerListener;

    private boolean menuShowing = false;
    private Class<I> mMenuItemClassModel;
    private int menuLayoutId;

    public BaseMenuFragment() {

    }

    public void setDrawerListener(FragmentDrawerListener listener) {
        this.drawerListener = listener;
    }

    public ArrayList<I> getMenItems() {
        return  new ArrayList<>();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layout = inflater.inflate(getMenuLayoutId(), container, false);
        ListView listView = (ListView) layout.findViewById(getListViewId());

        listView.setAdapter(new MenuAdapter(getActivity(), getMenItems()));

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                drawerListener.onDrawerItemSelected(view, position);
                mDrawerLayout.closeDrawer(containerView);
            }
        });

        listView.setOnLongClickListener(new AdapterView.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return false;
            }
        });

        return layout;
    }

    public void setUp(int fragmentId, DrawerLayout drawerLayout,
                      final Toolbar toolbar,
                      final Class<I> mMenuItemClassModel) {

        this.containerView = getActivity().findViewById(fragmentId);
        this.mDrawerLayout = drawerLayout;
        this.mDrawerToggle = new ActionBarDrawerToggle(getActivity(), drawerLayout, toolbar, R.string.menu_open, R.string.menu_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getActivity().invalidateOptionsMenu();
                menuShowing = true;
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                getActivity().invalidateOptionsMenu();
                menuShowing = false;
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                toolbar.setAlpha(1 - slideOffset / 2);
            }
        };

        this.mDrawerLayout.setDrawerListener(mDrawerToggle);
        this.mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();
            }
        });

        this.mDrawerToggle.setDrawerIndicatorEnabled(false);
        this.mMenuItemClassModel = mMenuItemClassModel;

        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);

        if (getView() != null) {
            getView().getLayoutParams().width = (int) (metrics.widthPixels * 0.5);
        }
    }

    private I initMenuItem() throws Exception {
        return mMenuItemClassModel.newInstance();
    }

    public interface FragmentDrawerListener {
        void onDrawerItemSelected(View view, int position);
    }

    public boolean isMenuShowing() {
        return menuShowing;
    }

    public void closeMenu() {
        mDrawerLayout.closeDrawer(containerView);
    }


    public void openMenu() {
        mDrawerLayout.openDrawer(containerView);
    }

    public class MenuAdapter extends ArrayAdapter<I> {

        public MenuAdapter(Context context, List<I> objects) {
            super(context, 0, objects);
        }

        @Override
        public View getView(int position, View view, ViewGroup parent) {
            I c = getItem(position);
            return c.getView(getContext(), view);
        }
    }

    public abstract int getMenuLayoutId();

    public abstract int getListViewId();

}
