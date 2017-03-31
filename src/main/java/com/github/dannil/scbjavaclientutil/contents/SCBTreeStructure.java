package com.github.dannil.scbjavaclientutil.contents;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.dannil.scbjavaclient.exception.SCBClientException;
import com.github.dannil.scbjavaclient.format.json.JsonConverter;
import com.github.dannil.scbjavaclientutil.client.IgnorePrependingTableClient;
import com.github.dannil.scbjavaclientutil.files.FileUtility;
import com.github.dannil.scbjavaclientutil.model.Entry;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.joda.time.DateTime;
import org.joda.time.Duration;

public class SCBTreeStructure {

    private IgnorePrependingTableClient client;

    public SCBTreeStructure(Locale locale) {
        this.client = new IgnorePrependingTableClient(locale);
    }

    public List<Entry> getTableOfContents(String currentAddress) throws InterruptedException {
        System.out.println("getTableOfContents(String): calling getTableOfContents(" + currentAddress + ") ["
                + this.client.getLocale().getLanguage() + "]");

        if (currentAddress.length() > 0) {
            currentAddress = currentAddress + '/';
        }

        String response = null;
        try {
            response = this.client.doGetRequest(client.getUrl() + currentAddress);
        } catch (SCBClientException e) {
            System.err.println(e.getMessage());
            return new ArrayList<Entry>();
        }

        JsonNode fetched = new JsonConverter().toNode(response);

        List<Entry> entries = new ArrayList<Entry>();
        Iterator<JsonNode> iterator = fetched.iterator();
        while (iterator.hasNext()) {
            JsonNode next = iterator.next();
            if (next.has("id")) {
                Entry entry = new Entry();
                entry.setId(next.get("id").asText());
                entry.setText(next.get("text").asText());

                Thread.sleep(1000);

                String nextTable = currentAddress + entry.getId();
                List<Entry> children = getTableOfContents(nextTable);
                if (children.size() > 0) {
                    entry.addChildren(children);
                }
                entries.add(entry);
            }
        }
        return entries;
    }

    public void generateFile(String table, DateTime before, DateTime after, List<Entry> entries) throws IOException {
        Duration duration = new Duration(before, after);
        long minutes = duration.getStandardMinutes();
        System.out.println("Elapsed time (minutes): " + minutes);

        StringBuilder builder = new StringBuilder(64);
        builder.append("scb");
        builder.append("_");

        if (table.length() > 0) {
            builder.append(table.replace('/', '-'));
            builder.append("_");
        }

        builder.append(before.toLocalDateTime().toString().replace(':', '-'));
        builder.append("_");
        builder.append(after.toLocalDateTime().toString().replace(':', '-'));
        builder.append("_");
        builder.append(this.client.getLocale().getLanguage());
        builder.append(".json");

        System.out.println("Writing " + builder.toString());
        File file = new File(builder.toString());
        FileUtility.writeToSystem(file, entries);
    }

    public Map<String, Integer> getLevels(File file) throws IOException {
        String json = new String(Files.readAllBytes(file.toPath()));

        Gson gson = new GsonBuilder().create();

        List<Entry> entries = gson.fromJson(json, new TypeToken<List<Entry>>() {
        }.getType());

        Entry rootChild = new Entry();
        rootChild.setId("");
        rootChild.addChildren(entries);

        return getLevels(rootChild, "", 0);
    }

    private Map<String, Integer> getLevels(Entry entry, String previousTable, int level) throws IOException {
        Map<String, Integer> map = new HashMap<>();

        if (entry.getChildren() != null) {
            for (Entry e : entry.getChildren()) {
                String altered = previousTable;
                if (previousTable.length() > 0) {
                    altered = previousTable + "/";
                }
                map.putAll(getLevels(e, altered + e.getId(), level + 1));
            }
        } else {
            map.put(previousTable, level);
        }

        return map;
    }

}
