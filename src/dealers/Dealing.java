package dealers;

import java.util.ArrayList;

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
	
	
	public Dealing(Dealer dealer) { dealer_ = dealer;}
	public Dealing(Dealing d) { super(d); dealer_ = d.dealer_; }
	
	public Dealing(int... private_cards) {
		for (int i = 0; i < private_cards.length; i++) {
			add(new Deal(i,private_cards[i]));
		}
	}
	
	public Dealing copy(Deal deal) {
		Dealing new_dealing = new Dealing(this);
		new_dealing.add(deal);
		return new_dealing;
	}
	
	
	public Dealing restrict_player(int player) {
		Dealing dealing = new Dealing();
		for (Deal deal: this) {
			if (deal.player_ == player || deal.type_ == DealType.PUBLIC 
					|| deal.type_ == DealType.COMMON)
				dealing.add(deal);
		}
		return dealing;
	}
	
	public boolean is_end() {
		return dealer_.is_end(this);
	}
	
	
}
