package com.github.dannil.scbjavaclientutil.client;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Locale;

import com.github.dannil.scbjavaclient.client.AbstractClient;
import com.github.dannil.scbjavaclient.communication.URLEndpoint;

public class IgnorePrependingTableClient extends AbstractClient {

    public IgnorePrependingTableClient() {
        super();
    }

    public IgnorePrependingTableClient(Locale locale) {
        super(locale);
    }

    @Override
    public String doGetRequest(String url) {
        return super.doGetRequest(escapeTableUrl(url).toString());
    }

    @Override
    public String doPostRequest(String url, String query) {
        return super.doPostRequest(escapeTableUrl(url).toString(), query);
    }

    @Override
    public URLEndpoint getUrl() {
        return getRootUrl();
    }
    
    private URLEndpoint escapeTableUrl(String url) {
        try {
            URLEndpoint urlEndpoint = new URLEndpoint(url);
            String tablePart = urlEndpoint.getTable();
            String encodedTablePart = URLEncoder.encode(urlEndpoint.getTable(), "UTF-8");
            String rootPart = urlEndpoint.toString().substring(0, urlEndpoint.toString().length() - tablePart.length());
            return new URLEndpoint(rootPart + encodedTablePart);
        } catch (UnsupportedEncodingException e) {
            throw new UnsupportedOperationException(e);
        }
    }

}
