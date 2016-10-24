package com.github.dannil.scbjavaclientutil.contents;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import org.joda.time.DateTime;
import org.joda.time.Duration;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.dannil.scbjavaclient.exception.SCBClientException;
import com.github.dannil.scbjavaclient.utility.JsonUtility;
import com.github.dannil.scbjavaclientutil.client.IgnorePrependingTableClient;
import com.github.dannil.scbjavaclientutil.files.FileUtility;
import com.github.dannil.scbjavaclientutil.model.Entry;

public class SCBTreeStructure {

	public List<Entry> getTableOfContents(String currentAddress) throws InterruptedException {
		System.out.println("findChildren(String): calling findChildren(String) with address " + currentAddress);

		IgnorePrependingTableClient client = new IgnorePrependingTableClient(new Locale("sv", "SE"));

		String response = null;
		try {
			response = client.get(currentAddress + "/");
		} catch (SCBClientException e) {
			System.err.println(e.getMessage());
			return new ArrayList<Entry>();
		}

		JsonNode fetched = JsonUtility.toNode(response);

		List<Entry> entries = new ArrayList<Entry>();
		Iterator<JsonNode> iterator = fetched.iterator();
		while (iterator.hasNext()) {
			JsonNode next = iterator.next();
			if (next.has("id")) {
				Entry entry = new Entry();
				entry.setId(next.get("id").asText());
				entry.setText(next.get("text").asText());

				Thread.sleep(1000);

				List<Entry> children = getTableOfContents(currentAddress + "/" + entry.getId());
				if (children.size() > 0) {
					entry.addChildren(children);
				}
				entries.add(entry);
			}
		}

		return entries;
	}

	public void generateFile(String table, DateTime before, DateTime after, List<Entry> entries) throws IOException {
		System.out.println("Final list: " + entries);

		Duration duration = new Duration(before, after);
		long minutes = duration.getStandardMinutes();
		System.out.println("Elapsed time (minutes) : " + minutes);

		StringBuilder builder = new StringBuilder(64);
		builder.append("scb");
		builder.append("_");

		if (table.length() > 0) {
			builder.append(table.replace('/', '-').substring(0, table.length() - 1));
			builder.append("_");
		}

		builder.append(before.toLocalDateTime().toString().replace(':', '-'));
		builder.append("_");
		builder.append(after.toLocalDateTime().toString().replace(':', '-'));
		builder.append(".json");

		File file = new File(builder.toString());
		FileUtility.writeToSystem(file, entries);
	}

}
