package ru.gdo.android.example.webview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import ru.gdo.android.library.materialdesign.interfaces.IMenuItem;

/**
 * @author Danil Gudkov <danil.gudkov@progforce.com>
 * @copyrights ProgForce, 2015
 * @since 05.08.15.
 */

public class MenuTextItem implements IMenuItem {

    private String title;

    public MenuTextItem(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public View getView(Context context, View view) {
        Holder holder;

        if (view == null) {

            holder = new Holder();

            view = LayoutInflater.from(context).inflate(R.layout.menu_list_item, null);
            holder.title = (TextView) view.findViewById(R.id.titlemenu);

            view.setTag(holder);

        } else {
            holder = (Holder) view.getTag();
        }

        holder.title.setText(getTitle());

        return view;
    }

    class Holder {
        TextView title;
    }

}
