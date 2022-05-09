package com.cmpe202.billing;

import java.io.*;
import java.util.*;

public class Billing {
    public static void update_card(String card_number) throws IOException{
        FileWriter writer = new FileWriter("Cards.csv", true);
        writer.append(card_number + ",");
        writer.append("\n");
        writer.flush();
        writer.close();
    }
	
	private static ArrayList<Cards>load_cards(String path_to_cards_file) throws Exception {
		try {
			ArrayList<Cards> cards = new ArrayList<Cards>();
			File cards_input = new File(path_to_cards_file);
			
			Scanner sc = new Scanner(cards_input);
			sc.nextLine();
			
			while(sc.hasNextLine()) {
				String[] line = sc.nextLine().split(",");
				Cards new_card = new Cards(line[0]);
				cards.add(new_card);
			};
			
			sc.close();
			return cards;
		} catch(FileNotFoundException ex) {
			System.err.println("Cards File Not Found");
			throw ex;
		} catch(Exception ex) {
			System.err.println("Exception @ Loading Cards");
			throw ex;
		}
	};
	
	private static ArrayList<Items>load_items(String path_to_items_file) throws Exception {
		try {
			ArrayList<Items> items_list = new ArrayList<Items>();
			File input = new File(path_to_items_file);
			
			Scanner sc = new Scanner(input);
			sc.nextLine();
			
			while(sc.hasNextLine()) {
				String line = sc.nextLine().replace("\"", "");
                String[] lineData = line.split(",");
                
				Items item= new Items(
						lineData[0].trim(),
						lineData[1].trim(), 
						Integer.parseInt(lineData[2].trim()), 
						Double.parseDouble(lineData[3].trim())
				);
				
				items_list.add(item);
			};
			
			sc.close();
			return items_list;
		} catch(FileNotFoundException ex) {
			System.err.println("Inventory File Not Found");
			throw ex;
		} catch(Exception ex) {
			System.err.println("Exception @ Loading Inventory");
			throw ex;
		}
	};
	
	
	private static ArrayList<Orders>load_orders(String path_to_orders_file) throws Exception {
		try {
			ArrayList<Orders> orders_list = new ArrayList<Orders>();
			String card_number = "";
			
			File input = new File(path_to_orders_file);			
			Scanner sc = new Scanner(input);
			sc.nextLine();
			
			while(sc.hasNextLine()) {
                String line = sc.nextLine().replace("\"", "");
                String[] line_data = line.split(",");
                
                // Set the card_number
                if (card_number.equals("")) {
                    card_number = line_data[2];
                };
                
            	Orders new_order = new Orders(
        			line_data[0],
        			card_number,
        			Integer.parseInt(line_data[1])
            	);
            	
            	orders_list.add(new_order);	
			};
			
			sc.close();
			return orders_list;
		} catch(FileNotFoundException ex) {
			System.err.println("Orders File Not Found");
			throw ex;
		} catch(Exception ex) {
			System.err.println("Exception @ Loading Orders");
			throw ex;
		}
	};
	
	private static boolean card_exists(ArrayList<Cards> cards, String card_number) {
        for (Cards card : cards) {
            if (card.getCard().equals(card_number)) {
                return true;
            }
        }

        return false;
    }
	
	
	public static void display_bill(double amount, String bill, String errors) throws IOException {
		String output_file_name = "output.csv";
		String err_file_name = "errors.txt";
		
		FileWriter op_writer = new FileWriter(output_file_name);
		FileWriter err_writer = new FileWriter(err_file_name);
		
		op_writer.append("Amount");
		op_writer.append("\n");
		op_writer.append("" + amount);
		op_writer.append("\n");
		op_writer.append("List of items");
		op_writer.append("\n");
		op_writer.append(bill);
		op_writer.append("\n");
		op_writer.flush();
		op_writer.close();
		
		if (!errors.equals("")) {
			err_writer.append("Errors\n");
			err_writer.append(errors);
		}
		err_writer.flush();
		err_writer.close();
		
		
		
		
	};
	
	public static void main(String[] args) throws IOException {
		 /**
		  * Load CSV files
		  */
		try {
			System.out.println("Loading data\n");
			ArrayList<Cards> cards = load_cards("./Cards.csv");
			ArrayList<Items> items = load_items("./Items.csv");
			ArrayList<Orders> orders = load_orders("./Orders.csv");
			System.out.println("Data loading complete\n");
			
			double total = 0;
			String line_item = "";
			String err_text = "";
			
			HashMap<String, Integer> item_validations = new HashMap<String, Integer>();
			item_validations.put("essentials", 3);
			item_validations.put("luxury", 4);
			item_validations.put("misc", 6);
			
			for(Orders order: orders) {
				for(Items item: items) {
					if (order.getItem().equalsIgnoreCase(item.getItem())) {
						if (
							item_validations.containsKey(item.getCategory().toLowerCase()) && 
							item.getQuantity() >= order.getQuantity() && 
							order.getQuantity() <= item_validations.get(item.getCategory().toLowerCase())
						) {
							double price = item.getPrice() * order.getQuantity();
							total += price;
							line_item += "Item: " + order.getItem()  +", " + "Quantity: " + order.getQuantity() +", "+ "Price: " + price +", " +"\n";
							
							if (!card_exists(cards, order.getCard())) {
								update_card(order.getCard());
							};
						} else {
							// Incorrect quantity
							System.out.println("Incorrect quantity :: " + order + "\n" + item);
							err_text += "Incorrect quantity\tItem: " + order.getItem() + "\tQuantity: " + order.getQuantity() + "\n";
						};
					};
				}
			};
			
			display_bill(total, line_item, err_text);
			System.out.println("\n\nGoodnight Sweet Prince");
			
			
		} catch(Exception ex) {
			System.err.println("Exception @ Main = " + ex.toString());
		};
		
		
	};
};