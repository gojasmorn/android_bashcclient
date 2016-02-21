package gojas.ru.bashclient;

import android.app.Fragment;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

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
        BookmarksAdapter adapter=new BookmarksAdapter(getActivity(),cursor,getActivity().getFragmentManager(),((ActionBarActivity)getActivity()).getSupportActionBar());
        bookmarksList.setAdapter(adapter);
        return rootView;
    }
}
