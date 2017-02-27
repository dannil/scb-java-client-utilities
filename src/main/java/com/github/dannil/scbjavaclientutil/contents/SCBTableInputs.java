package com.github.dannil.scbjavaclientutil.contents;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

import com.github.dannil.scbjavaclient.exception.SCBClientNotFoundException;
import com.github.dannil.scbjavaclientutil.client.IgnorePrependingTableClient;
import com.github.dannil.scbjavaclientutil.files.FileUtility;
import com.github.dannil.scbjavaclientutil.model.Entry;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.joda.time.DateTime;

public class SCBTableInputs {

    private File baseDir;

    private File baseDirWithDate;

    public SCBTableInputs(File baseDir) {
        this.baseDir = baseDir;
    }

    public void getInputs(String table, String json) throws InterruptedException, IOException {
        // convert

        Gson gson = new GsonBuilder().create();

        List<Entry> entries = gson.fromJson(json, new TypeToken<List<Entry>>() {
        }.getType());

        Entry rootChild = new Entry();
        rootChild.setId(table);
        rootChild.addChildren(entries);

        getInputs("", rootChild);
    }

    public void getInputs(String table, Entry child) throws InterruptedException, IOException {
        if (this.baseDirWithDate == null) {
            DateTime now = DateTime.now();
            String formattedNow = now.toLocalDateTime().toString().replace(':', '-');

            this.baseDirWithDate = new File(this.baseDir.getCanonicalPath() + "_" + formattedNow);
            this.baseDirWithDate.mkdir();
        }

        String actualTable = table;
        if (table.length() > 0) {
            actualTable += "/" + child.getId();
        } else {
            actualTable += child.getId();
        }

        String response = "";
        try {
            IgnorePrependingTableClient c = new IgnorePrependingTableClient(new Locale("sv", "SE"));
            //
            // System.out.println("getInputs(String, Entry): calling getInputs(" +
            // actualTable + ") ["
            // + c.getLocale().getLanguage() + "]");

            response = c.getRequest(c.getUrl() + actualTable);
            if (response.contains("variables")) {
                String formattedParent = this.baseDirWithDate.toString() + "/" + actualTable.replace('/', '-');

                StringBuilder builder = new StringBuilder(formattedParent);
                builder.append(".json");

                System.out.println("Writing " + builder.toString());

                File file = new File(builder.toString());
                FileUtility.writeToSystem(file, response);
            }
        } catch (SCBClientNotFoundException e) {
            System.err.println(e);
        }

        Thread.sleep(1000);

        if (child.getChildren() != null) {
            for (Entry child2 : child.getChildren()) {
                getInputs(actualTable, child2);
            }
        }

    }
}
