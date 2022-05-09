package com.cmpe202.billing;

public class Items {
	
	private String item;
	private String category;
	private double price;
	private int quantity;
	
	public String getItem() {
		return item;
	};
	
	public void setItem(String item) {
		this.item = item;
	};
	
	public String getCategory() {
		return category;
	};
	
	public void setCategory(String category) {
		this.category = category;
	};
	
	public double getPrice() {
		return price;
	};
	
	public void setPrice(double price) {
		this.price = price;
	};
	
	public int getQuantity() {
		return quantity;
	};
	
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	};
	
	public Items(String item, String category, int quantity, double price) {
		this.setItem(item);
		this.setCategory(category);
		this.setQuantity(quantity);
		this.setPrice(price);
	};
	
	@Override
	public String toString() {
		return "Items{" + "category=" + this.getCategory()+ ", item=" + this.getItem() + ", quantity=" + this.getQuantity() + ", price=" + this.getPrice() + '}';
	};
};