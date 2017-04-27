package email.com.gmail.ttsai0509.gitbookpocket.service;

import android.content.Context;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public enum Const {

    Service;
    private String clientId;
    private String clientSecret;
    private String redirectUrl;
    private String responseType;
    private String grantType;

    Const() {}

    public void init(Context context) {
        try(InputStream is = context.getAssets().open("gitbook.property")) {
            Properties properties = new Properties();
            properties.load(is);
            this.clientId = properties.getProperty("clientId");
            this.clientSecret = properties.getProperty("clientSecret");
            this.redirectUrl = properties.getProperty("redirectUrl");
            this.responseType = properties.getProperty("responseType");
            this.grantType = properties.getProperty("grantType");
        } catch (IOException e) {
            throw new RuntimeException("gitbook.property expected in assets folder.");
        }

    }

    public String getClientId() {
        return clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public String getRedirectUrl() {
        return redirectUrl;
    }

    public String getResponseType() {
        return responseType;
    }

    public String getGrantType() {
        return grantType;
    }

}
