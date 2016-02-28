package gojas.ru.bashclient;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.view.View;
import android.widget.ToggleButton;

import java.io.IOException;

public class Utility {
    private static MainActivity mainActivity;
    public static String MODE_LABEL="NIGHT_MODE_VALUE";
    public static String MODE_TEXT_SIZE="TEXT_SIZE_MODE";
    public static String DEFAULT_TEXT_SIZE="16";
    public static String PAGE_MODE="page_mode";
    public static String FAB_MODE="fab_mode";
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
    private static int settingsToggleNight;
    private static int settingsToggleDay;
    private static int infoItemDay;
    private static int infoItemNight;
    private static ConnectivityManager connectivityManager;

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
        settingsToggleDay=R.drawable.toggle_settings_day;
        settingsToggleNight=R.drawable.toggle_settings_night;
        infoItemDay=R.layout.info_item_day;
        infoItemNight=R.layout.info_item_night;
        connectivityManager=((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
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

    public static boolean isNetworkAvailable(final Context c) {
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();

        /*
        Runtime runtime = Runtime.getRuntime();
        try {

            Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
            int     exitValue = ipProcess.waitFor();
            return (exitValue == 0);

        } catch (IOException e)          { e.printStackTrace(); }
        catch (InterruptedException e) { e.printStackTrace(); }

        return false;
        */

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

    public static int getSettingsToggle() {

        if (nightModeOn){
            return settingsToggleNight;
        }else{
            return settingsToggleDay;
        }
    }

    private static String getFaqText(){
        return "<h2>Часто задаваемые вопросы и ответы на них</h2>\n" +
                "\n" +
                "<h3>О сайте в общем</h3>\n" +
                "\n" +
                "<h4>Где я?...</h4>\n" +
                "<p>Это Цитатник Рунета. Работает всё так:</p>\n" +
                "<ol>\n" +
                "\t<li>Вы добавляете на сайт цитаты из IRC, ICQ, форумов — откуда угодно, хоть подслушанные в лифте.</li><br/>\n" +
                "\t<li>Другие пользователи просматривают их в Бездне и голосуют за или против.</li><br/>\n" +
                "\t<li>Мы разбираем получившееся и то, что нам понравилось, аппрувим (от <em>to approve</em>, «одобрять»). Результаты попадают на главную страницу.</li><br/>\n" +
                "\t<li>Вы можете выразить своё мнение, проголосовав за или против отобранных нами цитат.</li><br/>\n" +
                "</ol>\n" +
                "<p>Не забудьте прочитать пользовательское соглашение. Если вы не согласны с ним, вам не стоит пользоваться нашим сайтом.</p>\n" +
                "\n" +
                "\n" +
                "<h4>Как добавить цитату на сайт?</h4>\n" +
                "<p>Через форму в соответствующем разделе. Цитата после этого попадает в Бездну.</p>\n" +
                "\n" +
                "\n" +
                "<h4>Хорошо, а что такое Бездна?</h4>\n" +
                "<p>Раздел, в котором пользователи могут в случайном порядке посмотреть цитаты, добавленные посетителями на сайт, но еще не просмотренные модераторами (они же — аппруверы) и&nbsp;проголосовать за эти цитаты.</p>\n" +
                "\n" +
                "\n" +
                "<h4>И как оно все работает?</h4>\n" +
                "<p>Вы добавляете цитату.</p>\n" +
                "<p>После этого цитата попадает в Бездну, где ее могут увидеть и проголосовать за нее наши посетители, читающие сей суровый раздел. (Нефильтрованный Глас Интернета — не для слабых духом, истину говорим вам. Можете убедиться сами.)</p>\n" +
                "<p>В Бездне цитата находится до тех пор, пока не умрет от старости (можете считать этот срок равным неделе) или ее не просмотрят аппруверы. То, что аппруверам не понравилось — уходит на свалку истории, то, что понравилось — попадает в цепкие лапы редактора. Редактор определяет, не дало ли чувство юмора аппрувера сбой, убирает из цитаты лишнее, если это необходимо — и цитата уходит на главную. Или в небытие, если цитата редактору не понравилась.</p>\n" +
                "\t\n" +
                "<h4>И кто же у нас редакторы?</h4>\n" +
                "<p>Например, DarkRider и zoi.</p>\n" +
                "\t\n" +
                "\n" +
                "<h4>Мне не нравится раздел ***, уберите его!</h4>\n" +
                "<p>Увы вам.</p>\n" +
                "\t\n" +
                "\n" +
                "<h4>Я хочу стать аппрувером!</h4>\n" +
                "<p>Спасибо за предложение, но в данный момент новых модераторов мы не набираем.</p>\n" +
                "\t\n" +
                "\n" +
                "<h4>Баш опопсел! / Баш уже не тот! / Раньше был другой, айтишный баш <s>с блэкджеком и шлюхами!</s></h4>\n" +
                "<p>Еще трава раньше была зеленее, а вода мокрее. Можете обсудить эти вопросы с друзьями, коллегами или психоаналитиком.</p>\n" +
                "\n" +
                "\n" +
                "<h4>Кажется, раньше у вас был другой адрес?</h4>\n" +
                "<p>1 марта 2012 года мы переехали на <b>bash.im</b>. После восьми лет в org.ru уже можно-то!</p>\n" +
                "\n" +
                "<h4>Я очень остроумно сказал группе агрессивных молодых людей то же, что и ххх из цитаты #yyyyy, и теперь мне придется вставлять себе зубы! / Я пропустил кота через шреддер и они теперь не работают!! / Я сделал на работе то же, что тот чувак из цитаты в топе Бездны, а меня уволили!</h4>\n" +
                "<p>Пожалуйста, не пытайтесь заменить головной мозг нашим цитатником (равно как и чем угодно другим), если получение Премии Дарвина не входит в ваши планы.</p>\n" +
                "\t\n" +
                "\t\n" +
                "<h3>О Главной странице и цитатах</h3>\n" +
                "\n" +
                "<h4>Что вы сделали с моей цитатой?!</h4>\n" +
                "<p>Скорее всего, отредактировали. Как правило, мы убираем лишний мат (к мату мы относимся с пониманием, но только в случае, когда он к месту, а не для связи слов в предложении), лишние смайлы, упоминания о том, как вы от смеха лежали всем заводом, лишние таймстампы, если вы все-таки поленились удалить их и прочий шлак.</p>\n" +
                "<p>По необходимости мы также стараемся править заметные ошибки и опечатки — если, по нашему мнению, они цитату портят.</p>\n" +
                "\t\n" +
                "\n" +
                "<h4>Мне не понравилась цитата!</h4>\n" +
                "<p>Проголосуйте против. Писать нам не надо, все опусы на эту тему будут молча проигнорированы.</p>\n" +
                "<p>Мы не обсуждаем чужое чувство юмора и не собираемся обсуждать наше.</p>\n" +
                "\t\n" +
                "\n" +
                "<h4>Что делает кнопка [:||||:]?</h4>\n" +
                "<p>Сообщает нам о том, что цитата, по вашему мнению, баян. Если таких, как вы, много — мы отреагируем.</p>\n" +
                "<p>Следует при этом помнить, что баян в данном случае — старая шутка (<em>Снесла как-то курочка...</em>), а не то, что лично вам не нравится.</p>\n" +
                "<p>Работает и для Бездны.</p>\n" +
                "\t\n" +
                "\n" +
                "<h4>Я это уже видел! / Это было на www.yyy.xxx! / Эту историю рассказывал мой&nbsp;прадедушка! / Это перевод с китайского башорга!</h4>\n" +
                "<p>Нажмите кнопку [:||||:]. Можете также написать нам письмо, в котором нас будет интересовать только ссылка на цитату и на оригинал истории.</p>\n" +
                "<p>Просьба при написании письма руководствоваться тем, что мы не ставили и не ставим перед собой задачу прочитать и запомнить дословно все сайты рунета, так что любой объем праведного негодования будет потрачен вами абсолютно зря.</p>\n" +
                "\t\n" +
                "\n" +
                "<h4>Почему я не вижу рейтинг у новых цитат?</h4>\n" +
                "<p>Многие пользователи голосуют за или против цитаты только потому, что у нее большой (или маленький) рейтинг.</p>\n" +
                "<p>Дабы голосование было обьективным хотя бы первое время, первые два часа жизни цитаты ее рейтинг не показывается.</p>\n" +
                "\t\n" +
                "\n" +
                "<h4>Что происходит с цитатами, набравшими отрицательный рейтинг?</h4>\n" +
                "<p>Их рейтинг становится невидим для посетителей. При желании, их отображение можно отключить — для этого в меню есть соответствующая кнопка.</p>\n" +
                "\t\n" +
                "\n" +
                "<h4>Где можно увидеть утвердившего цитату модератора?</h4>\n" +
                "<p>Модераторы всегда работали анонимно. <span class=\"nowrap\">2 &times; 2 = 5.</span></p>\n" +
                "\n" +
                "\n" +
                "<h4>Куда вы дели просмотр цитат по дате добавления?!</h4>\n" +
                "<p>Объединили с поиском и главной. Приглядитесь, над цитатами есть переключатель страниц. Кстати, по вашим просьбам мы добавили возможность вручную указать номер страницы — просто нажмите на текущую страницу, впишите желаемый номер в окошко и нажмите Enter. Разумеется, всю дорогу это было можно делать и непосредственно в адресной строке.</p>\n" +
                "\t\n" +
                "\n" +
                "<h4>У вас в Лучших Цитатах лучшие цитаты дня и недели одни и те же!!! <em>(задаётся в понедельник)</em></h4>\n" +
                "<p>Вы не поверите...</p>\n" +
                "\t\n" +
                "\n" +
                "<h4>У вас в Лучших Цитатах лучшие цитаты дня — вчерашние!</h4>\n" +
                "<p>Если сегодня еще не было утверждено ни одной цитаты — показываются вчерашние.</p>\n" +
                "\t\n" +
                "\n" +
                "\n" +
                "<h3>О Бездне</h3>\n" +
                "\n" +
                "<h4><abbr title=\"Нам печально писать об этом прямым текстом, но вы так часто спрашиваете… Нет, это не опечатка.\">Админы что вы за отстой аппрувите в бездну!1</abbr></h4>\n" +
                "<p>За содержимое Бездны большей частью отвечают пользователи — такие же, как и вы (хотя некоторые вещи мы стараемся фильтровать). Адресуйте свое негодование зеркалу.</p>\n" +
                "<p>Как вариант — не читайте Бездну, она весьма своеобразна.</p>\n" +
                "\n" +
                "<h4>Как цитаты попадают в Лучшее Бездны?</h4>\n" +
                "<p>Автоматически &mdash; после отклонения аппрувером или редактором, при достижении необходимого рейтинга. При этом цитаты, предназначенные для главной, в&nbsp;лучшее Бездны <i>не попадают</i>. Пороговая величина рейтинга определяется субьективно по качеству выходящего материала, сейчас равна 30.</p>\n" +
                "\n" +
                "\n" +
                "\t\n" +
                "<h3>О комиксах</h3>\n" +
                "\n" +
                "<h4>Мне не понравился какой-то из комиксов!</h4>\n" +
                "<p>А нам понравился.</p>\n";
    }

    private static String getUserDoc(){
        return "<h2>Кто мы?</h2>\n" +
                "<p>Проектом управляет общество с ограниченной ответственностью <b>Chattyfish Ltd</b>, зарегистрированное и ведущее деятельность на территории Республики Кипр.</p>\n" +
                "<p>Адрес: Galateias 2-4, 8046, Pafos, Cyprus.<br>" +
                "E-mail:noc@chatty.fish </p>\n" +
                "\n" +
                "<h2>Пользовательское соглашение</h2>\n" +
                "<p>Все права на&nbsp;присланные цитаты принадлежат их&nbsp;владельцам&nbsp;&mdash; непосредственным участникам цитат и&nbsp;тем, кто прислал эти цитаты через форму на&nbsp;нашем сайте. Администрация сайта не&nbsp;несет ответственности за&nbsp;их&nbsp;использование третьими сторонами.</p>\n" +
                "<p>На&nbsp;сайте неизбежно есть и&nbsp;будут цитаты, содержащие нецензурную лексику. Если использование обсценной лексики вас раздражает, рекомендуем не&nbsp;читать наш сайт.</p>\n" +
                "<p>Цитаты на&nbsp;нашем сайте являются произведениями народного творчества. Администрация сайта не&nbsp;ставит перед собой цели оскорблять честь и&nbsp;достоинство физических лиц, либо посягать на&nbsp;чью-либо деловую репутацию. Совпадения реальных имён и&nbsp;названий считаются случайными.</p>\n" +
                "<p>Администрация сайта не&nbsp;занимается предварительной оценкой присланных материалов на&nbsp;предмет соответствия их&nbsp;действующему законодательству, чьим-то личным представлениям о&nbsp;прекрасном, и&nbsp;не&nbsp;оценивает содержащиеся в&nbsp;цитатах оценочные суждения. При этом администрация оставляет за&nbsp;собой право удалять явно нарушающие законодательство, разглашающие личные сведения или являющиеся личными выпадами против конкретных лиц и&nbsp;групп лиц цитаты, в&nbsp;том числе и&nbsp;автоматически, но&nbsp;не&nbsp;гарантирует полного отсутствия подобного контента. В&nbsp;случае обнаружения таких цитат просьба написать нам, мы&nbsp;постараемся принять меры.</p>\n" +
                "<p>Желая соответствовать нормам закона № 436-ФЗ «О защите детей от информации, причиняющей вред их здоровью и развитию», и с учётом предыдущего пункта, администрация классифицирует сайт, как содержащий запрещенную для детей информационную продукцию (18+). Таким образом, если вам нет восемнадцати, пользоваться сайтом вам запрещается. Если вам есть восемнадцать и вы не хотите, чтобы сайт читали ваши дети или младшие родственники, вы можете воспользоваться многочисленными программами родительского контроля.</p>\n" +
                "<p>Администрация сайта имеет право публиковать, удалять и&nbsp;редактировать <em>любые</em> материалы, присланные пользователями, по&nbsp;своему усмотрению.</p>\n" +
                "<p>Администрация сайта оставляет за&nbsp;собой право на&nbsp;использование информации, содержащейся на&nbsp;сайте, по&nbsp;своему усмотрению.</p>\n";
    }
    public static int getInfoItem() {
        if (nightModeOn){
            return infoItemNight;
        }else{
            return infoItemDay;
        }
    }

    public static String getInfoDoc(int param){
        switch (param){
            case 0:
                return getFaqText();
            case 1:
                return getUserDoc();
            case 2:
                return getAuthorDoc();
        }
        return getFaqText();
    }

    public static String getAuthorDoc() {
        String authorDoc="<p>Данное приложение является неофициальной версией клиента сайта bash.im</p><br/>" +
                "<p>Разработка:Мурзин Михиал<br/>"+
                "Тестирование:Мельник Дмитрий</p>";
        return authorDoc;
    }

    public static int getStatus(int vote) {
        switch (vote){
            case Quote.VOTE_PLUS:
                if(nightModeOn){
                    return  R.drawable.ic_plus_pressed_night;
                }else{
                    return R.drawable.ic_plus_pressed;
                }
            case Quote.VOTE_MINUS:
                if(nightModeOn){
                    return  R.drawable.ic_minus_pressed_night;
                }else{
                    return R.drawable.ic_minus_pressed;
                }
            case Quote.VOTE_BOJAN:
                if(nightModeOn){
                    return  R.drawable.ic_bojan_pressed_night;
                }else{
                    return R.drawable.ic_bojan_pressed;
                }
        }
        return 0;
    }

    public static void shareQuote(String shareBody) {
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
        sharingIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mainActivity.startActivity(Intent.createChooser(sharingIntent, mainActivity.getResources().getString(R.string.share_quote)));

    }

    public static void setActivity(MainActivity activity) {
        Utility.mainActivity = activity;
    }
}
