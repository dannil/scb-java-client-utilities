package com.github.dannil.scbjavaclientutil;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
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

        for (Locale locale : locales) {
            // Run tree generation for current locale
            System.out.println("Now running tree generation for locale [" + locale.getLanguage() + "]");
            List<String> subTables = getSubTables(locale, "");
            Collections.sort(subTables);
            File fileFullTree = generateFullTree(locale);
            // Generate sub tree files for current locale
            for (String table : subTables) {
                generateSubTree(locale, fileFullTree, table);
            }
        }
    }
    
    public static List<String> getSubTables(Locale locale, String table) {
        SCBTreeStructure c = new SCBTreeStructure(locale);
        return c.getSubTables(table);
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
