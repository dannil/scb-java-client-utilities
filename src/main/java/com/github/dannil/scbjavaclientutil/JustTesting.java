package com.github.dannil.scbjavaclientutil;

import java.util.Map;

import com.github.dannil.scbjavaclient.client.SCBClient;
import com.github.dannil.scbjavaclientutil.client.IgnorePrependingTableClient;

public class JustTesting {

    public static void main(String[] args) {
        SCBClient c1 = new SCBClient();
        Map<String, String> config = c1.getConfig();
        System.out.println(config);

        IgnorePrependingTableClient c2 = new IgnorePrependingTableClient();
        c2.doGetRequest("https://api.scb.se/OV0104/v1/doris/sv/ssd/LE/LE0201/LE0201Hälsa/");
    }

}
