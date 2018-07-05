package com.github.dannil.scbjavaclientutil;

import java.util.Map;

import com.github.dannil.scbjavaclient.client.SCBClient;

public class JustTesting {

    public static void main(String[] args) {
        // TODO Auto-generated method stub

        SCBClient c = new SCBClient();
        Map<String, String> config = c.getConfig();
        System.out.println(config);
        
    }

}
