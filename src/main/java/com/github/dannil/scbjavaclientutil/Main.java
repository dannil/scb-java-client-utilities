package com.github.dannil.scbjavaclientutil;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

import com.github.dannil.scbjavaclientutil.contents.SCBTableStatistics;
import com.github.dannil.scbjavaclientutil.contents.SCBTableValues;
import com.github.dannil.scbjavaclientutil.contents.SCBTreeStructure;
import com.github.dannil.scbjavaclientutil.model.Entry;

import org.joda.time.DateTime;

public class Main {

    public static void main(String[] args) throws InterruptedException, IOException {
        String table = "BO";

        SCBTreeStructure c = new SCBTreeStructure(new Locale("sv", "SE"));
        DateTime before = DateTime.now();
        List<Entry> children = c.getTableOfContents(table);
        DateTime after = DateTime.now();
        c.generateFile(table, before, after, children);

        c = new SCBTreeStructure(new Locale("en", "US"));
        before = DateTime.now();
        children = c.getTableOfContents(table);
        after = DateTime.now();
        c.generateFile(table, before, after, children);

        File f = new File("values");
        File statisticsDestination = new File("local/statistics");

        SCBTableValues v = new SCBTableValues(f);
        SCBTableStatistics s = new SCBTableStatistics(statisticsDestination);

        // Entry rootChild = new Entry();
        // rootChild.setId(table);
        // rootChild.addChildren(children);

        // v.getValues("", rootChild);

        // File jsonLocation = new
        // File("scb_2016-10-24T17-49-41.124_2016-10-24T19-25-10.077.json");
        // String json = new String(Files.readAllBytes(jsonLocation.toPath()));
        //
        // v.getValues("", json);

        // File f3 = new File("local/values_2016-10-24T21-14-03.307");
        // s.getStatistics(f3);

        System.out.println("Done!");
    }
}
