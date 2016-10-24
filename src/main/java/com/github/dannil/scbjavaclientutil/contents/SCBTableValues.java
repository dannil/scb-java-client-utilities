package com.github.dannil.scbjavaclientutil.contents;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Locale;

import com.github.dannil.scbjavaclientutil.client.IgnorePrependingTableClient;
import com.github.dannil.scbjavaclientutil.model.Entry;

public class SCBTableValues {

	private File baseDir;

	public SCBTableValues(File baseDir) {
		this.baseDir = baseDir;
	}

	public void getValues(String parentTable, Entry child) throws InterruptedException, IOException {
		this.baseDir.mkdir();

		IgnorePrependingTableClient c = new IgnorePrependingTableClient(new Locale("sv", "SE"));
		String response = c.get(parentTable + child.getId() + "/");

		Thread.sleep(1000);

		if (response.contains("variables")) {
			String formattedParent = this.baseDir.toString() + '/' + parentTable.replace('/', '-');

			StringBuilder builder = new StringBuilder(formattedParent);
			builder.append(child.getId());
			builder.append(".json");

			System.out.println("Writing " + builder.toString());

			File file = new File(builder.toString());
			try (FileWriter fw = new FileWriter(file.getAbsoluteFile())) {
				try (BufferedWriter bw = new BufferedWriter(fw)) {
					bw.write(response);
				}
			}
		}

		if (child.getChildren() != null) {
			for (Entry child2 : child.getChildren()) {
				getValues(child.getId() + "/", child2);
			}
		}

	}
}
