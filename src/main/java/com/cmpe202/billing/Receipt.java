package com.cmpe202.billing;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Receipt implements Outputter {
	
	private FileWriter writer;
	
	public Receipt(String file_path) throws IOException {
		this.writer = new FileWriter(file_path);
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
	
	public String inject_amount_for_csv(String heading, double amount, ArrayList<String> line_items) {
		String receipt = heading;
		
		for(int i = 0; i < line_items.size(); i++) {
			if(i == 0) {
				receipt += line_items.get(0).replace("\n", amount + ",\n");
				continue;
			};
			receipt += line_items.get(i);
		};
		
		return receipt;
	};
};