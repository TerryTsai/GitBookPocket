package email.com.gmail.ttsai0509.gitbookpocket.service;

import email.com.gmail.ttsai0509.gitbookpocket.network.GitBookToken;

public enum User {

    Service;

    private GitBookToken accessToken;

    public void setAccessToken(GitBookToken accessToken) {
        this.accessToken = accessToken;
    }

    public GitBookToken getAccessToken() {
        return accessToken;
    }
}



