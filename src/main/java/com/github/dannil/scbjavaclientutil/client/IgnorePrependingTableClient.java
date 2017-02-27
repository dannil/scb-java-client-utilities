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
    public String getRequest(String url) {
        return super.getRequest(url);
    }

    @Override
    public String postRequest(String url, String query) {
        return super.postRequest(url, query);
    }

    @Override
    public String getUrl() {
        return getRootUrl();
    }

}
