package com.teachandroid.app.data;

public class Session {

    private String mUserId;

    private String mAccessToken;

    private String mExpireIn;

    public void setUserId(String mid) {
        this.mUserId = mid;
    }

    public String getUserId() {
        return mUserId;
    }

    public void setAccessToken(String accessToken) {
        this.mAccessToken = accessToken;
    }

    public String getAccessToken() {
        return mAccessToken;
    }

    public void setExpireIn(String expireIn) {
        this.mExpireIn = expireIn;
    }

    public String getExpireIn() {
        return mExpireIn;
    }

    public boolean isValid() {
        return getUserId() != null && getAccessToken() != null & getExpireIn() != null;
    }

}
