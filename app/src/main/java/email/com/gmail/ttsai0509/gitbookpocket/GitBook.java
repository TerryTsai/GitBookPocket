package email.com.gmail.ttsai0509.gitbookpocket;

import android.app.Application;
import email.com.gmail.ttsai0509.gitbookpocket.service.Const;

public class GitBook extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Const.Service.init(this);
    }
}
