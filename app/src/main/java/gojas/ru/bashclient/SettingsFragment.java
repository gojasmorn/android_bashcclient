package gojas.ru.bashclient;

import android.app.Activity;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;


import com.balysv.materialripple.MaterialRippleLayout;

import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar;

/**
 * Created by gojas on 15.07.2015.
 */
public class SettingsFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView=inflater.inflate(R.layout.settings_fragment,container,false);
        final ListView settingsList=(ListView)rootView.findViewById(R.id.settings_list);
        settingsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        break;
                    case 1:
                        final Dialog dialog=new Dialog(getActivity());
                        dialog.setCancelable(false);
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.setContentView(R.layout.dialog_text_format);
                        RelativeLayout dialogLayout=(RelativeLayout)dialog.findViewById(R.id.dialog_layout);
                        dialogLayout.setBackgroundColor(Utility.getActivityBackgroundColor());
                        final TextView quoteText=(TextView)dialog.findViewById(R.id.quote_text);
                        quoteText.setTextColor(Utility.getDefaultTextColor());
                        quoteText.setTextSize(TypedValue.COMPLEX_UNIT_SP,Utility.getTextSize());
                        final DiscreteSeekBar formatSeeker=(DiscreteSeekBar)dialog.findViewById(R.id.quote_text_format);
                        formatSeeker.setProgress(Utility.getTextSize());
                        formatSeeker.setOnProgressChangeListener(new DiscreteSeekBar.OnProgressChangeListener() {
                            @Override
                            public void onProgressChanged(DiscreteSeekBar discreteSeekBar, int i, boolean b) {
                                quoteText.setTextSize(TypedValue.COMPLEX_UNIT_SP, formatSeeker.getProgress());
                            }

                            @Override
                            public void onStartTrackingTouch(DiscreteSeekBar discreteSeekBar) {

                            }

                            @Override
                            public void onStopTrackingTouch(DiscreteSeekBar discreteSeekBar) {

                            }
                        });
                        quoteText.setText(getActivity().getResources().getString(R.string.quote_text_example));
                        MaterialRippleLayout rippleOk=(MaterialRippleLayout)dialog.findViewById(R.id.ripple_ok);
                        rippleOk.setRippleColor(Utility.getDefaultTextColor());
                        rippleOk.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                SharedPreferences preferences=getActivity().getPreferences(Activity.MODE_PRIVATE);
                                SharedPreferences.Editor ed = preferences.edit();
                                ed.putString(Utility.MODE_TEXT_SIZE, String.valueOf(formatSeeker.getProgress()));
                                ed.apply();
                                Utility.setTextSize(String.valueOf(formatSeeker.getProgress()));
                                dialog.cancel();
                            }
                        });
                        MaterialRippleLayout rippleCancel=(MaterialRippleLayout)dialog.findViewById(R.id.ripple_cancel);
                        rippleCancel.setRippleColor(Utility.getDefaultTextColor());
                        rippleCancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.cancel();
                            }
                        });
                        dialog.show();
                        break;
                }
            }
        });
        settingsList.setAdapter(new SettingsAdapter(getActivity()));
        return rootView;
    }

    
}
