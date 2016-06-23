package bids;

public class Bid {
	
	public Action act;
	public double amount;
	public int player;
	
	public  Bid(Action a, double am, int player) {act=a; amount=am; this.player=player;}
	
	public boolean isFold() { return act==Action.FOLD; }
	public boolean isCall() { return act==Action.CALL; }
	public boolean isCheck() { return act==Action.CHECK; }

	public static Bid FOLD(int num_player) {
		return new Bid(Action.FOLD, 0, num_player);
	}
	public static Bid[] BLINDS(double... amounts) {
		Bid[] bids= new Bid[amounts.length];
		for (int i = 0; i < amounts.length; i++) {
			bids[i]=new Bid(Action.BLIND,amounts[i],i);
		}
		return bids;
	}
	public static final Bid CHECK(int num_player) {
		return new Bid(Action.CHECK, 0, num_player);
	}
	public static final Bid CALL(int num_player) {
		return new Bid(Action.CALL, 0, num_player);
	}
	

}

enum Action {
	BLIND, FOLD, CALL, CHECK, BET, RAISE
}