package com.github.dannil.scbjavaclientutil;

import com.github.dannil.scbjavaclient.client.SCBClient;

public class Test {

	public static void main(String[] args) {
		// AbstractRequester req = RequesterFactory.getRequester(RequestMethod.GET);
		//
		// String json = req.getBodyAsStringFromTable("BE/BE0101/BE0101B/BefolkningMedelAlder");
		// Map<String, List<String>> inputs = JsonUtility.getInputs(json);
		//
		// StringBuilder builder = new StringBuilder();
		// for (Entry<String, List<String>> entry : inputs.entrySet()) {
		// String key = entry.getKey();
		// List<String> value = entry.getValue();
		//
		// builder.append(key);
		// builder.append(": ");
		//
		// for (String s : value) {
		// builder.append(s);
		// builder.append(", ");
		// }
		// }
		// System.out.println(builder.toString());

		SCBClient c = new SCBClient();

		// Map<String, Collection<?>> inputs = new HashMap<String, Collection<?>>();
		// inputs.put("ContentsCode", Arrays.asList("BE0101N1", "BE0101N2"));
		// inputs.put("Region", Arrays.asList("0114"));
		// inputs.put("Civilstand", Arrays.asList("OG"));
		// inputs.put("Alder", Arrays.asList(45, 50, 52));
		// inputs.put("Kon", null);
		// inputs.put("Tid", Arrays.asList(2011));
		//
		// String raw = c.getRawData("BE/BE0101/BE0101A/BefolkningNy/", inputs);
		// String conventional = JsonUtility.toConventionalJson(raw).toString();
		//
		// System.out.println(raw);
		// System.out.println(conventional);
	}
}
