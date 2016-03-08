import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.dannil.scbjavaclient.client.SCBClient;
import com.github.dannil.scbjavaclient.test.Entry;
import com.github.dannil.scbjavaclient.utility.JsonUtility;

public class Test {

	private static Locale locale = new Locale("en");
	private static SCBClient client = new SCBClient(locale);

	public static List<Entry> findChildren() throws InterruptedException {
		System.out.println("findChildren()");

		// List<Entry> lst = findChildren("BE/");
		List<Entry> lst = findChildren("");
		lst.removeAll(Collections.singleton(null));

		return lst;
	}

	public static List<Entry> findChildren(String currentAddress) throws InterruptedException {
		System.out.println("findChildren(String): calling findChildren(String) with address " + currentAddress);

		String response = client.get(currentAddress);

		JsonNode fetched = JsonUtility.getNode(response);

		List<Entry> nodes = new ArrayList<>();

		Iterator<JsonNode> it = fetched.iterator();
		while (it.hasNext()) {
			JsonNode next = it.next();
			if (next.has("id")) {
				System.out.println("Node has id");

				Entry e = new Entry();
				e.setId(next.get("id").asText());
				e.setText(next.get("text").asText());

				Thread.sleep(1000);

				nodes.add(e);
				List<Entry> lst = findChildren(currentAddress + next.get("id").asText() + "/");
				// System.out.println("Nodes: " + nodes);
				if (lst.size() != 0) {
					e.addChildren(lst);
				}

			}
		}
		// System.out.println(nodes);
		return nodes;
	}

	public static void main(String[] args) throws InterruptedException, JsonProcessingException, FileNotFoundException,
			UnsupportedEncodingException {

		List<Entry> children = findChildren();
		System.out.println("Final list: " + children);

		ObjectMapper mapper = new ObjectMapper();
		mapper.setSerializationInclusion(Include.NON_NULL);
		JsonNode res = mapper.convertValue(children, JsonNode.class);
		System.out.println(res);

		String indented = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(children);
		PrintWriter writer = new PrintWriter("scb.json", "UTF-8");
		writer.print(indented);
		writer.close();
	}

}
