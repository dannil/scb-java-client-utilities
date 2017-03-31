package com.github.dannil.scbjavaclientutil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

import com.github.dannil.scbjavaclientutil.contents.SCBTreeStructure;
import com.github.dannil.scbjavaclientutil.model.Entry;

import org.joda.time.DateTime;

public class Main {

    public static void main(String[] args) throws InterruptedException, IOException {
        Collection<String> rootTables = new ArrayList<>();
        // rootTables.add("");
        rootTables.add("AM");
        rootTables.add("BO");
        rootTables.add("FM");
        rootTables.add("HE");
        rootTables.add("JO");
        rootTables.add("LE");
        rootTables.add("MI");
        rootTables.add("NV");
        rootTables.add("PR");
        rootTables.add("SO");
        rootTables.add("UF");
        rootTables.add("AA");
        rootTables.add("BE");
        rootTables.add("EN");
        rootTables.add("HA");
        rootTables.add("HS");
        rootTables.add("KU");
        rootTables.add("ME");
        rootTables.add("NR");
        rootTables.add("OE");
        rootTables.add("SF");
        rootTables.add("TK");

        for (String table : rootTables) {
            // String table = "BO";

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
        }

        // File inputsLocation = new File("inputs");
        // File treeLocation = new File("local/tree");
        //
        // SCBTableInputs i = new SCBTableInputs(inputsLocation);
        // SCBTableDataSet s = new SCBTableDataSet(treeLocation);
        //
        // File jsonLocation = new
        // File("scb_2017-02-16T01-15-09.870_2017-02-16T02-56-47.768_sv.json");
        //
        // SCBTreeStructure c = new SCBTreeStructure(new Locale("sv", "SE"));
        //
        // Map<String, Integer> levels = c.getLevels(jsonLocation);
        // // for (Entry<String, Integer> entry : levels.entrySet()) {
        // // System.out.println(entry);
        // // }
        //
        // // 1. Convert Map to List of Map
        // List<Map.Entry<String, Integer>> list = new LinkedList<Map.Entry<String,
        // Integer>>(levels.entrySet());
        //
        // // 2. Sort list with Collections.sort(), provide a custom Comparator
        // // Try switch the o1 o2 position for a different order
        // Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
        // @Override
        // public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer>
        // o2) {
        // return (o1.getValue()).compareTo(o2.getValue());
        // }
        // });
        //
        // for (Entry<String, Integer> entry : list) {
        // if (entry.getValue() < 4) {
        // System.out.println(entry);
        // }
        //
        // }
        //
        // // i.getInputs("", jsonLocation);
        //
        // // File f3 = new File("local/values_2016-10-24T21-14-03.307");
        // // s.getStatistics(f3);

        System.out.println("Done!");
    }
}
