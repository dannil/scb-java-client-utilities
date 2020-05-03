package com.github.dannil.scbjavaclientutil.contents;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.TreeSet;

import org.joda.time.DateTime;
import org.joda.time.Duration;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.dannil.scbjavaclient.exception.SCBClientException;
import com.github.dannil.scbjavaclient.format.json.JsonConverter;
import com.github.dannil.scbjavaclientutil.client.IgnorePrependingTableClient;
import com.github.dannil.scbjavaclientutil.files.FileUtility;
import com.github.dannil.scbjavaclientutil.model.Entry;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

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

    public List<Entry> getTree(String table, File file) throws IOException {
        String json = new String(Files.readAllBytes(file.toPath()));
        System.out.println(json);

        JsonElement element = JsonParser.parseString(json);

        for (JsonElement e : element.getAsJsonArray()) {
            JsonObject obj = e.getAsJsonObject();
            if (table == "" || Objects.equals(obj.get("id").getAsString(), table)) {
                JsonElement children = obj.get("children");
                if (children == null) {
                    return null;
                }
                JsonArray array = children.getAsJsonArray();

                String subJson = array.toString();

                Gson gson = new GsonBuilder().create();
                List<Entry> entries = gson.fromJson(subJson, new TypeToken<List<Entry>>() {
                }.getType());

                return entries;
            }
        }
        return null;
    }

    public Collection<String> getTables(String table, File f) throws IOException {
        // convert

        String json = new String(Files.readAllBytes(f.toPath()));

        Gson gson = new GsonBuilder().create();

        List<Entry> entries = gson.fromJson(json, new TypeToken<List<Entry>>() {
        }.getType());

        Entry rootChild = new Entry();
        rootChild.setId(table);
        rootChild.addChildren(entries);

        return getTables("", rootChild);
    }

    public Collection<String> getTables(String table, Entry child) {
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
                ids.addAll(getTables(actualTable, child2));
            }
        }

        return ids;
    }
    
    public List<String> getSubTables(String table) {
        String response = this.client.doGetRequest(this.client.getUrl().toString() + table);
        JsonArray array = JsonParser.parseString(response).getAsJsonArray();
        List<String> subTables = new ArrayList<String>();
        for (JsonElement element : array) {
            JsonObject object = element.getAsJsonObject();
            String id = object.get("id").getAsString();
            subTables.add(id);
        }
        return subTables;
    }

    public File generateFile(String table, DateTime before, DateTime after, List<Entry> entries) throws IOException {
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
        return file;
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

    public Map<String, Integer> getImplementationPriority(File tree, Collection<String> implementedTables)
            throws IOException {
        Collection<String> implementedTablesWithoutLast = new ArrayList<>();
        for (String table : implementedTables) {
            String[] pa = table.split("/");
            String withoutLast = "";
            for (int i = 0; i < pa.length - 1; i++) {
                withoutLast += pa[i] + "/";
            }
            implementedTablesWithoutLast.add(withoutLast);
        }
        Map<String, Integer> priorities = new LinkedHashMap<>();
        Collection<String> allTables = getTables("", tree);
        for (String table : allTables) {
            int weight = 20;
            String[] parts = table.split("/");
            for (int i = parts.length; i > 0; i--) {
                String[] subParts = new String[i];
                System.arraycopy(parts, 0, subParts, 0, i);
                String subTable = "";
                for (int j = 0; j < subParts.length; j++) {
                    subTable += subParts[j] + "/";
                }
                if (implementedTablesWithoutLast.contains(subTable)) {
                    priorities.put(table, weight);
                    break;
                }
                weight--;
            }
            if (!priorities.containsKey(table)) {
                priorities.put(table, weight - parts.length);
            }
        }
        for (String implemented : implementedTables) {
            if (priorities.containsKey(implemented)) {
                priorities.remove(implemented);
            }
        }
        priorities.remove("/");
        return priorities;
    }
    
    public String getParent(String table, int stepsBackwards) {
        String parent = table;
        for (int i = 0; i < stepsBackwards; i++) {
            parent = getParent(parent);
        }
        return parent;
    }
    
    public String getParent(String table) {
        String tableWithoutLast = table;
        if (table.endsWith("/")) {
            tableWithoutLast = table.substring(0, table.length() - 1);
        }
        String directParent = tableWithoutLast;
        if (directParent.contains("/")) {
            directParent = tableWithoutLast.substring(0, tableWithoutLast.lastIndexOf("/") + 1);
        }
        return directParent;
    }

}
