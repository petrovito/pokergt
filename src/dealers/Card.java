package dealers;

import java.util.ArrayList;

//import java.util.Comparator;

public class Card {
	
	public int num_;
	public int color_ = 0;

	public Card(int n) { num_ = n; }
	public Card(int n, int c) { num_ = n; color_=c; }
	
	public boolean equals(Object o) {
		if (o instanceof Card) {
			Card c = (Card) o;
			return c.num_ == num_ & c.color_ == color_;
		}
		return false;
	}
	
	public static ArrayList<Card> card_set(int... cards) {
		ArrayList<Card> card_set= new ArrayList<Card>();
		for (int card : cards) {
			card_set.add(new Card(card));
		}
		return card_set;
	}
	
	
	@Override
	public String toString() {
		return ""+num_;
	}
	
	//public static final CardComparator comparator = new CardComparator();

}

/*
class CardComparator implements Comparator<Card> {

	public int compare(Card c1, Card c2) {
		return c1.num-c2.num;
	}
}*/
