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
    public String get(String url) {
        return super.get(url);
    }

    @Override
    public String post(String url, String query) {
        return super.post(url, query);
    }

    @Override
    public String getUrl() {
        // TODO Auto-generated method stub
        return null;
    }

}
