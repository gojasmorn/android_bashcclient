package gojas.ru.bashclient;


import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.melnykov.fab.FloatingActionButton;
import java.util.ArrayList;


/**
 * Created by gojas on 06.07.2015.
 */
public class QuoteFragment extends Fragment {
    private static final String END_STATE ="isEnd";
    ListView listView;
    SwipeRefreshLayout mSwipeRefreshLayout;
    private int currentIndex=0;
    public static final String ARG_MENU_INDEX = "index";
    public static final String HAS_ADRESS="has_adress";
    public static final String POSITION="position";
    public static final String NEXT_LINK="next_link";
    public static final String QUOTES_VALUE="quotes";
    public static final String REFRESH_VALUE="refresh";
    public static final String LOADED_VALUE="loaded";
    public static final String LIST_INDEX="listIndex";
    public static final String LIST_TOP="listTop";
    public static final String TOAST_MODE="toast_mode";
    public static final String NONE="none";
    boolean showPage=false;
    private boolean isEnd=false;
    View footer;
    private boolean toastShowed=false;


    private String nextRefreshLink="";
    ArrayList<Quote> quotes=null;
    boolean loaded=false;
    private boolean refresh=false;
    QuotesDatabase database;
    ProgressBar bar;
    volatile boolean stopped=false;
    RelativeLayout emptyLayout;
    String currentAdress=NONE;
    String nextAdress=NONE;
    int positionQuote;
    MainActivity TaskCallback;
    QuoteFragment fragment;
    int listIndex;
    int listTop;

    QuoteAdapter quoteAdapter;
    public static final String[] adressArray={
            "http://bash.im/index",
            "http://bash.im/random",
            "http://bash.im/best",
            "http://bash.im/byrating",
            "http://bash.im/abyss",
            "http://bash.im/abysstop",
            "http://bash.im/abyssbest"

    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setRetainInstance(true);
        String[] quotesArray=null;
        if (savedInstanceState != null){
            loaded = savedInstanceState.getBoolean(LOADED_VALUE, false);
            refresh = savedInstanceState.getBoolean(REFRESH_VALUE, false);
            quotesArray=savedInstanceState.getStringArray(QUOTES_VALUE);
            listIndex=savedInstanceState.getInt(LIST_INDEX);
            listTop=savedInstanceState.getInt(LIST_TOP);
            toastShowed=savedInstanceState.getBoolean(TOAST_MODE, false);
            isEnd=savedInstanceState.getBoolean(END_STATE, false);
        }
        if (quotesArray!=null && quotesArray.length>0)quotes=quoteFromArray(quotesArray);
    }

    public QuoteFragment(){
        //Log.d(MainActivity.TAG, "new QuoteFragment");
        fragment=this;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView=inflater.inflate(R.layout.fragment_main,container,false);
        initArguments(getArguments());
        initFragmentView(rootView, inflater);
        TaskCallback.deleteTask(currentIndex);
        database=QuotesDatabase.getInstance(getActivity());


        final Button buttonPage=(Button)rootView.findViewById(R.id.quote_page);
        final FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.quote_fab);

        setPageSettings(buttonPage);
        setFabsettings(fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listView.smoothScrollBy(0, 0);
                listView.setSelectionFromTop(0, 0);
                fab.hide();
            }
        });

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
                int prevFirstVisible=-1;
                int oldTop;
                @Override
                public void onScrollStateChanged(AbsListView view, int scrollState) {

                    if(showPage && (currentIndex==MainActivity.NEW || currentIndex==MainActivity.BYRATING) && scrollState!=0){
                        buttonPage.setVisibility(View.VISIBLE);
                    }else{
                        buttonPage.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                    View firstView = view.getChildAt(0);
                    int top = (firstView == null) ? 0 : view.getTop();

                    if (firstVisibleItem == prevFirstVisible) {
                        if (top > oldTop) {
                            fab.show();
                        } else if (top < oldTop) {
                            fab.hide();
                        }
                    } else {
                        if (firstVisibleItem < prevFirstVisible) {
                            fab.show();
                        } else {
                            fab.hide();
                        }
                    }

                    oldTop = top;
                    prevFirstVisible = firstVisibleItem;
                    prevFirstVisible=firstVisibleItem;


                    boolean loadMore = firstVisibleItem + visibleItemCount >= totalItemCount;

                    if (currentIndex!=MainActivity.BEST && currentIndex!=MainActivity.ABYSSTOP && loaded && loadMore) {
                        if (Utility.isNetworkAvailable(getActivity())&& !isEnd) {
                            //new HtmlTask1().execute(getAdress());
                            TaskCallback.nulledTask();
                            TaskCallback.execute(getAdress(),fragment,loaded,refresh,currentIndex,quotes);
                        }else{
                            showToast(isEnd);
                        }

                    }

                    if(loaded && firstVisibleItem<quotes.size() && (currentIndex==MainActivity.NEW || currentIndex==MainActivity.BYRATING )){
                        Quote quote=quotes.get(firstVisibleItem);
                        String s=quote.getAdress();
                        s=s.replace(adressArray[MainActivity.NEW]+"/","");
                        s=s.replace(adressArray[MainActivity.BYRATING]+"/","");
                        buttonPage.setText(s);
                    }
                }
            });


        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSwipeRefreshLayout.setRefreshing(true);
                String adress = adressArray[currentIndex];
                refresh = true;
                if (Utility.isNetworkAvailable(getActivity())&&!isEnd) {
                    if (canShowNewData(currentIndex)) adress += nextRefreshLink;
                    //new HtmlTask1().execute(adress);
                    TaskCallback.nulledTask();
                    TaskCallback.execute(adress,fragment,loaded,refresh,currentIndex,quotes);
                } else {
                    showToast(isEnd);
                }
            }
        });

        if(quotes!=null && quotes.size()>0){
            bar.setVisibility(View.GONE);
            quoteAdapter=new QuoteAdapter(getActivity(),quotes,currentIndex);
            listView.setAdapter(quoteAdapter);
            listView.setSelectionFromTop(listIndex, listTop);
        }else{
            getNewData();
        }
        if(!toastShowed && !Utility.isNetworkAvailable(getActivity()))
        {
            showToast(isEnd);
            toastShowed=true;
        }


        return rootView;
    }

    private void initFragmentView(View rootView,LayoutInflater inflater) {
        listView=(ListView)rootView.findViewById(R.id.quote_list);

        emptyLayout=(RelativeLayout)rootView.findViewById(R.id.empty_layout);
        bar=(ProgressBar)rootView.findViewById(R.id.download_status);
        footer=inflater.inflate(R.layout.footer, null, false);
        mSwipeRefreshLayout = (SwipeRefreshLayout)rootView.findViewById(R.id.quote_refresh);
        listView.setTag(this);
        emptyLayout.setVisibility(View.GONE);
        if(!isEnd && Utility.isNetworkAvailable(getActivity()) && currentIndex!=MainActivity.BEST && currentIndex!=MainActivity.ABYSSTOP) {
            listView.addFooterView(footer);
        }
    }

    private void initArguments(Bundle arguments) {
        currentIndex=arguments.getInt(ARG_MENU_INDEX, 0);
        currentAdress=arguments.getString(HAS_ADRESS, NONE);
        nextAdress=arguments.getString(NEXT_LINK, NONE);
        positionQuote=arguments.getInt(POSITION, 0);
        //Log.d(MainActivity.TAG,"menuIndex fragment "+currentIndex);
    }


    public void showToast( boolean isEnd) {
        String message;
        if (!isEnd){
            message=getActivity().getResources().getString(R.string.toast_text);
        }else{
            message=getActivity().getResources().getString(R.string.end_text);
        }
        Toast toast = Toast.makeText(getActivity(),message, Toast.LENGTH_SHORT);
        toast.show();
        bar.setVisibility(View.GONE);
        //Log.d(MainActivity.TAG,"mSwipeRefreshLayout null ? "+String.valueOf(mSwipeRefreshLayout==null));
        mSwipeRefreshLayout.setRefreshing(false);
        if(quotes==null || quotes.size()==0){
            emptyLayout.setVisibility(View.VISIBLE);
            TextView emptyText=(TextView)emptyLayout.findViewById(R.id.empty_text);
            emptyText.setTextColor(Utility.getDefaultTextColor());
            ImageView emptyImage=(ImageView)emptyLayout.findViewById(R.id.empty_image);
            emptyImage.setImageDrawable(Utility.getEmptyImage());
        }
    }

    private String getAdress(){
        return adressArray[currentIndex]+nextRefreshLink;
    }

    public void updateNextLink(Quote quote) {
        nextRefreshLink=quote.getNextLink();
    }

    private boolean canShowNewData(int index){
        boolean result;

        switch (index){
            case MainActivity.RANDOM:
                result=true;
                break;
            case MainActivity.ABYSS:
                result=true;
                break;
            default:
                result=false;
                break;
        }

        return result;
    }

    @Override
    public void onStop() {
        super.onStop();
        stopped=true;
        if (quotes!=null && quotes.size()>0){

            CacheElement cacheElement=new CacheElement();
            cacheElement.setQuotes(quotes);
            int position=0;
            int margin=0;
            if(listView!=null) {
                position=listView.getFirstVisiblePosition();
                if(listView.getChildAt(0)!=null)margin=listView.getChildAt(0).getTop();
            }
            cacheElement.setPosition(position);
            cacheElement.setMargin(margin);
            try {
                String json=cacheElement.toJSON();
                database.deleteCacheByType(currentIndex);
                database.addCache(currentIndex, json);
            }catch (Exception e){
                //Log.d(MainActivity.TAG,"exception "+e.toString());
            }
        }
    }

    public RelativeLayout getEmptyLayout() {
        return emptyLayout;
    }

    public ListView getListView() {
        return listView;
    }

    public SwipeRefreshLayout getSwipeRefreshLayout() {
        return mSwipeRefreshLayout;
    }

    public ProgressBar getBar() {
        return bar;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        TaskCallback=(MainActivity)activity;
        TaskCallback.updateData(this);
    }

    public boolean isLoaded() {
        return loaded;
    }

    public boolean isRefresh() {
        return refresh;
    }

    public void setLoaded(boolean loaded) {
        this.loaded = loaded;
    }

    public void setRefresh(boolean refresh) {
        this.refresh = refresh;
    }

    public void setQuotes(ArrayList<Quote> quotes) {
        this.quotes = quotes;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if(quotes!=null && quotes.size()>0){
            //Log.d(MainActivity.TAG, "quotes!=null && quotes.size()>0");
            //outState.putSerializable(QUOTES_VALUE, quotes);
            outState.putStringArray(QUOTES_VALUE,quoteToArray(quotes));
        }
        outState.putBoolean(REFRESH_VALUE,refresh);
        outState.putBoolean(END_STATE,isEnd);
        outState.putBoolean(LOADED_VALUE, loaded);
        int index=listView.getFirstVisiblePosition();
        View view=listView.getChildAt(0);
        int top = (view == null) ? 0 : view.getTop();
        outState.putInt(LIST_INDEX, index);
        outState.putInt(LIST_TOP, top);
        outState.putBoolean(TOAST_MODE,toastShowed);
    }

    public void setQuoteAdapter(QuoteAdapter quoteAdapter) {
        this.quoteAdapter = quoteAdapter;
    }


    public QuoteAdapter getQuoteAdapter() {
        return quoteAdapter;
    }

    private void getNewData(){
        //***LOAD QUOTES
        Cursor cursor=database.getCacheByType(currentIndex);
        if(currentAdress.equals(NONE) && cursor.getCount()>0){
            String json=cursor.getString(cursor.getColumnIndex("cache"));
            //Log.d(MainActivity.TAG,"json "+json);
            CacheElement cacheElement=CacheElement.getFromJson(json);
            quotes=cacheElement.getQuotes();
            //Log.d(MainActivity.TAG, "quotes==null?" + String.valueOf(quotes == null));
            //new HtmlTask1().onPostExecute(quotes);
            TaskCallback.nulledTask();
            TaskCallback.onPostExecute(fragment, quotes,currentIndex);
            updateNextLink(quotes.get(quotes.size() - 1));

            listView.setSelectionFromTop(cacheElement.getPosition(), cacheElement.getMargin());

        }
        if(currentAdress.equals(NONE) && cursor.getCount()==0 && Utility.isNetworkAvailable(getActivity())&&!isEnd) {
            //new HtmlTask1().execute(getAdress());
            TaskCallback.nulledTask();
            TaskCallback.execute(getAdress(), fragment, loaded, refresh, currentIndex, quotes);
        }
        if (nextRefreshLink.equals(NONE))isEnd=true;
        if(!currentAdress.equals(NONE) && Utility.isNetworkAvailable(getActivity())&&!isEnd) {
            //new HtmlTask1().execute(currentAdress);
            TaskCallback.nulledTask();
            TaskCallback.execute(currentAdress, fragment, loaded, refresh, currentIndex, quotes);
            TaskCallback.shiftList(positionQuote);

            nextRefreshLink=nextAdress;

            //listView.setSelectionFromTop(positionQuote,0);
        }
        ///*LOAD QUOTES*///
        if (cursor!=null)cursor.close();
    }

    private String[] quoteToArray(ArrayList<Quote> quotes){
        int size=quotes.size();
        String[] quotesArray=new String[size];
        for (int i=0;i<size;i++){
            quotesArray[i]=quotes.get(i).toJSON();
        }
        return quotesArray;
    }

    private ArrayList<Quote> quoteFromArray(String[] quotesArray){
        ArrayList<Quote> quotes=new ArrayList<>();
        for (int i=0;i<quotesArray.length;i++){
            quotes.add(Quote.fromJSON(quotesArray[i]));
        }
        return quotes;
    }

    public void setPageSettings(Button buttonPage) {
        SharedPreferences preferences=getActivity().getPreferences(Context.MODE_PRIVATE);
        String pageValue=preferences.getString(Utility.PAGE_MODE, Utility.MODE_OFF);

        if(pageValue.equals(Utility.MODE_ON)){
            showPage=true;
            //buttonPage.setVisibility(View.VISIBLE);
        }else{
            showPage=false;
            buttonPage.setVisibility(View.GONE);
        }
        buttonPage.setTextColor(Utility.getDefaultTextColor());
        buttonPage.setBackgroundResource(Utility.getPageResourse());
    }

    public void setFabsettings(FloatingActionButton fab) {
        SharedPreferences preferences=getActivity().getPreferences(Context.MODE_PRIVATE);
        String upValue=preferences.getString(Utility.FAB_MODE, Utility.MODE_OFF);
        if(upValue.equals(Utility.MODE_ON)){
            fab.setVisibility(View.VISIBLE);
        }else{
            fab.setVisibility(View.GONE);
        }
        fab.hide(false);
        fab.setColorNormal(Utility.getRatingTextCheckedColor());
        fab.setColorPressed(Utility.getRatingTextCheckedColor());
    }

    public void setIsEnd(boolean isEnd) {
        this.isEnd = isEnd;
    }

    public boolean isEnd() {
        return isEnd;
    }
}
