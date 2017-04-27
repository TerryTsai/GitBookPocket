package email.com.gmail.ttsai0509.gitbookpocket.service;

import email.com.gmail.ttsai0509.gitbookpocket.network.interceptor.HttpLoggingInterceptor;

import java.util.Locale;

public enum Logger implements HttpLoggingInterceptor.Logger {

    Service;
    private String prefix;
    private HttpLoggingInterceptor.Level httpLevel;

    Logger() {
        prefix = "GitBook";
        httpLevel = HttpLoggingInterceptor.Level.BODY;
    }

    public HttpLoggingInterceptor.Level getHttpLevel() {
        return httpLevel;
    }

    @Override
    public void log(String msg) {
        android.util.Log.d(getTag(this), msg);
    }

    public void e(Object context, String msg) {
        android.util.Log.e(getTag(context), msg);
    }

    public void w(Object context, String msg) {
        android.util.Log.w(getTag(context), msg);
    }

    public void i(Object context, String msg) {
        android.util.Log.i(getTag(context), msg);
    }

    public void d(Object context, String msg) {
        android.util.Log.d(getTag(context), msg);
    }

    public void v(Object context, String msg) {
        android.util.Log.v(getTag(context), msg);
    }

    private String getTag(Object context) {
        return String.format(
                Locale.ENGLISH,
                "%s.%s.%d",
                prefix,
                context.getClass().getSimpleName(),
                Thread.currentThread().getId()
        );
    }
}
