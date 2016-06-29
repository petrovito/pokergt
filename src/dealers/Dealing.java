package dealers;

import java.util.ArrayList;
import java.util.Arrays;

import org.jscience.mathematics.number.Rational;

public class Dealing extends ArrayList<Deal> {
	
	private static final long serialVersionUID = 817816938083279142L;
	
	private Dealer dealer_;
	
	public ArrayList<Card> all_cards() {
		ArrayList<Card> cards = new ArrayList<Card>();
		for (Deal deal : this) {
			cards.addAll(deal.cards_);
		}
		return cards;
	}
	
	
	public Rational possibility() {
		return dealer_.possibility(this);
	}
	
	
	public Dealing(Dealer dealer) { dealer_ = dealer;}
	public Dealing(Dealing d) { super(d); dealer_ = d.dealer_; }
	
	public Dealing(Dealer dealer, int... private_cards) {
		dealer_ = dealer;
		for (int i = 0; i < private_cards.length; i++) {
			add(new Deal(i,private_cards[i]));
		}
	}
	
	public Dealing(Dealer dealer, Deal... deals) {
		super(Arrays.asList(deals));
		dealer_ = dealer;
	}
	
	public Dealing copy(Deal deal) {
		Dealing new_dealing = new Dealing(this);
		new_dealing.add(deal);
		return new_dealing;
	}
	
	public ArrayList<Dealing> next_dealings(int player) {
		return dealer_.next_dealings(this, player);
	}
	
	public Dealing restrict_player(int player) {
		Dealing dealing = new Dealing(dealer_);
		for (Deal deal: this) {
			if (deal.player_ == player || deal.type_ == DealType.PUBLIC 
					|| deal.type_ == DealType.COMMON)
				dealing.add(deal);
		}
		return dealing;
	}

	public Dealing restrict_players(ArrayList<Integer> players) {
		Dealing dealing = new Dealing(dealer_);
		for (Deal deal: this) {
			if (players.contains(deal.player_) || deal.type_ == DealType.PUBLIC 
					|| deal.type_ == DealType.COMMON)
				dealing.add(deal);
		}
		return dealing;		
	}
	
	
	public Dealing merge(Dealing other) {
		Dealing dealing = new Dealing(this);
		for (Deal deal: other) {
			if (!dealing.contains(deal)) {
				dealing.add(deal);
			}
		}
		return dealing;
	}
	
	public boolean is_end() {
		return dealer_.is_end(this);
	}
	
	public ArrayList<Dealing> possible_opponent_dealings() {
		return dealer_.possible_opponent_dealings(this);
	}


	public Dealing copy() {
		return new Dealing(this);
	}

	
}
