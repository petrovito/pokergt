package dealers;

import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

public class Dealing {
	
	public Set<Card> cards;
	
	
	
	public static void main(String[] a) {
		SortedSet<Card> s = new TreeSet<Card>(Card.comparator);
		s.add(new Card(1));
		s.add(new Card(3));
		s.add(new Card(2));
	}
}
