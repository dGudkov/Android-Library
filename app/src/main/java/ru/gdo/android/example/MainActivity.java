package ru.gdo.android.example;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.gdo.android.example.foldinghorizontallayout.FoldingHorizontalLayoutActivity;

import java.util.ArrayList;
import java.util.List;

import ru.gdo.android.example.foldingverticallayout.FoldingVerticalLayoutActivity;
import ru.gdo.android.example.horizontalscrollview.HorizontalScrollViewActivity;
import ru.gdo.android.example.horizontalsliding.HorizontalSlidingLayoutActivity;

/**
 * @author Danil Gudkov <d_n_l@mail.ru>
 * @copyrights Danil Gudkov, 2015
 * @since 23.07.15.
 */

public class MainActivity extends AppCompatActivity implements OnItemClickListener  {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        objects.add(new ContentItem("Horizontal sliding layout",
                "A simple demonstration of the horizontal sliding layout.",
                HorizontalSlidingLayoutActivity.class));

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
