package email.com.gmail.ttsai0509.gitbookpocket.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import email.com.gmail.ttsai0509.gitbookpocket.service.Logger;

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Logger.Service.d(this, "onCreate");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Logger.Service.d(this, "onRestart");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Logger.Service.d(this,  "onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Logger.Service.d(this, "onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Logger.Service.d(this, "onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Logger.Service.d(this, "onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Logger.Service.d(this, "onDestroy");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Logger.Service.d(this, "onSaveInstanceState");
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Logger.Service.d(this, "onRestoreInstanceState");
    }
}
