package com.github.dannil.scbjavaclientutil;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import com.github.dannil.scbjavaclientutil.contents.SCBTableValues;
import com.github.dannil.scbjavaclientutil.contents.SCBTreeStructure;

public class Main {

	public static void main(String[] args) throws InterruptedException, IOException {
		SCBTreeStructure c = new SCBTreeStructure();

		String table = "TK";

		// DateTime before = DateTime.now();
		// List<Entry> children = c.getTableOfContents(table);
		// DateTime after = DateTime.now();
		//
		// c.generateFile(table, before, after, children);

		File f = new File("values");

		// Entry rootChild = new Entry();
		// rootChild.setId(table);
		// rootChild.addChildren(children);

		SCBTableValues v = new SCBTableValues(f);
		// v.getValues("", rootChild);

		File jsonLocation = new File("scb_2016-10-24T17-49-41.124_2016-10-24T19-25-10.077.json");
		String json = new String(Files.readAllBytes(jsonLocation.toPath()));

		v.getValues("", json);

		System.out.println("Done!");
	}
}
