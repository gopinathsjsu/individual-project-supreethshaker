package com.cmpe202.billing.Factory;

import java.io.IOException;

public class OutputterFactory {
	public Outputter getOutput(String type, String file_path) throws IOException {
		if (type == null) {
			return null;
		};
		
		if(type.equalsIgnoreCase("RECEIPT")) {
			return new Receipt(file_path);
		}
		
		if(type.equalsIgnoreCase("ERROR_RECEIPT")) {
			return new ErrorReceipt(file_path);
		}
		
		if(type.equalsIgnoreCase("FILE")) {
			return new Writer(file_path);
		}
		
		return null;
	};
};