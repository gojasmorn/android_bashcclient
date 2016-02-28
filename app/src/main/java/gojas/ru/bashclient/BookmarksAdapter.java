package gojas.ru.bashclient;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.balysv.materialripple.MaterialRippleLayout;

/**
 * Created by gojas on 28.07.2015.
 */
public class BookmarksAdapter extends CursorAdapter {
    ActionBar actionBar;
    LayoutInflater inflater;
    QuotesDatabase db;
    FragmentManager fragmentManager;
    public BookmarksAdapter(Activity activity, Cursor c,FragmentManager fragmentManager, ActionBar actionBar) {
        super(activity.getApplicationContext(), c);
        Context context=activity.getApplicationContext();
        inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        db=QuotesDatabase.getInstance(context);
        this.fragmentManager=fragmentManager;
        this.actionBar=actionBar;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view=inflater.inflate(R.layout.bookmark_item, null);
        return view;
    }

    @Override
    public void bindView(View view, final Context context, Cursor cursor) {
        final Context currentContext=context;
        int quoteId=cursor.getInt(cursor.getColumnIndex("quote_id"));
        String json=cursor.getString(cursor.getColumnIndex("quote_text"));
        final Quote quote=Quote.fromJSON(json);
        RelativeLayout mainLayout=(RelativeLayout)view.findViewById(R.id.bookmark_layout);
        CardView quoteCard=(CardView)view.findViewById(R.id.quote_card);
        TextView quoteIdText=(TextView)view.findViewById(R.id.quote_id);
        TextView quoteText=(TextView)view.findViewById(R.id.quote_text);
        TextView continueText=(TextView)view.findViewById(R.id.continue_reading_text);
        ImageView bookmarkShare=(ImageView)view.findViewById(R.id.bookmark_share);
        ImageView bookmarkDelete=(ImageView)view.findViewById(R.id.bookmark_delete);
        MaterialRippleLayout continueButton=(MaterialRippleLayout)view.findViewById(R.id.continue_reading);

        mainLayout.setBackgroundColor(Utility.getActivityBackgroundColor());

        bookmarkDelete.setImageResource(Utility.getDeleteResourse());
        bookmarkDelete.setTag(quoteId);
        bookmarkDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int quoteId = (Integer) v.getTag();
                db.deleteBookMark(quoteId);
                swapCursor(db.getBookMarks());
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
                Utility.shareQuote(shareBody);
            }
        });

        quoteIdText.setTextColor(Utility.getDefaultTextColor());
        quoteIdText.setText("#" + quote.getId());

        quoteCard.setCardBackgroundColor(Utility.getDefaultItemBackgroundColor());

        quoteText.setTextColor(Utility.getDefaultTextColor());
        quoteText.setText(quote.getText());

        continueButton.setTag(quote);

        //Log.d(MainActivity.TAG,"hasAdress "+String.valueOf(quote.hasPageLink()));
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
                    actionBar.setTitle(context.getResources().getStringArray(R.array.navigation_drawer_items)[quote.getMenuIndex()]);
                }
            });
        }

    }
}
