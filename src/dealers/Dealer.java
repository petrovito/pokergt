package dealers;

import java.util.ArrayList;

import org.jscience.mathematics.number.Rational;

public abstract class Dealer {
	
	
	public abstract ArrayList<Deal> possible_deals(Dealing dealing, int player); /*{
		Set<Card> poss_cards = new HashSet<Card>(cards_);
		poss_cards.removeAll(dealing.all_cards());
		return poss_cards;
	}*/
	
	public abstract ArrayList<Dealing> possible_opponent_dealings(Dealing dealing);
	
	public abstract Rational possibility(Dealing dealing); 
	
	public abstract boolean is_end(Dealing dealing);
	public abstract int[] winners(Dealing dealing);
	
	public ArrayList<Dealing> possible_dealings(int player) {
		ArrayList<Dealing> dealings = new ArrayList<Dealing>();
		Dealing dealing = new Dealing();
		recursive_dealings(dealings, dealing,player);
		return dealings;
	}

	private void recursive_dealings(ArrayList<Dealing> dealings, 
			Dealing actual_dealing, int player) {
		if (is_end(actual_dealing)) return;
		for (Deal deal: possible_deals(actual_dealing, player)) {
			Dealing new_dealing = actual_dealing.copy(deal);
			dealings.add(new_dealing);
		}
	}

}
