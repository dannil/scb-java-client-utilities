package com.github.dannil.scbjavaclientutil.json;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import org.joda.time.DateTime;
import org.joda.time.Duration;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.dannil.scbjavaclient.client.AbstractClient;
import com.github.dannil.scbjavaclient.exception.SCBClientException;
import com.github.dannil.scbjavaclient.utility.JsonUtility;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class APITree {

	private static Locale locale = new Locale("sv");
	private static DummyClient client = new DummyClient(locale);

	private static class DummyClient extends AbstractClient {

		public DummyClient() {
			super();
		}

		public DummyClient(Locale locale) {
			super(locale);
		}

		public String get(String url) {
			return super.get(url);
		}

		public String post(String url, String query) {
			return super.post(url, query);
		}

	}

	public static List<Entry> findChildren(String currentAddress) throws InterruptedException {
		System.out.println("findChildren(String): calling findChildren(String) with address " + currentAddress);

		String response = null;
		try {
			response = client.get(currentAddress);
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

				List<Entry> children = findChildren(currentAddress + entry.getId() + "/");
				if (children.size() > 0) {
					entry.addChildren(children);
				}
				entries.add(entry);
			}
		}
		return entries;
	}

	public static void main(String[] args) throws InterruptedException, IOException {
		String table = "";

		DateTime before = DateTime.now();
		List<Entry> children = findChildren(table);
		DateTime after = DateTime.now();

		System.out.println("Final list: " + children);

		Duration duration = new Duration(before, after);
		long minutes = duration.getStandardMinutes();
		System.out.println("Elapsed time (minutes) : " + minutes);

		StringBuilder builder = new StringBuilder(table.length() * 2);
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
		try (FileWriter fw = new FileWriter(file.getAbsoluteFile())) {
			try (BufferedWriter bw = new BufferedWriter(fw)) {
				Gson gson = new GsonBuilder().setPrettyPrinting().create();

				String content = gson.toJson(children, List.class);
				bw.write(content);
			}
		}
	}

}
