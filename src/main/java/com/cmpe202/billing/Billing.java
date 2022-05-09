package com.cmpe202.billing;

import java.io.*;
import java.util.*;
import com.cmpe202.billing.Factory.*;
import com.cmpe202.billing.Strategy.*;

public class Billing {
	
	private static OutputterFactory factory = new OutputterFactory();
	private static HashMap<String, Integer> item_validations = new HashMap<String, Integer>();
	static {
		item_validations = new HashMap<String, Integer>();
		item_validations.put("essentials", 3);
		item_validations.put("luxury", 4);
		item_validations.put("misc", 6);
	}	
	
	
	/**
	 * @category STRATEGY PATTERN
	 * the load_cards, load_items, and the load_orders methods all utilize
	 * the Strategy pattern. The author was not very satisfied encoding all the
	 * CSV traversals in different places, and hence, based on the use case,
	 * trivial as it might be,
	 * decided to encode the processing logic using the strategy pattern
	 */
	private static ArrayList<Cards>load_cards(String path_to_cards_file) throws Exception {
		try {
			ArrayList<Cards> cards = new ArrayList<Cards>();
			StrategyLoader loader = new StrategyLoader(new LoadCardsStrategy());
			ArrayList<String []> card_csv_data = loader.load_data(path_to_cards_file);
			
			for(String[] csv_card: card_csv_data ) {
				Cards new_card = new Cards(csv_card[0]);
				cards.add(new_card);
			};
			
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
			StrategyLoader loader = new StrategyLoader(new LoadItemsStrategy());
			ArrayList<String[]> items_csv_data = loader.load_data(path_to_items_file);
			
			for(String [] lineData: items_csv_data) {
				Items item= new Items(
						lineData[0].trim(),
						lineData[1].trim(), 
						Integer.parseInt(lineData[2].trim()), 
						Double.parseDouble(lineData[3].trim())
				);	
				items_list.add(item);
			};
			
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
			
			StrategyLoader loader = new StrategyLoader(new LoadOrdersStrategy());
			ArrayList<String []> orders_csv_data = loader.load_data(path_to_orders_file);
			
			for (String[] line_data: orders_csv_data) {
                
                // If a card number is specified set that as the card
                if (line_data.length >= 3) {
                    card_number = line_data[2];
                };
                
            	Orders new_order = new Orders(
        			line_data[0],
        			card_number,
        			Integer.parseInt(line_data[1])
            	);
            	
            	orders_list.add(new_order);	
			};
			
			return orders_list;
		} catch(FileNotFoundException ex) {
			System.err.println("Orders File Not Found");
			throw ex;
		} catch(Exception ex) {
			System.err.println("Exception @ Loading Orders" + ex.toString());
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
	
	
	/**
	 * @category FACTORY pattern
	 * The below 2 methods make use of the factory pattern as demonstrated
	 * by OutputterFactory
	 * The printing methods may be varied and each may have it's own design principle
	 * The responsibility of the design and the file output is dictated by the caller, 
	 * but the callee takes care of the specific output storage and input and output
	 */
	public static void update_card(String card_number) throws IOException{
        Outputter writer = factory.getOutput("FILE", "Cards.csv");
        String input = card_number + ",\n";
        writer.output(input);
    }
	
	
	public static void persist_bill_details(double amount, String errors, ArrayList<String> line_items) throws IOException {
		String output_file_name = "output.csv";
		String err_file_name = "errors.txt";
		
		Receipt receipt = (Receipt)factory.getOutput("RECEIPT", output_file_name);
		String heading = "Item,Quantity,Price,TotalPrice,\n";		
		
		receipt.output(
				receipt.inject_amount_for_csv(heading, amount, line_items)
		);
		
		Outputter err_bill = factory.getOutput("ERROR_RECEIPT", err_file_name);
		String err_msg= "Errors\n";
		if (errors.length() > 0) err_msg += errors;
		err_bill.output(err_msg);
	};
	
	public static void main(String[] args) {
		 /**
		  * Load CSV files
		  * 
		  * Orders: {Item, Quantity}, CardNumber
		  * Items: {Item<essentials, luxury, misc>, Category, Quantity, Price }
		  */
		try {
			System.out.println("Loading...");
			ArrayList<Cards> cards = load_cards("./Cards.csv");
			ArrayList<Items> items = load_items("./Items.csv");
			ArrayList<Orders> orders = load_orders("./Orders.csv");
			System.out.println("Loaded data successfully");
			
			double total = 0;
			ArrayList<String> line_items = new ArrayList<String>();;
			String err_text = "";
			
			HashMap<String, Items> item_set = new HashMap<String, Items>();
			for (Items item: items) {
				item_set.put(item.getItem().toLowerCase(), item);
			};

			System.out.println("Orders are being processed...\n");
			for(Orders order: orders) {
					
				Items item = item_set.getOrDefault(order.getItem().toLowerCase(), null);
				if (item == null) {
					err_text += "Missing item\tOrder = " + order.getItem() + "\n";
					System.out.println("Missing item\tOrder = " + order.getItem() + "\n");
					continue;
				};
				
				if (order.getQuantity() > item.getQuantity()) {
					err_text += "Not enough inventory\tOrder = " + order.getItem() + "\tRequested vs Available = " + order.getQuantity() + "\t" + item.getQuantity() + "\n";
					System.out.println("Not enough inventory\tOrder = " + order.getItem() + "\nRequested vs Available = " + order.getQuantity() + "\t" + item.getQuantity() + "\n");
					continue;
				};
				
				Integer quantity_limit = item_validations.getOrDefault(item.getCategory().toLowerCase(), null);
				if (quantity_limit == null) {
					err_text += "Invalid category\tItem = " + item.getCategory() + "\n";
					System.out.println("Invalid category\tItem = " + item.getCategory() + "\n");
					continue;
				};
				
				if (order.getQuantity() > quantity_limit) {
					err_text += "Incorrect quantity\tItem: " + order.getItem() + "\tQuantity: " + order.getQuantity() + "\n";
					System.out.println("Incorrect quantity\tItem: " + order.getItem() + "\tQuantity: " + order.getQuantity() + "\n");
					continue;
				};
				
				double price = item.getPrice() * order.getQuantity();
				total += price;
				line_items.add(order.getItem() + "," + (int) order.getQuantity() + "," + (int) price + ",\n");
				System.out.println("Valid order for " + (int) price + ".\t Total = " + total);
				
				// Add a card from a valid order if not encountered
				if (!card_exists(cards, order.getCard())) {
					cards.add(new Cards(order.getCard()));
					update_card(order.getCard());
				};
			}
			
			System.out.println("\nOrders processed. Generating receipt...\n");
			persist_bill_details(total, err_text, line_items);
			System.out.println("=========================================");
			System.out.println("\t\tSuccess\t\t\t|");
			System.out.println("=========================================");
			System.exit(0);
			
		} catch(IOException ex) {
			System.err.println("Error handling files" + ex.toString());
			System.exit(0);
		} catch(Exception ex) {
			System.err.println("Exception @ Main = " + ex.toString());
			System.exit(0);
		};
	};
};