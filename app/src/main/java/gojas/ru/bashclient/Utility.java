package gojas.ru.bashclient;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.view.View;
import android.widget.ToggleButton;

public class Utility {
    public static String MODE_LABEL="NIGHT_MODE_VALUE";
    public static String MODE_TEXT_SIZE="TEXT_SIZE_MODE";
    public static String DEFAULT_TEXT_SIZE="16";
    public static boolean nightModeOn=false;
    public static String MODE_ON="ON";
    public static String MODE_OFF="OFF";

    private static TypedArray drawerDay;
    private static TypedArray drawerNight;

    private static TypedArray settingDay;
    private static TypedArray settingNight;

    private static int ratingTextCheckedDay;
    private static int ratingTextUnCheckedDay;
    private static int ratingTextCheckedNight;
    private static int ratingTextUnCheckedNight;

    private static int activityBackgroundColorDay;
    private static int activityBackgroundColorNight;

    private static String[] drawerItems;
    private static int drawerItemBackgroundColorDay;
    private static int drawerItemBackgroundColorNight;
    private static int defaultTextColorDay;
    private static int defaultTextColorNight;
    private static int cardBackgroundColorDay;
    private static int cardBackgroundColorNight;
    private static int toolbarColorDay;
    private static int toolbarColorNight;
    private static int statusBarColorDay;
    private static int statusBarColorNight;
    private static int textSize;
    public static  Drawable emotionSadDay;
    public static  Drawable emotionSadNight;
    private static int drawableBookmarksDay;
    private static int drawableBookmarksNight;
    private static int pageResourseDay;
    private static int pageResourseNight;
    private static int shareResourseNight;
    private static int shareResourseDay;
    private static int deleteResourseNight;
    private static int deleteResourseDay;
    private static Context c;

    public static void setNightMode(String stringMode){
        if(stringMode.equals(MODE_ON)) nightModeOn=true;
        if(stringMode.equals(MODE_OFF)) nightModeOn=false;
    }

    public static void setTextSize(String textString){
        textSize=Integer.valueOf(textString);
    }
    public static boolean isNightModeOn(){
        return nightModeOn;
    }

    public static void setContext(Context context) {
        c=context;
        drawerItems=context.getResources().getStringArray(R.array.navigation_drawer_items);
        drawerDay=context.getResources().obtainTypedArray(R.array.drawer_icons_black);
        drawerNight=context.getResources().obtainTypedArray(R.array.drawer_icons_white);
        settingDay=context.getResources().obtainTypedArray(R.array.settings_icons_day);
        settingNight=context.getResources().obtainTypedArray(R.array.settings_icons_night);
        ratingTextCheckedDay = context.getResources().getColor(R.color.checked_rating_text_day);
        ratingTextUnCheckedDay=context.getResources().getColor(R.color.unchecked_rating_text_day);
        ratingTextCheckedNight = context.getResources().getColor(R.color.checked_rating_text_night);
        ratingTextUnCheckedNight=context.getResources().getColor(R.color.unchecked_rating_text_night);
        activityBackgroundColorDay=context.getResources().getColor(R.color.activity_background_day);
        activityBackgroundColorNight=context.getResources().getColor(R.color.activity_background_night);
        drawerItemBackgroundColorDay=context.getResources().getColor(R.color.drawer_item_background_color_day);
        drawerItemBackgroundColorNight=context.getResources().getColor(R.color.drawer_item_background_color_night);
        defaultTextColorDay=context.getResources().getColor(R.color.default_text_color_day);
        defaultTextColorNight=context.getResources().getColor(R.color.default_text_color_night);
        cardBackgroundColorDay=context.getResources().getColor(R.color.card_background_day);
        cardBackgroundColorNight=context.getResources().getColor(R.color.card_background_night);
        toolbarColorDay=context.getResources().getColor(R.color.toolbar_color_day);
        toolbarColorNight=context.getResources().getColor(R.color.toolbar_color_night);
        statusBarColorDay=context.getResources().getColor(R.color.statusbar_color_day);
        statusBarColorNight=context.getResources().getColor(R.color.statusbar_color_night);
        emotionSadDay=context.getResources().getDrawable(R.drawable.ic_emoticon_sad_black_48dp);
        emotionSadNight=context.getResources().getDrawable(R.drawable.ic_emoticon_sad_white_48dp);
        drawableBookmarksDay=R.drawable.toogle_bookmark_day;
        drawableBookmarksNight=R.drawable.toogle_bookmark_night;
        pageResourseDay=R.drawable.button_page_day;
        pageResourseNight=R.drawable.button_page_night;
        shareResourseNight=R.drawable.ic_share_variant_white_24dp;
        shareResourseDay=R.drawable.ic_share_variant_black_24dp;
        deleteResourseDay=R.drawable.ic_action_delete_black;
        deleteResourseNight=R.drawable.ic_action_delete_white;
    }

    public static String[] getDrawerItems() {
        return drawerItems;
    }

    public static Drawable getDrawableDrawerIcon(int position) {
        if(nightModeOn){
            return drawerNight.getDrawable(position);
        }else{
            return drawerDay.getDrawable(position);
        }
    }

    public static int getRatingTextCheckedColor() {
        if(nightModeOn){
            return ratingTextCheckedNight;
        }else {
            return ratingTextCheckedDay;
        }
    }

    public static int getRatingTextUnCheckedColor() {
        if(nightModeOn){
            return ratingTextUnCheckedNight;
        }else {
            return ratingTextUnCheckedDay;
        }
    }

    public static int getActivityBackgroundColor() {
        if(nightModeOn){
            return activityBackgroundColorNight;
        }else{
            return activityBackgroundColorDay;
        }
    }

    public static int getDefaultItemBackgroundColor() {
        if(nightModeOn){
            return drawerItemBackgroundColorNight;
        }else{
            return drawerItemBackgroundColorDay;
        }
    }

    public static int getDefaultTextColor() {
        if(nightModeOn){
            return defaultTextColorNight;
        }else{
            return defaultTextColorDay;
        }
    }

    public static int getCardBackgroundColor() {
        if(nightModeOn){
            return cardBackgroundColorNight;
        }else{
            return cardBackgroundColorDay;
        }
    }

    public static int getToolbarColor() {
        if(nightModeOn){
            return toolbarColorNight;
        }else{
            return toolbarColorDay;
        }
    }

    public static int getStatusBarColor() {
        if(nightModeOn){
            return statusBarColorNight;
        }else{
            return statusBarColorDay;
        }
    }

    public static Drawable getDrawableSettingsIcon(int position) {
        if(nightModeOn){
            return settingNight.getDrawable(position);
        }else{
            return settingDay.getDrawable(position);
        }
    }

    public static ToggleButton getButtonPlus(View item) {
        if(nightModeOn){
            item.findViewById(R.id.button_plus).setVisibility(View.GONE);
            return (ToggleButton)item.findViewById(R.id.button_plus_night);
        }else{
            item.findViewById(R.id.button_plus_night).setVisibility(View.GONE);
            return (ToggleButton)item.findViewById(R.id.button_plus);
        }
    }

    public static ToggleButton getButtonMinus(View item) {
        if(nightModeOn){
            item.findViewById(R.id.button_minus).setVisibility(View.GONE);
            return (ToggleButton)item.findViewById(R.id.button_minus_night);
        }else{
            item.findViewById(R.id.button_minus_night).setVisibility(View.GONE);
            return (ToggleButton)item.findViewById(R.id.button_minus);
        }
    }

    public static ToggleButton getButtonBojan(View item) {
        if(nightModeOn){
            item.findViewById(R.id.button_bojan).setVisibility(View.GONE);
            return (ToggleButton)item.findViewById(R.id.button_bojan_night);
        }else{
            item.findViewById(R.id.button_bojan_night).setVisibility(View.GONE);
            return (ToggleButton)item.findViewById(R.id.button_bojan);
        }
    }

    public static int getTextSize() {
        return textSize;
    }

    public static boolean isNetworkAvailable(final Context context) {
        final ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }

    public static Drawable getEmptyImage(){
        if (nightModeOn){
            return emotionSadNight;
        }else{
            return emotionSadDay;
        }
    }

    public static int getDrawableBookmarksId() {
        if (nightModeOn){
            return drawableBookmarksNight;
        }else{
            return drawableBookmarksDay;
        }
    }

    public static int getPageResourse() {
        if (nightModeOn){
            return pageResourseNight;
        }else{
            return pageResourseDay;
        }

    }

    public static int getShareResourse() {
        if (nightModeOn){
            return shareResourseNight;
        }else{
            return shareResourseDay;
        }

    }

    public static int getDeleteResourse() {

        if (nightModeOn){
            return deleteResourseNight;
        }else{
            return deleteResourseDay;
        }
    }

    public static Context getContext(){
        return  c;
    }
}
