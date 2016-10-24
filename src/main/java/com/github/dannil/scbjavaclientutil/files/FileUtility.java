package com.github.dannil.scbjavaclientutil.files;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import com.github.dannil.scbjavaclientutil.model.Entry;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class FileUtility {

	public static void writeToSystem(File f, String content) throws IOException {
		try (FileWriter fw = new FileWriter(f.getAbsoluteFile())) {
			try (BufferedWriter bw = new BufferedWriter(fw)) {
				bw.write(content);
			}
		}
	}

	public static void writeToSystem(File f, List<Entry> entries) throws IOException {
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		String content = gson.toJson(entries, List.class);
		writeToSystem(f, content);
	}

}
