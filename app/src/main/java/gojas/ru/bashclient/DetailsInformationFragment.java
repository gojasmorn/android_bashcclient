package gojas.ru.bashclient;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by gojas on 09.08.2015.
 */
public class DetailsInformationFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        int parametr=getArguments().getInt(InformationFragment.DETAILS_PARAMETR,0);
        View rootView=inflater.inflate(R.layout.fragment_details_information,container,false);
        TextView textView=(TextView)rootView.findViewById(R.id.text_main);
        RelativeLayout layout=(RelativeLayout)rootView.findViewById(R.id.layout_main);
        layout.setBackgroundColor(Utility.getActivityBackgroundColor());
        textView.setTextColor(Utility.getDefaultTextColor());
        textView.setText(Html.fromHtml(Utility.getInfoDoc(parametr)));
        return rootView;
    }
}
