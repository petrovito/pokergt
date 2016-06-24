package dealers;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Deal {
	
	Set<Card> cards_;
	DealType type_ = DealType.PRIVATE;
	int player_;

	public Deal(int player, Card... cards) { 
		this.cards_ = new HashSet<Card>(Arrays.asList(cards));
		player_=player;
	}
	
	public Deal(int player, int... cards) { 
		this.cards_ = Card.card_set(cards);
		player_=player;
	}
	
	public static Deal common_deal(int... cards) {
		Deal deal = new Deal(-1, cards);
		deal.type_=DealType.COMMON;
		return deal;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Deal) {
			Deal deal = (Deal) obj;
			return deal.player_ == player_ && deal.type_ == type_ 
					&& deal.cards_.equals(cards_);
		}
		return super.equals(obj);
	}

}

enum DealType {
	PRIVATE, PUBLIC, COMMON, OUT,
}
