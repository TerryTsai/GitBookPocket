package email.com.gmail.ttsai0509.gitbookpocket.network.interceptor;

import email.com.gmail.ttsai0509.gitbookpocket.network.GitBookToken;
import email.com.gmail.ttsai0509.gitbookpocket.service.User;
import okhttp3.Interceptor;
import okhttp3.Request;

import java.io.IOException;

public class Authorization implements Interceptor {

    public okhttp3.Response intercept(Chain chain) throws IOException {
        GitBookToken token = User.Service.getAccessToken();

        Request request = (token == null)
                ? chain.request()
                : chain.request().newBuilder()
                .addHeader("Authorization", User.Service.getAccessToken().toString())
                .build();

        // TODO : If any response returns with an authorization failure, delete access token and forward to login page

        return chain.proceed(request);
    }

}
