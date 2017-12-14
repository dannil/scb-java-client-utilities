package com.github.dannil.scbjavaclientutil;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

import com.github.dannil.scbjavaclientutil.contents.SCBTreeStructure;
import com.github.dannil.scbjavaclientutil.model.Entry;

import org.joda.time.DateTime;

public class GenerateTree {

    public static void main(String[] args) throws IOException, InterruptedException {
        Collection<String> subTables = new ArrayList<>();
        subTables.add("AM");
        subTables.add("BO");
        subTables.add("FM");
        subTables.add("HE");
        subTables.add("JO");
        subTables.add("LE");
        subTables.add("MI");
        subTables.add("NV");
        subTables.add("PR");
        subTables.add("SO");
        subTables.add("UF");
        subTables.add("AA");
        subTables.add("BE");
        subTables.add("EN");
        subTables.add("HA");
        subTables.add("HS");
        subTables.add("KU");
        subTables.add("ME");
        subTables.add("NR");
        subTables.add("OE");
        subTables.add("SF");
        subTables.add("TK");

        SCBTreeStructure c = new SCBTreeStructure(new Locale("sv", "SE"));
        // // Run tree generation for Swedish locale
        // DateTime beforeSv = DateTime.now();
        // List<Entry> childrenSv = c.getTree("");
        // DateTime afterSv = DateTime.now();
        // c.generateFile("", beforeSv, afterSv, childrenSv);
        //
        // // Run tree generation for English locale
        // c = new SCBTreeStructure(new Locale("en", "US"));
        // DateTime beforeEn = DateTime.now();
        // List<Entry> childrenEn = c.getTree("");
        // DateTime afterEn = DateTime.now();
        // c.generateFile("", beforeEn, afterEn, childrenEn);

        // Generate sub tree files for Swedish locale
        c = new SCBTreeStructure(new Locale("sv", "SE"));
        for (String table : subTables) {
            DateTime before = DateTime.now();
            File f = new File("scb_2017-08-31T09-47-02.011_2017-08-31T10-38-41.491_sv.json");
            List<Entry> entries = c.getTree(table, f);
            DateTime after = DateTime.now();
            if (entries != null) {
                c.generateFile(table, before, after, entries);
            }
        }

        // Generate sub tree files for English locale
        c = new SCBTreeStructure(new Locale("en", "US"));
        for (String table : subTables) {
            DateTime before = DateTime.now();
            File f = new File("scb_2017-08-31T10-38-41.588_2017-08-31T11-07-17.550_en.json");
            List<Entry> entries = c.getTree(table, f);
            DateTime after = DateTime.now();
            if (entries != null) {
                c.generateFile(table, before, after, entries);
            }
        }
    }

}
