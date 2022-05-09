package com.cmpe202.billing.Strategy;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class LoadOrdersStrategy implements Strategy {
	
	public ArrayList<String []> load_data(String path_to_file) throws FileNotFoundException {
		
		File input = new File(path_to_file);			
		Scanner sc = new Scanner(input);
		ArrayList<String []> output = new ArrayList<String []>();
		
		// Skip heading
		sc.nextLine();
		
		while(sc.hasNextLine()) {
            String line = sc.nextLine().replace("\"", "");
            String[] line_data = line.split(",");
            output.add(line_data);
            
//            // If a card number is specified set that as the card
//            if (line_data.length >= 3) {
//                card_number = line_data[2];
//            };
		};
		
		return output;
	};
};