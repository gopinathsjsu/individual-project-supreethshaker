package com.cmpe202.billing;

import java.io.IOException;

public interface Outputter {
	void add(String input) throws IOException;
	void output(String output) throws IOException;
};