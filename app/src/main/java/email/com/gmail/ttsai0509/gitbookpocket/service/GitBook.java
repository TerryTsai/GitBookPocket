package email.com.gmail.ttsai0509.gitbookpocket.service;

import android.net.Uri;
import email.com.gmail.ttsai0509.gitbookpocket.network.GitBookAPI;
import email.com.gmail.ttsai0509.gitbookpocket.network.GitBookSite;
import email.com.gmail.ttsai0509.gitbookpocket.network.interceptor.Authorization;
import email.com.gmail.ttsai0509.gitbookpocket.network.interceptor.HttpLoggingInterceptor;
import email.com.gmail.ttsai0509.gitbookpocket.network.interceptor.SiteScraper;
import lombok.experimental.Delegate;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public enum GitBook implements GitBookAPI, GitBookSite {

    Service;

    @Delegate
    private GitBookAPI api;

    @Delegate
    private GitBookSite site;

    private final Uri auth;

    GitBook() {
        OkHttpClient apiClient = new OkHttpClient.Builder()
                .addInterceptor(new HttpLoggingInterceptor(Logger.Service).setLevel(Logger.Service.getHttpLevel()))
                .addInterceptor(new Authorization())
                .build();

        api = new Retrofit.Builder()
                .baseUrl("https://api.gitbook.com")
                .client(apiClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(GitBookAPI.class);

        OkHttpClient siteClient = new OkHttpClient.Builder()
                .addInterceptor(new HttpLoggingInterceptor(Logger.Service).setLevel(Logger.Service.getHttpLevel()))
                .addInterceptor(new SiteScraper())
                .build();

        site = new Retrofit.Builder()
                .baseUrl("https://www.gitbook.com")
                .client(siteClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(GitBookSite.class);

        String authUrl = "https://api.gitbook.com/oauth/authorize?client_id=%s&redirect_uri=%s&response_type=%s";

        auth = Uri.parse(String.format(authUrl,
                Const.Service.getClientId(),
                Const.Service.getRedirectUrl(),
                Const.Service.getResponseType()));

    }

    public Uri getAuth() {
        return auth;
    }


}
