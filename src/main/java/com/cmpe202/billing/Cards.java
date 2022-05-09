package com.cmpe202.billing;

public class Cards {
	
	private String card;

	public String getCard() {
		return card;
	}

	public void setCard(String card) {
		this.card = card;
	};
	
	public Cards(String card_number) {
		this.setCard(card_number);
	}
	
	@Override
	public String toString() {
		return this.getCard();
	};
};