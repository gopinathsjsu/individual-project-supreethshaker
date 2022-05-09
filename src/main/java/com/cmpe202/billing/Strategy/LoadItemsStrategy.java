package com.cmpe202.billing.Strategy;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class LoadItemsStrategy implements Strategy {
	public ArrayList<String []> load_data(String path_to_file) throws FileNotFoundException {
		File cards_input = new File(path_to_file);
		Scanner sc = new Scanner(cards_input);
		
		// Skip heading
		sc.nextLine();
		
		ArrayList<String []> output =  new ArrayList<String []>();
		
		while(sc.hasNextLine()) {
			String line = sc.nextLine().replace("\"", "");
            String[] lineData = line.split(",");
        
			output.add(lineData);
		};
		
		sc.close();
		return output;
	};
};