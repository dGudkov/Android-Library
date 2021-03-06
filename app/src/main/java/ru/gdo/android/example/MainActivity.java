package ru.gdo.android.example;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;
import com.gdo.android.example.foldinghorizontallayout.FoldingHorizontalLayoutActivity;

import io.fabric.sdk.android.Fabric;
import java.util.ArrayList;
import java.util.List;

import ru.gdo.android.example.crashapplication.CrashActivity;
import ru.gdo.android.example.gcm.GcmActivity;
import ru.gdo.android.example.materialdesign.MaterialDesignActivity;
import ru.gdo.android.example.notifications.NotificationActivity;
import ru.gdo.android.example.pagecurl.PageCurlActivity;
import ru.gdo.android.example.foldingverticallayout.FoldingVerticalLayoutActivity;
import ru.gdo.android.example.horizontalscrollview.HorizontalScrollViewActivity;
import ru.gdo.android.example.horizontalsliding.HorizontalSlidingLayoutActivity;
import ru.gdo.android.example.infiniteviewpager.InfiniteViewPagerActivity;
import ru.gdo.android.example.verticalsliding.VerticalSlidingLayoutActivity;
import ru.gdo.android.example.webview.WebViewActivity;

/**
 * @author Danil Gudkov <d_n_l@mail.ru>
 * @copyrights Danil Gudkov, 2015
 * @since 23.07.15.
 */

public class MainActivity extends Activity implements OnItemClickListener  {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_main);

        ArrayList<ContentItem> objects = new ArrayList<>();

        objects.add(new ContentItem("Horizontal scroll view",
                "A simple demonstration of the horizontal scroll view.",
                HorizontalScrollViewActivity.class));
        objects.add(new ContentItem("Vertical folding layout",
                "A simple demonstration of the vertical folding layout.",
                FoldingVerticalLayoutActivity.class));
        objects.add(new ContentItem("Horizontal folding layout",
                "A simple demonstration of the horizontal folding layout.",
                FoldingHorizontalLayoutActivity.class));
        objects.add(new ContentItem("Vertical sliding layout",
                "A simple demonstration of the vertical sliding layout.",
                VerticalSlidingLayoutActivity.class));
        objects.add(new ContentItem("Horizontal sliding layout",
                "A simple demonstration of the horizontal sliding layout.",
                HorizontalSlidingLayoutActivity.class));
        objects.add(new ContentItem("Infinite view pager",
                "A simple demonstration of the infinite view pager.",
                InfiniteViewPagerActivity.class));
        objects.add(new ContentItem("Page curl",
                "A simple demonstration of the page curl.",
                PageCurlActivity.class));
        objects.add(new ContentItem("Google Cloud Messaging",
                "A simple demonstration of the Google Cloud Messaging.",
                GcmActivity.class));
        objects.add(new ContentItem("Notification",
                "A simple demonstration of the notifications.",
                NotificationActivity.class));
        objects.add(new ContentItem("WebView",
                "A simple demonstration of the WebView.",
                WebViewActivity.class));
        objects.add(new ContentItem("Material design",
                "A simple demonstration of the Material design.",
                MaterialDesignActivity.class));
        objects.add(new ContentItem("Crash application",
                "A simple demonstration of the сrash application.",
                CrashActivity.class));

        MyAdapter adapter = new MyAdapter(this, objects);

        ListView lv = (ListView) findViewById(R.id.listView1);
        lv.setAdapter(adapter);

        lv.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
        ViewHolder holder = (ViewHolder) view.getTag();
        if (holder != null) {
            Intent intent = new Intent(this, holder.mClass);
            startActivity(intent);
            overridePendingTransition(R.anim.move_right_in_activity, R.anim.move_left_out_activity);
        }
    }

    private class ContentItem {
        String mName;
        String mDescription;
        Class mClass;

        public ContentItem(String name, String description, Class clazz) {
            this.mName = name;
            this.mDescription = description;
            this.mClass = clazz;
        }
    }

    private class MyAdapter extends ArrayAdapter<ContentItem> {

        public MyAdapter(Context context, List<ContentItem> objects) {
            super(context, 0, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ContentItem c = getItem(position);

            ViewHolder holder;

            if (convertView == null) {

                holder = new ViewHolder();

                convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, null);
                holder.tvName = (TextView) convertView.findViewById(R.id.tvName);
                holder.tvDesc = (TextView) convertView.findViewById(R.id.tvDesc);
                holder.mClass = c.mClass;

                convertView.setTag(holder);

            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.tvName.setText(c.mName);
            holder.tvDesc.setText(c.mDescription);

            return convertView;
        }

    }

    private class ViewHolder {
        TextView tvName, tvDesc;
        Class mClass;
    }
}
