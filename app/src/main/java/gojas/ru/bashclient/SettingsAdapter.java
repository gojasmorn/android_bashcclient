package gojas.ru.bashclient;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

/**
 * Created by gojas on 15.07.2015.
 */
public class SettingsAdapter extends BaseAdapter {
    String[] main;
    String[] details;
    Context context;
    LayoutInflater inflater;
    Activity activity;
    public SettingsAdapter(Activity activity){
        this.context=activity;
        this.activity=activity;
        this.main=context.getResources().getStringArray(R.array.settings_item_main);
        this.details=context.getResources().getStringArray(R.array.settings_item_details);
        inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        return main.length;
    }

    @Override
    public Object getItem(int position) {
        return main[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View item=convertView;
        if(item==null){
            item=inflater.inflate(R.layout.settings_list_item,parent,false);
        }
        RelativeLayout itemLayout=(RelativeLayout)item.findViewById(R.id.item_main);
        itemLayout.setBackgroundColor(Utility.getDefaultItemBackgroundColor());
        ImageView icon=(ImageView)item.findViewById(R.id.settings_item_icon);
        icon.setImageDrawable(Utility.getDrawableSettingsIcon(position));
        TextView mainText=(TextView)item.findViewById(R.id.main_name);
        mainText.setText(main[position]);
        mainText.setTextColor(Utility.getDefaultTextColor());
        TextView detailsText=(TextView)item.findViewById(R.id.details_text);
        detailsText.setText(details[position]);
        if(position==0){
            final ToggleButton toggleMode=(ToggleButton)item.findViewById(R.id.toggle_mode);
            toggleMode.setVisibility(View.VISIBLE);
            if(Utility.isNightModeOn())toggleMode.setChecked(true);
            final FrameLayout mainLayout=(FrameLayout)activity.findViewById(R.id.content_frame);
            toggleMode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    SharedPreferences preferences=activity.getPreferences(Activity.MODE_PRIVATE);
                    if(isChecked){
                        SharedPreferences.Editor ed = preferences.edit();
                        ed.putString(Utility.MODE_LABEL, Utility.MODE_ON);
                        ed.apply();
                        Utility.setNightMode(Utility.MODE_ON);
                    }else{
                        SharedPreferences.Editor ed = preferences.edit();
                        ed.putString(Utility.MODE_LABEL, Utility.MODE_OFF);
                        ed.apply();
                        Utility.setNightMode(Utility.MODE_OFF);
                    }
                    mainLayout.setBackgroundColor(Utility.getActivityBackgroundColor());
                    Toolbar toolbar=(Toolbar)activity.findViewById(R.id.toolbar);
                    toolbar.setBackgroundColor(Utility.getToolbarColor());
                    if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP){
                        activity.getWindow().setStatusBarColor(Utility.getStatusBarColor());
                    }
                    FrameLayout mainLayout=(FrameLayout)activity.findViewById(R.id.content_frame);
                    mainLayout.setBackgroundColor(Utility.getActivityBackgroundColor());
                    ListView drawerListView=(ListView)activity.findViewById(R.id.left_drawer);
                    drawerListView.setBackgroundColor(Utility.getDefaultItemBackgroundColor());
                    ((DrawerAdapter)drawerListView.getAdapter()).notifyDataSetChanged();
                    notifyDataSetChanged();
                }
            });
            itemLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    toggleMode.toggle();
                }
            });
        }
        return item;
    }
}
