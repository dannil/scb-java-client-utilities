package com.github.dannil.scbjavaclientutil.client;

import java.util.Locale;

import com.github.dannil.scbjavaclient.client.AbstractClient;

public class IgnorePrependingTableClient extends AbstractClient {

    public IgnorePrependingTableClient() {
        super();
    }

    public IgnorePrependingTableClient(Locale locale) {
        super(locale);
    }

    @Override
    public String doGetRequest(String url) {
        return super.doGetRequest(url);
    }

    @Override
    public String doPostRequest(String url, String query) {
        return super.doPostRequest(url, query);
    }

    @Override
    public String getUrl() {
        return getRootUrl();
    }

}
