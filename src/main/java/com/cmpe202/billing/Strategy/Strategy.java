package com.cmpe202.billing.Strategy;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

public interface Strategy {
	ArrayList<String []> load_data(String path_to_file) throws FileNotFoundException;
};