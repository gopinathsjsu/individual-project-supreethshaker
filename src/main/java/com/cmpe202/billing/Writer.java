package com.cmpe202.billing;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Writer implements Outputter {
	
	private FileWriter writer;
	
	public Writer(String file_path) throws IOException {
		this.writer = new FileWriter(file_path, true);
	};
	
	public void close() throws IOException {
		this.writer.flush();
		this.writer.close();
	}

	public void add(String output) throws IOException {
		this.writer.append(output);
	}

	public void output(String output) throws IOException {
		this.add(output);
		this.close();
	};
};