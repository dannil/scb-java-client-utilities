package com.github.dannil.scbjavaclientutil.contents;

import java.io.File;
import java.io.IOException;

import com.github.dannil.scbjavaclient.client.SCBClient;
import com.github.dannil.scbjavaclient.exception.SCBClientException;
import com.github.dannil.scbjavaclientutil.files.FileUtility;

public class SCBTableContents {

	private File baseDir;

	public SCBTableContents(File f) {
		this.baseDir = f;
	}

	public void getStatistics(File f3) throws IOException, InterruptedException {
		File[] files = f3.listFiles();
		String formatted = f3.getName().substring(f3.getName().indexOf('_'), f3.getName().length());

		File baseDirWithTime = new File(this.baseDir.getCanonicalPath() + formatted);
		if (!baseDirWithTime.exists()) {
			baseDirWithTime.mkdir();
		}

		System.out.println(baseDirWithTime);

		SCBClient c = new SCBClient();
		for (int i = 0; i < files.length; i++) {
			// List<String> rows = Files.readAllLines(, StandardCharsets.UTF_8);

			File file = files[i];
			String table = file.getName().replace("-", "/").substring(0, file.getName().lastIndexOf('.'));

			File location = new File(baseDirWithTime + "/" + file.getName());

			if (!location.exists()) {
				try {
					Thread.sleep(1000);

					String data = c.getRawData(table);

					System.out.println("Location: " + location);
					FileUtility.writeToSystem(location, data);
				} catch (SCBClientException e) {
					System.err.println(e);
				}
			} else {
				System.out.println("Location " + file.getName() + " already exists, skipping...");
			}

		}
	}
}
