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
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.ToggleButton;


import com.balysv.materialripple.MaterialRippleLayout;


/**
 * Created by gojas on 15.07.2015.
 */
public class SettingsFragment extends Fragment {
    public static final int MIN_TEXT=14;

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
                        quoteText.setTextSize(TypedValue.COMPLEX_UNIT_SP, Utility.getTextSize());
                        final SeekBar formatSeeker=(SeekBar)dialog.findViewById(R.id.quote_text_format);
                        formatSeeker.setProgress(Utility.getTextSize()-MIN_TEXT);
                        formatSeeker.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                            @Override
                            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                                quoteText.setTextSize(TypedValue.COMPLEX_UNIT_SP, formatSeeker.getProgress()+MIN_TEXT);
                            }

                            @Override
                            public void onStartTrackingTouch(SeekBar seekBar) {

                            }

                            @Override
                            public void onStopTrackingTouch(SeekBar seekBar) {

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
                                ed.putString(Utility.MODE_TEXT_SIZE, String.valueOf(formatSeeker.getProgress()+MIN_TEXT));
                                ed.apply();
                                Utility.setTextSize(String.valueOf(formatSeeker.getProgress()+MIN_TEXT));
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
                    case 2:
                        final Dialog dialogRead=new Dialog(getActivity());
                        dialogRead.setCancelable(true);
                        dialogRead.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialogRead.setContentView(R.layout.dialog_read_function);
                        RelativeLayout dialogReadLayout=(RelativeLayout)dialogRead.findViewById(R.id.dialog_layout);
                        RelativeLayout pageItem=(RelativeLayout)dialogRead.findViewById(R.id.item_first);
                        RelativeLayout upItem=(RelativeLayout)dialogRead.findViewById(R.id.item_second);
                        TextView upText=(TextView)dialogRead.findViewById(R.id.text_up);
                        TextView pageText=(TextView)dialogRead.findViewById(R.id.text_page);
                        final ToggleButton pageToggle=(ToggleButton)dialogRead.findViewById(R.id.toggle_first);
                        final ToggleButton upToggle=(ToggleButton)dialogRead.findViewById(R.id.toggle_second);
                        final SharedPreferences preferences=getActivity().getPreferences(Activity.MODE_PRIVATE);
                        String upValue=preferences.getString(Utility.FAB_MODE, Utility.MODE_OFF);
                        String pageValue=preferences.getString(Utility.PAGE_MODE, Utility.MODE_OFF);
                        //Log.d(MainActivity.TAG,"upValue "+upValue);
                        //Log.d(MainActivity.TAG,"pageValue "+pageValue);
                        dialogReadLayout.setBackgroundColor(Utility.getDefaultItemBackgroundColor());
                        pageText.setTextColor(Utility.getDefaultTextColor());
                        upText.setTextColor(Utility.getDefaultTextColor());
                        upToggle.setButtonDrawable(Utility.getSettingsToggle());
                        pageToggle.setButtonDrawable(Utility.getSettingsToggle());

                        if(upValue.equals(Utility.MODE_ON)){
                            upToggle.setChecked(true);
                        }else{
                            upToggle.setChecked(false);
                        }

                        if (pageValue.equals(Utility.MODE_ON)){
                            pageToggle.setChecked(true);
                        }else{
                            pageToggle.setChecked(false);
                        }

                        upToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                String status;
                                if (isChecked) {
                                    status = Utility.MODE_ON;
                                } else {
                                    status = Utility.MODE_OFF;
                                }
                                SharedPreferences.Editor ed = preferences.edit();
                                ed.putString(Utility.FAB_MODE, status);
                                ed.commit();

                            }
                        });
                        pageToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                String status;
                                if (isChecked) {
                                    status = Utility.MODE_ON;
                                } else {
                                    status = Utility.MODE_OFF;
                                }
                                SharedPreferences.Editor ed = preferences.edit();
                                ed.putString(Utility.PAGE_MODE, status);
                                ed.commit();
                            }
                        });
                        pageItem.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                pageToggle.toggle();
                            }
                        });
                        upItem.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                upToggle.toggle();
                            }
                        });
                        dialogRead.show();
                        break;
                }
            }
        });
        settingsList.setAdapter(new SettingsAdapter(getActivity()));
        return rootView;
    }

    
}
