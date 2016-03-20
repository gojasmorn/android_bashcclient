package gojas.ru.bashclient;
import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Build;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.balysv.materialripple.MaterialRippleLayout;

import java.util.ArrayList;

public class MainActivity extends ActionBarActivity implements HtmlTask.TaskInterface {

    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private ListView drawerListView;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    SharedPreferences preferences;
    int globalPosition;
    boolean drawerIsShow=false;
    HtmlTask currentTask;
    MenuItem goToPage;
    FragmentManager fragmentManager;

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
    public final static int INFORMATION=9;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Log.d(TAG, "onCreate Activity");
        currentTask=(HtmlTask)getLastCustomNonConfigurationInstance();
        preferences=getPreferences(MODE_PRIVATE);
        String nightModeStatus = preferences.getString(Utility.MODE_LABEL, Utility.MODE_OFF);
        String textSize=preferences.getString(Utility.MODE_TEXT_SIZE,Utility.DEFAULT_TEXT_SIZE);
        Utility.setNightMode(nightModeStatus);
        Utility.setContext(getApplicationContext());
        Utility.setActivity(this);
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
            selectItem(0,false);
        }else{
            int position=savedInstanceState.getInt(MANU_INDEX,0);
            selectItem(position,false);
            globalPosition=position;
        }
        sendOfflineVotes();

    }

    private void sendOfflineVotes() {
        final QuotesDatabase db=QuotesDatabase.getInstance(getApplicationContext());
        final Cursor oflineVotes=db.getOfflineVotes();
        if(oflineVotes.getCount()>0&& Utility.isNetworkAvailable(getApplicationContext())){
        final Context context=getApplicationContext();

            final WebView webView = new WebView(context);
            webView.getSettings().setJavaScriptEnabled(true);
            Runnable runnable=new Runnable() {
                @Override
                public void run() {
                    while (oflineVotes.moveToNext()){
                        String quoteAdress = oflineVotes.getString(oflineVotes.getColumnIndex("quote_adress"));
                        String quoteJS = oflineVotes.getString(oflineVotes.getColumnIndex("vote_adress"));
                        webView.loadUrl(quoteAdress);
                        webView.loadUrl(quoteJS);
                        db.deleteOfflineVote(quoteAdress);
                    }
                }
            };
            runnable.run();
        }
    }


    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            globalPosition=position;
            selectItem(position,true);
        }
    }

    public void selectItem(int position,boolean fromDrawer) {
        //Log.d(TAG,"selectItem "+position);
        //**

        fragmentManager = getFragmentManager();
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
                case INFORMATION:
                    fragment=new InformationFragment();
                    break;
                default:
                    fragment = new QuoteFragment();
            }

            if(goToPage != null && position!=0){
                goToPage.setVisible(false);
            }
            if(goToPage != null && position == 0){
                goToPage.setVisible(true);
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
            if((globalPosition==SETTINGS || globalPosition==INFORMATION) && !drawerIsShow){
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
        currentTask.link(fragment, getApplicationContext());
        currentTask.setQuotes(quotes);
        currentTask.execute(adress);
    }

    @Override
    public void updateData(QuoteFragment fragment) {
        if (currentTask==null) return;
        currentTask.link(fragment, getApplicationContext());
    }

    @Override
    public void onPostExecute(QuoteFragment fragment,ArrayList<Quote> arrayList,int currentIndex) {
        if (currentTask==null) currentTask=new HtmlTask();
        currentTask.setQuotes(arrayList);
        currentTask.setCurrentIndex(currentIndex);
        currentTask.link(fragment,getApplicationContext());
        currentTask.onPostExecute(arrayList);
    }

    @Override
    public void shiftList(int position) {
        if (currentTask==null)return;
        currentTask.setShiftList(position);
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

    public  void shareQuote(String shareBody){
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
        sharingIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(Intent.createChooser(sharingIntent, getResources().getString(R.string.share_quote)));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        goToPage = menu.findItem(R.id.action_go_to_page);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == R.id.action_go_to_page){
            final Dialog dialog=new Dialog(this);
            dialog.setContentView(R.layout.dialog_go_to_page);
            RelativeLayout dialogLayout=(RelativeLayout)dialog.findViewById(R.id.dialog_layout);
            TextView goToPageLabel = (TextView)dialog.findViewById(R.id.page_label);
            goToPageLabel.setTextColor(Utility.getDefaultTextColor());
            dialogLayout.setBackgroundColor(Utility.getActivityBackgroundColor());
            MaterialRippleLayout rippleOk=(MaterialRippleLayout)dialog.findViewById(R.id.ripple_ok);
            final EditText inputPage= (EditText) dialog.findViewById(R.id.to_page);
            inputPage.setTextColor(Utility.getDefaultTextColor());
            rippleOk.setRippleColor(Utility.getDefaultTextColor());
            rippleOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int max = preferences.getInt(Utility.MAX_PAGE_LABEL, -1);
                    Fragment fragment = new QuoteFragment();
                    Bundle args = new Bundle();
                    int intValue = 0;
                    String value = inputPage.getText().toString();
                    try {
                        intValue = Integer.parseInt(value);
                    } catch (Exception e) {
                        value = String.valueOf(max);
                    }
                    if (intValue > max) {
                        value = String.valueOf(max);
                    }
                    if(intValue<1){
                        value = "1";
                    }
                    args.putInt(QuoteFragment.ARG_MENU_INDEX, globalPosition);
                    args.putString(QuoteFragment.HAS_ADRESS, QuoteFragment.adressArray[NEW] + "/" + String.valueOf(value));
                    args.putString(QuoteFragment.NEXT_LINK, QuoteFragment.adressArray[NEW] + "/" + String.valueOf(Integer.parseInt(value) - 1));
                    fragment.setArguments(args);
                    FragmentTransaction transaction = fragmentManager.beginTransaction();
                    transaction.replace(R.id.content_frame, fragment);
                    transaction.commit();
                    getSupportActionBar().setTitle(getResources().getStringArray(R.array.navigation_drawer_items)[globalPosition]);
                    dialog.cancel();
                }
            });
            dialog.show();
        }
        return super.onOptionsItemSelected(item);
    }
}
