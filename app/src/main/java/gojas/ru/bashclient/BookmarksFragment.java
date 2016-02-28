package gojas.ru.bashclient;

import android.app.Fragment;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by gojas on 28.07.2015.
 */
public class BookmarksFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView=inflater.inflate(R.layout.fragment_bookmarks,container,false);
        ListView bookmarksList=(ListView)rootView.findViewById(R.id.bookmarks_list);
        QuotesDatabase db=QuotesDatabase.getInstance(getActivity());
        Cursor cursor=db.getBookMarks();
        //BookmarksAdapterNew adapter=new BookmarksAdapterNew(cursorToArray(cursor),getActivity().getFragmentManager(),((ActionBarActivity)getActivity()).getSupportActionBar());
        BookmarksAdapter adapter=new BookmarksAdapter(getActivity(),cursor,getActivity().getFragmentManager(),((ActionBarActivity)getActivity()).getSupportActionBar());
        bookmarksList.setAdapter(adapter);
        return rootView;
    }

    private ArrayList<Quote> cursorToArray(Cursor cursor){
        cursor.moveToFirst();
        ArrayList<Quote> quotes=new ArrayList<>();
        while (cursor.moveToNext()){
            String json=cursor.getString(cursor.getColumnIndex("quote_text"));
            Quote quote=Quote.fromJSON(json);
            quotes.add(quote);
            //Log.d(MainActivity.TAG,"flaq= "+quote.hasPageLink());
        }
        return quotes;
    }
}
