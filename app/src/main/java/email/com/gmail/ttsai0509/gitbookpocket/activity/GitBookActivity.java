package email.com.gmail.ttsai0509.gitbookpocket.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import email.com.gmail.ttsai0509.gitbookpocket.R;
import email.com.gmail.ttsai0509.gitbookpocket.service.User;

public class GitBookActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        switch(settings.getString("theme", "Dark")) {
            case "Light": setTheme(R.style.GitbookPocketLight); break;
            case "Dark": setTheme(R.style.GitbookPocket); break;
            case "Ido": setTheme(R.style.GitBookPocketIdo); break;
            default: setTheme(R.style.GitbookPocket); break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (!(this instanceof LoginActivity) && User.Service.getAccessToken() == null) {
            // TODO : Consider starting for result
            Intent intent = new Intent(this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
    }
}
