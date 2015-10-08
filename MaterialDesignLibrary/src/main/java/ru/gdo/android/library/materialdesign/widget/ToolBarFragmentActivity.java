package ru.gdo.android.library.materialdesign.widget;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import ru.gdo.android.library.materialdesign.exception.ToolBarActivityException;
import ru.gdo.android.library.materialdesign.interfaces.IMenuItem;
import ru.gdo.android.library.materialdesign.interfaces.IToolBarInterface;

/**
 * @author Danil Gudkov <danil.gudkov@progforce.com>
 * @copyrights ProgForce, 2015
 * @since 23.09.15.
 */

public abstract class ToolBarFragmentActivity<I extends IMenuItem> extends AppCompatActivity
        implements
        View.OnClickListener,
        BaseMenuFragment.FragmentDrawerListener,
        IToolBarInterface {

    protected Toolbar mToolBar;
    protected TextView mTitle;
    protected BaseMenuFragment<I> mMenuFragment;
    protected BaseFragment mContent;

    @SuppressWarnings("unchecked")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentViewId());

        this.mToolBar = (Toolbar) findViewById(getToolbarId());
        if (this.mToolBar == null) {
            throw new ToolBarActivityException("Error toolbar initialization.");
        }
        setSupportActionBar(this.mToolBar);

        View view = this.mToolBar.findViewById(getMenuLayoutId());
        if (view != null) {
            view.setOnClickListener(this);
        }

        view = this.mToolBar.findViewById(getMenuId());
        if (view != null) {
            view.setOnClickListener(this);
        }
        this.mToolBar.setContentInsetsAbsolute(0, 0);

        this.mTitle = (TextView) this.mToolBar.findViewById(getTitleId());
        if (this.mTitle != null) {
            this.mTitle.setTextSize(24);
        }

        this.mMenuFragment = (BaseMenuFragment<I>)
                getSupportFragmentManager().findFragmentById(getNavigationDrawerId());
        if (this.mMenuFragment == null) {
            throw new ToolBarActivityException("Error menu initialization.");
        }
        this.mMenuFragment.setUp(getNavigationDrawerId(),
                (DrawerLayout) findViewById(getDrawerLayoutId()),
                this.mToolBar
//                getMenuItenClass()
        );

        this.mMenuFragment.setDrawerListener(this);

        if (savedInstanceState != null) {
            Fragment fragment = getSupportFragmentManager().findFragmentByTag("mContent");
            if (fragment != null) {
                if (fragment instanceof BaseFragment) {
                    BaseFragment baseFragment = (BaseFragment) fragment;
                    baseFragment.init(this, this.mToolBar, this);
                    this.mContent = baseFragment;
//                    this.mTitle.setText(Html.fromHtml(baseFragment.getTitleText()));
                }
            }
        } else {
            displayView(getHomeFragmentId());
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if ((id == this.getMenuLayoutId()) || (id == getMenuId())) {
            mMenuFragment.openMenu();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) { // если нажата кнопка "Назад"
            if (mMenuFragment.isMenuShowing()) { // и если Menu открыто
                mMenuFragment.closeMenu();
                return false;
            } else {
                if (!isMainFragment()) {
                    displayView(getHomeFragmentId());
                    return false;
                }
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onDrawerItemSelected(View view, int position) {
        displayView(position);
    }

    protected void displayView(int position) {
        switchView(getFragment(position));
    }

    protected void switchView(BaseFragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(getContainerId(), fragment, "mContent")
                    .commit();
            this.mContent = fragment;
        }
    }

    @Override
    public void onToolBarClick(View v) {
        Toast.makeText(this, "ToolBar component clicked.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setToolBarTitle(String title) {
        if (title != null) {
            this.mTitle.setText(Html.fromHtml(title));
        }
    }

    protected abstract int getContentViewId();

    protected abstract int getToolbarId();

    protected abstract int getMenuLayoutId();

    protected abstract int getMenuId();

    protected abstract int getTitleId();

    protected abstract int getNavigationDrawerId();

    protected abstract int getDrawerLayoutId();

    protected abstract int getContainerId();

    protected abstract boolean isMainFragment();

    protected abstract int getHomeFragmentId();

    protected abstract BaseFragment getFragment(int position);

//    protected abstract Class<I> getMenuItenClass();
}
