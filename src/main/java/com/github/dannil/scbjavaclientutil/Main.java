package com.github.dannil.scbjavaclientutil;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import com.github.dannil.scbjavaclientutil.contents.SCBTableContents;
import com.github.dannil.scbjavaclientutil.contents.SCBTableInputs;

public class Main {

    public static void main(String[] args) throws InterruptedException, IOException {
        // Collection<String> rootTables = new ArrayList<>();
        // rootTables.add("");
        // rootTables.add("AM");
        // rootTables.add("BO");
        // rootTables.add("FM");
        // rootTables.add("HE");
        // rootTables.add("JO");
        // rootTables.add("LE");
        // rootTables.add("MI");
        // rootTables.add("NV");
        // rootTables.add("PR");
        // rootTables.add("SO");
        // rootTables.add("UF");
        // rootTables.add("AA");
        // rootTables.add("BE");
        // rootTables.add("EN");
        // rootTables.add("HA");
        // rootTables.add("HS");
        // rootTables.add("KU");
        // rootTables.add("ME");
        // rootTables.add("NR");
        // rootTables.add("OE");
        // rootTables.add("SF");
        // rootTables.add("TK");
        //
        // for (String table : rootTables) {
        // // String table = "BO";
        //
        // SCBTreeStructure c = new SCBTreeStructure(new Locale("sv", "SE"));
        // DateTime before = DateTime.now();
        // List<Entry> children = c.getTableOfContents(table);
        // DateTime after = DateTime.now();
        // c.generateFile(table, before, after, children);
        //
        // c = new SCBTreeStructure(new Locale("en", "US"));
        // before = DateTime.now();
        // children = c.getTableOfContents(table);
        // after = DateTime.now();
        // c.generateFile(table, before, after, children);
        // }

        File inputsLocation = new File("inputs");
        File contentsDestination = new File("local/contents");

        SCBTableInputs i = new SCBTableInputs(inputsLocation);
        SCBTableContents s = new SCBTableContents(contentsDestination);

        // Entry rootChild = new Entry();
        // rootChild.setId("");
        // rootChild.addChildren(children);
        //
        // v.getValues("", rootChild);

        File jsonLocation = new File("scb_2017-02-16T01-15-09.870_2017-02-16T02-56-47.768_sv.json");
        String json = new String(Files.readAllBytes(jsonLocation.toPath()));

        i.getInputs("", json);

        // File f3 = new File("local/values_2016-10-24T21-14-03.307");
        // s.getStatistics(f3);

        System.out.println("Done!");
    }
}
