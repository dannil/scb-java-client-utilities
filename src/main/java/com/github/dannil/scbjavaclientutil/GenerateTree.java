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
        Collection<Locale> locales = new ArrayList<>();
        locales.add(new Locale("sv", "SE"));
        locales.add(new Locale("en", "US"));
        
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

        for (Locale locale : locales) {
            // Run tree generation for current locale
            System.out.println("Now running tree generation for locale [" + locale.getLanguage() + "]");
            File fileFullTree = generateFullTree(locale);
            // Generate sub tree files for current locale
            for (String table : subTables) {
                generateSubTree(locale, fileFullTree, table);
            }
        }

        // // Run tree generation for Swedish locale
        // Locale localeSv = new Locale("sv", "SE");
        // File fileFullTreeSv = generateFullTree(localeSv);
        // // Generate sub tree files for Swedish locale
        // for (String table : subTables) {
        // generateSubTree(localeSv, fileFullTreeSv, table);
        // }

        // // Run tree generation for English locale
        // Locale localeEn = new Locale("en", "US");
        // File fileFullTreeEn = generateFullTree(localeEn);
        // // Generate sub tree files for English locale
        // for (String table : subTables) {
        // generateSubTree(localeEn, fileFullTreeEn, table);
        // }

        // SCBTreeStructure c = new SCBTreeStructure(new Locale("sv", "SE"));
        // // Run tree generation for Swedish locale
        // DateTime beforeSv = DateTime.now();
        // List<Entry> childrenSv = c.getTree("");
        // DateTime afterSv = DateTime.now();
        // File fileSv = c.generateFile("", beforeSv, afterSv, childrenSv);
        //
        // // Run tree generation for English locale
        // c = new SCBTreeStructure(new Locale("en", "US"));
        // DateTime beforeEn = DateTime.now();
        // List<Entry> childrenEn = c.getTree("");
        // DateTime afterEn = DateTime.now();
        // File fileEn = c.generateFile("", beforeEn, afterEn, childrenEn);
        //
        // Generate sub tree files for Swedish locale
        // c = new SCBTreeStructure(new Locale("sv", "SE"));
        // for (String table : subTables) {
        // DateTime before = DateTime.now();
        // File f = new
        // File("scb_2019-03-02T06-55-21.211_2019-03-02T07-50-47.593_sv.json");
        // List<Entry> entries = c.getTree(table, f);
        // DateTime after = DateTime.now();
        // if (entries != null) {
        // c.generateFile(table, before, after, entries);
        // }
        // }

        // Generate sub tree files for English locale
        // c = new SCBTreeStructure(new Locale("en", "US"));
        // for (String table : subTables) {
        // DateTime before = DateTime.now();
        // File f = new
        // File("scb_2019-03-02T07-50-47.689_2019-03-02T08-23-51.947_en.json");
        // List<Entry> entries = c.getTree(table, f);
        // DateTime after = DateTime.now();
        // if (entries != null) {
        // c.generateFile(table, before, after, entries);
        // }
        // }
    }

    public static File generateFullTree(Locale locale) throws IOException, InterruptedException {
        SCBTreeStructure c = new SCBTreeStructure(locale);

        // Run tree generation for the specified locale
        DateTime before = DateTime.now();
        List<Entry> children = c.getTree("");
        DateTime after = DateTime.now();
        return c.generateFile("", before, after, children);
    }

    public static File generateSubTree(Locale locale, File file, String table)
            throws IOException, InterruptedException {
        SCBTreeStructure c = new SCBTreeStructure(locale);

        // Generate sub tree file the specified locale
        DateTime beforeSubTable = DateTime.now();
        List<Entry> entries = c.getTree(table, file);
        DateTime afterSubTable = DateTime.now();
        if (entries != null) {
            return c.generateFile(table, beforeSubTable, afterSubTable, entries);
        }
        return null;
    }

}
