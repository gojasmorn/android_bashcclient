package gojas.ru.bashclient;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Build;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends ActionBarActivity implements HtmlTask.TaskInterface {

    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private ListView drawerListView;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    public static final String TAG="bash";
    SharedPreferences preferences;
    int globalPosition;
    boolean drawerIsShow=false;
    HtmlTask currentTask;

    private static String MANU_INDEX="menu";

    public final static int NEW=0;
    public final static int RANDOM=1;
    public final static int BEST=2;
    public final static int BYRATING=3;
    public final static int ABYSS=4;
    public final static int ABYSSTOP=5;
    public final static int ABYSSBEST=6;
    public final static int BOOKMARKS=7;
    public final static int SETTINGS=8;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate Activity");
        currentTask=(HtmlTask)getLastCustomNonConfigurationInstance();
        preferences=getPreferences(MODE_PRIVATE);
        String nightModeStatus = preferences.getString(Utility.MODE_LABEL, Utility.MODE_OFF);
        String textSize=preferences.getString(Utility.MODE_TEXT_SIZE,Utility.DEFAULT_TEXT_SIZE);
        Utility.setNightMode(nightModeStatus);
        Utility.setContext(MainActivity.this);
        Utility.setTextSize(textSize);
        toolbar=(Toolbar)findViewById(R.id.toolbar);
        toolbar.setBackgroundColor(Utility.getToolbarColor());
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP){
            getWindow().setStatusBarColor(Utility.getStatusBarColor());
        }
        setSupportActionBar(toolbar);
        drawerLayout=(DrawerLayout)findViewById(R.id.drawer_layout);
        drawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        drawerListView=(ListView)findViewById(R.id.left_drawer);
        drawerListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        drawerListView.setAdapter(new DrawerAdapter(getApplicationContext()));
        drawerListView.setOnItemClickListener(new DrawerItemClickListener());
        drawerListView.setBackgroundColor(Utility.getDefaultItemBackgroundColor());
        actionBarDrawerToggle=new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.app_name,R.string.app_name);
        drawerLayout.setDrawerListener(actionBarDrawerToggle);
        FrameLayout mainLayout=(FrameLayout)findViewById(R.id.content_frame);
        mainLayout.setBackgroundColor(Utility.getActivityBackgroundColor());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        if (savedInstanceState == null) {
            Log.d(TAG,"savedInstanceState == null");
            selectItem(0,false);
        }else{
            int position=savedInstanceState.getInt(MANU_INDEX,0);
            selectItem(position,false);
            globalPosition=position;
        }


    }



    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            /*
            FragmentManager fragmentManager = getFragmentManager();
            Fragment currentFragment=fragmentManager.findFragmentById(R.id.content_frame);
            Log.d(MainActivity.TAG,"currentFragment == null ? "+String.valueOf(currentFragment == null));
            if(currentFragment != null){
                fragmentManager.beginTransaction().remove(currentFragment).commit();
            }
            */
            globalPosition=position;
            selectItem(position,true);
        }
    }

    public void selectItem(int position,boolean fromDrawer) {
        Log.d(TAG,"selectItem "+position);
        //**

        FragmentManager fragmentManager = getFragmentManager();
        Fragment currentFragment=fragmentManager.findFragmentById(R.id.content_frame);
        if(fromDrawer || currentFragment==null) {
            Fragment fragment;
            switch (position) {
                case SETTINGS:
                    fragment = new SettingsFragment();
                    break;
                case BOOKMARKS:
                    fragment = new BookmarksFragment();
                    break;
                default:
                    fragment = new QuoteFragment();
            }

            Bundle args = new Bundle();
            args.putInt(QuoteFragment.ARG_MENU_INDEX, position);
            fragment.setArguments(args);

            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.content_frame, fragment);
            transaction.commit();
        }
        drawerListView.setItemChecked(position, true);
        getSupportActionBar().setTitle(getResources().getStringArray(R.array.navigation_drawer_items)[position]);
        drawerLayout.closeDrawer(drawerListView);
        drawerIsShow=false;
    }


    @Override
    public void onBackPressed() {
            if(globalPosition==SETTINGS && !drawerIsShow){
                drawerLayout.openDrawer(drawerListView);
                drawerIsShow=true;
            }else{
                super.onBackPressed();
            }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(MANU_INDEX, globalPosition);
    }

    @Override
    public void execute(String adress, QuoteFragment fragment, boolean loaded, boolean refresh, int currentIndex, ArrayList<Quote> quotes) {
        if(currentTask==null){
            currentTask=new HtmlTask();
            currentTask.setCurrentIndex(currentIndex);

        }
        currentTask.link(fragment,getApplicationContext());
        currentTask.setQuotes(quotes);
        Log.d(TAG,"adress "+adress);
        currentTask.execute(adress);
    }

    @Override
    public void updateData(QuoteFragment fragment) {
        if (currentTask==null) return;
        currentTask.link(fragment, getApplicationContext());
    }

    @Override
    public void onPostExecute(QuoteFragment fragment,ArrayList<Quote> arrayList) {
        if (currentTask==null) currentTask=new HtmlTask();
        currentTask.setQuotes(arrayList);
        currentTask.link(fragment,getApplicationContext());
        currentTask.onPostExecute(arrayList);
    }

    @Override
    public void deleteTask(int index) {
        if (currentTask==null)return;
        if (index!=currentTask.getCurrentIndex()){
            currentTask.unLink();
            currentTask=null;
        }
    }

    @Override
    public void nulledTask() {
        currentTask=null;
    }

    @Override
    public Object onRetainCustomNonConfigurationInstance() {
        if (currentTask!=null){
            currentTask.unLink();
        }
        return currentTask;
    }
}
