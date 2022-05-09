package com.cmpe202.billing.Strategy;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

public class StrategyLoader implements Strategy {
	
	Strategy strategy = null;
	public StrategyLoader(Strategy strategy) {
		this.strategy = strategy;
	};
	
	public ArrayList<String []> load_data(String path_to_file) throws FileNotFoundException {
		return strategy.load_data(path_to_file);
	};
};