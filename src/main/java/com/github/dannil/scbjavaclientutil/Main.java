package com.github.dannil.scbjavaclientutil;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.joda.time.DateTime;

import com.github.dannil.scbjavaclientutil.contents.SCBTableValues;
import com.github.dannil.scbjavaclientutil.contents.SCBTreeStructure;
import com.github.dannil.scbjavaclientutil.model.Entry;

public class Main {

	public static void main(String[] args) throws InterruptedException, IOException {
		SCBTreeStructure c = new SCBTreeStructure();

		String table = "BO";

		String url = table.concat("/");

		DateTime before = DateTime.now();
		List<Entry> children = c.getTableOfContents(url);
		DateTime after = DateTime.now();

		c.generateFile(url, before, after, children);

		File f = new File("values");

		Entry rootChild = new Entry();
		rootChild.setId(table);
		rootChild.addChildren(children);

		SCBTableValues v = new SCBTableValues(f);
		v.getValues("", rootChild);

		System.out.println("Done!");
	}

}
