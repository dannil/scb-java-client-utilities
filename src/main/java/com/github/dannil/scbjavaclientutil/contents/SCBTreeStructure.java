package com.github.dannil.scbjavaclientutil.contents;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeSet;

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

    public List<Entry> getTree(String table) throws InterruptedException {
        System.out.println(
                "getTree(String): calling getTree(" + table + ") [" + this.client.getLocale().getLanguage() + "]");

        if (table.length() > 0) {
            table = table + '/';
        }

        String response = null;
        try {
            response = this.client.doGetRequest(client.getUrl() + table);
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

                Thread.sleep(600);

                String nextTable = table + entry.getId();
                List<Entry> children = getTree(nextTable);
                if (children.size() > 0) {
                    entry.addChildren(children);
                }
                entries.add(entry);
            }
        }
        return entries;
    }

    // public List<Entry> getTree(String table, File file) throws IOException {
    // String json = new String(Files.readAllBytes(file.toPath()));
    // System.out.println(json);
    //
    // JsonParser parser = new JsonParser();
    // JsonElement element = parser.parse(json);
    //
    // for (JsonElement e : element.getAsJsonArray()) {
    // JsonObject obj = e.getAsJsonObject();
    // System.out.println(obj);
    // if (table == "" || Objects.equals(obj.get("id").getAsString(), table)) {
    // JsonElement children = obj.get("children");
    // if (children == null) {
    // return null;
    // }
    // JsonArray array = children.getAsJsonArray();
    //
    // String subJson = array.toString();
    //
    // Gson gson = new GsonBuilder().create();
    // List<Entry> entries = gson.fromJson(subJson, new TypeToken<List<Entry>>() {
    // }.getType());
    //
    // return entries;
    // }
    // }
    // return null;
    // }

    public Collection<String> getTree2(String table, File f) throws IOException {
        // convert

        String json = new String(Files.readAllBytes(f.toPath()));

        Gson gson = new GsonBuilder().create();

        List<Entry> entries = gson.fromJson(json, new TypeToken<List<Entry>>() {
        }.getType());

        Entry rootChild = new Entry();
        rootChild.setId(table);
        rootChild.addChildren(entries);

        return getTree2("", rootChild);
    }

    public Collection<String> getTree2(String table, Entry child) {
        // if (this.baseDirWithDate == null) {
        // DateTime now = DateTime.now();
        // String formattedNow = now.toLocalDateTime().toString().replace(':', '-');
        //
        // this.baseDirWithDate = new File(this.baseDir.getCanonicalPath() + "_" +
        // formattedNow);
        // this.baseDirWithDate.mkdir();
        // }
        //

        Collection<String> ids = new TreeSet<String>(Collator.getInstance());
        String actualTable = table;
        if (table.length() > 0) {
            actualTable += "/" + child.getId();
        } else {
            actualTable += child.getId();
        }
        ids.add(actualTable + "/");

        if (child.getChildren() != null) {
            for (Entry child2 : child.getChildren()) {
                ids.addAll(getTree2(actualTable, child2));
            }
        }

        return ids;
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
