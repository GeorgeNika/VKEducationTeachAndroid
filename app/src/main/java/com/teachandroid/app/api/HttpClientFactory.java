package com.teachandroid.app.api;

import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpParams;

public class HttpClientFactory {

    private static HttpClient client;

    public synchronized static HttpClient getThreadSafeClient() {

        if (client != null)
            return client;

        client = new DefaultHttpClient();

        ClientConnectionManager mgr = client.getConnectionManager();

        HttpParams params = client.getParams();
        params.setParameter(CoreProtocolPNames.PROTOCOL_VERSION,
                HttpVersion.HTTP_1_1);
        client = new DefaultHttpClient(
                new ThreadSafeClientConnManager(params,
                        mgr.getSchemeRegistry()), params);

        return client;
    }
}