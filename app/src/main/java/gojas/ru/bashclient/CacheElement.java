package gojas.ru.bashclient;

import com.google.gson.Gson;

import java.util.ArrayList;

/**
 * Created by gojas on 25.07.2015.
 */
public class CacheElement {
    int position;
    int margin;
    ArrayList<Quote> quotes;

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getMargin() {
        return margin;
    }

    public void setMargin(int margin) {
        this.margin = margin;
    }

    public ArrayList<Quote> getQuotes() {
        return quotes;
    }

    public void setQuotes(ArrayList<Quote> quotes) {
        this.quotes = quotes;
    }

    public String toJSON(){
        Gson gson=new Gson();
        return gson.toJson(this);
    }

    public static CacheElement getFromJson(String json){
        CacheElement cacheElement=new Gson().fromJson(json,CacheElement.class);
        return cacheElement;
    }
}
