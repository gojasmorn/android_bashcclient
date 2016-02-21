package gojas.ru.bashclient;


import android.app.Activity;
import android.app.Fragment;
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
    private final String NONE="none";
    boolean shiftList=false;
    View footer;


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
        }
        if (quotesArray!=null && quotesArray.length>0)quotes=quoteFromArray(quotesArray);
    }

    public QuoteFragment(){
        Log.d(MainActivity.TAG,"new QuoteFragment");
        fragment=this;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final int index=getArguments().getInt(ARG_MENU_INDEX,0);
        currentIndex=getArguments().getInt(ARG_MENU_INDEX, 0);
        TaskCallback.deleteTask(currentIndex);
        currentAdress=getArguments().getString(HAS_ADRESS, NONE);
        nextAdress=getArguments().getString(NEXT_LINK, NONE);
        positionQuote=getArguments().getInt(POSITION, 0);

        View rootView=inflater.inflate(R.layout.fragment_main,container,false);
        listView=(ListView)rootView.findViewById(R.id.quote_list);
        listView.setTag(this);
        emptyLayout=(RelativeLayout)rootView.findViewById(R.id.empty_layout);
        emptyLayout.setVisibility(View.GONE);
        database=QuotesDatabase.getInstance(getActivity());
        bar=(ProgressBar)rootView.findViewById(R.id.download_status);


        final Button buttonPage=(Button)rootView.findViewById(R.id.quote_page);
        buttonPage.setVisibility(View.GONE);
        buttonPage.setTextColor(Utility.getDefaultTextColor());
        buttonPage.setBackgroundResource(Utility.getPageResourse());
        final FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.quote_fab);
        fab.setVisibility(View.GONE);
        fab.hide(false);
        fab.setColorNormal(Utility.getRatingTextCheckedColor());
        fab.setColorPressed(Utility.getRatingTextCheckedColor());
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listView.smoothScrollBy(0, 0);
                listView.setSelectionFromTop(0, 0);
            }
        });
        footer=inflater.inflate(R.layout.footer, null, false);

        if(currentIndex!=MainActivity.BEST && currentIndex!=MainActivity.ABYSSTOP) {
            listView.addFooterView(footer);
        }
            listView.setOnScrollListener(new AbsListView.OnScrollListener() {
                int prevFirstVisible=-1;
                int oldTop;
                @Override
                public void onScrollStateChanged(AbsListView view, int scrollState) {

                    if((currentIndex==MainActivity.NEW || currentIndex==MainActivity.BYRATING) && scrollState!=0){
                        //buttonPage.setVisibility(View.VISIBLE);
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
                        if (Utility.isNetworkAvailable(getActivity())) {
                            //new HtmlTask1().execute(getAdress());
                            TaskCallback.nulledTask();
                            TaskCallback.execute(getAdress(),fragment,loaded,refresh,currentIndex,quotes);
                        }else{
                            showToast();
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

        mSwipeRefreshLayout = (SwipeRefreshLayout)rootView.findViewById(R.id.quote_refresh);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSwipeRefreshLayout.setRefreshing(true);
                String adress = adressArray[index];
                refresh = true;
                if (Utility.isNetworkAvailable(getActivity())) {
                    if (canShowNewData(currentIndex)) adress += nextRefreshLink;
                    //new HtmlTask1().execute(adress);
                    TaskCallback.nulledTask();
                    TaskCallback.execute(adress,fragment,loaded,refresh,currentIndex,quotes);
                } else {
                    showToast();
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
        if(!Utility.isNetworkAvailable(getActivity()))showToast();


        return rootView;
    }



    private void showToast() {
        Toast toast = Toast.makeText(getActivity(),
                getActivity().getResources().getString(R.string.toast_text), Toast.LENGTH_SHORT);
        toast.show();
        bar.setVisibility(View.GONE);
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




   /*
    class HtmlTask extends AsyncTask<String,Void,ArrayList<Quote>> {
        String adress="";

        @Override
        protected ArrayList<Quote> doInBackground(String... params) {
            Document doc=null;
            Element body;
            if(!loaded||refresh) {
                quotes = new ArrayList<>();
            }
            try{
                adress=params[0];
                doc= Jsoup.connect(adress).get();


            }catch (Exception e){

            }

            if (doc!=null){
                body=doc.select("#body").first();
                Iterator<Element> iterator=body.select(".quote").iterator();

                int i=0;
                while(iterator.hasNext()){
                    Element current=iterator.next();
                    String text = current.select(".text").toString();
                    if(!text.equals("")) {
                        Cursor vote=null;
                        String date;
                        if(Quote.isAbyssTop(currentIndex)){
                            date=current.select(".abysstop-date").text();
                        }else{
                            date = current.select(".date").text();
                        }
                        String rating=current.select(".rating").text();
                        String id=current.select(".id").text();
                        id = id.replace("#","");
                        Quote quote=new Quote();
                        quote.setPosition(i);
                        quote.setMenuIndex(currentIndex);
                        quote.setText(Quote.parseHtml(text.toString()));
                        if(currentIndex==MainActivity.NEW || currentIndex==MainActivity.BYRATING)
                        {
                            String currentLink=getCurrentLink(currentIndex, body);
                            quote.setAdress(currentLink);
                            quote.hasPageLink(true);
                        }
                        quote.setNextLink(getNextLink(currentIndex, body));
                        if(Quote.hasRating(currentIndex)){
                            quote.setRating(rating);
                        }
                        quote.setDate(date);
                        if(Quote.hasRating(currentIndex)) {
                            quote.setId(Integer.valueOf(id));
                            vote=database.getVoteByQuoteId(Integer.valueOf(id));
                            if(vote!=null && vote.getCount()!=0){
                                quote.setVote(vote.getInt(vote.getColumnIndex("vote")));
                            }else{
                                quote.setVote(Quote.VOTE_NOTHING);
                            }

                        }
                        if(vote!=null)vote.close();
                        i++;
                        quotes.add(quote);
                    }
                }
                updateNextLink(quotes.get(quotes.size()-1));
            }
            return quotes;
        }

        @Override
        protected void onPostExecute(ArrayList<Quote> arrayList) {
            super.onPostExecute(arrayList);
            if(stopped)return;
            emptyLayout.setVisibility(View.GONE);
            if(!loaded || refresh) {
                quoteAdapter = new QuoteAdapter(getActivity(), arrayList, currentIndex);
                listView.setAdapter(quoteAdapter);
            }
            mSwipeRefreshLayout.setRefreshing(false);
            quoteAdapter.notifyDataSetChanged();
            if(shiftList)listView.setSelectionFromTop(positionQuote,0);
            shiftList=false;
            bar.setVisibility(View.GONE);
            loaded=true;
            refresh=false;
        }
    }
    */





    public void updateNextLink(Quote quote) {
        nextRefreshLink=quote.getNextLink();
    }



    private boolean canShowNewData(int index){
        boolean result;

        switch (index){
            case 1:
                result=true;
                break;
            case 4:
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
                Log.d(MainActivity.TAG,"exception "+e.toString());
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
            Log.d(MainActivity.TAG, "quotes!=null && quotes.size()>0");
            //outState.putSerializable(QUOTES_VALUE, quotes);
            outState.putStringArray(QUOTES_VALUE,quoteToArray(quotes));
        }
        outState.putBoolean(REFRESH_VALUE,refresh);
        outState.putBoolean(LOADED_VALUE, loaded);
        int index=listView.getFirstVisiblePosition();
        View view=listView.getChildAt(0);
        int top = (view == null) ? 0 : view.getTop();
        outState.putInt(LIST_INDEX, index);
        outState.putInt(LIST_TOP, top);
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
            Log.d(MainActivity.TAG,"json "+json);
            CacheElement cacheElement=CacheElement.getFromJson(json);
            quotes=cacheElement.getQuotes();
            Log.d(MainActivity.TAG,"quotes==null?"+String.valueOf(quotes==null));
            //new HtmlTask1().onPostExecute(quotes);
            TaskCallback.nulledTask();
            TaskCallback.onPostExecute(fragment,quotes);
            updateNextLink(quotes.get(quotes.size() - 1));
            listView.setSelectionFromTop(cacheElement.getPosition(), cacheElement.getMargin());

        }
        if(currentAdress.equals(NONE) && cursor.getCount()==0 && Utility.isNetworkAvailable(getActivity())) {
            //new HtmlTask1().execute(getAdress());
            TaskCallback.nulledTask();
            TaskCallback.execute(getAdress(),fragment,loaded,refresh,currentIndex,quotes);
        }

        if(!currentAdress.equals(NONE) && Utility.isNetworkAvailable(getActivity())) {
            //new HtmlTask1().execute(currentAdress);
            TaskCallback.nulledTask();
            TaskCallback.execute(currentAdress, fragment, loaded, refresh, currentIndex, quotes);
            nextRefreshLink=nextAdress;
            listView.setSelectionFromTop(positionQuote,0);
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
}
