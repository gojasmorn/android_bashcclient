package gojas.ru.bashclient;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.balysv.materialripple.MaterialRippleLayout;

import java.util.ArrayList;

/**
 * Created by gojas on 16.08.2015.
 */
public class BookmarksAdapterNew extends BaseAdapter {
    ArrayList<Quote> quotes;
    LayoutInflater inflater;
    QuotesDatabase db;
    ActionBar actionBar;
    FragmentManager fragmentManager;
    public BookmarksAdapterNew(ArrayList<Quote> quotes,FragmentManager fragmentManager, ActionBar actionBar){
        this.quotes=quotes;
        inflater=(LayoutInflater)Utility.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        db=QuotesDatabase.getInstance(Utility.getContext());
        this.actionBar=actionBar;
        this.fragmentManager=fragmentManager;
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view=convertView;
        if(view==null){
            view=inflater.inflate(R.layout.bookmark_item,null,false);
        }
        final Quote quote=quotes.get(position);
        RelativeLayout mainLayout=(RelativeLayout)view.findViewById(R.id.bookmark_layout);
        CardView quoteCard=(CardView)view.findViewById(R.id.quote_card);
        TextView quoteIdText=(TextView)view.findViewById(R.id.quote_id);
        final TextView quoteText=(TextView)view.findViewById(R.id.quote_text);
        TextView continueText=(TextView)view.findViewById(R.id.continue_reading_text);
        ImageView bookmarkShare=(ImageView)view.findViewById(R.id.bookmark_share);
        ImageView bookmarkDelete=(ImageView)view.findViewById(R.id.bookmark_delete);
        MaterialRippleLayout continueButton=(MaterialRippleLayout)view.findViewById(R.id.continue_reading);

        mainLayout.setBackgroundColor(Utility.getActivityBackgroundColor());

        bookmarkDelete.setImageResource(Utility.getDeleteResourse());
        bookmarkDelete.setTag(position);
        bookmarkDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = (Integer) v.getTag();
                Quote quoteDeleted = quotes.get(position);
                int quoteId = quoteDeleted.getId();
                db.deleteBookMark(quoteId);
                //swapCursor(db.getBookMarks());
                quotes.remove(position);
                notifyDataSetChanged();

            }
        });
        bookmarkShare.setImageResource(Utility.getShareResourse());
        bookmarkShare.setTag(quote);
        bookmarkShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Quote current = (Quote) v.getTag();
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareBody = current.getText();
                if (Quote.hasRating(current.getMenuIndex()))
                    shareBody = shareBody + "\n" + current.getQuoteAdress();
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                //sharingIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                Utility.getContext().startActivity(Intent.createChooser(sharingIntent, Utility.getContext().getResources().getString(R.string.share_quote)));

            }
        });

        quoteIdText.setTextColor(Utility.getDefaultTextColor());
        quoteIdText.setText("#" + quote.getId());

        quoteCard.setCardBackgroundColor(Utility.getDefaultItemBackgroundColor());

        quoteText.setTextColor(Utility.getDefaultTextColor());
        quoteText.setText(quote.getText());

        continueButton.setTag(quote);
        //Log.d(MainActivity.TAG,"position "+position +" has page link "+String.valueOf(quote.hasPageLink()));
        if(!quote.hasPageLink()){
            continueButton.setVisibility(View.GONE);
        }else{
            continueButton.setVisibility(View.VISIBLE);
        continueText.setTextColor(Utility.getRatingTextCheckedColor());
        continueText.setTag(quote);
        continueButton.setRippleColor(Utility.getDefaultTextColor());
        //Quote current=(Quote)continueButton.getTag();
        //Log.d(MainActivity.TAG,"quote in null ? "+String.valueOf(current==null));
        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Quote quoteClicked=(Quote)v.getTag();
                //Log.d(MainActivity.TAG,"quote in null ? "+String.valueOf(quoteClicked==null));
                Fragment fragment=new QuoteFragment();
                Bundle args = new Bundle();
                args.putInt(QuoteFragment.ARG_MENU_INDEX, quote.getMenuIndex());
                args.putString(QuoteFragment.HAS_ADRESS, quote.getAdress());
                args.putString(QuoteFragment.NEXT_LINK,quote.getNextLink());
                args.putInt(QuoteFragment.POSITION, quote.getPosition());
                fragment.setArguments(args);
                FragmentTransaction transaction=fragmentManager.beginTransaction();
                transaction.replace(R.id.content_frame, fragment);
                transaction.commit();
                actionBar.setTitle(Utility.getContext().getResources().getStringArray(R.array.navigation_drawer_items)[quote.getMenuIndex()]);
            }
        });
        }
        return view;
    }
}
