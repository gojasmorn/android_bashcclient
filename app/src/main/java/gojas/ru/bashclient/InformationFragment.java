package gojas.ru.bashclient;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by gojas on 09.08.2015.
 */
public class InformationFragment extends Fragment {
    String[] items={"О сайте","Пользовательское соглашение bash.im","О приложении"};
    public static String DETAILS_PARAMETR="details";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView=inflater.inflate(R.layout.fragment_information,container,false);
        RelativeLayout layout=(RelativeLayout)rootView.findViewById(R.id.layout_main);
        ListView listView=(ListView)rootView.findViewById(R.id.list_main);
        InformationAdapter adapter = new InformationAdapter();
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        replaceFragment(0);
                        break;
                    case 1:
                        replaceFragment(1);
                        break;
                    case 2:
                        replaceFragment(2);
                        break;
                }
            }
        });
        return rootView;
    }

    void replaceFragment(int parametr){
        FragmentManager fragmentManager = getFragmentManager();
        Fragment fragment=new DetailsInformationFragment();
        Bundle args = new Bundle();
        args.putInt(DETAILS_PARAMETR, parametr);
        fragment.setArguments(args);
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.content_frame, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    class InformationAdapter extends BaseAdapter{
        LayoutInflater inflater;

        public InformationAdapter(){

            inflater=(LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }


        @Override
        public int getCount() {
            return items.length;
        }

        @Override
        public Object getItem(int position) {
            return items[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View item=convertView;
            if(item==null){
                item=inflater.inflate(Utility.getInfoItem(),parent,false);
            }
            TextView textView=(TextView)item.findViewById(R.id.info_item_text);
            textView.setText(items[position]);
            return item;
        }
    }
}
