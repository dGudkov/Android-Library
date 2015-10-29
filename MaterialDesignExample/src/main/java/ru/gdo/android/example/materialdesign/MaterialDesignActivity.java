package ru.gdo.android.example.materialdesign;

import ru.gdo.android.example.materialdesign.fragments.ButtonFragment;
import ru.gdo.android.example.materialdesign.fragments.CheckBoxFragment;
import ru.gdo.android.example.materialdesign.fragments.FriendsFragment;
import ru.gdo.android.example.materialdesign.fragments.HomeFragment;
import ru.gdo.android.example.materialdesign.fragments.LayoutFragment;
import ru.gdo.android.example.materialdesign.fragments.MessagesFragment;
import ru.gdo.android.example.materialdesign.fragments.SwitchFragment;
import ru.gdo.android.library.materialdesign.widget.BaseFragment;
import ru.gdo.android.library.materialdesign.widget.ToolBarFragmentActivity;

public class MaterialDesignActivity extends ToolBarFragmentActivity {

    public static final int HOME_FRAGMENT = 0;
    public static final int FRIENDS_FRAGMENT = 1;
    public static final int MESSAGE_FRAGMENT = 2;
    public static final int BUTTON_FRAGMENT = 3;
    public static final int LAYOUTS_FRAGMENT = 4;
    public static final int SWITCH_FRAGMENT = 5;
    public static final int CHECKBOX_FRAGMENT = 6;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_material_design;
    }

    @Override
    protected int getToolbarId() {
        return R.id.toolbar;
    }

    @Override
    protected int getMenuLayoutId() {
        return R.id.status_bar_menu_layout;
    }

    @Override
    protected int getMenuId() {
        return R.id.status_bar_menu;
    }

    @Override
    protected int getTitleId() {
        return R.id.header_title;
    }

    @Override
    protected int getNavigationDrawerId() {
        return R.id.fragment_navigation_drawer;
    }

    @Override
    protected int getDrawerLayoutId() {
        return R.id.drawer_layout;
    }

    @Override
    protected int getContainerId() {
        return R.id.container_body;
    }

    @Override
    protected boolean isMainFragment() {
        return (mContent instanceof HomeFragment);
    }

    @Override
    protected int getHomeFragmentId() {
        return HOME_FRAGMENT;
    }

    @Override
    protected BaseFragment getFragment(int position) {
        BaseFragment fragment = null;
        switch (position) {
            case HOME_FRAGMENT:
                fragment = BaseFragment.newInstance(HomeFragment.class, this, this.mToolBar, this);
                break;
            case FRIENDS_FRAGMENT:
                fragment = BaseFragment.newInstance(FriendsFragment.class, this, this.mToolBar, this);
                break;
            case MESSAGE_FRAGMENT:
                fragment = BaseFragment.newInstance(MessagesFragment.class, this, this.mToolBar, this);
                break;
            case BUTTON_FRAGMENT:
                fragment = BaseFragment.newInstance(ButtonFragment.class, this, this.mToolBar, this);
                break;
            case LAYOUTS_FRAGMENT:
                fragment = BaseFragment.newInstance(LayoutFragment.class, this, this.mToolBar, this);
                break;
            case SWITCH_FRAGMENT:
                fragment = BaseFragment.newInstance(SwitchFragment.class, this, this.mToolBar, this);
                break;
            case CHECKBOX_FRAGMENT:
                fragment = BaseFragment.newInstance(CheckBoxFragment.class, this, this.mToolBar, this);
                break;
        }
        return fragment;
    }

}
