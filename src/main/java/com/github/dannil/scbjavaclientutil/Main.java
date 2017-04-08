package com.github.dannil.scbjavaclientutil;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.github.dannil.scbjavaclientutil.contents.SCBTreeStructure;

public class Main {

    public static void main(String[] args) throws InterruptedException, IOException {
        // File inputsLocation = new File("inputs");
        // File treeLocation = new File("local/tree");
        //
        // SCBTableInputs i = new SCBTableInputs(inputsLocation);
        // SCBTableDataSet s = new SCBTableDataSet(treeLocation);
        //
        File jsonLocation = new File("scb_2017-03-31T20-46-59.675_2017-03-31T21-56-23.252_sv.json");

        SCBTreeStructure c = new SCBTreeStructure(new Locale("sv", "SE"));

        Map<String, Integer> levels = c.getLevels(jsonLocation);
        // for (Entry<String, Integer> entry : levels.entrySet()) {
        // System.out.println(entry);
        // }

        // 1. Convert Map to List of Map
        List<Map.Entry<String, Integer>> list = new LinkedList<Map.Entry<String, Integer>>(levels.entrySet());

        // 2. Sort list with Collections.sort(), provide a custom Comparator
        // Try switch the o1 o2 position for a different order
        Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
            @Override
            public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
                return (o1.getValue()).compareTo(o2.getValue());
            }
        });

        for (Map.Entry<String, Integer> entry : list) {
            if (entry.getValue() < 4) {
                System.out.println(entry);
            }

        }
        //
        // // i.getInputs("", jsonLocation);
        //
        // // File f3 = new File("local/values_2016-10-24T21-14-03.307");
        // // s.getStatistics(f3);

        System.out.println("Done!");
    }
}
