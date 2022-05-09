package com.cmpe202.billing;

public class Orders {
	
	private String item;
	private String card;
	private int quantity;
	
	
	public Orders(String item, String card, int quantity) {
		this.item = item;
		this.card = card;
		this.quantity = quantity;
	}

	public String getItem() {
		return item;
	}

	public void setItem(String item) {
		this.item = item;
	}

	public String getCard() {
		return card;
	}

	public void setCard(String card) {
		this.card = card;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	};
	
	public String toString() {
		return this.item + "," + this.quantity + "," + this.card;
	};
	
};