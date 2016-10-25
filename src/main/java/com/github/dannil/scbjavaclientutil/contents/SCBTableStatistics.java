package com.github.dannil.scbjavaclientutil.contents;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.github.dannil.scbjavaclient.client.SCBClient;
import com.github.dannil.scbjavaclient.exception.SCBClientException;
import com.github.dannil.scbjavaclient.utility.JsonUtility;
import com.github.dannil.scbjavaclientutil.files.FileUtility;

public class SCBTableStatistics {

	private File baseDir;

	public SCBTableStatistics(File f) {
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
			// System.out.println(table);

			String content = new String(Files.readAllBytes(file.toPath()));

			Map<String, List<String>> inputs = JsonUtility.getInputs(content);

			Map<String, Collection<?>> newInputs = new HashMap<String, Collection<?>>();
			for (Map.Entry<String, List<String>> entry : inputs.entrySet()) {
				newInputs.put(entry.getKey(), entry.getValue());
			}

			try {
				String data = c.getRawData(table, newInputs);

				Thread.sleep(1000);

				File location = new File(baseDirWithTime + "/" + file.getName());
				System.out.println("Location: " + location);
				FileUtility.writeToSystem(location, data);
			} catch (SCBClientException e) {
				System.err.println(e);
			}

		}
	}
}
