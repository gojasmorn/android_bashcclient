package gojas.ru.bashclient;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.HeaderViewListAdapter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by gojas on 05.08.2015.
 */
public class HtmlTask extends AsyncTask<String,Void,ArrayList<Quote>> {
    QuoteFragment fragment;
    Context context;
    ArrayList<Quote> quotes;

    boolean loaded;
    boolean refresh;
    int currentIndex;
    QuotesDatabase database;
    Document doc=null;
    Element body;
    String adress="";

    interface TaskInterface{
        void execute(String adress,QuoteFragment fragment,boolean loaded, boolean refresh,int currentIndex,ArrayList<Quote> quotes);
        void updateData(QuoteFragment fragment);
        void onPostExecute(QuoteFragment fragment,ArrayList<Quote> arrayList);
        void deleteTask(int index);
        void nulledTask();
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();

    }

    public void link(QuoteFragment f,Context c){
        fragment=f;
        context=c;
        database=QuotesDatabase.getInstance(context);
        Log.d(MainActivity.TAG,"loaded= "+loaded);
        Log.d(MainActivity.TAG,"refresh= "+refresh);
        loaded=f.isLoaded();
        refresh=f.isRefresh();
    }
    public void unLink(){
        fragment=null;
        context=null;
    }


    public void setCurrentIndex(int currentIndex) {
        this.currentIndex = currentIndex;
    }

    @Override
    protected ArrayList<Quote> doInBackground(String... params) {


        Log.d(MainActivity.TAG,"loaded ="+loaded);
        Log.d(MainActivity.TAG,"refresh= "+refresh);
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
                    Log.d(MainActivity.TAG,"task is running");
                }
            }
        }
        return quotes;
    }

    @Override
    protected void onPostExecute(ArrayList<Quote> arrayList) {
        super.onPostExecute(arrayList);
        if(fragment!=null) {
            fragment.updateNextLink(quotes.get(quotes.size()-1));
            fragment.getEmptyLayout().setVisibility(View.GONE);
            QuoteAdapter quoteAdapter=null;
            if (!loaded || refresh) {
                quoteAdapter = new QuoteAdapter(context, arrayList, currentIndex);
                fragment.listView.setAdapter(quoteAdapter);
                fragment.setQuoteAdapter(quoteAdapter);
            }
            if(refresh){
                Log.d(MainActivity.TAG,"refresh");
                QuoteAdapter adapter=fragment.getQuoteAdapter();
                if (adapter!=null){
                    for (Quote quote:quotes){
                        Log.d(MainActivity.TAG,"quote text "+quote.getText());
                    }
                }
            }
            fragment.getSwipeRefreshLayout().setRefreshing(false);
            fragment.getQuoteAdapter().notifyDataSetChanged();
            fragment.getBar().setVisibility(View.GONE);
            //loaded = true;
            //refresh = false;
            fragment.setLoaded(true);
            fragment.setRefresh(false);
            fragment.setQuotes(quotes);
        }
    }
    private String getNextLink(int currentIndex, Element body) {
        Element next;
        int index;
        String nextLink="";
        switch (currentIndex){
            case MainActivity.NEW:
                index=Integer.valueOf(body.select("span.current").first().select("input.page").attr("value"))-1;
                nextLink="/"+String.valueOf(index);
                break;
            case MainActivity.RANDOM:
                next=body.select("a.button").first();
                nextLink=next.attr("href");
                nextLink=nextLink.replace("/random","");
                nextLink=nextLink.replace("/abyss","");
                break;
            case MainActivity.BEST:
                break;
            case MainActivity.BYRATING:
                index=Integer.valueOf(body.select("span.current").first().select("input.page").attr("value"))+1;
                nextLink="/"+String.valueOf(index);
                break;
            case MainActivity.ABYSS:
                next=body.select("a.button").first();
                nextLink=next.attr("href");
                nextLink=nextLink.replace("/random","");
                nextLink=nextLink.replace("/abyss","");
                break;
            case MainActivity.ABYSSTOP:
                break;
            case MainActivity.ABYSSBEST:
                nextLink=body.select(".pager").first().select("a").first().attr("href");
                nextLink=nextLink.replace("/abyssbest","");
                break;
        }
        return nextLink;
    }
    private String getCurrentLink(int currentIndex, Element body) {
        Element next;
        int index;
        String currentLink="";
        switch (currentIndex){
            case MainActivity.NEW:
                index=Integer.valueOf(body.select("span.current").first().select("input.page").attr("value"));
                currentLink="/"+String.valueOf(index);
                break;
            case MainActivity.RANDOM:
                next=body.select("a.button").first();
                currentLink=next.attr("href");
                currentLink=currentLink.replace("/random","");
                currentLink=currentLink.replace("/abyss","");
                break;
            case MainActivity.BEST:
                break;
            case MainActivity.BYRATING:
                index=Integer.valueOf(body.select("span.current").first().select("input.page").attr("value"));
                currentLink="/"+String.valueOf(index);
                break;
            case MainActivity.ABYSS:
                next=body.select("a.button").first();
                currentLink=next.attr("href");
                currentLink=currentLink.replace("/random","");
                currentLink=currentLink.replace("/abyss","");
                break;
            case MainActivity.ABYSSTOP:
                break;
            case MainActivity.ABYSSBEST:
                currentLink=body.select(".pager").first().select("a").first().attr("href");
                currentLink=currentLink.replace("/abyssbest","");
                break;
        }
        return QuoteFragment.adressArray[currentIndex]+currentLink;
    }

    public int getCurrentIndex() {
        return currentIndex;
    }

    public void setQuotes(ArrayList<Quote> quotes) {
        //if (quotes==null)quotes=new ArrayList<Quote>();
        this.quotes=quotes;
    }
}
