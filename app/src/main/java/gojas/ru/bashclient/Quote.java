package gojas.ru.bashclient;

import android.text.Spanned;
import android.util.Log;


import com.google.gson.Gson;

import org.apache.commons.lang3.StringEscapeUtils;

import java.util.Random;

/**
 * Created by gojas on 17.06.2015.
 */
public class Quote {
    private int  id;
    private int menuIndex;
    private int position;
    private String rating;
    private String date;
    private String adress;
    private String nextLink;
    private String text;
    private int vote=VOTE_NOTHING;
    private boolean hasPageLink=false;

    public static final int VOTE_NOTHING=3;
    public static final int VOTE_PLUS=0;
    public static final int VOTE_MINUS=1;
    public static final int VOTE_BOJAN=2;
    public static String QUOTE_ADRESS_BODY="http://bash.im/quote/";






    public static boolean hasRating(int index){
        if(index==MainActivity.ABYSSBEST || index==MainActivity.ABYSSTOP){
            return false;
        }else{
            return true;
        }
    }

    public String getText() {
        return text;
    }

    public int getId() {
        return id;
    }

    public String getRating() {
        return rating;
    }

    public String getDate() {
        return date;
    }

    public String getAdress(){
        return adress;
    }

    public int getVote() {
        return vote;
    }

    public String getNextLink() {
        return nextLink;
    }

    public String getQuoteAdress(){
        return QUOTE_ADRESS_BODY+String.valueOf(id);
    }

    public int getMenuIndex() {
        return menuIndex;
    }

    public int getPosition() {
        return position;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setAdress(String adress){
        this.adress=adress;
    }

    public void setVote(int vote) {
        this.vote = vote;
    }

    public void setNextLink(String nextLink) {
        this.nextLink = nextLink;
    }

    public void setMenuIndex(int menuIndex) {
        this.menuIndex = menuIndex;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public static boolean isAbyssTop(int currentIndex) {
        if(currentIndex==5)return true;
        return false;
    }

    public static String parseHtml(String source){
        source=source.replace("<div class=\"text\">","");
        source=source.replace("</div>","");
        source=source.replace("\n", "");
        source=source.replace("<br>","\n");
        source= StringEscapeUtils.unescapeHtml4(source);
        source=source.substring(1);
        return source;
    }

    public static boolean isNumeric(String rating){
        try
        {
            Integer.parseInt(rating);
        }
        catch(NumberFormatException nfe)
        {
            return false;
        }
        return true;
    }

    public void hasPageLink(boolean value){
        this.hasPageLink=value;
    }

    public boolean hasPageLink(){return hasPageLink;}

    public String toJSON(){
        Gson gson=new Gson();
        return gson.toJson(this);
    }

    public static Quote fromJSON(String json){
        Quote quote=new Gson().fromJson(json,Quote.class);
        return quote;
    }

}
