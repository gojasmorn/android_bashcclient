package gojas.ru.bashclient;

import android.app.Application;
import org.acra.*;
import org.acra.annotation.*;

@ReportsCrashes(
        formUri = "http://ismynotes.ru/bugs.php",
        customReportContent = { ReportField.APP_VERSION_CODE, ReportField.APP_VERSION_NAME, ReportField.ANDROID_VERSION, ReportField.PHONE_MODEL, ReportField.CUSTOM_DATA, ReportField.STACK_TRACE, ReportField.LOGCAT },
        mode = ReportingInteractionMode.TOAST,
        resToastText = R.string.crash_toast_text)

/**
 * Created by gojas on 17.08.2015.
 */
public class MyApp extends Application{
    @Override
    public void onCreate() {
        super.onCreate();
        ACRA.init(this);
    }
}
