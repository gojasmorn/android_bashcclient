package gojas.ru.bashclient;

import android.content.Context;
import android.content.res.TypedArray;
import android.media.Image;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by gojas on 05.07.2015.
 */
public class DrawerAdapter extends BaseAdapter {

    LayoutInflater inflater;
    String[] items;
    public DrawerAdapter(Context context){
        inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //items=context.getResources().getStringArray(R.array.navigation_drawer_items);
        //icons=context.getResources().obtainTypedArray(R.array.drawer_icons_black);
        items=Utility.getDrawerItems();
    }
    @Override
    public int getCount() {
        return items.length;
    }

    @Override
    public Object getItem(int position) {
        return items[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View item=convertView;
        if(item==null){
            item=inflater.inflate(R.layout.drawer_item,parent,false);
        }
        RelativeLayout mainLayout=(RelativeLayout)item.findViewById(R.id.drawer_item_layout);
        mainLayout.setBackgroundColor(Utility.getDefaultItemBackgroundColor());
        TextView itemText=(TextView)item.findViewById(R.id.drawer_item_text);
        itemText.setTextColor(Utility.getDefaultTextColor());
        itemText.setText(items[position]);
        ImageView itemIcon=(ImageView)item.findViewById(R.id.drawer_item_icon);
        itemIcon.setImageDrawable(Utility.getDrawableDrawerIcon(position));
        return item;
    }
}
