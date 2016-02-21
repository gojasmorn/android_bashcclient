package gojas.ru.bashclient;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.ArrayList;

/**
 * Created by gojas on 26.06.2015.
 */
public class QuoteAdapter extends BaseAdapter {
    private int DELTA_UP=1;
    private int DELTA_DOWN=-1;
    private ArrayList<Quote> quotes;
    private LayoutInflater inflater;
    private int menuIndex;
    private QuotesDatabase database;
    Context context;
    public QuoteAdapter(Context context,ArrayList<Quote> quotes,int menuIndex){
        this.menuIndex=menuIndex;
        this.quotes=quotes;
        this.context=context;
        inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        database=QuotesDatabase.getInstance(context);
        //database.clearVotes();

    }
    @Override
    public int getCount() {
        return quotes.size();
    }

    @Override
    public Object getItem(int position) {
        return quotes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View item=convertView;
        if(item==null){
            item=inflater.inflate(R.layout.list_item,parent,false);
        }
        final Quote quote=quotes.get(position);

        CardView cardView=(CardView)item.findViewById(R.id.quote_card);
        cardView.setCardBackgroundColor(Utility.getDefaultItemBackgroundColor());
        TextView quoteText=((TextView) item.findViewById(R.id.quote_text));
        quoteText.setText(quote.getText());
        quoteText.setTextSize(TypedValue.COMPLEX_UNIT_SP, Utility.getTextSize());
        quoteText.setTextColor(Utility.getDefaultTextColor());
        TextView dateText=((TextView) item.findViewById(R.id.quote_date));
        dateText.setText(quote.getDate());
        dateText.setTextColor(Utility.getDefaultTextColor());
        ImageView shareQuote=(ImageView)item.findViewById(R.id.quote_share);
        shareQuote.setImageResource(Utility.getShareResourse());
        shareQuote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareBody = quote.getText();
                if (Quote.hasRating(menuIndex))
                    shareBody = shareBody + "\n" + quote.getQuoteAdress();
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                //sharingIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                Utility.getContext().startActivity(Intent.createChooser(sharingIntent, context.getResources().getString(R.string.share_quote)));

            }
        });
        TextView quoteId=(TextView)item.findViewById(R.id.quote_id);
        ToggleButton bookmarkToggle=(ToggleButton)item.findViewById(R.id.quote_bookmark);
        if(Quote.hasRating(menuIndex)) {
            quoteId.setVisibility(View.VISIBLE);
            quoteId.setTextColor(Utility.getDefaultTextColor());
            quoteId.setText("# " + String.valueOf(quote.getId()));
            bookmarkToggle.setBackgroundResource(Utility.getDrawableBookmarksId());
            bookmarkToggle.setVisibility(View.VISIBLE);
            bookmarkToggle.setTag(quote);

            Cursor cursor = database.getVoteByQuoteId(quote.getId());
            Cursor bookmark=database.getBookMark(quote.getId());
            TextView rating=(TextView) item.findViewById(R.id.quote_rating);
            rating.setText(quote.getRating());
            rating.setTextColor(Utility.getRatingTextUnCheckedColor());

            ToggleButton buttonPlus = Utility.getButtonPlus(item);
            buttonPlus.setTag(quote);
            buttonPlus.setTag(R.id.item, item);
            buttonPlus.setOnCheckedChangeListener(plusChecked);

            ToggleButton buttonMinus = Utility.getButtonMinus(item);
            buttonMinus.setTag(quote);
            buttonMinus.setOnCheckedChangeListener(minusChecked);
            buttonMinus.setTag(R.id.item, item);

            ToggleButton buttonBojan = Utility.getButtonBojan(item);
            buttonBojan.setTag(quote);
            buttonBojan.setOnCheckedChangeListener(bojanChecked);
            buttonBojan.setTag(R.id.item, item);

            buttonPlus.setChecked(false);
            buttonMinus.setChecked(false);
            buttonBojan.setChecked(false);

            if(cursor.getCount()>0){
                rating.setTextColor(Utility.getRatingTextCheckedColor());
                switch (cursor.getInt(cursor.getColumnIndex("vote"))){
                    case Quote.VOTE_PLUS:
                        buttonPlus.setChecked(true);
                        break;
                    case Quote.VOTE_MINUS:
                        buttonMinus.setChecked(true);
                        break;
                    case Quote.VOTE_BOJAN:
                        buttonBojan.setChecked(true);
                        break;
                }
            }

            bookmarkToggle.setChecked(false);
            if(bookmark.getCount()>0)bookmarkToggle.setChecked(true);
            bookmarkToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    Quote quoteMark=(Quote)buttonView.getTag();
                    database.deleteBookMark(quoteMark.getId());
                    if(isChecked)database.addBookMark(quoteMark);
                }
            });
            if(cursor!=null)cursor.close();
            if(bookmark!=null)bookmark.close();
        }else {
            quoteId.setVisibility(View.GONE);
            bookmarkToggle.setVisibility(View.GONE);
            switchOffRating(item);
        }


        return item;
    }

    private void switchOffRating(View view){
        view.findViewById(R.id.button_plus).setVisibility(View.GONE);
        view.findViewById(R.id.button_minus).setVisibility(View.GONE);
        view.findViewById(R.id.button_bojan).setVisibility(View.GONE);
        view.findViewById(R.id.button_plus_night).setVisibility(View.GONE);
        view.findViewById(R.id.button_minus_night).setVisibility(View.GONE);
        view.findViewById(R.id.button_bojan_night).setVisibility(View.GONE);
        view.findViewById(R.id.quote_rating).setVisibility(View.GONE);

    }

    private void setVote(int quoteId,int voteId){
        WebView webView=new WebView(context);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(Quote.QUOTE_ADRESS_BODY + String.valueOf(quoteId));
        webView.loadUrl("javascript:v('" + String.valueOf(quoteId) + "'," + String.valueOf(voteId) + ",0);return false;");
    }



    CompoundButton.OnCheckedChangeListener plusChecked=new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            Quote quote=(Quote)buttonView.getTag();
            View view=(View)buttonView.getTag(R.id.item);
            TextView ratingText=(TextView)view.findViewById(R.id.quote_rating);

            database.deleteVoteByQuoteId(quote.getId());
            int delta;
            if(isChecked){
                delta=1;
                ToggleButton buttonMinus=Utility.getButtonMinus(view);
                buttonMinus.setChecked(false);
                ToggleButton buttonBojan=Utility.getButtonBojan(view);
                buttonBojan.setChecked(false);
                database.addVote(String.valueOf(quote.getId()), Quote.VOTE_PLUS);
                ratingText.setTextColor(Utility.getRatingTextCheckedColor());
            }else{
                delta=-1;
                ratingText.setTextColor(Utility.getRatingTextUnCheckedColor());
            }
            if(Quote.isNumeric(quote.getRating())){
                int ratingInt=Integer.valueOf(quote.getRating());
                ratingInt=ratingInt+delta;
                ratingText.setText(String.valueOf(ratingInt));
                quote.setRating(String.valueOf(ratingInt));
            }
            setVote(quote.getId(),Quote.VOTE_PLUS);
        }
    };

    CompoundButton.OnCheckedChangeListener minusChecked=new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            Quote quote=(Quote)buttonView.getTag();
            View view=(View)buttonView.getTag(R.id.item);
            TextView ratingText=(TextView)view.findViewById(R.id.quote_rating);

            database.deleteVoteByQuoteId(quote.getId());
            int delta;
            if(isChecked){
                delta=-1;
                ToggleButton buttonPlus=Utility.getButtonPlus(view);
                buttonPlus.setChecked(false);
                ToggleButton buttonBojan=Utility.getButtonBojan(view);
                buttonBojan.setChecked(false);
                database.addVote(String.valueOf(quote.getId()), Quote.VOTE_MINUS);
                ratingText.setTextColor(Utility.getRatingTextCheckedColor());
            }else{
                delta=1;
                ratingText.setTextColor(Utility.getRatingTextUnCheckedColor());
            }
            if(Quote.isNumeric(quote.getRating())){
                int ratingInt=Integer.valueOf(quote.getRating());
                ratingInt=ratingInt+delta;
                ratingText.setText(String.valueOf(ratingInt));
                quote.setRating(String.valueOf(ratingInt));
            }
            setVote(quote.getId(),Quote.VOTE_MINUS);
        }
    };

    CompoundButton.OnCheckedChangeListener bojanChecked=new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            Quote quote=(Quote)buttonView.getTag();
            View view=(View)buttonView.getTag(R.id.item);
            TextView ratingText=(TextView)view.findViewById(R.id.quote_rating);
            database.deleteVoteByQuoteId(quote.getId());
            if(isChecked){
                ToggleButton buttonMinus=Utility.getButtonMinus(view);
                buttonMinus.setChecked(false);
                ToggleButton buttonPlus=Utility.getButtonPlus(view);
                buttonPlus.setChecked(false);
                database.addVote(String.valueOf(quote.getId()), Quote.VOTE_BOJAN);
                ratingText.setTextColor(Utility.getRatingTextCheckedColor());
            }else{
                ratingText.setTextColor(Utility.getRatingTextUnCheckedColor());
            }
            setVote(quote.getId(),Quote.VOTE_BOJAN);
        }
    };

    public void setQuotes(ArrayList<Quote> quotes) {
        this.quotes = quotes;
        notifyDataSetChanged();
    }
}
