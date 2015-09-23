package ru.gdo.android.example.materialdesign;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Danil Gudkov <danil.gudkov@progforce.com>
 * @copyrights ProgForce, 2015
 * @since 05.08.15.
 */

public class MenuFragment extends Fragment {

    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private View containerView;
    private static String[] titles = null;
    private FragmentDrawerListener drawerListener;

    private boolean menuShowing = false;

    public MenuFragment() {

    }

    public void setDrawerListener(FragmentDrawerListener listener) {
        this.drawerListener = listener;
    }

    public static List<MenuItem> getData() {
        List<MenuItem> data = new ArrayList<>();

        // preparing navigation drawer items
        for (String title : titles) {
            MenuItem navItem = new MenuItem();
            navItem.setTitle(title);
            data.add(navItem);
        }
        return data;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // drawer labels
        titles = getActivity().getResources().getStringArray(R.array.menu_title);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflating view layout
        View layout = inflater.inflate(R.layout.menu_layout, container, false);
        ListView listView = (ListView) layout.findViewById(R.id.drawerList);

        ArrayList<ContentItem> objects = new ArrayList<>();

        objects.add(new ContentItem("Horizontal scroll view"));
        objects.add(new ContentItem("Vertical folding layout"));
        objects.add(new ContentItem("Horizontal folding layout"));
        objects.add(new ContentItem("Vertical sliding layout"));


        MenuAdapter adapter = new MenuAdapter(getActivity(), objects);
        listView.setAdapter(adapter);

//        listView.setLayoutManager(new LinearLayoutManager(getActivity()));
//        listView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), listView, new ClickListener() {
//            @Override
//            public void onClick(View view, int position) {
//                drawerListener.onDrawerItemSelected(view, position);
//                mDrawerLayout.closeDrawer(containerView);
//            }
//
//            @Override
//            public void onLongClick(View view, int position) {
//
//            }
//        }));


//        RelativeLayout.LayoutParams rLp = (RelativeLayout.LayoutParams) layout.findViewById(R.id.menu_account_picture_profile).getLayoutParams();
//        rLp.width = rLp.height = MENU_ACCOUNT_SECTION_IMAGE_SIZE;
//
//        View menuAccountSection = layout.findViewById(R.id.menu_account_section);
//        menuAccountSection.setPadding(MENU_ACCOUNT_SECTION_LEFT_PADDING,
//                0,
//                MENU_ACCOUNT_SECTION_RIGHT_PADDING,
//                MENU_ACCOUNT_SECTION_BOTTOM_PADDING);
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR1) {
//            menuAccountSection.setPaddingRelative(MENU_ACCOUNT_SECTION_LEFT_PADDING,
//                    0,
//                    MENU_ACCOUNT_SECTION_RIGHT_PADDING,
//                    MENU_ACCOUNT_SECTION_BOTTOM_PADDING);
//        }

//        rLp = (RelativeLayout.LayoutParams) menuAccountSection.getLayoutParams();
//        rLp.height = MENU_ACCOUNT_SECTION_HEIGHT;

//        TextView title = ((TextView) layout.findViewById(R.id.menu_jewishhistory_title));
//        title.setText(Html.fromHtml(getResources().getString(R.string.str_menu_jewishhistory_title)));
//        title.setTextSize(MENU_TITLE_TEXT_SIZE);
//
//        TextView feedBack = (TextView) layout.findViewById(R.id.feedback);
//        feedBack.setText(Html.fromHtml(getResources().getString(R.string.str_site_feedback)));
//        feedBack.setTextSize(MENU_FEEDBACK_TEXT_SIZE);
//        feedBack.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent i = new Intent(Intent.ACTION_VIEW);
//                i.setData(Uri.parse("http://www.chabad.org/calendar/view/month_cdo/jewish/Jewish-Calendar.htm"));
//                startActivity(i);
//            }
//        });
        return layout;
    }

    public void setUp(int fragmentId, DrawerLayout drawerLayout,
                      final Toolbar toolbar) {
        containerView = getActivity().findViewById(fragmentId);
        mDrawerLayout = drawerLayout;
        mDrawerToggle = new ActionBarDrawerToggle(getActivity(), drawerLayout, toolbar, R.string.menu_open, R.string.menu_close) {
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

        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();
            }
        });

//        if (getView() != null) {
//            getView().getLayoutParams().width = ApplicationWrapper.MENU_WIDTH;
//        }

        mDrawerToggle.setDrawerIndicatorEnabled(false);
    }

    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

//    static class RecyclerTouchListener implements ListView.OnItemTouchListener {
//
//        private GestureDetector gestureDetector;
//        private ClickListener clickListener;
//
//        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final ClickListener clickListener) {
//            this.clickListener = clickListener;
//            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
//                @Override
//                public boolean onSingleTapUp(MotionEvent e) {
//                    return true;
//                }
//
//                @Override
//                public void onLongPress(MotionEvent e) {
//                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
//                    if (child != null && clickListener != null) {
//                        clickListener.onLongClick(child, recyclerView.getChildLayoutPosition(child));
//                    }
//                }
//            });
//        }
//
//        @Override
//        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
//
//            View child = rv.findChildViewUnder(e.getX(), e.getY());
//            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
//                clickListener.onClick(child, rv.getChildLayoutPosition(child));
//            }
//            return false;
//        }
//
//        @Override
//        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
//        }
//
//        @Override
//        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
//
//        }
//    }
//
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

    class ContentItem {
        String mName;

        public ContentItem(String name) {
            this.mName = name;
        }
    }

    public class MenuAdapter extends ArrayAdapter<ContentItem> {

        private LayoutInflater inflater;

        public MenuAdapter(Context context, List<ContentItem> objects) {
            super(context, 0, objects);
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ContentItem c = getItem(position);

            MyViewHolder holder;

            if (convertView == null) {

                holder = new MyViewHolder();

                convertView = LayoutInflater.from(getContext()).inflate(R.layout.menu_list_item, null);
                holder.title = (TextView) convertView.findViewById(R.id.titlemenu);

                convertView.setTag(holder);

            } else {
                holder = (MyViewHolder) convertView.getTag();
            }

            holder.title.setText(c.mName);

            return convertView;
        }

        class MyViewHolder {
            TextView title;
        }

    }

}
