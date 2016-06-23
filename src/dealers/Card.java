package dealers;

import java.util.Comparator;

public class Card {
	
	public int num;
	public int color = 0;

	public Card(int n) { num = n; }
	public Card(int n, int c) { num = n; color=c; }
	
	public boolean equals(Object o) {
		if (o instanceof Card) {
			Card c = (Card) o;
			return c.num == num & c.color == color;
		}
		return false;
	}
	
	public static final CardComparator comparator = new CardComparator();

}


class CardComparator implements Comparator<Card> {

	public int compare(Card c1, Card c2) {
		return c1.num-c2.num;
	}
}
