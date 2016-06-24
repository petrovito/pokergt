package dealers;

import java.util.HashSet;
import java.util.Set;

public abstract class Dealer {
	
	public Set<Card> cards_;
	
	
	public abstract Set<Deal> possible_deals(Dealing dealing, int player); /*{
		Set<Card> poss_cards = new HashSet<Card>(cards_);
		poss_cards.removeAll(dealing.all_cards());
		return poss_cards;
	}*/
	
	//TODO rational...
	public abstract double possibility(Dealing dealing, Deal possible_deal); 
	
	public abstract boolean is_end(Dealing dealing);
	public abstract int[] winners(Dealing dealing);
	
	public Set<Dealing> possible_dealings(int player) {
		Set<Dealing> dealings = new HashSet<Dealing>();
		Dealing dealing = new Dealing();
		recursive_dealings(dealings, dealing,player);
		return dealings;
	}

	private void recursive_dealings(Set<Dealing> dealings, 
			Dealing actual_dealing, int player) {
		if (is_end(actual_dealing)) return;
		for (Deal deal: possible_deals(actual_dealing, player)) {
			Dealing new_dealing = actual_dealing.copy(deal);
			dealings.add(new_dealing);
		}
	}

}
