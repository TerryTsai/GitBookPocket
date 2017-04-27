package email.com.gmail.ttsai0509.gitbookpocket.activity;

import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import email.com.gmail.ttsai0509.gitbookpocket.R;
import email.com.gmail.ttsai0509.gitbookpocket.network.GitBookToken;
import email.com.gmail.ttsai0509.gitbookpocket.service.Const;
import email.com.gmail.ttsai0509.gitbookpocket.service.GitBook;
import email.com.gmail.ttsai0509.gitbookpocket.service.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.io.IOException;
import java.io.InputStream;

public class LoginActivity extends GitBookActivity implements View.OnClickListener, Callback<GitBookToken> {

    private ImageView loginImage;
    private Button loginButton;
    private TextView loginText;
    private ProgressBar loginProgress;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getSupportActionBar().hide();

        setContentView(R.layout.activity_login);

        loginImage = (ImageView) findViewById(R.id.login_image);
        loginImage.setImageDrawable(getDrawable(R.drawable.login));

        loginProgress = (ProgressBar) findViewById(R.id.login_progress);
        loginProgress.setVisibility(View.INVISIBLE);

        loginButton = (Button) findViewById(R.id.login_button);
        loginButton.setText(getString(R.string.login_get_started));
        loginButton.setOnClickListener(this);

        loginText = (TextView) findViewById(R.id.login_text);
        loginText.setText(getString(R.string.app_name));
    }

    @Override
    protected void onResume() {
        super.onResume();

        Uri uri = getIntent().getData();
        if (uri == null) {
            //loginText.setText("Unable to login.");
            loginProgress.setVisibility(View.INVISIBLE);
            return;
        }

        String code = uri.getQueryParameter("code");
        if (code == null) {
            //loginText.setText("Unable to login.");
            loginProgress.setVisibility(View.INVISIBLE);
            return;
        }

        loginProgress.setVisibility(View.VISIBLE);
        GitBook.Service.getAccessToken(code,
                Const.Service.getClientId(),
                Const.Service.getClientSecret(),
                Const.Service.getGrantType()).enqueue(this);
    }

    @Override
    public void onClick(View v) {
        loginProgress.setVisibility(View.VISIBLE);
        Intent intent = new Intent(Intent.ACTION_VIEW, GitBook.Service.getAuth());
        startActivity(intent);
    }

    @Override
    public void onResponse(Call<GitBookToken> call, Response<GitBookToken> response) {
        loginProgress.setVisibility(View.INVISIBLE);
        GitBookToken access = response.body();
        if (access != null) {
            User.Service.setAccessToken(response.body());
            Intent intent = new Intent(this, SearchActivity.class);
            finish();
            startActivity(intent);
        }
    }

    @Override
    public void onFailure(Call<GitBookToken> call, Throwable t) {
    }

}
