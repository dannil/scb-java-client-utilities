import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.dannil.scbjavaclient.client.AbstractClient;
import com.github.dannil.scbjavaclient.utility.JsonUtility;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Test {

	private static Locale locale = new Locale("en");
	private static DummyClient client = new DummyClient(locale);

	private static class DummyClient extends AbstractClient {

		public DummyClient() {
			super();
		}

		public DummyClient(Locale locale) {
			super(locale);
		}

		public String get(String address) {
			return super.get(address);
		}

		public String post(String address, String query) {
			return super.post(address, query);
		}

	}

	// public static List<Entry> findChildren() throws InterruptedException {
	// System.out.println("findChildren()");
	//
	// List<Entry> lst = findChildren("BE/");
	// // List<Entry> lst = findChildren("");
	// lst.removeAll(Collections.singleton(null));
	//
	// return lst;
	// }

	public static List<Entry> findChildren(String currentAddress) throws InterruptedException {
		System.out.println("findChildren(String): calling findChildren(String) with address " + currentAddress);

		String response = client.get(currentAddress);
		JsonNode fetched = JsonUtility.getNode(response);

		List<Entry> nodes = new ArrayList<>();
		Iterator<JsonNode> it = fetched.iterator();
		while (it.hasNext()) {
			JsonNode next = it.next();
			if (next.has("id")) {
				Thread.sleep(1000);

				Entry e = new Entry();
				e.setId(next.get("id").asText());
				e.setText(next.get("text").asText());

				nodes.add(e);
				List<Entry> children = findChildren(currentAddress + next.get("id").asText() + "/");
				if (children.size() != 0) {
					e.addChildren(children);
				}
			}
		}
		return nodes;
	}

	public static void main(String[] args) throws InterruptedException, IOException {
		List<Entry> children = findChildren("BE/");
		System.out.println("Final list: " + children);

		Gson gson = new GsonBuilder().setPrettyPrinting().create();

		File file = new File("scb.json");
		try (FileWriter fw = new FileWriter(file.getAbsoluteFile())) {
			try (BufferedWriter bw = new BufferedWriter(fw)) {
				String content = gson.toJson(children, List.class);
				bw.write(content);
			}
		}
	}
}
